package qStivi.db;

import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("unused")
public class DB {
    private static final Logger logger = getLogger(DB.class);

    public DB() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        String url = "jdbc:sqlite:bot.db";
        Connection connection = DriverManager.getConnection(url);
        String enableForeignKeys = "PRAGMA foreign_keys = ON;";
        String setupUserDataTable = """
                CREATE TABLE IF NOT EXISTS "UserData"
                (
                    "UserID"        INTEGER PRIMARY KEY NOT NULL,
                    "XP"            INTEGER             NOT NULL DEFAULT 0,
                    "Money"         INTEGER             NOT NULL DEFAULT 0,
                    "LastChat"      INTEGER             NOT NULL DEFAULT 0,
                    "LastReaction"  INTEGER             NOT NULL DEFAULT 0,
                    "LastVoiceJoin" INTEGER             NOT NULL DEFAULT 0,
                    "XPChat"        INTEGER             NOT NULL DEFAULT 0,
                    "XPReaction"    INTEGER             NOT NULL DEFAULT 0,
                    "XPVoice"       INTEGER             NOT NULL DEFAULT 0
                );
                """;
        String setupSkillTree = """
                CREATE TABLE IF NOT EXISTS "SkillTrees"
                (
                    "UserID"      INTEGER PRIMARY KEY NOT NULL,
                    "SkillPoints" INTEGER             NOT NULL DEFAULT 0,
                    "WorkXP"      INTEGER             NOT NULL DEFAULT 0,
                    "WorkMoney"   INTEGER             NOT NULL DEFAULT 0,
                    "GambleXP"    INTEGER             NOT NULL DEFAULT 0,
                    "SocialXP"    INTEGER             NOT NULL DEFAULT 0,
                    FOREIGN KEY ("UserID") REFERENCES "UserData" ("UserID") ON UPDATE CASCADE ON DELETE CASCADE
                );
                """;
        String setupCommandsStatisticsTable = """
                CREATE TABLE IF NOT EXISTS "CommandStatistics"
                (
                    "UserID"       INTEGER NOT NULL,
                    "CommandName"  TEXT    NOT NULL,
                    "TimesHandled" INTEGER NOT NULL DEFAULT 0,
                    "LastHandled"  INTEGER NOT NULL DEFAULT 0,
                    "XP"           INTEGER NOT NULL DEFAULT 0,
                    "Money"        INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY ("UserID") REFERENCES "UserData" ("UserID") ON UPDATE CASCADE ON DELETE CASCADE
                );
                """;
        String setupGameStatisticsTable = """
                CREATE TABLE IF NOT EXISTS "GameStatistics"
                (
                    "UserID"     INTEGER NOT NULL,
                    "GameName"   TEXT    NOT NULL,
                    "Plays"      INTEGER NOT NULL DEFAULT 0,
                    "Wins"       INTEGER NOT NULL DEFAULT 0,
                    "Loses"      INTEGER NOT NULL DEFAULT 0,
                    "Draws"      INTEGER NOT NULL DEFAULT 0,
                    "LastPlayed" INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY ("UserID") REFERENCES "UserData" ("UserID") ON UPDATE CASCADE ON DELETE CASCADE
                );
                """;
        String setupLottoTable = """
                CREATE TABLE IF NOT EXISTS "Lotto"
                (
                    "UserID" INTEGER PRIMARY KEY NOT NULL,
                    "Vote"   INTEGER             NOT NULL DEFAULT 0,
                    FOREIGN KEY ("UserID") REFERENCES "UserData" ("UserID") ON UPDATE CASCADE ON DELETE CASCADE
                );
                """;
        String insertLottoPool = """
                INSERT INTO "Lotto"("UserID", "Vote") VALUES (0, 0) ON CONFLICT DO NOTHING;
                """;
        String insertLottoPoolUser = """
                INSERT INTO "UserData"("UserID") VALUES (0) ON CONFLICT DO NOTHING;
                """;


