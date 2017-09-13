package com.cydercode.homenet.server.rest;

import lombok.Data;

@Data
public class WSMessage {
    private String type;
    private Object message;
}
