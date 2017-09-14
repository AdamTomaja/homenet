package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.cdm.Parameter;
import lombok.Data;

import java.util.List;

@Data
public class HomeletConfiguration {
    private String name;
    private String description;
    private String source;
    private String type;
    private List<Parameter> parameters;
}
