package com.cydercode.homenet.cdm;

import static com.cydercode.homenet.cdm.HomenetTopics.UCU_GPIO_CONFIGURE;

@UCUTopic(UCU_GPIO_CONFIGURE)
public class ConfigureGpioMessage {

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

    @Override
    public String toString() {
        return "ConfigureGpioMessage{" +
                "instanceId='" + instanceId + '\'' +
                ", pin=" + pin +
                ", mode=" + mode +
                ", isPullup=" + isPullup +
                '}';
    }
}
