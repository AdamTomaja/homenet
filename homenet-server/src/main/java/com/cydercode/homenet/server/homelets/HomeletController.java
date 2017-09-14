package com.cydercode.homenet.server.homelets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class HomeletController {

    @Autowired
    private HomeletService homeletService;

    @Autowired
    private HomeletToRestHomeletConverter converter;

    @GetMapping("/api/homelets")
    public List<RestHomelet> getHomelets() {
        return homeletService.getHomelets()
                .stream()
                .map(converter::convert)
                .collect(toList());
    }

    @DeleteMapping("/api/homelet/{id}")
    public void configureHomelet(@PathVariable String id) {
        homeletService.removeHomelet(id);
    }

    @PostMapping("/api/homelet")
    public void configureHomelet(@RequestBody RestHomelet homelet) {
        homeletService.configureHomelet(homelet.getId(), homelet.getParameters());
    }

    @PostMapping("/api/homelet/operation")
    public void callOperation(@RequestBody HomeletOperationCall operationCall) {
        homeletService.callOperation(operationCall.getHomeletId(), operationCall.getOperation(), operationCall.getParameters());
    }
}
