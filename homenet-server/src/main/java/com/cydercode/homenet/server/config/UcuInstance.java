package com.cydercode.homenet.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class UcuInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(UcuInstance.class);

    private String id;
    private String name;
    private String description;
    private List<GpioConfiguration> gpios;

    private Map<Integer, Object> lastKnownValues = new HashMap<>();

    private long lastHelloTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GpioConfiguration> getGpios() {
        return gpios;
    }

    public void setGpios(List<GpioConfiguration> gpios) {
        this.gpios = gpios;
    }

    public Map<Integer, Object> getLastKnownValues() {
        return lastKnownValues;
    }

    public void setLastHelloTimestamp(long lastHelloTimestamp) {
        this.lastHelloTimestamp = lastHelloTimestamp;
    }

    public long getLastHelloTimestamp() {
        return lastHelloTimestamp;
    }

    public Optional<GpioConfiguration> getGpioByPin(Integer pin) {
        return gpios.stream()
                .filter(gpio -> pin.equals(gpio.getPin()))
                .findFirst();
    }

    public Optional<GpioConfiguration> getGpioByName(String name) {
        return gpios.stream()
                .filter(gpio -> name.equals(gpio.getName()))
                .findFirst();
    }

    public void executeOnGpioIfExists(Integer pin, Consumer<GpioConfiguration> gpioConfigurationConsumer) {
        Optional<GpioConfiguration> gpioConfiguration = gpios.stream()
                .filter(gpio -> pin.equals(gpio.getPin()))
                .findFirst();

        if (gpioConfiguration.isPresent()) {
            gpioConfigurationConsumer.accept(gpioConfiguration.get());
        } else {
            LOGGER.info("Pin {} not found in instance {} configuration", pin, id);
        }
    }
}

