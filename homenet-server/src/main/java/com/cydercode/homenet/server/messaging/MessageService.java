package com.cydercode.homenet.server.messaging;

import com.cydercode.homenet.server.messaging.messagehandlers.MessageType;
import com.cydercode.homenet.server.messaging.messagehandlers.TypedMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class MessageService implements ApplicationListener<MessageEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageBus messageBus;

    @Autowired
    private Set<TypedMessageHandler> messageHandlers;

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
