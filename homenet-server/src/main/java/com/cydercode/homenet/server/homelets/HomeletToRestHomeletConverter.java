package com.cydercode.homenet.server.homelets;

import com.cydercode.homenet.server.homelets.Homelet;
import com.cydercode.homenet.server.homelets.RestHomelet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class HomeletToRestHomeletConverter implements Converter<Homelet, RestHomelet> {

    @Override
    public RestHomelet convert(Homelet homelet) {
        return RestHomelet.builder()
                .id(homelet.getId())
                .name(homelet.getName())
                .type(homelet.getConfiguration().getType())
                .parameters(new HashMap<>(homelet.getParameters()))
                .operations(homelet.getOperationNames())
                .build();
    }
}
