package de.qStivi.enitities.player;

import de.qStivi.DB;

import java.sql.SQLException;

public class Skills {

    private long workEfficiency;
    private long workSpeed;

    Skills() {
        this.workEfficiency = 0;
        this.workSpeed = 0;
    }

    public double getWorkEfficiency() {
        return workEfficiency;
    }

    public void setWorkEfficiency(long workEfficiency, long id) throws SQLException {
        DB.update("Skills", "WorkEfficiency", workEfficiency, "ID", id);
        this.workEfficiency = workEfficiency;
    }

    public double getWorkSpeed() {
        return workSpeed;
    }

    public void setWorkSpeed(long workSpeed, long id) throws SQLException {
        DB.update("Skills", "WorkSpeed", workSpeed, "ID", id);
        this.workSpeed = workSpeed;
    }

    public long getTotal() {
        return this.workEfficiency + this.workSpeed;
    }
}
