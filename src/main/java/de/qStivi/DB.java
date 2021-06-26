package de.qStivi;

import de.qStivi.sportBet.crawler.CrawlerInfo;
import de.qStivi.sportBet.crawler.CrawlerResult;
import de.qStivi.sportBet.objects.Match;
import de.qStivi.sportBet.objects.Result;
import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("unused")
public class DB {
    private static final Logger logger = getLogger(DB.class);
    private static DB instance;
    Connection connection = connect();

    private DB() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

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
                "LastReset"   INTEGER             NOT NULL DEFAULT 0,
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

        String createWette = """
                create table if not exists Wette(
                    UserID   int
                    references UserData,
                    Mannschaft TEXT,
                    Einsatz    int,
                    WettID     INTEGER
                    constraint Wette_pk
                    primary key autoincrement,
                    Quote      double
                    );
                                """;


        if (connection != null) {
            var stmt = connection.createStatement();
            stmt.addBatch(enableForeignKeys);
            stmt.addBatch(setupUserDataTable);
            stmt.addBatch(setupCommandsStatisticsTable);
            stmt.addBatch(setupGameStatisticsTable);
            stmt.addBatch(setupLottoTable);
            stmt.addBatch(insertLottoPoolUser);
            stmt.addBatch(insertLottoPool);
            stmt.addBatch(setupSkillTree);
            stmt.addBatch(createWette);
            stmt.executeBatch();
        }

    }

    public static DB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    public void setLottoVote(long value, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "Lotto"("UserID", "Vote") VALUES (%s, %s);
                """.formatted(UserID, value));
    }

    public List<Long> getLottoParticipants() throws SQLException {
        List<Long> list = new ArrayList<>();
        var result = connection.createStatement().executeQuery("""
                SELECT "UserID" FROM "Lotto" WHERE "UserID" != 0;
                """);
        while (result.next()) {
            list.add(result.getLong("UserID"));
        }
        return list;
    }

    public List<Long> getLottoParticipantsByVote(int vote) throws SQLException {
        List<Long> list = new ArrayList<>();
        var result = connection.createStatement().executeQuery("""
                SELECT "UserID" FROM "Lotto" WHERE "Vote" = %s;
                """.formatted(vote));
        while (result.next()) {
            list.add(result.getLong("UserID"));
        }
        return list;
    }

    public void incrementLottoPool(long amount) throws SQLException {
        connection.createStatement().execute("""
                UPDATE "Lotto"
                SET "Vote" = "Vote" + %S
                WHERE "UserID" = 0
                """.formatted(amount / 4));
    }

    public Long getLevel(Long id) throws SQLException {
        return (long) Math.floor(-5000 * Math.pow(2.7, ((float) -getXP(id) / 3700000)) + 5000);
    }

    public void delete(String table, String colName, Object value) throws SQLException {
        String sql = "DELETE FROM %s WHERE %s = ?".formatted(table, colName);
        connection.createStatement().execute(sql);
    }

    public boolean commandNameAlreadyExists(String CommandName, long UserID) throws SQLException {
        var list = new ArrayList<String>();
        var result = connection.createStatement().executeQuery("""
                SELECT "CommandName"
                FROM "CommandStatistics"
                WHERE "UserID" = '%s'
                """.formatted(UserID));
        while (result.next()) {
            list.add(result.getString("CommandName"));
        }
        return list.contains(CommandName);
    }

    public boolean gameNameAlreadyExists(String CommandName, long UserID) throws SQLException {
        var list = new ArrayList<String>();
        var result = connection.createStatement().executeQuery("""
                SELECT "GameName"
                FROM "GameStatistics"
                WHERE "UserID" = '%s'
                """.formatted(UserID));
        while (result.next()) {
            list.add(result.getString("GameName"));
        }
        return list.contains(CommandName);
    }

    public void setCommandLastHandled(String name, long value, long UserID) throws SQLException {
        if (commandNameAlreadyExists(name, UserID)) {
            connection.createStatement().execute("""
                    UPDATE "CommandStatistics"
                    SET "LastHandled" = %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(value, UserID, name));
        } else {
            connection.createStatement().execute("""
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "LastHandled")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, value));
        }
    }

    public void setGameLastPlayed(String name, long value, long UserID) throws SQLException {
        if (gameNameAlreadyExists(name, UserID)) {
            connection.createStatement().execute("""
                    UPDATE "GameStatistics"
                    SET "LastPlayed" = %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(value, UserID, name));
        } else {
            connection.createStatement().execute("""
                    INSERT INTO "GameStatistics"("UserID", "GameName", "LastPlayed")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, value));
        }
    }

    public void setLastChat(long value, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "LastChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "LastChat" = %s
                WHERE "UserID" = %s;
                """.formatted(UserID, value, value, UserID));
    }

    public void setLastReaction(long value, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "LastReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "LastReaction" = %s
                WHERE "UserID" = %s;
                """.formatted(UserID, value, value, UserID));
    }

    public void setLastVoiceJoin(long value, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "LastVoiceJoin")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "LastVoiceJoin" = %s
                WHERE "UserID" = %s;
                """.formatted(UserID, value, value, UserID));
    }

    public void incrementXP(long amount, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "XP")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XP" = "XP" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID));
    }

    public void incrementMoney(long amount, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "Money")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "Money" = "Money" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID));
    }

    public void incrementXPChat(long amount, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "XPChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPChat" = "XPChat" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID));
    }

    public void incrementXPReaction(long amount, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "XPReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPReaction" = "XPReaction" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID));
    }

    public void incrementXPVoice(long amount, long UserID) throws SQLException {
        connection.createStatement().execute("""
                INSERT INTO "UserData"("UserID", "XPVoice")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPVoice" = "XPVoice" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID));
    }

    public void incrementCommandTimesHandled(String name, long amount, long UserID) throws SQLException {
        if (commandNameAlreadyExists(name, UserID)) {
            connection.createStatement().execute("""
                    UPDATE "CommandStatistics"
                    SET "TimesHandled" = "TimesHandled" + %S
                    WHERE "UserID" = %s AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name));
        } else {
            connection.createStatement().execute("""
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "TimesHandled")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount));
        }
    }

    public void incrementCommandXP(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "CommandStatistics"
                    SET "XP"
                            = "XP" + %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "XP")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void incrementCommandMoney(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "CommandStatistics"
                    SET "Money"
                            = "Money" + %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "Money")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void incrementGamePlays(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Plays"
                            = "Plays" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Plays")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void incrementGameWins(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (gameNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Wins"
                            = "Wins" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Wins")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void incrementGameLoses(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (gameNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Loses"
                            = "Loses" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Loses")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void incrementGameDraws(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (gameNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Draws"
                            = "Draws" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Draws")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void decrementXP(long amount, long UserID) throws SQLException {
        var sql = """
                INSERT INTO "UserData"("UserID", "XP")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XP" = "XP" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        connection.createStatement().execute(sql);
    }

    public void decrementMoney(long amount, long UserID) throws SQLException {
        var sql = """
                INSERT INTO "UserData"("UserID", "Money")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "Money" = "Money" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        connection.createStatement().execute(sql);
    }

    public void decrementXPChat(long amount, long UserID) throws SQLException {
        var sql = """
                INSERT INTO "UserData"("UserID", "XPChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPChat" = "XPChat" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        connection.createStatement().execute(sql);
    }

    public void decrementXPReaction(long amount, long UserID) throws SQLException {
        var sql = """
                INSERT INTO "UserData"("UserID", "XPReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPReaction" = "XPReaction" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        connection.createStatement().execute(sql);
    }

    public void decrementXPVoice(long amount, long UserID) throws SQLException {
        var sql = """
                INSERT INTO "UserData"("UserID", "XPVoice")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPVoice" = "XPVoice" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        connection.createStatement().execute(sql);
    }

    public void decrementCommandTimesRecognized(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "CommandStatistics"
                    SET "TimesHandled"
                            = "TimesHandled" - %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "TimesHandled")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void decrementCommandXP(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "CommandStatistics"
                    SET "XP"
                            = "XP" - %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "XP")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void decrementCommandMoney(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "CommandStatistics"
                    SET "Money"
                            = "Money" - %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "Money")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void decrementGamePlays(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Plays"
                            = "Plays" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Plays")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void decrementGameWins(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Wins"
                            = "Wins" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Wins")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void decrementGameLoses(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Loses"
                            = "Loses" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Loses")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public void decrementGameDraws(String name, long amount, long UserID) throws SQLException {
        String sql;
        if (commandNameAlreadyExists(name, UserID)) {
            sql = """
                    UPDATE "GameStatistics"
                    SET "Draws"
                            = "Draws" - %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);

        } else {
            sql = """
                    INSERT INTO "GameStatistics"("UserID", "GameName", "Draws")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);

        }
        connection.createStatement().execute(sql);
    }

    public Long getCommandTimesRecognized(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "TimesHandled" from "CommandStatistics" where "UserID" = %s AND "CommandName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("TimesRecognized");
        }
        return value;
    }

    public Long getCommandLastRecognized(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "TimesHandled" from "CommandStatistics" where "UserID" = %s AND "CommandName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastRecognized");
        }
        return value;
    }

    public Long getCommandLastHandled(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "LastHandled" from "CommandStatistics" where "UserID" = %s AND "CommandName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastHandled");
        }
        return value;
    }

    public Long getCommandXP(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "XP" from "CommandStatistics" where "UserID" = %s AND "CommandName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("XP");
        }
        return value;
    }

    public Long getCommandMoney(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "Money" from "CommandStatistics" where "UserID" = %s AND "CommandName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Money");
        }
        return value;
    }

    public Long getGamePlays(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "Plays" from "GameStatistics" where "UserID" = %s AND "GameName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Plays");
        }
        return value;
    }

    public Long getGameWins(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "Wins" from "GameStatistics" where "UserID" = %s AND "GameName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Wins");
        }
        return value;
    }

    public Long getGameLoses(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "Loses" from "GameStatistics" where "UserID" = %s AND "GameName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Loses");
        }
        return value;
    }

    public Long getGameDraws(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "Draws" from "GameStatistics" where "UserID" = %s AND "GameName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Draws");
        }
        return value;
    }

    public Long getGameLastPlayed(String CommandName, long UserID) throws SQLException {
        String sql = """
                select "LastPlayed" from "GameStatistics" where "UserID" = %s AND "GameName" = '%s'
                """.formatted(UserID, CommandName);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastPlayed");
        }
        return value;
    }

    public Long getXP(long UserID) throws SQLException {
        String sql = """
                select "XP" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("XP");
        }
        return value;
    }

    public Long getMoney(long UserID) throws SQLException {
        String sql = """
                select "Money" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Money");
        }
        return value;
    }

    public Long getLastChat(long UserID) throws SQLException {
        String sql = """
                select "LastChat" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastChat");
        }
        return value;
    }

    public Long getLastReaction(long UserID) throws SQLException {
        String sql = """
                select "LastReaction" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastReaction");
        }
        return value;
    }

    public Long getLastVoiceJoin(long UserID) throws SQLException {
        String sql = """
                select "LastVoiceJoin" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastVoiceJoin");
        }
        return value;
    }

    public Long getXPChat(long UserID) throws SQLException {
        String sql = """
                select "XPChat" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("XPChat");
        }
        return value;
    }

    public Long getXPReaction(long UserID) throws SQLException {
        String sql = """
                select "XPReaction" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("XPReaction");
        }
        return value;
    }

    public Long getXPVoice(long UserID) throws SQLException {
        String sql = """
                select "XPVoice" from "UserData" where "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("XPVoice");
        }
        return value;
    }

    public Long getLottoVote(long UserID) throws SQLException {
        String sql = """
                SELECT "Vote" FROM "Lotto" WHERE "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Vote");
        }
        return value;
    }

    public Long getSkillPoints(long UserID) throws SQLException {
        String sql = """
                SELECT "SkillPoints" FROM "SkillTrees" WHERE "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("SkillPoints");
        }
        return value;
    }

    public Long getSkillPointsWorkXP(long UserID) throws SQLException {
        String sql = """
                SELECT "WorkXP" FROM "SkillTrees" WHERE "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("WorkXP");
        }
        return value;
    }

    public Long getSkillPointsWorkMoney(long UserID) throws SQLException {
        String sql = """
                SELECT "WorkMoney" FROM "SkillTrees" WHERE "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("WorkMoney");
        }
        return value;
    }

    public Long getSkillPointsGambleXP(long UserID) throws SQLException {
        String sql = """
                SELECT "GambleXP" FROM "SkillTrees" WHERE "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("GambleXP");
        }
        return value;
    }

    public Long getSkillPointsSocialXP(long UserID) throws SQLException {
        String sql = """
                SELECT "SocialXP" FROM "SkillTrees" WHERE "UserID" = %s
                """.formatted(UserID);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("SocialXP");
        }
        return value;
    }

    public void resetLottoVotes() throws SQLException {
        String sql = """
                DELETE FROM "Lotto" WHERE "UserID" != 0;
                """;
        connection.createStatement().execute(sql);
    }

    public void resetLottoPool() throws SQLException {
        String sql = """
                UPDATE "Lotto" SET "Vote" = 0 WHERE "UserID" = 0;
                """;
        connection.createStatement().execute(sql);
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

    public List<Long> getRanking() throws SQLException {
        String sql = """
                select "UserID" from "UserData" order by "Money" DESC, "XP" desc
                """;
        List<Long> list = new ArrayList<>();
        var rs = connection.prepareStatement(sql).executeQuery();
        while (rs.next()) {
            long id = rs.getLong("UserID");
            if (id == 0) break;
            list.add(id);
        }
        return list;
    }

    public long getLottoPool() throws SQLException {
        String sql = """
                SELECT "Vote" FROM "Lotto" WHERE "UserID" = 0;
                """;
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("Vote");
        }
        return value;
    }

    public void insertSkillTreeIfNotExists(long id, long skillPoints) throws SQLException {
        String sql = """
                INSERT INTO "SkillTrees"("UserID", "SkillPoints") VALUES(%s, %s) ON CONFLICT DO NOTHING;
                """.formatted(id, skillPoints);
        connection.createStatement().execute(sql);
    }

    public void resetSkillTree(long id) throws SQLException {
        long level = getXP(id) / 800;
        String sql = """
                UPDATE "SkillTrees"
                SET "SkillPoints" = %s,
                "SocialXP"    = 0,
                "GambleXP"    = 0,
                "WorkMoney"   = 0,
                "WorkXP"      = 0
                WHERE "UserID" = %s;
                """.formatted(level, id);
        connection.createStatement().execute(sql);
    }

    public void incrementSkillWorkXP(long id, int amount) throws SQLException {
        var sql = """
                UPDATE "SkillTrees" SET "WorkXP" = "WorkXP" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        connection.createStatement().execute(sql);
    }

    public void incrementSkillWorkMoney(long id, int amount) throws SQLException {
        var sql = """
                UPDATE "SkillTrees" SET "WorkMoney" = "WorkMoney" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        connection.createStatement().execute(sql);
    }

    public void incrementSkillGambleXP(long id, int amount) throws SQLException {
        var sql = """
                UPDATE "SkillTrees" SET "GambleXP" = "GambleXP" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        connection.createStatement().execute(sql);
    }

    public void incrementSkillSocialXP(long id, int amount) throws SQLException {
        var sql = """
                UPDATE "SkillTrees" SET "SocialXP" = "SocialXP" + %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        connection.createStatement().execute(sql);
    }

    public void decrementSkillPoints(long id, int amount) throws SQLException {
        var sql = """
                UPDATE "SkillTrees" SET "SkillPoints" = "SkillPoints" - %s WHERE "UserID" = %s;
                """.formatted(amount, id);
        connection.createStatement().execute(sql);
    }

    public long getSpentSkillPoints(long id) throws SQLException {
        String sql = """
                SELECT "WorkXP", "WorkMoney", "GambleXP", "SocialXP" FROM "SkillTrees" WHERE "UserID" = %s;
                """.formatted(id);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value += result.getLong("WorkMoney");
            value += result.getLong("WorkXP");
            value += result.getLong("GambleXP");
            value += result.getLong("SocialXP");
        }
        return value;
    }

    public long getSkillLastReset(long id) throws SQLException {
        String sql = """
                select "LastReset" from "SkillTrees" where "UserID" = %s
                """.formatted(id);
        var result = connection.createStatement().executeQuery(sql);
        long value = 0;
        while (result.next()) {
            value = result.getLong("LastReset");
        }
        return value;
    }

    public void setSkillLastReset(long id, long time) throws SQLException {
        var sql = """
                UPDATE SkillTrees SET LastReset = %s WHERE UserID = %s;
                """.formatted(time, id);
        connection.createStatement().execute(sql);
    }

    //__________________________________________________________________________________________________________________

    public boolean makeBet(long userID, int bet, String team) throws SQLException {
        boolean condition1 = false;
        double quote = 0;
        boolean condition = false;
        ArrayList<Match> matches = new ArrayList<>();
        CrawlerInfo.saveInMatches(matches);
        for (Match match : matches) {
            if (match.getTeam1().equalsIgnoreCase(team)) {
                quote = match.getWinRateTeam1();
                condition = true;
            }
            if (match.getTeam2().equalsIgnoreCase(team)) {
                quote = match.getWinRateTeam2();
                condition = true;
            }
        }
        if (condition) {
            if (pay(userID, bet) && bet > 0) {
                String actualTeam = Result.getActualTeam(team);
                Statement statement = connection.createStatement();
                String sql = "INSERT INTO Wette (UserID, Mannschaft, Einsatz, Quote) VALUES (%s, '%s', %s, %s)".formatted(userID, actualTeam, bet, quote);
                statement.executeUpdate(sql);
                condition1 = true;
            }
        }
        return condition1;
    }

    public ArrayList<Long> getBetUserID() throws SQLException {
        ArrayList<Long> user = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "SELECT UserID FROM Wette";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            user.add(Long.parseLong(rs.getString("UserID")));
        }
        return user;
    }

    // TODO add DB.incrementGameWins("sports", 1, id) and such in the right places (Wins, Draws and Loses)
    public void getProfit(long userID) throws SQLException {
        Statement statement = connection.createStatement();
        ArrayList<String> teams = new ArrayList<>();
        ArrayList<Integer> bet = new ArrayList<>();
        ArrayList<Double> quote = new ArrayList<>();
        String sql1 = "SELECT Einsatz, Mannschaft, Quote FROM Wette WHERE UserID = %s".formatted(userID);
        ResultSet rs = statement.executeQuery(sql1);
        while (rs.next()) {
            teams.add(rs.getString("Mannschaft"));
            bet.add(Integer.parseInt(rs.getString("Einsatz")));
            quote.add(Double.parseDouble(rs.getString("Quote")));
        }
        String url = "https://livescore.bet3000.com/de/handball/deutschland";
        for (int i = 0; i < teams.size(); i++) {
            var winner = CrawlerResult.isWinner(teams.get(i));
            var team = teams.get(i);
            if (CrawlerResult.isFinished(url, new ArrayList<String>(), team) && winner) {
                String sql2 = "UPDATE UserData SET Money = Money + %s * %s WHERE UserID = %s"
                        .formatted(bet.get(i), quote.get(i), userID);
                statement.executeUpdate(sql2);
                String sql3 = "DELETE FROM Wette WHERE Mannschaft = '%s' AND UserID = %s".formatted(teams.get(i), userID);
                statement.executeUpdate(sql3);
            } else if (CrawlerResult.isFinished(url, new ArrayList<String>(), team)) {
                String sql3 = "DELETE FROM Wette WHERE Mannschaft = '%s' AND UserID = %s".formatted(teams.get(i), userID);
                statement.executeUpdate(sql3);
            }
        }
    }

    private boolean pay(long userID, int bet) throws SQLException {
        boolean condition = false;
        Statement statement = connection.createStatement();
        String sql1 = "SELECT Money FROM UserData WHERE UserID = %s".formatted(userID);
        ResultSet rs = statement.executeQuery(sql1);
        while (rs.next()) {
            int kontostand = Integer.parseInt(rs.getString("Money"));
            if (kontostand - bet >= 0) {
                String sql = "Update UserData SET Money = Money - %s WHERE UserID = %s".formatted(bet, userID);
                statement.executeUpdate(sql);
                condition = true;
            }
        }
        return condition;
    }

    public void nPlayer(long userID) throws SQLException {
        if (!getPlayer(userID)) {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO UserData (UserID, Money) VALUES (%s, 0)".formatted(userID);
            statement.executeUpdate(sql);
        }
    }


    private boolean getPlayer(long userID) throws SQLException {
        boolean a = false;
        String sql = "SELECT UserID FROM UserData WHERE UserID = %s".formatted(userID);
        if (connection != null) {
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("UserID").equals(String.valueOf(userID))) {
                    a = true;
                }
            }
        }
        return a;
    }

}