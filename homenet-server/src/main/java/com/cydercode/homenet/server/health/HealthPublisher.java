package com.cydercode.homenet.server.health;

import com.cydercode.homenet.server.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class HealthPublisher implements ApplicationListener<InstanceHealthEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthPublisher.class);

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    public void onApplicationEvent(InstanceHealthEvent event) {
        LOGGER.info("Instance {} now has health: {}", event.getInstance().getId(), event.getHealth());
        webSocketHandler.sendMessage(event);
    }
}
