package com.cydercode.homenet.server.rest;

public class UnitState {

    public enum Health {
        HEALTHY, DISCONNECTED, UNKNOWN
    }

    private String unitId;
    private Health health;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }
}
