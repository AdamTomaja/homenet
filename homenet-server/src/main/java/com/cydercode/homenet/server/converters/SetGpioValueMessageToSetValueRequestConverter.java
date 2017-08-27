package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.rest.SetValueRequest;
import com.cydercode.homenet.server.rest.ValueConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SetGpioValueMessageToSetValueRequestConverter implements Converter<SetGpioValueMessage, SetValueRequest> {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public SetValueRequest convert(SetGpioValueMessage setGpioValueMessage) {
        GpioConfiguration gpio = configurationService.getConfiguration(setGpioValueMessage.getInstanceId()).get().getGpioByPin(setGpioValueMessage.getPin()).get();

        SetValueRequest setValueRequest = new SetValueRequest();
        setValueRequest.setUnitId(String.valueOf(setGpioValueMessage.getInstanceId()));
        setValueRequest.setDeviceId(String.valueOf(setGpioValueMessage.getPin()));
        setValueRequest.setValue(ValueConverter.convertToUIValue(gpio, setGpioValueMessage.getValue()));
        return setValueRequest;
    }
}
