/** Consts **/
var motionSensor = "Motion Sensor";
var adamsRoom = "Adam Room";

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
            if(hour >= this.parameters.startHour && hour < this.parameters.endHour) {
                this.setValue(this.parameters.instanceName, this.parameters.gpioName, 0);
            }
        }
    });

    this.addOperation("doSomething", function(parameters){
        this.loginfo("Doing something" + parameters);
    });
}

loop = function() {

    var motionSensorValue = this.getValue(adamsRoom, motionSensor);
    var currentTimestamp = getTimestamp();

    if(motionSensorValue) {
        lastMovementTime = currentTimestamp;
    }

    var secondsElapsed = (currentTimestamp - lastMovementTime) / 1000;
    if(secondsElapsed > this.parameters.lightsOffTimeout) {
        this.setValue(this.parameters.instanceName, this.parameters.gpioName, 1);
    }
}