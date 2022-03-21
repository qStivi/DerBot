package de.qStivi.enitities;

import de.qStivi.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Player extends Entity {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private double money;
    private double xp;


    public Player(String displayName, long id) throws SQLException {
        super(displayName, id);
        var money = DB.getFirstInteger("Money", "Players", "ID", String.valueOf(id));
        var xp = DB.getFirstInteger("XP", "Players", "ID", String.valueOf(id));
        if (money == null || xp == null) {
            logger.info("Player does not Exist! Creating new one...");
            DB.insertInto("Players", "ID", id);
            this.money = 0;
            this.xp = 0;
        } else {
            this.money = money;
            this.xp = xp;
        }
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) throws SQLException {
        DB.update("Player", "Money", money, "ID", getId());
        this.money = money;
    }

    public double getXp() throws SQLException {
        DB.update("Player", "XP", xp, "ID", getId());
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public long getLevel() {
        return 0;
    }
}
