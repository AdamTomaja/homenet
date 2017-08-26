package com.cydercode.homenet.server;

import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.flow.FlowAPI;
import com.cydercode.homenet.server.messages.SetGpioValueMessage;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Optional;

@Component
public class FlowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowService.class);

    private static final String FLOW_FILENAME = "flow.js";

    ScriptEngine scriptEngine;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FlowAPI flowAPI;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @PostConstruct
    public void init() throws IOException, ScriptException {
        String flow = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("flow.js"), "UTF-8");
        LOGGER.info("Flow Loaded");

        scriptEngine = new ScriptEngineManager().getEngineByExtension("js");
        scriptEngine.eval(flow);
        ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) scriptEngine.get("setup");
        scriptObjectMirror.call(flowAPI);
    }

    public void postMessage(Object message) {
        if (message instanceof SetGpioValueMessage) {
            webSocketHandler.sendMessage();
            SetGpioValueMessage setGpioValueMessage = (SetGpioValueMessage) message;
            UcuInstance instance = configurationService.getConfiguration(((SetGpioValueMessage) message).getInstanceId()).get();
            Optional<GpioConfiguration> optionalGpio = instance.getGpioByPin(setGpioValueMessage.getPin());
            if (optionalGpio.isPresent()) {
                GpioConfiguration gpioConfiguration = optionalGpio.get();
                flowAPI.fireListeners(instance.getName(), gpioConfiguration.getName(), setGpioValueMessage.getValue());
            }
        } else {
            LOGGER.error("Unkown message type {}", message);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void loop() {
        ((ScriptObjectMirror) scriptEngine.get("loop")).call(flowAPI);
    }
}