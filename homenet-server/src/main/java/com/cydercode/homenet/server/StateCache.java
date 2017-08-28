package com.cydercode.homenet.server;

import com.cydercode.homenet.server.config.UcuInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class StateCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateCache.class);

    @Autowired
    private ConfigurationService configurationService;

    public void setState(String instanceId, int pin, Object value) {
        executeOnInstanceIfExist(instanceId, ucuInstance -> ucuInstance.executeOnGpioIfExists(pin, gpio -> {
            gpio.setLastKnownValue(value);
            gpio.setLastKnownValueTimestamp(new Date().getTime());
        }));
    }

    public void setLastHelloTimestamp(String instanceId, long time) {
        executeOnInstanceIfExist(instanceId, ucuInstance -> ucuInstance.setLastHelloTimestamp(time));
    }

    private void executeOnInstanceIfExist(String instanceId, Consumer<UcuInstance> instanceConsumer) {
        Optional<UcuInstance> instanceConfiguration = configurationService.getConfiguration(instanceId);

        if (instanceConfiguration.isPresent()) {
            instanceConsumer.accept(instanceConfiguration.get());
        } else {
            LOGGER.warn("Instance {} not found in configuration, ignoring state", instanceId);
        }
    }
}
