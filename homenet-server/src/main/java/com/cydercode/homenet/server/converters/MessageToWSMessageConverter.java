package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.cdm.ErrorMessage;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.rest.WSMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageToWSMessageConverter implements Converter<Object, WSMessage> {

    @Autowired
    private SetGpioValueMessageToSetValueRequestConverter setGpioValueMessageToSetValueRequestConverter;

    @Autowired
    private ErrorMessageToErrorNotificationConverter errorMessageToErrorNotificationConverter;

    @Override
    public WSMessage convert(Object message) {
        WSMessage wsMessage = new WSMessage();

        if (message instanceof SetGpioValueMessage) {
            wsMessage.setMessage(setGpioValueMessageToSetValueRequestConverter.convert((SetGpioValueMessage) message));
        } else if (message instanceof ErrorMessage) {
            wsMessage.setMessage(errorMessageToErrorNotificationConverter.convert((ErrorMessage) message));
        } else {
            throw new IllegalArgumentException();
        }

        wsMessage.setType(wsMessage.getMessage().getClass().getSimpleName());

        return wsMessage;
    }
}
