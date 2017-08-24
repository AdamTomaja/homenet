package com.cydercode.homenet.server;


import com.cydercode.homenet.server.messages.ConfigureGpioMessage;
import com.cydercode.homenet.server.messages.ImmutableConfigureGpioMessage;
import com.cydercode.homenet.server.messages.ImmutableSetGpioValueMessage;
import com.cydercode.homenet.server.messages.SetGpioValueMessage;
import com.google.gson.Gson;

public class App {


    public static void main(String[] args) throws Exception {

        ConfigureGpioMessage message = ImmutableConfigureGpioMessage.builder()
                .pin(3)
                .mode(ConfigureGpioMessage.GpioMode.INPUT)
                .isPullup(true)
                .build();

        SetGpioValueMessage message2 = ImmutableSetGpioValueMessage.builder().pin(3).value(1).build();

        System.out.println(new Gson().toJson(message));

        System.out.println(new Gson().toJson(message2));

//        MQTT mqtt = new MQTT();
//        mqtt.setHost("192.168.0.20", 1883);
//        BlockingConnection connection = mqtt.blockingConnection();
//        connection.connect();
//        connection.publish("/test", "{\"payload\":\"hello world\"}".getBytes(), QoS.AT_LEAST_ONCE, false);
    }
}
