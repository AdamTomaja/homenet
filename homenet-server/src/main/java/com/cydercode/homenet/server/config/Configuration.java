package com.cydercode.homenet.server.config;

import java.util.List;

public class Configuration {

    private MqttBrokerConfiguration mqtt;

    private List<UcuInstance> instances;

    private List<HomeletConfiguration> homelets;

    public MqttBrokerConfiguration getMqtt() {
        return mqtt;
    }

    public void setMqtt(MqttBrokerConfiguration mqtt) {
        this.mqtt = mqtt;
    }

    public List<UcuInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<UcuInstance> instances) {
        this.instances = instances;
    }

    public List<HomeletConfiguration> getHomelets() {
        return homelets;
    }

    public void setHomelets(List<HomeletConfiguration> homelets) {
        this.homelets = homelets;
    }
}
