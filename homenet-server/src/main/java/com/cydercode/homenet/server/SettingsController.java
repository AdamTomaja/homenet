package com.cydercode.homenet.server;

import com.cydercode.homenet.server.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController {

    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/api/settings")
    public Configuration getSettings() {
        return configurationService.getConfiguration();
    }
}
