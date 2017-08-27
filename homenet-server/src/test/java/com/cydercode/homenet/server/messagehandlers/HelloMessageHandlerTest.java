package com.cydercode.homenet.server.messagehandlers;

import com.cydercode.homenet.cdm.ConfigureGpioMessage;
import com.cydercode.homenet.cdm.GpioMode;
import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.messaging.MessageBus;
import com.cydercode.homenet.server.StateCache;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.messaging.messagehandlers.HelloMessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class HelloMessageHandlerTest {

    @Mock
    private MessageBus messageBus;

    @Mock
    private StateCache stateCache;

    @Mock
    private ConfigurationService configurationService;

    @InjectMocks
    private HelloMessageHandler helloMessageHandler;

    @Test
    public void shouldCreateNewInstanceIfDoesNotExistAndSetLastHelloTimestamp() {
        // given
        HelloMessage helloMessage = new HelloMessage();
        helloMessage.setInstanceId("id");
        String id = helloMessage.getInstanceId();
        when(configurationService.getConfiguration(id)).thenReturn(Optional.empty());

        ArgumentCaptor<UcuInstance> ucuInstance = ArgumentCaptor.forClass(UcuInstance.class);

        // when
        helloMessageHandler.handleMessage(helloMessage);

        // then
        verify(configurationService).createInstance(ucuInstance.capture());
        UcuInstance capturedInstance = ucuInstance.getValue();

        assertThat(capturedInstance.getId()).isEqualTo(id);
        assertThat(capturedInstance.getDescription()).isEqualTo("Unconfigured unit " + id);
        assertThat(capturedInstance.getName()).isEqualTo("Unit " + id);
        assertThat(capturedInstance.getGpios()).isEqualTo(Collections.emptyList());
    }

    @Test
    public void shouldSendConfigureMessageAndInitialValueWhenInstanceConfigured() throws Exception {
        // given
        String id = "example-id";
        Integer initialValue = 1;

        UcuInstance ucuInstance = new UcuInstance();
        ucuInstance.setId(id);

        GpioConfiguration gpioConfiguration = new GpioConfiguration();
        gpioConfiguration.setPin(1);
        gpioConfiguration.setMode(GpioMode.INPUT);
        gpioConfiguration.setPullup(true);
        gpioConfiguration.setInitialValue(initialValue);
        ucuInstance.setGpios(Collections.singletonList(gpioConfiguration));
        when(configurationService.getConfiguration(id)).thenReturn(Optional.of(ucuInstance));

        HelloMessage helloMessage = new HelloMessage();
        helloMessage.setInstanceId(id);

        ArgumentCaptor<Object> messagesCaptor = ArgumentCaptor.forClass(Object.class);

        // when
        helloMessageHandler.handleMessage(helloMessage);

        // then
        verify(messageBus, times(2)).sendMessage(messagesCaptor.capture());
        ConfigureGpioMessage message = (ConfigureGpioMessage) messagesCaptor.getAllValues().get(0);
        assertThat(message.getPin()).isEqualTo(1);
        assertThat(message.getMode()).isEqualTo(GpioMode.INPUT);
        assertThat(message.getInstanceId()).isEqualTo(id);
        assertThat(message.isPullup()).isTrue();

        SetGpioValueMessage setGpioValueMessage = (SetGpioValueMessage) messagesCaptor.getAllValues().get(1);
        assertThat(setGpioValueMessage.getInstanceId()).isEqualTo(id);
        assertThat(setGpioValueMessage.getPin()).isEqualTo(1);
        assertThat(setGpioValueMessage.getValue()).isEqualTo(initialValue);
    }
}