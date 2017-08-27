package com.cydercode.homenet.server.messaging.messagehandlers;

import com.cydercode.homenet.cdm.ErrorMessage;
import com.cydercode.homenet.server.websocket.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MessageType(ErrorMessage.class)
public class ErrorMessageHandler implements TypedMessageHandler<ErrorMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMessageHandler.class);

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    public void handleMessage(ErrorMessage message) {
        LOGGER.error("Error occured in ucu: {}", message);
        webSocketHandler.sendMessage(message);
    }
}
