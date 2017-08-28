package com.cydercode.homenet.server.rest;

import java.util.List;

public class ControlUnit {

    private String id;
    private String name;
    private String description;
    private List<Device> devices;
    private UnitState.Health health;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public UnitState.Health getHealth() {
        return health;
    }

    public void setHealth(UnitState.Health health) {
        this.health = health;
    }
}
