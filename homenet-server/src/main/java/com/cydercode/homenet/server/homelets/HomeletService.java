package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.HomeletConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class HomeletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeletService.class);

    private static final String FLOW_FILENAME = "homelets/light-switch.js";

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private HomeletAPI homeletAPI;

    private List<Homelet> homelets = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException, ScriptException {
        configurationService.getConfiguration().getHomelets().forEach(homeletConfiguration -> {
            try {
                String source = loadHomeletSource(homeletConfiguration);
                Homelet homelet = new Homelet(homeletConfiguration,
                        homeletConfiguration.getName(),
                        source,
                        homeletAPI,
                        homeletConfiguration.getParameters());
                homelet.setup();
                homelets.add(homelet);
                LOGGER.info("Homelet {} with name {} loaded", homeletConfiguration.getType(), homelet.getName());
            } catch (IOException | ScriptException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String loadHomeletSource(HomeletConfiguration homeletConfiguration) throws IOException {
        return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(
                format("homelets/%s.js", homeletConfiguration.getType())), "UTF-8");
    }

    public void postMessage(Object message) {
        if (message instanceof SetGpioValueMessage) {
            SetGpioValueMessage setGpioValueMessage = (SetGpioValueMessage) message;
            Optional<UcuInstance> optionalUcuInstance = configurationService.getConfiguration(((SetGpioValueMessage) message).getInstanceId());
            if (optionalUcuInstance.isPresent()) {
                UcuInstance instance = optionalUcuInstance.get();
                Optional<GpioConfiguration> optionalGpio = instance.getGpioByPin(setGpioValueMessage.getPin());
                if (optionalGpio.isPresent()) {
                    GpioConfiguration gpioConfiguration = optionalGpio.get();
                    homelets.forEach(homelet -> homelet.fireListeners(instance.getName(), gpioConfiguration.getName(), setGpioValueMessage.getValue()));
                }
            } else {
                LOGGER.warn("{} ucu configuration not found, ignoring", setGpioValueMessage.getInstanceId());
            }
        } else {
            LOGGER.error("Unkown message type {}", message);
        }
    }

    public List<Homelet> getHomelets() {
        return new ArrayList<>(homelets);
    }

    @Scheduled(fixedRate = 1000)
    public void loop() {
        homelets.forEach(Homelet::callLoop);
    }

    public void configureHomelet(String homeletName, Map<String, Object> parameters) {
        getHomelet(homeletName).getParameters().putAll(parameters);
    }

    public void callOperation(String homeletName, String operation, Map<String, Object> parameters) {
        getHomelet(homeletName).callOperation(operation, parameters);
    }

    private Homelet getHomelet(String homeletName) {
        Optional<Homelet> homelet = homelets.stream().filter(h -> h.getName().equals(homeletName)).findFirst();
        if (!homelet.isPresent()) {
            throw new RuntimeException("Homelet " + homeletName + " not found");
        }

        return homelet.get();
    }
}
