package com.cydercode.homenet.server.config;

import lombok.Data;

@Data
public class MqttBrokerConfiguration {
    private String host;
    private Integer port;
}
