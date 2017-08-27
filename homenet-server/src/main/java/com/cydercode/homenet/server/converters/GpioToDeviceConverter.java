package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.rest.Device;
import com.cydercode.homenet.server.rest.ValueConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GpioToDeviceConverter implements Converter<GpioConfiguration, Device> {

    @Override
    public Device convert(GpioConfiguration gpioConfiguration) {
        Device device = new Device();
        device.setId(gpioConfiguration.getPin());
        device.setDescription(gpioConfiguration.getDescription());
        device.setName(gpioConfiguration.getName());
        device.setValue(ValueConverter.convertToUIValue(gpioConfiguration, gpioConfiguration.getLastKnownValue()));
        device.setType(Device.Type.fromGpioMode(gpioConfiguration.getMode()));
        return device;
    }
}
