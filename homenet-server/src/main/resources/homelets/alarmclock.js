/** Consts **/
var adamsRoom = "Adam Room";
var lightRelay = "Light Relay";

function appendZero(number) {
    if(number < 10) {
        return "0" + number;
    }

    return number;
}

function getCurrentTime() {
    var now = new Date();
    return appendZero(now.getHours()) + ":" + appendZero(now.getMinutes());
}

function getSeconds() {
    return new Date().getSeconds();
}

function invert(value) {
    return value ? 0 : 1;
}

setup = function() {
    var hour = this.parameters.alarmTime;
    this.loginfo("Alarm will occur at " + hour);
}

loop = function() {
    if(getCurrentTime() == this.parameters.alarmTime) {
        var instance = this.parameters.instanceName;
        var gpio = this.parameters.gpioName;

        this.loginfo("Alarm occured!");

        if(getSeconds() < this.parameters.blinkPeriod) {
            this.setValue(instance, gpio, invert(this.getValue(instance, gpio))); // Blink
        } else {
            this.setValue(instance, gpio, 0); // Turn on
        }
    }
}