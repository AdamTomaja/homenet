messageHandlers = {
["/ucu/gpio/set"] = function(message) 
    local outputValue = nil
    local pin = message.pin
    local value = message.value
    
    if monitoredGpios[pin] == "ANALOG_OUTPUT" then
        -- Analog output
        pwm.setduty(pin, message.value)
    else
        -- Digital output
        if value == 1 then
            gpio.write(message.pin, gpio.HIGH)
        else 
            gpio.write(message.pin, gpio.LOW)
        end
    end
    
    print("gpio set " .. tostring(message.pin) .. " to value " .. tostring(value))
end, 
["/ucu/gpio/configure"] = function(message) 
    local mode = gpio.INPUT
    local pullup = gpio.FLOAT
    local pin = message.pin
    local mode = message.mode

    if message.isPullup then
        pullup = gpio.PULLUP
    end

    if mode == "ANALOG_OUTPUT" then
        pwm.setup(message.pin, 100, 0)
        pwm.start(message.pin)
    end

    if mode == "OUTPUT" then
        gpio.mode(pin, gpio.OUTPUT, pullup)
    end
    
    if mode == "INPUT" then
        gpio.mode(pin, gpio.INPUT, pullup)
    end

    monitoredGpios[pin] = mode;
    

    print("gpio " .. tostring(pin) .. " configured to mode " .. tostring(mode) .. " and pullup " .. tostring(pullup))
end
}

function readPinValue(pin)
    local mode = monitoredGpios[pin]
    if mode == "INPUT" or mode == "OUTPUT" then
        return gpio.read(pin)
    end

    if mode == "ANALOG_INPUT" then
        return adc.read(pin)
    end

    if mode == "ANALOG_OUTPUT" then
        return pwm.getduty(pin)
    end
end

function checkGpio()
    for pin, mode in pairs(monitoredGpios) do
        local value = readPinValue(pin)
        if value ~= gpioStates[pin] then
            gpioStates[pin] = value
            sendMessage("/umu/gpio/set", {pin = pin, value = value})
        end
    end
end

function sendAllValues()
     for pin, mode in pairs(monitoredGpios) do
        sendMessage("/umu/gpio/set", {pin = pin, value = readPinValue(pin)})
     end
end