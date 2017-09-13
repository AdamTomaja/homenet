package com.cydercode.homenet.server.config;

import com.cydercode.homenet.cdm.GpioMode;
import com.google.common.base.Objects;
import lombok.Data;

public class GpioConfiguration {

    private GpioMode mode;
    private boolean isPullup;
    private String name;
    private String description;
    private Integer pin;
    private boolean invert;
    private Integer initialValue;
    private String displayAs;
    private boolean favorite;

    private Long lastKnownValueTimestamp;
    private Object lastKnownValue;

    public GpioMode getMode() {
        return mode;
    }

    public void setMode(GpioMode mode) {
        this.mode = mode;
    }

    public boolean getPullup() {
        return isPullup;
    }

    public void setPullup(boolean pullup) {
        isPullup = pullup;
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

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public boolean getInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public Integer getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

    public String getDisplayAs() {
        return displayAs;
    }

    public void setDisplayAs(String displayAs) {
        this.displayAs = displayAs;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GpioConfiguration that = (GpioConfiguration) o;
        return mode == that.mode &&
                Objects.equal(isPullup, that.isPullup) &&
                Objects.equal(name, that.name) &&
                Objects.equal(description, that.description) &&
                Objects.equal(pin, that.pin) &&
                Objects.equal(invert, that.invert) &&
                Objects.equal(initialValue, that.initialValue) &&
                Objects.equal(lastKnownValueTimestamp, that.lastKnownValueTimestamp) &&
                Objects.equal(lastKnownValue, that.lastKnownValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mode, isPullup, name, description, pin, invert, initialValue, lastKnownValueTimestamp, lastKnownValue);
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
