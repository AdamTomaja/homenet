package com.cydercode.homenet.virtualucu;


import org.springframework.stereotype.Component;

@Component
public class Instance {

    private String instanceName = "virtual-ucu";

    public String getID() {
        return instanceName;
    }
}
