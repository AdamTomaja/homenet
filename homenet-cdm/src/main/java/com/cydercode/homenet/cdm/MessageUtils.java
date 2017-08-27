package com.cydercode.homenet.cdm;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class MessageUtils {

    public enum ApplicationComponent {
        UMU, UCU
    }

    public static Set<Class<?>> getMessageClasses() {
        return Arrays.asList(ConfigureGpioMessage.class,
                HelloMessage.class,
                SetGpioValueMessage.class)
                .stream()
                .collect(toSet());
    }
}
