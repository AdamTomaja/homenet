package com.cydercode.homenet.server;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.cydercode.homenet.server.rest.SetValueRequest.fromSetGpioValueMessage;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    private ConfigurationService configurationService;

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void sendMessage(Object message) {
        sessions.forEach(session -> {
            try {
                if (message instanceof SetGpioValueMessage) {
                    SetGpioValueMessage setGpioValueMessage = (SetGpioValueMessage) message;
                    GpioConfiguration gpio = configurationService.getConfiguration(setGpioValueMessage.getInstanceId()).get().getGpioByPin(setGpioValueMessage.getPin()).get();
                    session.sendMessage(new TextMessage(new Gson().toJson(fromSetGpioValueMessage(gpio, setGpioValueMessage))));
                }
            } catch (Exception e) {
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
