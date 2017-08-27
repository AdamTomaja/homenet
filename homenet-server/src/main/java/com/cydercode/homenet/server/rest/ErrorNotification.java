package com.cydercode.homenet.server.rest;

public class ErrorNotification {

    private String unitId;
    private String error;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
