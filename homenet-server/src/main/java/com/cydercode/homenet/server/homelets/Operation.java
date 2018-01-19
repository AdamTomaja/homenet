package com.cydercode.homenet.server.homelets;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Map;

public class Operation {

    private final ScriptObjectMirror function;
    private final Map attributes;

    public Operation(ScriptObjectMirror function, Map attributes) {
        this.function = function;
        this.attributes = attributes;
    }

    public ScriptObjectMirror getFunction() {
        return function;
    }

    public boolean isAsynchronous() {
        return Boolean.TRUE.equals(attributes.get("asynchronous"));
    }
}
