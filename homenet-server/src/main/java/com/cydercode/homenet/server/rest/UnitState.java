package com.cydercode.homenet.server.rest;

import lombok.Data;

@Data
public class UnitState {

    public enum Health {
        HEALTHY, DISCONNECTED, UNKNOWN
    }

    private String unitId;
    private Health health;
}
