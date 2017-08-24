package com.cydercode.homenet.server.messages;

import org.immutables.value.Value;

@Value.Immutable
public interface HelloMessage {

    String getInstanceId();

}
