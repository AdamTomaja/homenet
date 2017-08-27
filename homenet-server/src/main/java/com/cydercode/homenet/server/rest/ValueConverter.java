package com.cydercode.homenet.server.rest;

import com.cydercode.homenet.server.config.GpioConfiguration;

import java.util.Objects;


public class ValueConverter {

    public static final String ON = "ON";
    public static final String OFF = "OFF";

    public static Object convertToUIValue(GpioConfiguration gpio, Object systemValue) {
        Object value = Objects.equals(0d, gpio.getLastKnownValue()) ? OFF : ON;

        if (gpio.getInvert() != null && gpio.getInvert()) {
            if (value.equals(ON)) {
                value = OFF;
            } else {
                value = ON;
            }
        }

        return value;
    }

    public static Object convertToSystemValue(Object uiValue) {
        return Objects.equals(ON, uiValue) ? 1 : 0;
    }
}
