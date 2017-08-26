package com.cydercode.homenet.server.controller;

import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.flow.FlowAPI;
import com.cydercode.homenet.server.rest.ControlUnit;
import com.cydercode.homenet.server.rest.SetValueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class StateController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FlowAPI flowAPI;

    @GetMapping("/api/state/units")
    public List<ControlUnit> inits() {
        return configurationService.getConfiguration()
                .getInstances()
                .stream()
                .map(ControlUnit::fromUcuInstance)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/state/set")
    public void setValue(@RequestBody SetValueRequest request) throws Exception {
        flowAPI.setValueById(request.getUnitId(), request.getDeviceId(), translateValue(request.getValue()));
    }

    private Object translateValue(Object value) {
        return Objects.equals("ON", value) ? 1 : 0;
    }
}
