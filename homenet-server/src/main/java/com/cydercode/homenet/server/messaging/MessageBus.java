package com.cydercode.homenet.server.messaging;

import com.cydercode.homenet.cdm.MessageUtils;
import com.cydercode.homenet.cdm.UCUTopic;
import com.cydercode.homenet.cdm.UMUTopic;
import com.cydercode.homenet.server.ConfigurationService;
import com.cydercode.homenet.server.config.MqttBrokerConfiguration;
import com.google.gson.Gson;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.cydercode.homenet.cdm.HomenetTopics.UMU_GPIO_SET;
import static com.cydercode.homenet.cdm.HomenetTopics.UMU_HELLO;

@Component
public class MessageBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBus.class);

    private String[] SUBSCRIBED_TOPICS = {UMU_HELLO, UMU_GPIO_SET};

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ApplicationEventPublisher publisher;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private BlockingConnection connection;

    private Runnable messageLoopTask = () -> {
        Message message = null;
        AtomicBoolean running = new AtomicBoolean(true);

        while (running.get()) {
            LOGGER.debug("Waiting for message...");
            try {
                message = connection.receive();
                String payload = new String(message.getPayload());
                String topic = message.getTopic();
                LOGGER.debug("Message received : {} {}", topic, payload);
                MessageEvent messageEvent = new MessageEvent(this);
                Optional<Class<?>> messageClass = MessageUtils.getMessageClasses().stream()
                        .filter(clazz -> {
                            UMUTopic annotation = clazz.getAnnotation(UMUTopic.class);
                            if (annotation == null) {
                                return false;
                            }

                            return annotation.value().equals(topic);
                        })
                        .findFirst();

                if (!messageClass.isPresent()) {
                    throw new IllegalArgumentException("Cannot find message class for topic " + topic);
                }

                messageEvent.setMessage(parseMessage(payload, messageClass.get()));
                publisher.publishEvent(messageEvent);
                message.ack();
            } catch (Exception e) {
                LOGGER.error("Error when receiving data from MQTT", e);
                running.set(false);
            }
        }
    };

    @PostConstruct
    public void createConnectionSubscribeAndRunMessageLoop() throws Exception {
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


    private Gson getJson() {
        return new Gson();
    }

    public void sendMessage(Object message) throws Exception {
        connection.publish(getTopicForUcuMessage(message), getJson().toJson(message).getBytes(), QoS.AT_LEAST_ONCE, false);
    }

    private String getTopicForUcuMessage(Object message) {
        UCUTopic annotation = message.getClass().getAnnotation(UCUTopic.class);
        if (annotation == null) {
            throw new IllegalStateException(message.getClass().getSimpleName() + " has no " + UCUTopic.class.getSimpleName() + " annotation");
        }

        return annotation.value();
    }

    private Topic[] createTopicsList() {
        return Arrays.asList(SUBSCRIBED_TOPICS).stream()
                .map(topic -> new Topic(topic, QoS.AT_LEAST_ONCE))
                .collect(Collectors.toList())
                .toArray(new Topic[0]);
    }
}
