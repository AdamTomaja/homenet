package com.cydercode.homenet.server.messages;

import org.immutables.value.Value;

@Value.Immutable
public interface SetGpioValueMessage {

    String getInstanceId();
    int getPin();
    Object getValue();

}
