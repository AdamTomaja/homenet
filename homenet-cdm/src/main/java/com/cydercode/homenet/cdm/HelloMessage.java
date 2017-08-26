package com.cydercode.homenet.cdm;

public class HelloMessage {

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "HelloMessage{" +
                "instanceId='" + instanceId + '\'' +
                '}';
    }
}
