package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.cdm.Parameter;
import com.cydercode.homenet.server.config.UcuInstance;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class Homelet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Homelet.class);

    @Getter
    private final HomeletConfiguration configuration;

    @Getter
    private String id;

    @Getter
    private final Map<String, ScriptObjectMirror> operations = new HashMap<>();

    public Map<String, Object> parameters = new HashMap<>();

    private final ScriptEngine scriptEngine;
    private final String source;
    private final HomeletAPI api;

    public Homelet(String id, HomeletConfiguration homeletConfiguration, String source, HomeletAPI api) {
        this.id = id;
        scriptEngine = new ScriptEngineManager().getEngineByExtension("js");
        this.source = source;
        this.api = api;
        this.configuration = homeletConfiguration;
    }

    public void setup() throws ScriptException {
        extractParameters(configuration.getParameters());
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
        Optional<Parameter> optionalParameter = configuration.getParameters()
                .stream()
                .filter(p -> name.equals(p.getName()))
                .findFirst();

        if (!optionalParameter.isPresent()) {
            throw new IllegalArgumentException(name + " parameter not found!");
        }

        return optionalParameter.get().getValue();
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

    public void callLoop() {
        ((ScriptObjectMirror) scriptEngine.get("loop")).call(this);
    }

    public Set<String> getOperationNames() {
        return operations.keySet();
    }

    public void configure(List<Parameter> parameters) {
        configuration.setParameters(parameters);
        extractParameters(parameters);
    }

    private void extractParameters(List<Parameter> parameters) {
        parameters.forEach(p -> this.parameters.put(p.getName(), p.getValue()));
    }
}
