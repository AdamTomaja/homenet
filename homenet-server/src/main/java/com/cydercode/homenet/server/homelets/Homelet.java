package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.server.config.HomeletConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class Homelet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Homelet.class);

    private final HomeletConfiguration configuration;
    private String name;
    private final Map<String, Object> parameters = new HashMap<>();
    private final Map<String, ScriptObjectMirror> operations = new HashMap<>();

    private final ScriptEngine scriptEngine;
    private final String source;
    private final HomeletAPI api;

    public Homelet(HomeletConfiguration homeletConfiguration, String name, String source, HomeletAPI api, Map<String, Object> parameters) {
        this.name = name;
        this.parameters.putAll(parameters);
        scriptEngine = new ScriptEngineManager().getEngineByExtension("js");
        this.source = source;
        this.api = api;
        this.configuration = homeletConfiguration;
    }

    public void setup() throws ScriptException {
        scriptEngine.eval(source);
        ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) scriptEngine.get("setup");
        scriptObjectMirror.call(this);
    }

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

    public void addOperation(String operation, ScriptObjectMirror function) {
        operations.put(operation, function);
    }

    public void callOperation(String operation, Map<String, Object> parameters) {
        operations.get(operation).call(this, parameters);
    }

    public Object getParameter(String name) {
        return parameters.get(name);
    }

    public Optional<UcuInstance> getInstance(String instanceName) {
        return api.getInstance(instanceName);
    }

    public Object getValue(String instanceName, String gpioName) {
        return api.getValue(instanceName, gpioName);
    }

    public void setValue(String instanceName, String gpioName, Object value) throws Exception {
        api.setValue(instanceName, gpioName, value);
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

    public String getName() {
        return name;
    }

    public void callLoop() {
        ((ScriptObjectMirror) scriptEngine.get("loop")).call(this);
    }

    public HomeletConfiguration getConfiguration() {
        return configuration;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
    }

    public Set<String> getOperationNames() {
        return operations.keySet();
    }
}
