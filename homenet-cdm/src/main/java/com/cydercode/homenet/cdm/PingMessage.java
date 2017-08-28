package com.cydercode.homenet.cdm;

import static com.cydercode.homenet.cdm.HomenetTopics.UCU_PING;
import static com.cydercode.homenet.cdm.HomenetTopics.UMU_PING;

@UMUTopic(UMU_PING)
@UCUTopic(UCU_PING)
public class PingMessage {

    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
