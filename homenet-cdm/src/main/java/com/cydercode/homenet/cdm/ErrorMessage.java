package com.cydercode.homenet.cdm;

import static com.cydercode.homenet.cdm.HomenetTopics.UCU_GPIO_CONFIGURE;
import static com.cydercode.homenet.cdm.HomenetTopics.UMU_ERROR;

@UMUTopic(UMU_ERROR)
public class ErrorMessage {

    private String instanceId;
    private String error;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "instanceId='" + instanceId + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
