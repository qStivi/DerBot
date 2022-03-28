package de.qStivi.enitities.player;

import de.qStivi.DB;

import java.sql.SQLException;

public class Skills {

    private double workEfficiency;
    private double workSpeed;

    Skills() {
        this.workEfficiency = 0;
        this.workSpeed = 0;
    }

    public double getWorkEfficiency() {
        return workEfficiency;
    }

    public void setWorkEfficiency(double workEfficiency, long id) throws SQLException {
        DB.update("Skills", "WorkEfficiency", workEfficiency, "ID", id);
        this.workEfficiency = workEfficiency;
    }

    public double getWorkSpeed() {
        return workSpeed;
    }

    public void setWorkSpeed(double workSpeed, long id) throws SQLException {
        DB.update("Skills", "WorkSpeed", workSpeed, "ID", id);
        this.workSpeed = workSpeed;
    }
}
