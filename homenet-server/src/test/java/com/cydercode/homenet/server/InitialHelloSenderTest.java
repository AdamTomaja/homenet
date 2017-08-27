package com.cydercode.homenet.server;

import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.server.messaging.MessageBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InitialHelloSenderTest {

    @Mock
    private MessageBus messageBus;

    @InjectMocks
    private InitialHelloSender sender;

    @Test
    public void shouldSendHelloMessageOnApplicationEvent() throws Exception {
        // when
        sender.onApplicationEvent(null);

        // then
        verify(messageBus).sendMessage(any(HelloMessage.class));
    }
}