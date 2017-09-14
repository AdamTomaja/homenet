package com.cydercode.homenet.server.homelets;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class HomeletToRestHomeletConverter implements Converter<Homelet, RestHomelet> {

    @Override
    public RestHomelet convert(Homelet homelet) {
        return RestHomelet.builder()
                .id(homelet.getId())
                .name(homelet.getConfiguration().getName())
                .type(homelet.getConfiguration().getType())
                .parameters(new ArrayList<>(homelet.getConfiguration().getParameters()))
                .operations(homelet.getOperationNames())
                .build();
    }
}
