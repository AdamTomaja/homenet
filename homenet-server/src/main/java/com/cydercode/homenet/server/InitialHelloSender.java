package com.cydercode.homenet.server;

import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.server.messaging.MessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InitialHelloSender implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialHelloSender.class);

    @Autowired
    private MessageBus messageBus;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            messageBus.sendMessage(new HelloMessage());
            LOGGER.info("Initial Hello Message Sent");
        } catch (Exception e) {
            LOGGER.error("Unable to send initial Hello message", e);
        }
    }
}
