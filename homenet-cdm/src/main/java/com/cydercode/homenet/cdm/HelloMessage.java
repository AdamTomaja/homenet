package com.cydercode.homenet.cdm;

import static com.cydercode.homenet.cdm.HomenetTopics.UCU_HELLO;
import static com.cydercode.homenet.cdm.HomenetTopics.UMU_HELLO;

@UCUTopic(UCU_HELLO)
@UMUTopic(UMU_HELLO)
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
