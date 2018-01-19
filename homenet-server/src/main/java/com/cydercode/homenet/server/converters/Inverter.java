package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.server.config.GpioConfiguration;
import org.springframework.stereotype.Component;

@Component
public class Inverter {

    public Object invertIfRequired(GpioConfiguration gpio, Object value) {
        if (value == null) {
            return value;
        }

        if (gpio.getInvert()) {
            return invert(value);
        }

        return value;
    }

    private Object invert(Object value) {
        if (value instanceof Double) {
            value = ((Double) value).intValue();
        }

        if (value.equals(0)) {
            return 1;
        }

        if (value.equals(1)) {
            return 0;
        }

        throw new IllegalArgumentException("It has to be 1 or 0");
    }

}
