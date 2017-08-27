package com.cydercode.homenet.server;

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

        // when
        handler.sendMessage();

        // then
        sessions.forEach(session -> {
            try {
                verify(session).sendMessage(any(TextMessage.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
        handler.sendMessage();

        // then
        verify(sessionClosed, never()).sendMessage(any());
    }
}
