package com.cydercode.homenet.server.messages;

public class ConfigureGpioMessage {

    public enum GpioMode {
        INPUT, OUTPUT
    }

    String instanceId;
    int pin;
    GpioMode mode;
    boolean isPullup;

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

    public GpioMode getMode() {
        return mode;
    }

    public void setMode(GpioMode mode) {
        this.mode = mode;
    }

    public boolean isPullup() {
        return isPullup;
    }

    public void setPullup(boolean pullup) {
        isPullup = pullup;
    }
}
