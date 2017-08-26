package com.cydercode.homenet.server;

import com.cydercode.homenet.cdm.ConfigureGpioMessage;
import com.cydercode.homenet.cdm.GpioMode;
import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Optional<UcuInstance> optionalInstance = configurationService.getConfiguration(setGpioMessage.getInstanceId());
        if (optionalInstance.isPresent()) {
            UcuInstance instance = optionalInstance.get();

            Optional<GpioConfiguration> optionalGpio = instance.getGpioByPin(setGpioMessage.getPin());
            if (!optionalGpio.isPresent()) {
                LOGGER.warn("GPIO {} not found in {} instance, creating...", setGpioMessage.getPin(), setGpioMessage.getInstanceId());
                GpioConfiguration gpioConfiguration = new GpioConfiguration();
                gpioConfiguration.setPin(setGpioMessage.getPin());
                gpioConfiguration.setLastKnownValueTimestamp(new Date().getTime());
                gpioConfiguration.setLastKnownValue(setGpioMessage.getValue());
                gpioConfiguration.setMode(GpioMode.INPUT);
                gpioConfiguration.setDescription("Dynamically detected ucu");
                gpioConfiguration.setName("Pin " + setGpioMessage.getPin());
                gpioConfiguration.setInvert(false);
                gpioConfiguration.setPullup(false);

                configurationService.createGpio(instance, gpioConfiguration);
                LOGGER.warn("New gpio {} detected in {} instance", gpioConfiguration.getName(), instance.getId());
            }

            stateCache.setState(setGpioMessage.getInstanceId(), setGpioMessage.getPin(), setGpioMessage.getValue());
            flowService.postMessage(setGpioMessage);
        } else {
            LOGGER.warn("Instance {} not found, ignoring the message.", setGpioMessage.getInstanceId());
        }
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
                    if (gpio.getInitialValue() != null) {
                        SetGpioValueMessage setGpioValueMessage = new SetGpioValueMessage();
                        setGpioValueMessage.setValue(gpio.getInitialValue());
                        setGpioValueMessage.setInstanceId(instanceId);
                        setGpioValueMessage.setPin(gpio.getPin());
                        messageService.sendMessage(setGpioValueMessage);
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
            instance.setDescription("ucu detected dynamically");
            instance.setName("new ucu");
            configurationService.createInstance(instance);
            LOGGER.warn("New instance detected: {}", instance.getId());
        }
    }
}
