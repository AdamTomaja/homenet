package com.cydercode.homenet.server;

import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.messages.ConfigureGpioMessage;
import com.cydercode.homenet.server.messages.HelloMessage;
import com.cydercode.homenet.server.messages.SetGpioValueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private StateCache stateCache;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FlowService flowService;

    public void handleSetGpioValueMessage(MessageService messageService, SetGpioValueMessage setGpioMessage) {
        stateCache.setState(setGpioMessage.getInstanceId(), setGpioMessage.getPin(), setGpioMessage.getValue());
        flowService.postMessage(setGpioMessage);
    }

    public void handleHelloMessage(MessageService messageService, HelloMessage helloMessage) {
        String instanceId = helloMessage.getInstanceId();

        stateCache.setLastHelloTimestamp(instanceId, new Date().getTime());

        Optional<UcuInstance> instanceConfiguration = configurationService.getConfiguration(instanceId);
        if (instanceConfiguration.isPresent()) {
            UcuInstance ucuInstance = instanceConfiguration.get();
            ucuInstance.getGpios().forEach(gpio -> {
                ConfigureGpioMessage configureGpioMessage = new ConfigureGpioMessage();
                configureGpioMessage.setInstanceId(instanceId);
                configureGpioMessage.setPin(gpio.getPin());
                configureGpioMessage.setMode(gpio.getMode());
                configureGpioMessage.setPullup(gpio.getPullup());

                try {
                    messageService.sendMessage(configureGpioMessage);
                } catch (Exception e) {
                    LOGGER.warn("Unable to send gpio configuration to {}", instanceId, e);
                }
            });
        }
    }
}
