package com.cydercode.homenet.server.websocket;

import com.cydercode.homenet.server.rest.WSMessage;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    private ConversionService conversionService;

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void sendMessage(Object message) {
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(new Gson().toJson(conversionService.convert(message, WSMessage.class))));
            } catch (Exception e) {
                LOGGER.error("Unable to send to client: {}", session.getRemoteAddress(), e);
            }
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("WS connection estabilished {}", session.getRemoteAddress());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("WS connection closed {} {}", session.getRemoteAddress(), status);
        sessions.remove(session);
    }
}
