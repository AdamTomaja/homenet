package com.cydercode.homenet.virtualucu;

import com.cydercode.homenet.cdm.ConfigureGpioMessage;
import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.google.gson.Gson;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.cydercode.homenet.cdm.HomenetTopics.*;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private static final String[] SUBSCRIBED_TOPICS = {UCU_HELLO, UCU_GPIO_CONFIGURE, UCU_GPIO_SET};

    @Value("${mqtt.host}")
    private String mqttHost;

    @Value("${mqtt.port}")
    private Integer mqttPort;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private Instance instance;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private BlockingConnection connection;

    private Runnable messageLoopTask = () -> {
        Message message = null;
        AtomicBoolean running = new AtomicBoolean(true);
        try {
            HelloMessage helloMessage = new HelloMessage();
            helloMessage.setInstanceId(instance.getID());
            connection.publish(UMU_HELLO, getJson().toJson(helloMessage).getBytes(), QoS.AT_LEAST_ONCE, false);
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
                    case UCU_GPIO_SET:
                        messageHandler.handleSetGpioValueMessage(this, parseMessage(payload, SetGpioValueMessage.class));
                        break;
                    case UCU_GPIO_CONFIGURE:
                        messageHandler.handleGpioConfigureMessage(this, parseMessage(payload, ConfigureGpioMessage.class));
                        break;
                    case UCU_HELLO:
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
        if (message instanceof HelloMessage) {
            sendMessage(UMU_HELLO, message);
        } else if (message instanceof SetGpioValueMessage) {
            sendMessage(UMU_GPIO_SET, message);
        }
    }

    private void sendMessage(String topic, Object message) throws Exception {
        connection.publish(topic, getJson().toJson(message).getBytes(), QoS.AT_LEAST_ONCE, false);

    }

    @PostConstruct
    public void monitorTopics() throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost(mqttHost, mqttPort);
        connection = mqtt.blockingConnection();
        connection.connect();
        connection.subscribe(createTopicsList());
        executor.submit(messageLoopTask);

        LOGGER.info("Initialized {}:{}", mqttHost, mqttPort);
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
