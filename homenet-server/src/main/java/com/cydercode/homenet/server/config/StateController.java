package com.cydercode.homenet.server.config;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.flow.FlowAPI;
import com.cydercode.homenet.server.messaging.MessageBus;
import com.cydercode.homenet.server.rest.ControlUnit;
import com.cydercode.homenet.server.rest.SetValueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StateController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private MessageBus messageBus;

    @GetMapping("/api/state/units")
    public List<ControlUnit> inits() {
        return configurationService.getConfiguration()
                .getInstances()
                .stream()
                .map(ucuInstance -> conversionService.convert(ucuInstance, ControlUnit.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/state/set")
    public void setValue(@RequestBody SetValueRequest request) throws Exception {
        messageBus.sendMessage(conversionService.convert(request, SetGpioValueMessage.class));
    }
}
