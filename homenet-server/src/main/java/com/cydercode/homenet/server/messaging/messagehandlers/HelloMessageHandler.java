package com.cydercode.homenet.server.messaging.messagehandlers;

import com.cydercode.homenet.cdm.ConfigureGpioMessage;
import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.*;
import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.messaging.MessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Component
@MessageType(HelloMessage.class)
public class HelloMessageHandler implements TypedMessageHandler<HelloMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloMessageHandler.class);

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private StateCache stateCache;

    @Autowired
    private MessageBus messageBus;

    @Override
    public void handleMessage(HelloMessage message) {
        String instanceId = message.getInstanceId();

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
                    messageBus.sendMessage(configureGpioMessage);
                    if (gpio.getInitialValue() != null) {
                        SetGpioValueMessage setGpioValueMessage = new SetGpioValueMessage();
                        setGpioValueMessage.setValue(gpio.getInitialValue());
                        setGpioValueMessage.setInstanceId(instanceId);
                        setGpioValueMessage.setPin(gpio.getPin());
                        messageBus.sendMessage(setGpioValueMessage);
                    }

                } catch (Exception e) {
                    LOGGER.warn("Unable to send initial configuration to {}", instanceId, e);
                }
            });
        } else {
            UcuInstance instance = new UcuInstance();
            instance.setId(instanceId);
            instance.setLastHelloTimestamp(new Date().getTime());
            instance.setGpios(new ArrayList<>());
            instance.setDescription("Unconfigured unit " + instanceId);
            instance.setName("Unit " + instanceId);
            configurationService.createInstance(instance);
            LOGGER.warn("New instance detected: {}", instance.getId());
        }
    }
}
