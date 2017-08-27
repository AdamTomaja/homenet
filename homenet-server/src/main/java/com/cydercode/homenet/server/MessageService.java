package com.cydercode.homenet.server;

import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.server.messagehandlers.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageService implements ApplicationListener<MessageEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private MessageBus messageBus;

    @Autowired
    private Set<TypedMessageHandler> messageHandlers;

    @PostConstruct
    public void sendHelloAtStartup() throws Exception {
        messageBus.sendMessage(new HelloMessage());
    }

    @Override
    public void onApplicationEvent(MessageEvent messageEvent) {
        Object message = messageEvent.getMessage();
        Optional<TypedMessageHandler> handlerOptional = messageHandlers.stream()
                .filter(handler -> message.getClass().equals(getHandlerMessageType(handler)))
                .findFirst();

        if (!handlerOptional.isPresent()) {
            throw new IllegalArgumentException("Cannot find handler for message type: " + message.getClass().getSimpleName());
        }

        try {
            handlerOptional.get().handleMessage(message);
        } catch (Exception e) {
            LOGGER.error("Error in message handler", e);
        }
    }

    private static Class<?> getHandlerMessageType(TypedMessageHandler handler) {
        return handler.getClass().getAnnotation(MessageType.class).value();
    }
}
