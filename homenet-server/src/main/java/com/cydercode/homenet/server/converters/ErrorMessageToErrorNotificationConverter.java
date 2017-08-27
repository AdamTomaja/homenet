package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.cdm.ErrorMessage;
import com.cydercode.homenet.server.rest.ErrorNotification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageToErrorNotificationConverter implements Converter<ErrorMessage, ErrorNotification> {

    @Override
    public ErrorNotification convert(ErrorMessage errorMessage) {
        ErrorNotification notification = new ErrorNotification();
        notification.setUnitId(errorMessage.getInstanceId());
        notification.setError(errorMessage.getError());
        return notification;
    }
}
