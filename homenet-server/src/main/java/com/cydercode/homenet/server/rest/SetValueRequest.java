package com.cydercode.homenet.server.rest;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.config.GpioConfiguration;

public class SetValueRequest {

    private String unitId;
    private String deviceId;
    private Object value;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
