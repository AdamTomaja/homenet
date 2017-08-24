This is description of MQTT topics used by HomeNet UCU and UMU. 

| Topic name                   | Description                                        | Message class        |   |
|------------------------------|----------------------------------------------------|----------------------|---|
| /ucu/{ucu-id}/configure-gpio | configure gpio direction, input output pullup      | ConfigureGpioMessage |   |
| /ucu/{ucu-id}/set-gpio       | set value for gpio                                 | SetGpioValueMessage  |   |
| /hello                       | can be send to make all ucus to intruduce themself | HelloMessage         |   |