package com.cydercode.homenet.server;

import com.cydercode.homenet.server.config.MqttBrokerConfiguration;
import com.cydercode.homenet.server.messages.ConfigureGpioMessage;
import com.cydercode.homenet.server.messages.HelloMessage;
import com.cydercode.homenet.server.messages.SetGpioValueMessage;
import com.google.gson.Gson;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public static final String UMU_HELLO = "/umu/hello";
    public static final String UMU_GPIO_SET = "/umu/gpio/set";
    public static final String UCU_GPIO_SET = "/ucu/gpio/set";
    public static final String UCU_GPIO_CONFIGURE = "/ucu/gpio/configure";

    private static final String[] SUBSCRIBED_TOPICS = {UMU_HELLO, UMU_GPIO_SET};

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageHandler messageHandler;

    private BlockingConnection connection;

    private Runnable messageLoopTask = () -> {
        Message message = null;
        AtomicBoolean running = new AtomicBoolean(true);
        try {
            connection.publish("/ucu/hello", getJson().toJson(new HelloMessage()).getBytes(), QoS.AT_LEAST_ONCE, false);
        } catch (Exception e) {
            LOGGER.error("Unable to send hello message", e);
        }

        while (running.get()) {
            LOGGER.info("Waiting for message...");
            try {
                message = connection.receive();
                String payload = new String(message.getPayload());
                LOGGER.info("Message received : {} {}", message.getTopic(), payload);

                switch (message.getTopic()) {
                    case "/umu/gpio/set":
                        messageHandler.handleSetGpioValueMessage(this, parseMessage(payload, SetGpioValueMessage.class));
                        break;
                    case "/umu/hello":
                        messageHandler.handleHelloMessage(this, parseMessage(payload, HelloMessage.class));
                        break;
                    default:
                        LOGGER.warn("Unknown topic: {}", message.getTopic());
                }

                message.ack();
            } catch (Exception e) {
                LOGGER.error("Error when receiving data from MQTT", e);
                running.set(false);
            }
        }
    };

    public void sendMessage(Object message) throws Exception {
        if (message instanceof ConfigureGpioMessage) {
            sendMessage(UCU_GPIO_CONFIGURE, message);
        } else if (message instanceof SetGpioValueMessage) {
            sendMessage(UCU_GPIO_SET, message);
        }
    }

    private void sendMessage(String topic, Object message) throws Exception {
        connection.publish(topic, getJson().toJson(message).getBytes(), QoS.AT_LEAST_ONCE, false);

    }

    @PostConstruct
    public void monitorTopics() throws Exception {
        MQTT mqtt = new MQTT();
        MqttBrokerConfiguration mqttConfiguration = configurationService.getConfiguration().getMqtt();
        mqtt.setHost(mqttConfiguration.getHost(), mqttConfiguration.getPort());
        connection = mqtt.blockingConnection();
        connection.connect();
        connection.subscribe(createTopicsList());
        executor.submit(messageLoopTask);
        return;
    }

    private <T> T parseMessage(String payload, Class<T> type) {
        return getJson().fromJson(payload, type);
    }

    private Topic[] createTopicsList() {
        return Arrays.asList(SUBSCRIBED_TOPICS).stream()
                .map(topic -> new Topic(topic, QoS.AT_LEAST_ONCE))
                .collect(Collectors.toList())
                .toArray(new Topic[0]);
    }

    private Gson getJson() {
        return new Gson();
    }
}
