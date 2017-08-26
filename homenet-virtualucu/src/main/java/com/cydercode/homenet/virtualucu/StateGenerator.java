package com.cydercode.homenet.virtualucu;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StateGenerator {

    @Autowired
    private MessageService messageService;

    @Autowired
    private Instance instance;

    private Random random = new Random();

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(16);

    @PostConstruct
    public void init() throws Exception {

        executor.scheduleAtFixedRate(() -> {
            SetGpioValueMessage message = new SetGpioValueMessage();
            message.setInstanceId(instance.getID());
            message.setPin(random.nextInt(10));
            message.setValue(random.nextInt(2));

            try {
                messageService.sendMessage(message);
            } catch (Exception e) {
                LoggerFactory.getLogger(StateGenerator.class).error("Cannot send message", e);
            }

        }, 0, 50, TimeUnit.MILLISECONDS);
    }

}
