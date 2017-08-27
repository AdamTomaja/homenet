package com.cydercode.homenet.server.messaging.messagehandlers;

public interface TypedMessageHandler<T> {

    void handleMessage(T message);

}
