package com.cydercode.homenet.server.messaging.messagehandlers;

import com.cydercode.homenet.cdm.GpioMode;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.FlowService;
import com.cydercode.homenet.server.StateCache;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@MessageType(SetGpioValueMessage.class)
public class SetGpioValueMessageHandler implements TypedMessageHandler<SetGpioValueMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetGpioValueMessageHandler.class);

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private StateCache stateCache;

    @Autowired
    private FlowService flowService;

    @Override
    public void handleMessage(SetGpioValueMessage message) {
        Optional<UcuInstance> optionalInstance = configurationService.getConfiguration(message.getInstanceId());
        if (optionalInstance.isPresent()) {
            UcuInstance instance = optionalInstance.get();

            Optional<GpioConfiguration> optionalGpio = instance.getGpioByPin(message.getPin());
            if (!optionalGpio.isPresent()) {
                LOGGER.warn("GPIO {} not found in {} instance, creating...", message.getPin(), message.getInstanceId());
                GpioConfiguration gpioConfiguration = new GpioConfiguration();
                gpioConfiguration.setPin(message.getPin());
                gpioConfiguration.setLastKnownValueTimestamp(new Date().getTime());
                gpioConfiguration.setLastKnownValue(message.getValue());
                gpioConfiguration.setMode(GpioMode.INPUT);
                gpioConfiguration.setDescription("Dynamically detected ucu");
                gpioConfiguration.setName("Pin " + message.getPin());
                gpioConfiguration.setInvert(false);
                gpioConfiguration.setPullup(false);

                configurationService.createGpio(instance, gpioConfiguration);
                LOGGER.warn("New gpio {} detected in {} instance", gpioConfiguration.getName(), instance.getId());
            }

            stateCache.setState(message.getInstanceId(), message.getPin(), message.getValue());
            flowService.postMessage(message);
        } else {
            LOGGER.warn("Instance {} not found, ignoring the message.", message.getInstanceId());
        }
    }
}
