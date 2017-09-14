package com.cydercode.homenet.server.homelets;

import lombok.Data;

import java.util.Map;

@Data
public class HomeletOperationCall {

    private String homeletId;
    private String operation;
    private Map<String, Object> parameters;
}
