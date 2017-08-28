package com.cydercode.homenet.server.rest;

import com.cydercode.homenet.cdm.GpioMode;
import com.cydercode.homenet.server.config.GpioConfiguration;

import java.util.Objects;


public class ValueConverter {

    public static final String ON = "ON";
    public static final String OFF = "OFF";

    static final int SERIESRESISTOR = 10000;
    static final int NOMINAL_RESISTANCE = 10000;
    static final int NOMINAL_TEMPERATURE = 25;
    static final int BCOEFFICIENT = 3950;

    public static Object convertToUIValue(GpioConfiguration gpio, Object systemValue) {
        if (systemValue == null) {
            return systemValue;
        }

        if (gpio.getMode() == GpioMode.ANALOG_INPUT) {
            if ("temperature".equals(gpio.getDisplayAs())) {
                return convertRawADCToTemperature((Double) systemValue) + " ℃";
            } else if ("voltage".equals(gpio.getDisplayAs())) {
                return convertRawADCToVoltage((Double) systemValue) + " V";
            } else {
                return systemValue;
            }
        }

        Object value = Objects.equals(0d, systemValue) ? OFF : ON;

        if (gpio.getInvert()) {
            if (value.equals(ON)) {
                value = OFF;
            } else {
                value = ON;
            }
        }

        return value;
    }

    private static double convertRawADCToVoltage(double rawADC) {
        return rawADC / 1024 * 3.3;
    }

    private static double convertRawADCToTemperature(double rawAdc) {
        double resistance;
        double steinhart;

        resistance = (1023 / rawAdc) - 1;
        resistance = SERIESRESISTOR / resistance;

        steinhart = resistance / NOMINAL_RESISTANCE; // (R/Ro)
        steinhart = Math.log(steinhart); // ln(R/Ro)
        steinhart /= BCOEFFICIENT; // 1/B * ln(R/Ro)
        steinhart += 1.0 / (NOMINAL_TEMPERATURE + 273.15); // + (1/To)
        steinhart = 1.0 / steinhart; // Invert
        steinhart -= 273.15; // convert to C
        return steinhart;
    }

    public static Object convertToSystemValue(Object uiValue) {
        return Objects.equals(ON, uiValue) ? 1 : 0;
    }
}
