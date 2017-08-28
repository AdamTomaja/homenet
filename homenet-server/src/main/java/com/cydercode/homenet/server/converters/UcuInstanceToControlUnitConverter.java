package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.health.InstancesHealthService;
import com.cydercode.homenet.server.rest.ControlUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class UcuInstanceToControlUnitConverter implements Converter<UcuInstance, ControlUnit> {

    @Autowired
    private GpioToDeviceConverter gpioToDeviceConverter;

    @Autowired
    private InstancesHealthService instancesHealthService;

    @Override
    public ControlUnit convert(UcuInstance ucuInstance) {
        ControlUnit unit = new ControlUnit();
        unit.setId(ucuInstance.getId());
        unit.setName(ucuInstance.getName());
        unit.setDescription(ucuInstance.getDescription());
        unit.setHealth(instancesHealthService.getHealth(ucuInstance));
        unit.setDevices(ucuInstance.getGpios()
                .stream()
                .map(gpioToDeviceConverter::convert)
                .collect(toList()));

        return unit;
    }
}
