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

# LCU - Light Control Unit - Specification
* One 230V Relay output
* Integrated 5V Power Supply
* External Reset Button - in case of BUGs in software
* External MicroUSB port - for programming
* Fixed to wall like normal light switch - using metal frame from common light switch
* External BLS 2x2 Ports for additional sensors and devices `(GND, 5V, 3.3V, GPIO)`

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