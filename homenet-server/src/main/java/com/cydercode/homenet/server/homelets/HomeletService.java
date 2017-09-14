package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.cdm.Parameter;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.config.GpioConfiguration;
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
import java.util.*;

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
                UUID uuid = UUID.randomUUID();
                String source = loadHomeletSource(homeletConfiguration);
                Homelet homelet = new Homelet(uuid.toString(), homeletConfiguration,
                        source,
                        homeletAPI);
                homelet.setup();
                homelets.add(homelet);
                LOGGER.info("Homelet {} with name {} loaded", homeletConfiguration.getType(), homelet.getConfiguration().getName());
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

    public void configureHomelet(String homeletId, List<Parameter> parameters) {
        Homelet homelet = getHomelet(homeletId);
        homelet.configure(parameters);
        LOGGER.info("{} - {} reconfigured with parameters: {}", homelet.getConfiguration().getName(), homelet.getId(), parameters);
    }

    public void callOperation(String homeletId, String operation, Map<String, Object> parameters) {
        getHomelet(homeletId).callOperation(operation, parameters);
    }

    private Homelet getHomelet(String homeletId) {
        Optional<Homelet> homelet = homelets.stream().filter(h -> h.getId().equals(homeletId)).findFirst();
        if (!homelet.isPresent()) {
            throw new RuntimeException("Homelet " + homeletId + " not found");
        }

        return homelet.get();
    }
}
