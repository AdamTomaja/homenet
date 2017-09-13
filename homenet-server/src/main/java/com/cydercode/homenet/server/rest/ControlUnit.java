package com.cydercode.homenet.server.rest;

import lombok.Data;

import java.util.List;

@Data
public class ControlUnit {

    private String id;
    private String name;
    private String description;
    private List<Device> devices;
    private UnitState.Health health;
}
