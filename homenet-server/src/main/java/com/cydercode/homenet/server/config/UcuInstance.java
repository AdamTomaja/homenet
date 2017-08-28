package com.cydercode.homenet.server.config;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class UcuInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(UcuInstance.class);

    private String id;
    private String name;
    private String description;
    private List<GpioConfiguration> gpios;
    private Long lastHeartBeatTime;

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

    public void setLastHeartBeatTime(long lastHeartBeatTime) {
        this.lastHeartBeatTime = lastHeartBeatTime;
    }

    public Long getLastHeartBeatTime() {
        return lastHeartBeatTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UcuInstance that = (UcuInstance) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description);
    }
}

