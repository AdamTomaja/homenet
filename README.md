# Home-Net
What is Home-Net? Home-Net is a project which will make Your Home Smart :) 
With Home-Net You can script Your devices in JavaScript and Control them from WEB.

# Screenshots
## Home-Net: Main Page
![Main Page](screenshots/main-page.PNG "Home-Net Landing Page")

## Home-Net: Control Panel
![Main Page](screenshots/control-panel.PNG "Home-Net Control Panel")


# Running UMU
Execute following command in **homenet-server** directory
```bash
maven spring-boot:run
```
# Network Configuration
MQTT Broker has to be configured with static IP.
To do this, please reconfigure Your /etc/dhcpcd.conf file as follows:

```
interface wlan0
static ip_address 192.168.0.10/24
static routers=192.168.0.1
static domain_name_servers=127.0.0.1 8.8.8.8 8.8.4.4
```

# MQTT Topics
This is description of MQTT topics used by HomeNet UCU and UMU. 

| Topic name          | Description                                                                    | Message class        |
|---------------------|--------------------------------------------------------------------------------|----------------------|
| /ucu/gpio/configure | configure gpio direction, input output pullup                                  | ConfigureGpioMessage |
| /ucu/gpio/set       | set value for gpio on ucu                                                      | SetGpioValueMessage  |
| /umu/gpio/set       | gpio value changed on ucu                                                      | SetGpioValueMessage  |
| /ucu/hello          | request to all ucus to send message to umu with instance names                 | HelloMessage         |
| /umu/hello          | all ucus can introduce themselfs to umu                                        | HelloMessage         |
| /umu/error          | ucu sends to this topic information about errors                               | ErrorMessage         |
| /umu/ping           | ucus sends ping message as answer for ping message from umu                    | PingMessage          |
| /ucu/ping           | umu send ping request to all ucus                                              | PingMessage          |

# UCU Connector
| Signal    | Wire Color    | Connector Pin     |
|-----------|---------------|-------------------|
| 5V        | Red           | 1                 |
| 3.3V      | White         | 2                 |
| IO        | Blue          | 3                 |
| GND       | Black         | 4                 |
  

# LCU - Light Control Unit - Specification
* One 230V Relay output
* Integrated 5V Power Supply
* External Reset Button - in case of BUGs in software
* External MicroUSB port - for programming
* Fixed to wall like normal light switch - using metal frame from common light switch
* External BLS 2x2 Ports for additional sensors and devices `(GND, 5V, 3.3V, GPIO)`
* Fallback to direct power to light

## Sensors and outputs:
| Name                  | Type      | Pin   |
|-----------------------|-----------|-------|
| Movement Sensor       | External  | x     |
| Temperature Sensor    | External  | AD0   |
| Door Opened Sensor    | External  | x     |
| Relay                 | Internal  | x     |
| Internal Button A     | Internal  | x     |
| Internal Button B     | Internal  | x     |
| Notification Led      | Internal  | x     |

# UMU Configuration
```json
{
  "mqtt": {
    "host": "rpi-umu",
    "port": 1883
  },
  "instances": [
    {
      "id": "ucu-389469",
      "name": "Daily Room",
      "description": "Room I live in",
      "gpios": [
        {
          "name": "Button",
          "mode": "INPUT",
          "isPullup": true,
          "pin": 3,
          "invert": true
        },
        {
          "name": "Led",
          "mode": "OUTPUT",
          "isPullup": false,
          "pin": 4,
          "initialValue": 1,
          "invert": true
        }
      ]
    }
  ]
}
```

# UCU Configuration
Every UCU has embedded configuration server service. 

It exposes WIFI Access Point with Http server. AP will be automatically disabled after 60 seconds after boot time.
Name of AP is the same as chipid (instanceId). 

To configure UCU You should connect with UCU with WiFi and send following http request:
## UCU Configuration Http Request
* host: `192.168.4.1`
* port: `80`
* method: `POST`
* path: `/`
* headers:
    * **ssid** - wifi SSID
    * **password** - wifi PASSWORD
    * **mqtt-host** - MQTT Broker Host
    * **mqtt-port** - MQTT Broker Port
