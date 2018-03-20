package com.cydercode.homenet.mqfox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class App {

    @Autowired
    private MqttService mqttService;

    @Autowired
    private CommandsService commandsService;

    @Autowired
    private ExecutionService executionService;

    @PostConstruct
    private void init() {
        mqttService.addListener(this::handleCommandRequest);

        commandsService.getCommands().forEach(command -> {
            mqttService.subscribeTopic(command.getName());
        });
    }

    private void handleCommandRequest(String commandName) {
        commandsService.getCommand(commandName).ifPresent(executionService::execute);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
}