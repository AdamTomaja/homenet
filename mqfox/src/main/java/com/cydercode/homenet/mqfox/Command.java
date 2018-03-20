package com.cydercode.homenet.mqfox;

import java.util.Objects;

public class Command {

    private final String name;
    private final String commandLine;

    public Command(String name, String commandLine) {
        this.name = name;
        this.commandLine = commandLine;
    }

    public String getName() {
        return name;
    }

    public String getCommandLine() {
        return commandLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) &&
                Objects.equals(commandLine, command.commandLine);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, commandLine);
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", commandLine='" + commandLine + '\'' +
                '}';
    }
}
