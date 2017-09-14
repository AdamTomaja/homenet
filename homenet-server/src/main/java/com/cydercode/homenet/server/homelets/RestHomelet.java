package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.cdm.Parameter;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class RestHomelet {

    private String id;
    private String name;
    private String type;
    private List<Parameter> parameters;
    private Set<String> operations;
}
