package com.cydercode.homenet.server;

import java.util.HashMap;
import java.util.Map;

public class UcuInstance {

    private final String id;

    private Map<Integer, Object> lastKnownValues = new HashMap<>();

    public UcuInstance(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<Integer, Object> getLastKnownValues() {
        return lastKnownValues;
    }
}

