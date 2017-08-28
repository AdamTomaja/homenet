package com.cydercode.homenet.server.health;

import com.cydercode.homenet.cdm.PingMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.messaging.MessageBus;
import com.cydercode.homenet.server.rest.UnitState;
import com.cydercode.homenet.server.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.cydercode.homenet.server.Time.getTimestamp;
import static com.cydercode.homenet.server.rest.UnitState.Health.*;

@Service
public class InstancesHealthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstancesHealthService.class);

    private final Map<UcuInstance, UnitState.Health> healthStates = new HashMap<>();

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageBus messageBus;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Scheduled(initialDelay = 1_000, fixedRate = 5000)
    public void sendPing() throws Exception {
        messageBus.sendMessage(new PingMessage());
    }

    @Scheduled(initialDelay = 2_000, fixedRate = 5000)
    public void checkInstancesHealth() {
        configurationService.getConfiguration()
                .getInstances()
                .forEach(this::checkInstanceHealth);
    }

    private void checkInstanceHealth(UcuInstance ucuInstance) {
        UnitState.Health currentHealth = getHealth(ucuInstance);
        if (!currentHealth.equals(healthStates.get(ucuInstance))) {
            publisher.publishEvent(new InstanceHealthEvent(this, ucuInstance, currentHealth));
            healthStates.put(ucuInstance, currentHealth);
        }
    }

    public UnitState.Health getHealth(UcuInstance ucuInstance) {
        if (ucuInstance.getLastHeartBeatTime() == null) {
            return UNKNOWN;
        }

        return getTimestamp() - ucuInstance.getLastHeartBeatTime() > 15_000 ? DISCONNECTED : HEALTHY;
    }
}
