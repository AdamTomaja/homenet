package com.cydercode.homenet.server.rest;

import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.GpioMode;

import java.util.Objects;

public class Device {

    public enum Type {
        INPUT, OUTPUT;

        public static Type fromGpioMode(GpioMode gpioMode) {
            switch (gpioMode) {
                case INPUT:
                    return INPUT;
                case OUTPUT:
                    return OUTPUT;
            }

            throw new IllegalArgumentException("Unknown Gpio mode");
        }
    }

    private Object id;
    private String name;
    private String description;
    private Type type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static Device fromGpio(GpioConfiguration gpio) {
        Device device = new Device();
        device.setId(gpio.getPin());
        device.setDescription(gpio.getDescription());
        device.setName(gpio.getName());
        device.setValue(Objects.equals(0d, gpio.getLastKnownValue()) ? "OFF" : "ON");
        if (gpio.getInvert() != null && gpio.getInvert()) {
            if (device.getValue().equals("ON")) {
                device.setValue("OFF");
            } else {
                device.setValue("ON");
            }
        }

        device.setType(Type.fromGpioMode(gpio.getMode()));
        return device;
    }
}
