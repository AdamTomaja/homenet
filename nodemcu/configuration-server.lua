function get_http_req (instr)
   local t = {}
   local first = nil
   local key, v, strt_ndx, end_ndx

   for str in string.gmatch (instr, "([^\n]+)") do
      -- First line in the method and path
      if (first == nil) then
         first = 1
         strt_ndx, end_ndx = string.find (str, "([^ ]+)")
         v = trim (string.sub (str, end_ndx + 2))
         key = trim (string.sub (str, strt_ndx, end_ndx))
         t["METHOD"] = key
         t["REQUEST"] = v
      else -- Process and reamaining ":" fields
         strt_ndx, end_ndx = string.find (str, "([^:]+)")
         if (end_ndx ~= nil) then
            v = trim (string.sub (str, end_ndx + 2))
            key = trim (string.sub (str, strt_ndx, end_ndx))
            t[key] = v
         end
      end
   end

   return t
end

-- String trim left and right
function trim (s)
  return (s:gsub ("^%s*(.-)%s*$", "%1"))
end

function receiveRequest(socket, data)
    httpReq = get_http_req(data)

    if httpReq["METHOD"] == "POST" then
        writeSettings({ssid = httpReq["ssid"], pwd = httpReq["password"], mqttBroker = httpReq["mqtt-host"], mqttPort = httpReq["mqtt-port"]});    
        socket:send("OK", function(socket) 
            socket:close()
        end)
        print("New configuration saved")
    else 
        socket:send("Hello World", function(socket) 
            socket:close()
        end)
    end
end

function onConfigurationConnection(connection)
    connection:on("receive", receiveRequest);
end

function startConfigurationServer()
    local apConfig = {ssid = ucuId, auth = wifi.OPEN}
    wifi.ap.config(apConfig)
    wifi.setmode(wifi.STATIONAP)
    
    configurationServer = net.createServer(net.TCP, 30)
    configurationServer:listen(80, onConfigurationConnection)
    print("Configuration Server Started")
end

function shutdownConfigurationServer()
    wifi.setmode(wifi.STATION)
    configurationServer:close()
    print("Configuration Server Stopped")
end
