package com.cydercode.homenet.server;

import com.cydercode.homenet.server.messages.SetGpioValueMessage;
import com.google.gson.Gson;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MqttService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttService.class);

    @Autowired
    private StateCache stateCache;

    @PostConstruct
    public void monitorTopics() throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost("192.168.0.20", 1883);
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();
        Topic[] topics = {new Topic("/umu/hello", QoS.AT_LEAST_ONCE), new Topic("/umu/gpio/set", QoS.AT_LEAST_ONCE)};
        connection.subscribe(topics);
        Message message;
        while (true) {
            LOGGER.info("Waiting for message...");
            message = connection.receive();
            String payload = new String(message.getPayload());
            LOGGER.info("Message received : {} {}", message.getTopic(), payload);
            if (message.getTopic().equals("/umu/gpio/set")) {
                SetGpioValueMessage setGpioMessage = new Gson().fromJson(payload, SetGpioValueMessage.class);
                stateCache.setState(setGpioMessage.getInstanceId(), setGpioMessage.getPin(), setGpioMessage.getValue());

                SetGpioValueMessage newMessage = new SetGpioValueMessage();
                newMessage.setInstanceId(setGpioMessage.getInstanceId());
                newMessage.setPin(4);
                newMessage.setValue(0);
                connection.publish("/ucu/gpio/set", new Gson().toJson(newMessage).getBytes(), QoS.AT_LEAST_ONCE, false);
            }

            message.ack();
        }
    }

}
