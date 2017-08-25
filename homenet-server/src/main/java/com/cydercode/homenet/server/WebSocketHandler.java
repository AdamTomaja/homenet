package com.cydercode.homenet.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void sendMessage() {
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(""));
            } catch (IOException e) {
                LOGGER.error("Unable to send to client: {}", session.getRemoteAddress());
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
