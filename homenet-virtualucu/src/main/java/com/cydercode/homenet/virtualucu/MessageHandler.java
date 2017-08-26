package com.cydercode.homenet.virtualucu;

import com.cydercode.homenet.cdm.ConfigureGpioMessage;
import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private Instance instance;

    public void handleSetGpioValueMessage(MessageService messageService, SetGpioValueMessage setGpioValueMessage) {
        if (setGpioValueMessage.getInstanceId().equals(instance.getID())) {
            LOGGER.info(setGpioValueMessage.toString());
        }
    }

    public void handleGpioConfigureMessage(MessageService messageService, ConfigureGpioMessage configureGpioMessage) {
        if (configureGpioMessage.getInstanceId().equals(instance.getID())) {
            LOGGER.info(configureGpioMessage.toString());
        }
    }

    public void handleHelloMessage(MessageService messageService, HelloMessage helloMessage) throws Exception {
        LOGGER.info(helloMessage.toString());

        HelloMessage hello = new HelloMessage();
        hello.setInstanceId(instance.getID());
        messageService.sendMessage(hello);
    }
}
