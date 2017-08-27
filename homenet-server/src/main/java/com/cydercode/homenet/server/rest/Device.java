package com.cydercode.homenet.server.rest;

import com.cydercode.homenet.cdm.GpioMode;
import com.cydercode.homenet.server.config.GpioConfiguration;

public class Device {

    private Object id;
    private String name;
    private String description;
    private GpioMode type;
    private Object value;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
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

    public GpioMode getType() {
        return type;
    }

    public void setType(GpioMode type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
