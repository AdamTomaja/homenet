package com.cydercode.homenet.mqfox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "mqfox")
@Service
public class CommandsService {

    @Autowired
    private Environment environment;

    public Map<String, String> getCommand() {
        return command;
    }

    public void setCommand(Map<String, String> command) {
        this.command = command;
    }

    private Map<String, String> command;

    public List<Command> getCommands() {
        return command.entrySet().stream()
                .map(e -> new Command(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public Optional<Command> getCommand(String name) {
        if (command.containsKey(name)) {
            return Optional.of(new Command(name, command.get(name)));
        }

        return Optional.empty();
    }
}
