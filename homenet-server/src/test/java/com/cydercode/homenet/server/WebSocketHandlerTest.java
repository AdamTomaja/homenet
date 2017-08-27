package com.cydercode.homenet.server;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.rest.SetValueRequest;
import com.cydercode.homenet.server.websocket.WebSocketHandler;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class WebSocketHandlerTest {

    @Test
    public void shouldSendMessageToAllClients() {
        // given
        String instanceId = "inst-id";
        int value = 1;
        int pin = 2;

        SetGpioValueMessage setGpioValueMessage = new SetGpioValueMessage();
        setGpioValueMessage.setInstanceId(instanceId);
        setGpioValueMessage.setValue(value);
        setGpioValueMessage.setPin(pin);

        List<WebSocketSession> sessions = IntStream.range(0, 2)
                .mapToObj(i -> mock(WebSocketSession.class))
                .collect(Collectors.toList());
        WebSocketHandler handler = new WebSocketHandler();
        sessions.forEach(session -> {
            try {
                handler.afterConnectionEstablished(session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        SetValueRequest setValueRequest = new SetValueRequest();
        setValueRequest.setDeviceId(instanceId);
        setValueRequest.setUnitId(String.valueOf(pin));
        setValueRequest.setValue(value);

        TextMessage textMessage = new TextMessage(toJson(setValueRequest));

        // when
        handler.sendMessage(setGpioValueMessage);

        // then
        sessions.forEach(session -> {
            try {
                verify(session).sendMessage(textMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String toJson(Object object) {
        return new Gson().toJson(object);
    }

    @Test
    public void shouldNotSendToClosedSession() throws Exception {
        // given
        WebSocketSession sessionOpened = mock(WebSocketSession.class);
        WebSocketSession sessionClosed = mock(WebSocketSession.class);
        WebSocketHandler handler = new WebSocketHandler();
        handler.afterConnectionEstablished(sessionClosed);
        handler.afterConnectionEstablished(sessionOpened);

        // when
        handler.afterConnectionClosed(sessionClosed, CloseStatus.NORMAL);
        handler.sendMessage(new Object());

        // then
        verify(sessionClosed, never()).sendMessage(any());
    }
}
