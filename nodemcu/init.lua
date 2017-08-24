function onConnectToAP(connection)
    print("Connected to AP")
end

monitoredGpios = {4, 3}
gpioStates = { }


if file.open("settings.json", "r") then
    text = file.read()
    print(text)
    settings = sjson.decode(text)
    file.close()
else 
    print("Settings file not found!")
end

wifiConfig = {ssid = settings.ssid, pwd = settings.pwd, auto = false, connected_cb = onConnectToAP}

notificationLedPin = 4
flashButtonPin = 3

ucuId = "ucu-default-id"

wifi.sta.config(wifiConfig)
wifi.setmode(wifi.STATION)

wifi.sta.connect()

function checkGpio()
    for i,pin in pairs(monitoredGpios) do
        currentState = gpio.read(pin)
        if currentState ~= gpioStates[pin] then
            print("Pin " .. tostring(pin) .. " changed to value " .. tostring(currentState))
            gpioStates[pin] = currentState
            sendMessage("/umu/gpio/set", {pin = pin, value = currentState})
        end
     
    end
end

connectionChecker = tmr.create()
gpioMonitor = tmr.create()

gpioMonitor:register(300, tmr.ALARM_AUTO, checkGpio)

gpio.mode(3, gpio.INPUT, gpio.PULLUP)

messageHandlers = {
["/ucu/gpio/set"] = function(message) 
    outputValue = nil
    if message.value == 1 then
        outputValue = gpio.HIGH
    else 
        outputValue = gpio.LOW
    end
    gpio.write(message.pin, outputValue)
    print("gpio set " .. tostring(message.pin) .. " to value " .. tostring(outputValue))
end, 
["/ucu/gpio/configure"] = function(message) 
    mode = gpio.INPUT
    pullup = gpio.FLOAT

    if message.mode == "OUTPUT" then
        mode = gpio.OUTPUT
    end
    
    if message.isPullup then
        pullup = gpio.PULLUP
    end

    print("gpio configure pin " .. tostring(message.pin) .. " to mode " .. tostring(mode) .. " and pullup " .. tostring(pullup))
    gpio.mode(message.pin, mode, pullup)
end
}

function sendHelloMessage()
    sendMessage("/umu/hello", {})
end 

function sendMessage(topic, messageObject)
    messageObject.instanceId = ucuId
    m:publish(topic, sjson.encode(messageObject), 0, 0)
end

function initializeMQTT() 
    m = mqtt.Client("main-client", 120)

    m:on("connect", function(client) 
        print("MQTT Connected") 
        m:subscribe({
            ["/ucu/hello"]=0, 
            ["/ucu/gpio/configure"]=0, 
            ["/ucu/gpio/set"]=0}, 
            function(conn) 
                print("All topics subscribed") 
            end)

        gpioMonitor:start()

        sendHelloMessage()
    end)

    function mqttDisconnected(client)
        print("MQTT disconnected")
        tryConnectToMqtt()
    end
    
    m:on("offline", mqttDisconnected)

    m:on("message", function(client, topic, data) 
        message = sjson.decode(data);
        
        print("Message received")
        if topic == "/ucu/hello" then
            sendHelloMessage()
        end

        if message.instanceId == ucuId then
            print("Received message for me")
            messageHandlers[topic](message)
        end
    end)

    function tryConnectToMqtt(client)
        print("Trying connect to MQTT...")
        m:connect(settings.mqttBroker, settings.mqttPort, 0, false, mqttError)
    end

    function mqttError(client, reason)
        print("MQTT was unable to connect")
        tmr.create():alarm(3000, tmr.ALARM_SINGLE, tryConnectToMqtt)
    end

    tryConnectToMqtt()

    
end

function checkConnection() 
    print("Checking connection...")
    if wifi.sta.status() == wifi.STA_GOTIP then
        print("Node is connected!")
        print("IP: ")
        print(wifi.sta.getip())
        connectionChecker:stop()

        ip, nm = wifi.sta.getip()
        
        ucuId = "ucu-" .. ip;

        print("UCU id is " .. ucuId)
       
        initializeMQTT()

        gpio.write(notificationLedPin, gpio.LOW)
    end
end

connectionChecker:register(1000, tmr.ALARM_AUTO, checkConnection)
connectionChecker:start()

gpio.mode(flashButtonPin, gpio.INPUT, gpio.PULLUP)
gpio.mode(notificationLedPin, gpio.OUTPUT)
gpio.write(notificationLedPin, gpio.HIGH)
