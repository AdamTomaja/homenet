package com.cydercode.homenet.server;

import com.cydercode.homenet.server.config.Configuration;
import com.cydercode.homenet.server.config.UcuInstance;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
public class ConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationService.class);

    private static final String CONFIGURATION_FILE = "configuration.json";

    private final Gson gson = new Gson();

    private Configuration configuration;

    @Value("#{systemProperties['environment']}")
    private String environment;

    @PostConstruct
    public void init() {
        String filename = environment == null ? CONFIGURATION_FILE : environment + "-configuration.json";
        InputStreamReader jsonReader = new InputStreamReader(this.getClass()
                .getClassLoader()
                .getResourceAsStream(filename));

        configuration = gson.fromJson(jsonReader, Configuration.class);

        LOGGER.info("Configuration loaded");
    }

    public Optional<UcuInstance> getConfigurationByName(String instanceName) {
        return configuration.getInstances()
                .stream()
                .filter(instance -> instanceName.equals(instance.getName()))
                .findFirst();
    }

    public Optional<UcuInstance> getConfiguration(String instanceId) {
        return configuration.getInstances()
                .stream()
                .filter(instance -> instanceId.equals(instance.getId()))
                .findFirst();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
