package com.cydercode.homenet.server.rest;

import lombok.Data;

@Data
public class ErrorNotification {
    private String unitId;
    private String error;
}
