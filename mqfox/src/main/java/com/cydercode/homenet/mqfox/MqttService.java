package com.cydercode.homenet.mqfox;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Service
public class MqttService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttService.class);

    @Value("${mqtt.host}")
    private String mqttHost;

    @Value("${mqtt.port}")
    private Integer mqttPort;

    @Value("${mqtt.nodename}")
    private String nodeName;

    private volatile Optional<CallbackConnection> callbackConnectionOptional = Optional.empty();

    private final List<Consumer<String>> commandsListeners = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() throws URISyntaxException {
        LOGGER.info("Connecting to MQTT...");
        MQTT mqtt = new MQTT();
        mqtt.setHost(mqttHost, mqttPort);

        CallbackConnection callbackConnection = mqtt.callbackConnection();
        callbackConnection.listener(new Listener() {
            @Override
            public void onConnected() {
                LOGGER.info("Connected to MQTT");
            }

            @Override
            public void onDisconnected() {
                LOGGER.info("Disconnected from MQTT");
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                String topicString = topic.toString();

                LOGGER.info("Message received, topic: {}, message: {}", topicString, new String(body.getData()));

                String[] topicSplitted = topicString.split("/");
                String commandName = topicSplitted[2];

                commandsListeners.forEach(listener -> listener.accept(commandName));
                ack.run();
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Connection failure");
            }
        });

        callbackConnection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                LOGGER.info("Connected");
                callbackConnectionOptional = Optional.of(callbackConnection);
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Unable to connect");
            }
        });


    }

    public void subscribeTopic(String topic) {
        String effectiveTopic = String.format("mqfox/%s/%s", nodeName, topic);

        callbackConnectionOptional.ifPresent(connection -> connection.subscribe(createTopics(effectiveTopic), new Callback<byte[]>() {
            @Override
            public void onSuccess(byte[] value) {
                LOGGER.info("Topic subscribed: ", effectiveTopic);
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.error("Unable to subscribe topic: ", effectiveTopic);
            }
        }));

    }

    private Topic[] createTopics(String effectiveTopic) {
        return new Topic[]{new Topic(effectiveTopic, QoS.EXACTLY_ONCE)};
    }

    public void addListener(Consumer<String> listener) {
        commandsListeners.add(listener);
    }
}
