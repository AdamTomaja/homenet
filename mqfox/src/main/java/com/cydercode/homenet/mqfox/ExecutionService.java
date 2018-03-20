package com.cydercode.homenet.mqfox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionService.class);

    public void execute(Command command) {
        LOGGER.info("Executing command: {}", command);
    }
}
