package com.cydercode.homenet.server;

public interface TypedMessageHandler<T> {

    void handleMessage(T message);

}
