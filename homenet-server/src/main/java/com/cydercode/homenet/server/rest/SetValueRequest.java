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

    public static SetValueRequest fromSetGpioValueMessage(GpioConfiguration gpio, SetGpioValueMessage setGpioValueMessage) {
        SetValueRequest setValueRequest = new SetValueRequest();
        setValueRequest.setUnitId(String.valueOf(setGpioValueMessage.getPin()));
        setValueRequest.setDeviceId(setGpioValueMessage.getInstanceId());
        setValueRequest.setValue(ValueConverter.convertToUIValue(gpio, setGpioValueMessage.getValue()));
        return setValueRequest;
    }
}
