package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.rest.SetValueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.cydercode.homenet.server.rest.ValueConverter.convertToSystemValue;

@Component
public class SetValueRequestToSetGpioValueMessageConverter implements Converter<SetValueRequest, SetGpioValueMessage> {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public SetGpioValueMessage convert(SetValueRequest request) {
        Integer pin = Integer.parseInt(request.getDeviceId());
        String unitId = request.getUnitId();

        GpioConfiguration gpioConfiguration = configurationService.getConfiguration(unitId).get().getGpioByPin(pin).get();

        SetGpioValueMessage setGpioValueMessage = new SetGpioValueMessage();
        setGpioValueMessage.setInstanceId(unitId);
        setGpioValueMessage.setPin(pin);

        Object systemValue = convertToSystemValue(gpioConfiguration, request.getValue());

        if (gpioConfiguration.getInvert()) {
            setGpioValueMessage.setValue(invert(systemValue));
        } else {
            setGpioValueMessage.setValue(systemValue);
        }

        return setGpioValueMessage;
    }

    private Object invert(Object value) {
        if (value.equals(1)) {
            return 0;
        } else {
            return 1;
        }
    }
}
