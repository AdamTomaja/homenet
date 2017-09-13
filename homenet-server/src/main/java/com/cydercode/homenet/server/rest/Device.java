package com.cydercode.homenet.server.rest;

import com.cydercode.homenet.cdm.GpioMode;
import lombok.Data;

@Data
public class Device {
    private Object id;
    private String name;
    private String description;
    private GpioMode type;
    private Object value;
    private boolean favorite;
}
