/** Consts **/
var adamsRoom = "Adam Room";
var lightRelay = "Light Relay";
var motionSensor = "Motion Sensor";

var LIGHTS_OFF_TIMEOUT = 15 * 60;

/* Global Variables */
var lastMovementTime = 0;

/* Methods */
function getTimestamp() {
    return new Date().getTime();
}

function getHours() {
    return new Date().getHours();
}

setup = function() {
    this.addListener(adamsRoom, motionSensor, function(value) {
        if(value) {
            var hour = getHours();
            if(hour >= 18 && hour <= 22) {
                this.setValue(adamsRoom, lightRelay, 0);
            }
        }
    });
}

loop = function() {

    var motionSensorValue = this.getValue(adamsRoom, motionSensor);
    var currentTimestamp = getTimestamp();

    if(motionSensorValue) {
        lastMovementTime = currentTimestamp;
    }

    var secondsElapsed = (currentTimestamp - lastMovementTime) / 1000;
    if(secondsElapsed > LIGHTS_OFF_TIMEOUT) {
        this.setValue(adamsRoom, lightRelay, 1);
    }
}