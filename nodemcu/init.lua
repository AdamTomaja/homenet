dofile("configuration-server.lua")
dofile("settings.lua")
dofile("gpio.lua")

monitoredGpios = {}
gpioStates = { }

ucuId = "ucu-" .. tostring(node.chipid())
print("UCU id is " .. ucuId)
notificationLedPin = 4

settings = readSettings()

stationConfig = {ssid = settings.ssid, pwd = settings.pwd, auto = false, connected_cb = onConnectToAP}

wifi.sta.config(stationConfig)
wifi.setmode(wifi.STATION)
wifi.sta.connect()


startConfigurationServer()
tmr.create():alarm(60 * 1000, tmr.ALARM_SINGLE, shutdownConfigurationServer)



connectionChecker = tmr.create()
gpioMonitor = tmr.create()

gpioMonitor:register(300, tmr.ALARM_AUTO, checkGpio)

function blinkNotificationLed() 
    local currentState = gpio.read(notificationLedPin)
    if currentState == 1 then
        gpio.write(notificationLedPin, 0)
    else
        gpio.write(notificationLedPin, 1);
    end
end

initializingBlinker = tmr.create()
initializingBlinker:register(1000, tmr.ALARM_AUTO, blinkNotificationLed)
initializingBlinker:start()

function sendHelloMessage()
    sendMessage("/umu/hello", {})
end 

function sendMessage(topic, messageObject)
    messageObject.instanceId = ucuId
    m:publish(topic, sjson.encode(messageObject), 0, 0)
end

function initializeMQTT() 
    m = mqtt.Client(ucuId, 120)

    m:on("connect", function(client) 
        print("MQTT Connected") 
        m:subscribe({
            ["/ucu/hello"]=0, 
            ["/ucu/gpio/configure"]=0, 
            ["/ucu/gpio/set"]=0}, 
            function(conn) 
                print("All topics subscribed") 
            end)
        initializingBlinker:stop()
        gpioMonitor:start()
        sendHelloMessage()
    end)

    function mqttDisconnected(client)
        print("MQTT disconnected")
        initializingBlinker:start()
        tryConnectToMqtt()
    end
    
    m:on("offline", mqttDisconnected)

    m:on("message", function(client, topic, data) 
        local status,err = pcall(function() 
            message = sjson.decode(data)
           
            if topic == "/ucu/hello" then
                sendHelloMessage()
                sendAllValues()
                return
            end
    
            if message.instanceId == ucuId then
               print("")
               messageHandlers[topic](message)
            end 
        end) 

        if not status then
            print("Error during message handling")
            local errStr = tostring(err)
            print(errStr)
            sendMessage("/umu/error", {error = errStr})
        end
    end)

    function tryConnectToMqtt(client)
        print("Trying connect to MQTT...")
        m:connect(settings.mqttBroker, settings.mqttPort, 0, false, mqttError)
    end

    function mqttError(client, reason)
        print("MQTT was unable to connect")
        initializingBlinker:start()
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
        initializeMQTT()
        gpio.write(notificationLedPin, gpio.LOW)
    end
end

connectionChecker:register(1000, tmr.ALARM_AUTO, checkConnection)
connectionChecker:start()

gpio.mode(notificationLedPin, gpio.OUTPUT)
gpio.write(notificationLedPin, gpio.HIGH)
