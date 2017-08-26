package com.cydercode.homenet.cdm;

public class SetGpioValueMessage {

    String instanceId;
    int pin;
    Object value;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }



    @Override
    public String toString() {
        return "SetGpioValueMessage{" +
                "instanceId='" + instanceId + '\'' +
                ", pin=" + pin +
                ", value=" + value +
                '}';
    }
}
