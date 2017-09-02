package com.cydercode.homenet.server.messaging.messagehandlers;

import com.cydercode.homenet.cdm.PingMessage;
import com.cydercode.homenet.server.StateCache;
import com.cydercode.homenet.server.health.InstancesHealthService;
import com.cydercode.homenet.server.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@MessageType(PingMessage.class)
@Component
public class PingMessageHandler implements TypedMessageHandler<PingMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstancesHealthService.class);

    @Autowired
    private StateCache stateCache;

    @Override
    public void handleMessage(PingMessage message) {
        LOGGER.debug("Ping received from {}", message.getInstanceId());
        stateCache.setLastHeartBeat(message.getInstanceId(), new Date().getTime());
    }
}
