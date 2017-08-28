package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.cdm.PingMessage;
import com.cydercode.homenet.server.rest.ControlUnitPingMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PingMessageToControlUnitPingMessageConverter implements Converter<PingMessage, ControlUnitPingMessage> {

    @Override
    public ControlUnitPingMessage convert(PingMessage pingMessage) {
        ControlUnitPingMessage controlUnitPingMessage = new ControlUnitPingMessage();
        controlUnitPingMessage.setUnitId(pingMessage.getInstanceId());
        return controlUnitPingMessage;
    }
}
