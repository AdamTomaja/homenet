package com.cydercode.homenet.server.messaging;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {

    Object message;

    public MessageEvent(Object source) {
        super(source);
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
