package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.journal.JournalService;
import com.cydercode.homenet.server.messaging.MessageBus;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HomeletAPI {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageBus messageBus;

    @Autowired
    private JournalService journalService;

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

    public Object getService(String name) {
        switch (name) {
            case "journal":
                return journalService;
            default:
                throw new IllegalArgumentException();
        }
    }
}
