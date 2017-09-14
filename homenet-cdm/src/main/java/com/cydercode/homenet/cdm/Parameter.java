package com.cydercode.homenet.cdm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Parameter {

    public enum ParameterType {
        STRING, INTEGER
    }

    private String name;
    private String description;
    private ParameterType type;
    private Object value;
}
