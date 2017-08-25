package com.cydercode.homenet.server.config;

public class GpioConfiguration {

    private GpioMode mode;
    private Boolean isPullup;
    private String name;
    private Integer pin;

    private Long lastKnownValueTimestamp;
    private Object lastKnownValue;

    public GpioMode getMode() {
        return mode;
    }

    public void setMode(GpioMode mode) {
        this.mode = mode;
    }

    public Boolean getPullup() {
        return isPullup;
    }

    public void setPullup(Boolean pullup) {
        isPullup = pullup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Long getLastKnownValueTimestamp() {
        return lastKnownValueTimestamp;
    }

    public void setLastKnownValueTimestamp(Long lastKnownValueTimestamp) {
        this.lastKnownValueTimestamp = lastKnownValueTimestamp;
    }

    public Object getLastKnownValue() {
        return lastKnownValue;
    }

    public void setLastKnownValue(Object lastKnownValue) {
        this.lastKnownValue = lastKnownValue;
    }

    @Override
    public String toString() {
        return "GpioConfiguration{" +
                "mode=" + mode +
                ", isPullup=" + isPullup +
                ", name='" + name + '\'' +
                ", pin=" + pin +
                ", lastKnownValueTimestamp=" + lastKnownValueTimestamp +
                ", lastKnownValue=" + lastKnownValue +
                '}';
    }
}
