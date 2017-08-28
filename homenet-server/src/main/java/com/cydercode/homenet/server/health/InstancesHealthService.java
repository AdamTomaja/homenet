package com.cydercode.homenet.server.health;

import com.cydercode.homenet.cdm.PingMessage;
import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.messaging.MessageBus;
import com.cydercode.homenet.server.rest.DeviceState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.cydercode.homenet.server.Time.getTimestamp;
import static com.cydercode.homenet.server.rest.DeviceState.Health.*;

@Service
public class InstancesHealthService {

    @Autowired
    private MessageBus messageBus;

    @Scheduled(initialDelay = 5_000, fixedRate = 5000)
    public void sendPing() throws Exception {
        messageBus.sendMessage(new PingMessage());
    }

    public DeviceState.Health getHealth(UcuInstance ucuInstance) {
        if (ucuInstance.getLastHeartBeatTime() == null) {
            return UNKNOWN;
        }

        return getTimestamp() - ucuInstance.getLastHeartBeatTime() > 15_000 ? DISCONNECTED : HEALTHY;
    }
}
