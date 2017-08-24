package com.cydercode.homenet.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StateCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateCache.class);

    public void setState(String instanceId, int pin, Object value) {
        LOGGER.info("State changed in instance {} for pin {} to value {}", instanceId, pin, value);
    }
}
