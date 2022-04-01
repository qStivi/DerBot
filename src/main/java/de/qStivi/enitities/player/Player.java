package de.qStivi.enitities.player;

import de.qStivi.DB;
import de.qStivi.enitities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.SQLException;

public class Player extends Entity {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Skills skills = new Skills();
    private double money;
    private double xp;
    public java.util.Date lastWork;

    public Player(String displayName, long id) throws SQLException {
        super(displayName, id);
        int money = 0;
        int xp = 0;
        int workEfficiency = 0;
        int workSpeed = 0;
        try {
            money = DB.getIntegers("Money", "Players", "ID", String.valueOf(id)).get(0);
            xp = DB.getIntegers("XP", "Players", "ID", String.valueOf(id)).get(0);
            workEfficiency = DB.getIntegers("WorkEfficiency", "Skills", "ID", String.valueOf(id)).get(0);
            workSpeed = DB.getIntegers("WorkSpeed", "Skills", "ID", String.valueOf(id)).get(0);
        } catch (IndexOutOfBoundsException ignored) {
            logger.info("Player does not Exist! Creating new one...");
            DB.insertInto("Players", "ID", id);
            DB.insertInto("SKILLS", "ID", id);
        } finally {
            this.money = money;
            this.xp = xp;
            this.skills.setWorkEfficiency(workEfficiency, id);
            this.skills.setWorkSpeed(workSpeed, id);
            this.lastWork = new Date(0);
        }
    }

    public Skills getSkills() {
        return skills;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) throws SQLException {
        DB.update("Players", "Money", money, "ID", getId());
        this.money = money;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) throws SQLException {
        DB.update("Players", "XP", xp, "ID", getId());
        this.xp = xp;
    }

    public long getLevel() {
        return Math.floorDiv((int) getXp(), 1000);
    }

    public long getAvailableSkillPoints() {
        return getLevel() - this.skills.getTotal();
    }
}
