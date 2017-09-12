package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.server.converters.HomeletToRestHomeletConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/api/homelet")
    public void configureHomelet(@RequestBody RestHomelet homelet) {
        homeletService.configureHomelet(homelet.getName(), homelet.getParameters());
    }
}
