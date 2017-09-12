package com.cydercode.homenet.server.homelets;

import java.util.Map;

public class HomeletOperationCall {

    private String homeletName;
    private String operation;
    private Map<String, Object> parameters;

    public String getHomeletName() {
        return homeletName;
    }

    public void setHomeletName(String homeletName) {
        this.homeletName = homeletName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
