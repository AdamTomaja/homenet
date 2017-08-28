package com.cydercode.homenet.server.health;

import com.cydercode.homenet.server.config.UcuInstance;
import com.cydercode.homenet.server.rest.UnitState;
import org.springframework.context.ApplicationEvent;

public class InstanceHealthEvent extends ApplicationEvent {

    private final UcuInstance instance;
    private final UnitState.Health health;

    public InstanceHealthEvent(Object source, UcuInstance instance, UnitState.Health health) {
        super(source);
        this.instance = instance;
        this.health = health;
    }

    public UcuInstance getInstance() {
        return instance;
    }

    public UnitState.Health getHealth() {
        return health;
    }
}
