package com.cydercode.homenet.server.messages;

import org.immutables.value.Value;

@Value.Immutable
public interface ConfigureGpioMessage {

    public enum GpioMode {
        INPUT, OUTPUT
    }

    String getInstanceId();
    int getPin();
    GpioMode getMode();
    boolean isPullup();

}