        if (connection != null) {
            DatabaseMetaData meta = connection.getMetaData();


            connection.createStatement().execute(enableForeignKeys);
            connection.createStatement().execute(setupUserDataTable);
            connection.createStatement().execute(setupCommandsStatisticsTable);
            connection.createStatement().execute(setupGameStatisticsTable);
            connection.createStatement().execute(setupLottoTable);
            connection.createStatement().execute(insertLottoPoolUser);
            connection.createStatement().execute(insertLottoPool);
            connection.createStatement().execute(setupSkillTree);
            connection.close();
        }

    }

    public void setLottoVote(long value, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "Lotto"("UserID", "Vote")
                VALUES (%s, %s);
                """.formatted(UserID, value);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public List<Long> getLottoParticipants() throws SQLException {
        String sql = "SELECT \"UserID\" FROM \"Lotto\" WHERE \"UserID\" != 0;";
        List<Long> list = new ArrayList<>();
        var connection = connect();
        var result = connection.createStatement().executeQuery(sql);
        while (result.next()) {
            list.add(result.getLong("UserID"));
        }
        connection.close();
        return list;
    }

    public List<Long> getLottoParticipantsByVote(int vote) throws SQLException {
        String sql = "SELECT \"UserID\" FROM \"Lotto\" WHERE \"Vote\" = %s;".formatted(vote);
        List<Long> list = new ArrayList<>();
        var connection = connect();
        var result = connection.createStatement().executeQuery(sql);
        while (result.next()) {
            list.add(result.getLong("UserID"));
        }
        connection.close();
        return list;
    }

    public void incrementLottoPool(long amount) throws SQLException {
        String query = """
                UPDATE "Lotto"
                SET "Vote" = "Vote" + %S
                WHERE "UserID" = 0
                """.formatted(amount);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public Long getLevel(Long id) throws SQLException {
        var xp = getXP(id);
        xp = xp == null ? 0 : xp;
        return (long) Math.floor((double) xp / 800);
    }

    private void executeUpdate(Object value, String sql) {
        try (Connection conn = this.connect()) {
            PreparedStatement pstmt;
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setObject(1, value);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a warehouse specified by the id
     *
     * @param table   name of table to insert to
     * @param colName name of column to insert to
     * @param value   value to insert to
     */
    public void delete(String table, String colName, Object value) {
        String sql = "DELETE FROM %s WHERE %s = ?".formatted(table, colName);

        executeUpdate(value, sql);
    }

    public boolean commandNameAlreadyExists(String CommandName, long UserID) throws SQLException {
        var connection = connect();
        var select = """
                SELECT "CommandName"
                FROM "CommandStatistics"
                WHERE "UserID" = '%s'
                """.formatted(UserID);
        var list = new ArrayList<String>();
        if (connection != null) {
            var result = connection.createStatement().executeQuery(select);
            while (result.next()) {
                var temp = result.getString("CommandName");
                list.add(temp);

            }
            connection.close();
        }
        return list.contains(CommandName);
    }

    public boolean gameNameAlreadyExists(String CommandName, long UserID) throws SQLException {
        var connection = connect();
        var select = """
                SELECT "GameName"
                FROM "GameStatistics"
                WHERE "UserID" = '%s'
                """.formatted(UserID);
        var list = new ArrayList<String>();
        if (connection != null) {
            var result = connection.createStatement().executeQuery(select);
            while (result.next()) {
                var temp = result.getString("GameName");
                list.add(temp);

            }
            connection.close();
        }
        return list.contains(CommandName);
    }

    public void setCommandLastHandled(String name, long value, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "LastHandled" = %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(value, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "LastHandled")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, value);

        }
        var upsert = """
                INSERT INTO "CommandStatistics"("UserID", "LastHandled", "CommandName")
                VALUES (%s, %s, '%s')
                ON CONFLICT("UserID") DO UPDATE SET "LastHandled" = %s
                WHERE "UserID" = %s
                AND "CommandName" = '%s';
                """.formatted(UserID, value, name, value, UserID, name);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void setGameLastPlayed(String name, long value, long UserID) throws SQLException {
        String query;
        if (gameNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "LastPlayed" = %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(value, UserID, name);

        } else {
            query = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "LastPlayed")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, value);

        }
        var upsert = """
                INSERT INTO "GameStatistics"("UserID", "LastPlayed", "GameName")
                VALUES (%s, %s, '%s')
                ON CONFLICT("UserID") DO UPDATE SET "LastPlayed" = %s
                WHERE "UserID" = %s
                AND "GameName" = '%s';
                """.formatted(UserID, value, name, value, UserID, name);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void setLastChat(long value, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "LastChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "LastChat" = %s
                WHERE "UserID" = %s;
                """.formatted(UserID, value, value, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void setLastReaction(long value, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "LastReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "LastReaction" = %s
                WHERE "UserID" = %s;
                """.formatted(UserID, value, value, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void setLastVoiceJoin(long value, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "LastVoiceJoin")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "LastVoiceJoin" = %s
                WHERE "UserID" = %s;
                """.formatted(UserID, value, value, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementXP(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XP")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XP" = "XP" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementMoney(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "Money")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "Money" = "Money" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementXPChat(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPChat" = "XPChat" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementXPReaction(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPReaction" = "XPReaction" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementXPVoice(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPVoice")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPVoice" = "XPVoice" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementCommandTimesHandled(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "TimesHandled"
                            = "TimesHandled" + %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "TimesHandled")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void incrementCommandXP(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "XP"
                            = "XP" + %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "XP")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void incrementCommandMoney(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "Money"
                            = "Money" + %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "Money")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void incrementGamePlays(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Plays"
                            = "Plays" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Plays")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void incrementGameWins(String name, long amount, long UserID) throws SQLException {
        String query;
        if (gameNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Wins"
                            = "Wins" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Wins")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void incrementGameLoses(String name, long amount, long UserID) throws SQLException {
        String query;
        if (gameNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Loses"
                            = "Loses" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Loses")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void incrementGameDraws(String name, long amount, long UserID) throws SQLException {
        String query;
        if (gameNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Draws"
                            = "Draws" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Draws")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void decrementXP(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XP")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XP" = "XP" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void decrementMoney(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "Money")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "Money" = "Money" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void decrementXPChat(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPChat" = "XPChat" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void decrementXPReaction(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPReaction" = "XPReaction" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void decrementXPCommand(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPCommand")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPCommand" = "XPCommand" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void decrementXPVoice(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPVoice")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPVoice" = "XPVoice" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void decrementCommandTimesRecognized(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "TimesRecognized"
                            = "TimesRecognized" - %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "TimesRecognized")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void decrementCommandXP(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "XP"
                            = "XP" - %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "XP")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void decrementCommandMoney(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "Money"
                            = "Money" - %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "Money")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void decrementGamePlays(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Plays"
                            = "Plays" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Plays")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void decrementGameWins(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Wins"
                            = "Wins" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Wins")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void decrementGameLoses(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Loses"
                            = "Loses" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Loses")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public void decrementGameDraws(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Draws"
                            = "Draws" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Draws")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(query);
            connection.close();
        }
    }

    public Long getCommandTimesRecognized(String CommandName, long UserID) throws SQLException {
        String query = "select \"TimesRecognized\" from \"CommandStatistics\" where \"UserID\" = %s AND \"CommandName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("TimesRecognized");
        connection.close();
        return value;
    }

    public Long getCommandLastRecognized(String CommandName, long UserID) throws SQLException {
        String query = "select \"LastRecognized\" from \"CommandStatistics\" where \"UserID\" = %s AND \"CommandName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("LastRecognized");
        connection.close();
        return value;
    }

    public Long getCommandLastHandled(String CommandName, long UserID) throws SQLException {
        String query = "select \"LastHandled\" from \"CommandStatistics\" where \"UserID\" = %s AND \"CommandName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastHandled");
        }
        connection.close();
        return value;
    }

    public Long getCommandXP(String CommandName, long UserID) throws SQLException {
        String query = "select \"XP\" from \"CommandStatistics\" where \"UserID\" = %s AND \"CommandName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("XP");
        connection.close();
        return value;
    }

    public Long getCommandMoney(String CommandName, long UserID) throws SQLException {
        String query = "select \"Money\" from \"CommandStatistics\" where \"UserID\" = %s AND \"CommandName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("Money");
        connection.close();
        return value;
    }

    public Long getGamePlays(String CommandName, long UserID) throws SQLException {
        String query = "select \"Plays\" from \"GameStatistics\" where \"UserID\" = %s AND \"GameName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("Plays");
        connection.close();
        return value;
    }

    public Long getGameWins(String CommandName, long UserID) throws SQLException {
        String query = "select \"Wins\" from \"GameStatistics\" where \"UserID\" = %s AND \"GameName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Wins");
        }
        connection.close();
        return value;
    }

    public Long getGameLoses(String CommandName, long UserID) throws SQLException {
        String query = "select \"Loses\" from \"GameStatistics\" where \"UserID\" = %s AND \"GameName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Loses");
        }
        connection.close();
        return value;
    }

    public Long getGameDraws(String CommandName, long UserID) throws SQLException {
        String query = "select \"Draws\" from \"GameStatistics\" where \"UserID\" = %s AND \"GameName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("Draws");
        connection.close();
        return value;
    }

    public Long getGameLastPlayed(String CommandName, long UserID) throws SQLException {
        String query = "select \"LastPlayed\" from \"GameStatistics\" where \"UserID\" = %s AND \"GameName\" = '%s'".formatted(UserID, CommandName);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastPlayed");
        }
        connection.close();
        return value;
    }

    public Long getXP(long UserID) throws SQLException {
        String query = "select \"XP\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value = result.getLong("XP");
        }
        connection.close();
        return value;
    }

    public Long getMoney(long UserID) throws SQLException {
        String query = "select \"Money\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Money");
        }
        connection.close();
        return value;
    }

    public Long getLastChat(long UserID) throws SQLException {
        String query = "select \"LastChat\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("LastChat");
        connection.close();
        return value;
    }

    public Long getLastReaction(long UserID) throws SQLException {
        String query = "select \"LastReaction\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("LastReaction");
        connection.close();
        return value;
    }

    public Long getLastCommand(long UserID) throws SQLException {
        String query = "select \"LastCommand\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("LastCommand");
        connection.close();
        return value;
    }

    public Long getLastVoiceJoin(long UserID) throws SQLException {
        String query = "select \"LastVoiceJoin\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("LastVoiceJoin");
        connection.close();
        return value;
    }

    public Long getXPChat(long UserID) throws SQLException {
        String query = "select \"XPChat\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("XPChat");
        connection.close();
        return value;
    }

    public Long getXPReaction(long UserID) throws SQLException {
        String query = "select \"XPReaction\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("XPReaction");
        connection.close();
        return value;
    }

    public Long getXPCommand(long UserID) throws SQLException {
        String query = "select \"XPCommand\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("XPCommand");
        connection.close();
        return value;
    }

    public Long getXPVoice(long UserID) throws SQLException {
        String query = "select \"XPVoice\" from \"UserData\" where \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("XPVoice");
        connection.close();
        return value;
    }

    public Long getLottoVote(long UserID) throws SQLException {
        String query = "SELECT \"Vote\" FROM \"Lotto\" WHERE \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("Vote");
        connection.close();
        return value;
    }

    public Long getSkillPoints(long UserID) throws SQLException {
        String query = "SELECT \"SkillPoints\" FROM \"SkillTrees\" WHERE \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value = result.getLong("SkillPoints");
        }
        connection.close();
        return value;
    }

    public Long getSkillPointsWorkXP(long UserID) throws SQLException {
        String query = "SELECT \"WorkXP\" FROM \"SkillTrees\" WHERE \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("WorkXP");
        connection.close();
        return value;
    }

    public Long getSkillPointsWorkMoney(long UserID) throws SQLException {
        String query = "SELECT \"WorkMoney\" FROM \"SkillTrees\" WHERE \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("WorkMoney");
        connection.close();
        return value;
    }

    public Long getSkillPointsGambleXP(long UserID) throws SQLException {
        String query = "SELECT \"GambleXP\" FROM \"SkillTrees\" WHERE \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("GambleXP");
        connection.close();
        return value;
    }

    public Long getSkillPointsSocialXP(long UserID) throws SQLException {
        String query = "SELECT \"SocialXP\" FROM \"SkillTrees\" WHERE \"UserID\" = %s".formatted(UserID);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        result.next();
        var value = result.getLong("SocialXP");
        connection.close();
        return value;
    }

    public void resetLottoVotes() throws SQLException {
        String query = "DELETE FROM \"Lotto\" WHERE \"UserID\" != 0;";
        var connection = connect();
        connection.createStatement().execute(query);
        connection.close();
    }

    public void resetLottoPool() throws SQLException {
        String update = "UPDATE \"Lotto\" SET \"Vote\" = 0 WHERE \"UserID\" = 0;";
        var connection = connect();
        connection.createStatement().execute(update);
        connection.close();
    }


    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:bot.db";
        return DriverManager.getConnection(url);
    }

    public boolean userDoesNotExists(long id) {
        String sql = "select id from users where id = ? LIMIT 1";

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt;
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Deprecated
    public List<Long> getRanking() {
        String sql = "select \"UserID\" from \"UserData\" order by \"Money\" DESC, \"XP\" desc";
        List<Long> list = new ArrayList<>();

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt;
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                var rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(rs.getLong("UserID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getLottoPool() throws SQLException {
        String query = "SELECT \"Vote\" FROM \"Lotto\" WHERE \"UserID\" = 0;";
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        int value = 0;
        while (result.next()) {
            value = result.getInt("Vote");
        }
        connection.close();
        return value;
    }

    public void insertSkillTreeIfNotExists(long id) throws SQLException {
        String query = "INSERT INTO \"SkillTrees\"(UserID) VALUES(%s) ON CONFLICT DO NOTHING;".formatted(id);
        var connection = connect();
        connection.createStatement().execute(query);
        connection.close();
    }

    public void resetSkillTree(long id) throws SQLException {
        long level = getXP(id) / 800;
        String setSkillPoints = """
                UPDATE "SkillTrees"
                SET "SkillPoints" = %s,
                "SocialXP"    = 0,
                "GambleXP"    = 0,
                "WorkMoney"   = 0,
                "WorkXP"      = 0
                WHERE "UserID" = %s;
                """.formatted(level, id);
        var connection = connect();
        connection.createStatement().execute(setSkillPoints);
        connection.close();
    }

    public void incrementSkillWorkXP(long id, int amount) throws SQLException {
        var upsert = """
                UPDATE "SkillTrees" SET "WorkXP" = "WorkXP" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementSkillWorkMoney(long id, int amount) throws SQLException {
        var upsert = """
                UPDATE "SkillTrees" SET "WorkMoney" = "WorkMoney" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementSkillGambleXP(long id, int amount) throws SQLException {
        var upsert = """
                UPDATE "SkillTrees" SET "GambleXP" = "GambleXP" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void incrementSkillSocialXP(long id, int amount) throws SQLException {
        var upsert = """
                UPDATE "SkillTrees" SET "SocialXP" = "SocialXP" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public void decrementSkillPoints(long id, int amount) throws SQLException {
        var upsert = """
                UPDATE "SkillTrees" SET "SkillPoints" = "SkillPoints" - %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        var connection = connect();
        if (connection != null) {
            connection.createStatement().execute(upsert);
            connection.close();
        }
    }

    public long getSpentSkillPoints(long id) throws SQLException {
        String query = "SELECT \"WorkXP\", \"WorkMoney\", \"GambleXP\", \"SocialXP\" FROM \"SkillTrees\" WHERE \"UserID\" = %s;".formatted(id);
        var connection = connect();
        var result = connection.createStatement().executeQuery(query);
        long value = 0;
        while (result.next()) {
            value += result.getLong("WorkMoney");
            value += result.getLong("WorkXP");
            value += result.getLong("GambleXP");
            value += result.getLong("SocialXP");
        }
        connection.close();
        return value;
    }
}