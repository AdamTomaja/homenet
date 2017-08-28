package com.cydercode.homenet.server.converters;

import com.cydercode.homenet.server.health.InstanceHealthEvent;
import com.cydercode.homenet.server.rest.UnitState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InstanceHealthEventToUnitStateConverter implements Converter<InstanceHealthEvent, UnitState> {

    @Override
    public UnitState convert(InstanceHealthEvent healthEvent) {
        UnitState unitState = new UnitState();
        unitState.setUnitId(healthEvent.getInstance().getId());
        unitState.setHealth(healthEvent.getHealth());
        return unitState;
    }
}
