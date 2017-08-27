package com.cydercode.homenet.server;

import com.cydercode.homenet.cdm.ConfigureGpioMessage;
import com.cydercode.homenet.cdm.GpioMode;
import com.cydercode.homenet.cdm.HelloMessage;
import com.cydercode.homenet.cdm.SetGpioValueMessage;
import com.cydercode.homenet.server.config.GpioConfiguration;
import com.cydercode.homenet.server.config.UcuInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private StateCache stateCache;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FlowService flowService;


}
