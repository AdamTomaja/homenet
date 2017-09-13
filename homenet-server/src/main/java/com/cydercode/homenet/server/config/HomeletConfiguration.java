package com.cydercode.homenet.server.config;

import lombok.Data;

import java.util.Map;

@Data
public class HomeletConfiguration {
    private String name;
    private String description;
    private String source;
    private String type;
    private Map<String, Object> parameters;
}
