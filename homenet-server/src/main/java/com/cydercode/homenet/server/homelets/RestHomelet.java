package com.cydercode.homenet.server.homelets;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class RestHomelet {

    private String id;
    private String name;
    private String type;
    private Map<String, Object> parameters;
    private Set<String> operations;
}
