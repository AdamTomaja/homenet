This is description of MQTT topics used by HomeNet UCU and UMU. 

| Topic name          | Description                                                                    | Message class        |
|---------------------|--------------------------------------------------------------------------------|----------------------|
| /ucu/configure-gpio | configure gpio direction, input output pullup                                  | ConfigureGpioMessage |
| /ucu/set-gpio       | set value for gpio on ucu                                                      | SetGpioValueMessage  |
| /umu/gpio-changed   | gpio value changed on ucu                                                      | SetGpioValueMessage  |
| /ucu/hello          | request to all ucus to send message to umu with instance names                 | HelloMessage         |
| /umu/hello          | all ucus can introduce themselfs to umu in this topic after /ucu/hello message | HelloMessage         |