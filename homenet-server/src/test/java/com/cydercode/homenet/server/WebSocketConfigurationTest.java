package com.cydercode.homenet.server;

import com.cydercode.homenet.server.websocket.WebSocketConfiguration;
import com.cydercode.homenet.server.websocket.WebSocketHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WebSocketConfigurationTest {

    @Mock
    private WebSocketHandler webSocketHandler;

    @InjectMocks
    private WebSocketConfiguration configuration;

    @Test
    public void registerWebSocketHandler() throws Exception {
        // given
        WebSocketHandlerRegistry registry = mock(WebSocketHandlerRegistry.class);

        // when
        configuration.registerWebSocketHandlers(registry);

        // then
        verify(registry).addHandler(webSocketHandler, "/control-panel/ws");
    }
}