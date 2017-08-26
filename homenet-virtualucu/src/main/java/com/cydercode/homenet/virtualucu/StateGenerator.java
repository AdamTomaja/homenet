package com.cydercode.homenet.virtualucu;

import com.cydercode.homenet.cdm.SetGpioValueMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StateGenerator {

    @Autowired
    private MessageService messageService;

    @Autowired
    private Instance instance;

    private Random random = new Random();

    @Scheduled(fixedRate = 200)
    public void sendState() throws Exception {
        SetGpioValueMessage message = new SetGpioValueMessage();
        message.setInstanceId(instance.getID());
        message.setPin(random.nextInt(10));
        message.setValue(random.nextInt(2));

        messageService.sendMessage(message);
    }

}
