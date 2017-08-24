function onConnectToAP(connection)
    print("Connected to AP")
end

wifiConfig = {ssid = "Ave2G", pwd = "", auto = false, connected_cb = onConnectToAP}

wifi.sta.config(wifiConfig)
wifi.setmode(wifi.STATION)

wifi.sta.connect()

connectionChecker = tmr.create()

function onConnected() 
    m = mqtt.Client("main-client", 120)

    m:on("connect", function(client) 
        print("MQTT Connected") 
        m:subscribe("/test",0, function(conn) print("subscribe success") end)
    end)
    
    m:on("offline", function(client) print("mqtt offline") end)

    m:on("message", function(client, topic, data) 
        print("Message received")
        print(topic)
        print(data)
    end)

    m:connect("192.168.0.19", 8883, 0, true)
end

function checkConnection() 
    print("Checking connection...")
    if wifi.sta.status() == wifi.STA_GOTIP then
        print("Node is connected!")
        print("IP: ")
        print(wifi.sta.getip())
        connectionChecker:stop()
        print("hello world!")
       
        onConnected()
    end
end


connectionChecker:register(1000, tmr.ALARM_AUTO, checkConnection)
connectionChecker:start()
