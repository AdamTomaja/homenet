package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.server.homelets.Homelet;
import com.cydercode.homenet.server.homelets.RestHomelet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class HomeletToRestHomeletConverter implements Converter<Homelet, RestHomelet> {

    @Override
    public RestHomelet convert(Homelet homelet) {
        RestHomelet restHomelet = new RestHomelet();
        restHomelet.setName(homelet.getName());
        restHomelet.setType(homelet.getConfiguration().getType());
        restHomelet.setParameters(new HashMap(homelet.getParameters()));
        restHomelet.setOperations(homelet.getOperationNames());
        return restHomelet;
    }
}
