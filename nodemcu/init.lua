function onConnectToAP(connection)
    print("Connected to AP")
end

wifiConfig = {ssid = "Ave2G", pwd = "", auto = false, connected_cb = onConnectToAP}

wifi.sta.config(wifiConfig)
wifi.setmode(wifi.STATION)

wifi.sta.connect()

connectionChecker = tmr.create()

jsonDecoder = sjson.decoder()

function initializeMQTT() 
    m = mqtt.Client("main-client", 120)

    m:on("connect", function(client) 
        print("MQTT Connected") 
        m:subscribe("/test",0, function(conn) print("subscribe success") end)
        -- m:publish("/test", "Hello World This is NodeMCU!", 0, 0)
    end)

    function mqttDisconnected(client)
        print("MQTT disconnected")
        tryConnectToMqtt()
    end
    
    m:on("offline", mqttDisconnected)

    m:on("message", function(client, topic, data) 
        print("Message received")
        print(topic)
        print(data)

       print(sjson.decode(data).payload)
    end)

    function tryConnectToMqtt(client)
        print("Trying connect to MQTT...")
        m:connect("192.168.0.20", 1883, 0, false, mqttError)
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
        print("hello world!")
       
        initializeMQTT()
    end
end


connectionChecker:register(1000, tmr.ALARM_AUTO, checkConnection)
connectionChecker:start()

gpio.mode(4,gpio.OUTPUT)
gpio.write(4,gpio.LOW)
