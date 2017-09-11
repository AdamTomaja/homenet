/** Consts **/
var dailyRoom = "Daily Room";
var kitchen = "Kitchen";
var adamsRoom = "Adam Room";
var button = "Button";
var buttonA = "Button A";
var buttonB = "Button B";
var lightRelay = "Light Relay";
var led = "Led";
var motionSensor = "Motion Sensor";
var LIGHTS_OFF_TIMEOUT = 15 ;//* 60; // 15 minutes

/* Global Variables */
var lastMovementTime = 0;
var warningLogged = false;

/* Methods */
function getTimestamp() {
    return new Date().getTime();
}

function getHours() {
    return new Date().getHours();
}

setup = function() {
    var thizz = this;
    this.loginfo("Initialized");
    this.addListener(dailyRoom, button, function(value) {
        thizz.loginfo("Value changed daily room :)))");
        if(value == 0) {
            this.setValue(kitchen, "Relay", 0);
        } else {
            this.setValue(kitchen, "Relay", 1);
        }
    });

    this.addListener(adamsRoom, buttonA, function(value) {
        this.setValue(adamsRoom, lightRelay, 1);
    });

    this.addListener(adamsRoom, buttonB, function(value){
        this.setValue(adamsRoom, lightRelay, 0);
    });

    this.addListener(adamsRoom, motionSensor, function(value) {
        if(value) {
            this.loginfo("New motion detected!");
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
        warningLogged = false;
    }

    var secondsElapsed = (currentTimestamp - lastMovementTime) / 1000;
    if(secondsElapsed > LIGHTS_OFF_TIMEOUT) {
        if(!warningLogged) {
            this.loginfo("Switching light off");
            warningLogged = true;
        }

        this.setValue(adamsRoom, lightRelay, 1);
    }
}