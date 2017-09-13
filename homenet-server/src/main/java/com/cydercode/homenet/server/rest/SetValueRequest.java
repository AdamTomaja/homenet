package com.cydercode.homenet.server.rest;

import lombok.Data;

@Data
public class SetValueRequest {
    private String unitId;
    private String deviceId;
    private Object value;
}
