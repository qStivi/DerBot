package qStivi.db;

import org.slf4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("unused")
public class DB {
    //TODO make every number a long
    private static final Logger logger = getLogger(DB.class);

    public DB() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        String url = "jdbc:sqlite:bot.db";
        Connection conn = DriverManager.getConnection(url);
        String enableForeignKeys = "PRAGMA foreign_keys = ON;";
        String setupUserDataTable = """
                CREATE TABLE IF NOT EXISTS "UserData"
                (
                    "UserID"        INTEGER PRIMARY KEY NOT NULL,
                    "XP"            INTEGER             NOT NULL DEFAULT 0,
                    "Money"         INTEGER             NOT NULL DEFAULT 0,
                    "LastChat"      INTEGER             NOT NULL DEFAULT 0,
                    "LastReaction"  INTEGER             NOT NULL DEFAULT 0,
                    "LastCommand"   INTEGER             NOT NULL DEFAULT 0,
                    "LastVoiceJoin" INTEGER             NOT NULL DEFAULT 0,
                    "XPChat"        INTEGER             NOT NULL DEFAULT 0,
                    "XPReaction"    INTEGER             NOT NULL DEFAULT 0,
                    "XPCommand"     INTEGER             NOT NULL DEFAULT 0,
                    "XPVoice"       INTEGER             NOT NULL DEFAULT 0
                );
                """;
        String setupCommandsStatisticsTable = """
                CREATE TABLE IF NOT EXISTS "CommandStatistics"
                (
                    "UserID"          INTEGER NOT NULL,
                    "CommandName"     TEXT    NOT NULL,
                    "TimesRecognized" INTEGER NOT NULL DEFAULT 0,
                    "LastRecognized"  INTEGER NOT NULL DEFAULT 0,
                    "LastHandled"     INTEGER NOT NULL DEFAULT 0,
                    "XP"              INTEGER NOT NULL DEFAULT 0,
                    "Money"           INTEGER NOT NULL DEFAULT 0,
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


        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            logger.info("The driver name is " + meta.getDriverName());
            logger.info("Database connection ok.");
            conn.createStatement().execute(enableForeignKeys);
            conn.createStatement().execute(setupUserDataTable);
            conn.createStatement().execute(setupCommandsStatisticsTable);
            conn.createStatement().execute(setupGameStatisticsTable);
        }

    }

    public Long getLevel(Long id) {
        var xp = selectLong("users", "xp", "id", id);
        xp = xp == null ? 0 : xp;
        return (long) Math.floor((double) xp / 800);
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param table   name of table to insert to
     * @param colName name of column to insert to
     * @param value   value to insert to
     */
    @Deprecated
    public void insert(String table, String colName, Object value) {
        String sql = "INSERT INTO %s(%s) VALUES(?)".formatted(table, colName);

        executeUpdate(value, sql);
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

    /**
     * Update data of a warehouse specified by the id
     *
     * @param tblName    name of table to update
     * @param colName    name of column to update
     * @param whereName  name of column to use as identifier
     * @param whereValue value of identifier
     * @param value      new value
     */
    public void update(String tblName, String colName, String whereName, Object whereValue, Object value) {
        String sql = "UPDATE %s SET %s = ? WHERE %s = ?".formatted(tblName, colName, whereName);

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt;
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setObject(1, value);
                pstmt.setObject(2, whereValue);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update data of a warehouse specified by the id
     *
     * @param tblName    name of table to update
     * @param colName    name of column to update
     * @param whereName  name of column to use as identifier
     * @param whereValue value of identifier
     * @param value      new value
     */
    @Deprecated
    public void increment(String tblName, String colName, String whereName, Object whereValue, long value) throws SQLException {
        String sql = "UPDATE %s SET %s = %s + ? WHERE %s = ?".formatted(tblName, colName, colName, whereName);

        inDeCrement(whereValue, value, sql);
    }

    /**
     * Update data of a warehouse specified by the id
     *
     * @param tblName    name of table to update
     * @param colName    name of column to update
     * @param whereName  name of column to use as identifier
     * @param whereValue value of identifier
     * @param value      new value
     */
    @Deprecated
    public void decrement(String tblName, String colName, String whereName, Object whereValue, long value) throws SQLException {
        String sql = "UPDATE %s SET %s = %s - ? WHERE %s = ?".formatted(tblName, colName, colName, whereName);

        inDeCrement(whereValue, value, sql);
    }

    @Deprecated
    public void incrementUserDataValue(String column, String at, long amount, long value) throws SQLException {
        String sql = "INSERT INTO \"UserData\"(\"%s\", \"%s\") VALUES(%s, %s) ON CONFLICT(\"%s\") DO UPDATE SET \"%s\" = \"%s\" + %s WHERE \"%s\" = %s".formatted(at, column, value, amount, at, column, column, amount, at, value);

        Objects.requireNonNull(connect()).createStatement().execute(sql);
    }

    public boolean commandNameAlreadyExists(String CommandName, long UserID) throws SQLException {
        var connection = connect();
        var select = """
                SELECT "CommandName"
                FROM "CommandStatistics"
                WHERE "UserID" = '%s';
                """.formatted(UserID);
        var list = new ArrayList<String>();
        if (connection != null) {
            var result = connection.createStatement().executeQuery(select);
            while (result.next()) {
                var temp = result.getString("CommandName");
                list.add(temp);
                logger.info(temp);
            }
        }
        return list.contains(CommandName);
    }

    public void incrementXP(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XP")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XP" = "XP" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void incrementMoney(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "Money")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "Money" = "Money" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void incrementXPChat(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPChat" = "XPChat" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void incrementXPReaction(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPReaction" = "XPReaction" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void incrementXPCommand(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPCommand")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPCommand" = "XPCommand" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void incrementXPVoice(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPVoice")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPVoice" = "XPVoice" + %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void incrementCommandTimesRecognized(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "CommandStatistics"
                    SET "TimesRecognized"
                            = "TimesRecognized" + %S
                    WHERE "UserID" = %s
                      AND "CommandName" = '%s'
                    """.formatted(amount, UserID, name);
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "TimesRecognized")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "XP")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "Money")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Plays")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
    }

    public void incrementGameWins(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Wins"
                            = "Wins" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Wins")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
    }

    public void incrementGameLoses(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Loses"
                            = "Loses" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Loses")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
    }

    public void incrementGameDraws(String name, long amount, long UserID) throws SQLException {
        String query;
        if (commandNameAlreadyExists(name, UserID)) {
            query = """
                    UPDATE "GameStatistics"
                    SET "Draws"
                            = "Draws" + %S
                    WHERE "UserID" = %s
                      AND "GameName" = '%s'
                    """.formatted(amount, UserID, name);
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Draws")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
    }

    public void decrementXP(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XP")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XP" = "XP" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void decrementMoney(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "Money")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "Money" = "Money" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void decrementXPChat(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPChat")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPChat" = "XPChat" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void decrementXPReaction(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPReaction")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPReaction" = "XPReaction" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void decrementXPCommand(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPCommand")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPCommand" = "XPCommand" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
    }

    public void decrementXPVoice(long amount, long UserID) throws SQLException {
        var upsert = """
                INSERT INTO "UserData"("UserID", "XPVoice")
                VALUES (%s, %s)
                ON CONFLICT("UserID") DO UPDATE SET "XPVoice" = "XPVoice" - %s
                WHERE "UserID" = %s;
                """.formatted(UserID, amount, amount, UserID);
        Objects.requireNonNull(connect()).createStatement().execute(upsert);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "TimesRecognized")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "XP")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "CommandName", "Money")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Plays")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Wins")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Loses")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
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
            logger.info("update");
        } else {
            query = """
                    INSERT INTO "CommandStatistics"("UserID", "GameName", "Draws")
                    VALUES (%s, '%s', %s)
                    """.formatted(UserID, name, amount);
            logger.info("insert");
        }
        Objects.requireNonNull(connect()).createStatement().execute(query);
    }

    @Deprecated
    public void incrementCommandStatisticsOrGameStatisticsValue(String table, String column, String nameColumn, String at, long amount, String name, long value) throws SQLException {
        List<String> list = new ArrayList<>();
        String select = "SELECT \"%s\" FROM \"%s\" WHERE \"%s\" = '%s'".formatted(column, table, at, value);
        String query;

        Connection conn = connect();
        if (conn != null) {
            var result = conn.prepareStatement(select).executeQuery();
            while (result.next()) {
                list.add(result.getString(nameColumn));
            }
        }
        if (list.contains(name)) {
            query = "UPDATE \"%s\" SET \"%s\" = \"%s\" + %s WHERE \"%s\" = %s".formatted(table, column, column, amount, at, value);
        } else {
            query = "INSERT INTO \"%s\"(\"%s\", \"%s\", \"%s\") VALUES (%s, '%s', %s);".formatted(table, at, nameColumn, column, value, name, amount);
        }
        Objects.requireNonNull(conn).createStatement().execute(query);
    }

    @Deprecated
    private void inDeCrement(Object whereValue, long value, String sql) throws SQLException {
        Connection conn = this.connect();
        PreparedStatement pstmt;
        if (conn != null) {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, value);
            pstmt.setObject(2, whereValue);
            pstmt.executeUpdate();
        }
    }

    public Long getLast(String col, long id) {
        var seconds = selectLong("users", col, "id", id);
        seconds = seconds == null ? 0 : seconds;
        var millis = seconds * 1000;
        var last = new java.util.Date(millis);
        var now = new Date();
        return (now.getTime() - last.getTime()) / 1000;
    }

    /**
     * Update data of a warehouse specified by the id
     *
     * @param tblName    name of table to update
     * @param colName    name of column to update
     * @param whereName  name of column to use as identifier
     * @param whereValue value of identifier
     * @return value
     */
    @Nullable
    @CheckForNull
    public Long selectLong(String tblName, String colName, String whereName, Object whereValue) {
        String sql = "select %s from %s where %s = ?".formatted(colName, tblName, whereName);

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt;
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                pstmt.setObject(1, whereValue);
                var rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getLong(colName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        String url = "jdbc:sqlite:bot.db";
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public List<Long> getRanking() {
        String sql = "select id from users order by money DESC, xp desc";
        List<Long> list = new ArrayList<>();

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt;
            if (conn != null) {
                pstmt = conn.prepareStatement(sql);
                var rs = pstmt.executeQuery();
                while (rs.next()) {
                    list.add(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}