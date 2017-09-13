package com.cydercode.homenet.server.config;

import lombok.Data;

import java.util.List;

@Data
public class Configuration {
    private MqttBrokerConfiguration mqtt;
    private List<UcuInstance> instances;
    private List<HomeletConfiguration> homelets;
}
