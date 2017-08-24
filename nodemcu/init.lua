function onConnectToAP(connection)
    print("Connected to AP")
end

wifiConfig = {ssid = "Ave2G", pwd = "4VcDAhu2m6LZ", auto = false, connected_cb = onConnectToAP}

wifi.sta.config(wifiConfig)
wifi.setmode(wifi.STATION)

wifi.sta.connect()

connectionChecker = tmr.create()

function checkConnection() 
    print("Checking connection...")
    if wifi.sta.status() == wifi.STA_GOTIP then
        print("Node is connected!")
        print("IP: ")
        print(wifi.sta.getip())
        connectionChecker:stop()
    end
end


connectionChecker:register(1000, tmr.ALARM_AUTO, checkConnection)
connectionChecker:start()
