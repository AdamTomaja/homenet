package com.cydercode.homenet.server.flow;

import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.MessageBus;
import com.cydercode.homenet.server.MessageService;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FlowAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowAPI.class);

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageBus messageBus;

    public void loginfo(Object object) {
        LOGGER.info("{}", object);
    }

    private Map<String, List<ScriptObjectMirror>> listeners = new HashMap<>();

    public void addListener(String instanceName, String gpioName, ScriptObjectMirror callback) {
        String callbackIndex = createCallbackIndex(instanceName, gpioName);
        List<ScriptObjectMirror> list = listeners.get(callbackIndex);
        if (list == null) {
            list = new ArrayList<>();
            listeners.put(callbackIndex, list);
        }

        list.add(callback);

        LOGGER.info("Listener added for: {}", callbackIndex);
    }

    public Optional<UcuInstance> getInstance(String instanceName) {
        return configurationService.getConfigurationByName(instanceName);
    }

    public Object getValue(String instanceName, String gpioName) {
        Optional<UcuInstance> instance = configurationService.getConfigurationByName(instanceName);
        if (!instance.isPresent()) {
            throw new RuntimeException("Instance " + instanceName + " not found");
        }

        Optional<GpioConfiguration> gpioConfiguration = instance.get().getGpioByName(gpioName);
        if (!gpioConfiguration.isPresent()) {
            throw new RuntimeException("Gpio " + gpioName + " not found in instance " + instanceName);
        }

        return gpioConfiguration.get().getLastKnownValue();
    }

    public void setValue(String instanceName, String gpioName, Object value) throws Exception {
        Optional<UcuInstance> instance = configurationService.getConfigurationByName(instanceName);
        if (!instance.isPresent()) {
            throw new RuntimeException("Instance " + instanceName + " not found");
        }

        Optional<GpioConfiguration> gpioConfiguration = instance.get().getGpioByName(gpioName);
        if (!gpioConfiguration.isPresent()) {
            throw new RuntimeException("Gpio " + gpioName + " not found in instance " + instanceName);
        }

        SetGpioValueMessage setGpioValueMessage = new SetGpioValueMessage();
        setGpioValueMessage.setInstanceId(instance.get().getId());
        setGpioValueMessage.setPin(gpioConfiguration.get().getPin());
        setGpioValueMessage.setValue(value);

        messageBus.sendMessage(setGpioValueMessage);
    }

    public void setValueById(String instanceId, String gpioId, Object value) throws Exception {
        Integer pin = Integer.parseInt(gpioId);
        GpioConfiguration gpioConfiguration = configurationService.getConfiguration(instanceId).get().getGpioByPin(pin).get();

        SetGpioValueMessage setGpioValueMessage = new SetGpioValueMessage();
        setGpioValueMessage.setInstanceId(instanceId);
        setGpioValueMessage.setPin(pin);

        if (gpioConfiguration.getInvert() != null && gpioConfiguration.getInvert()) {
            setGpioValueMessage.setValue(invert(value));
        } else {
            setGpioValueMessage.setValue(value);
        }

        messageBus.sendMessage(setGpioValueMessage);
    }

    private Object invert(Object value) {
        if (value.equals(1)) {
            return 0;
        } else {
            return 1;
        }
    }

    public void fireListeners(String instanceName, String gpioName, Object value) {
        List<ScriptObjectMirror> list = listeners.get(createCallbackIndex(instanceName, gpioName));
        if (list != null) {
            list.forEach(listener -> listener.call(this, value));
        }
    }

    private String createCallbackIndex(String instanceName, String gpioName) {
        return String.format("%s-%s", instanceName, gpioName);
    }
}
