package com.cydercode.homenet.server.messages;

import org.immutables.value.Value;

@Value.Immutable
public interface SetGpioValueMessage {

    int getPin();
    Object getValue();

}
