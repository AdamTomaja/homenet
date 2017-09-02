var dailyRoom = "Daily Room";
var kitchen = "Kitchen";
var adamsRoom = "Adam Room";
var button = "Button";
var buttonA = "Button A";
var buttonB = "Button B";
var lightRelay = "Light Relay";
var led = "Led";

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
}

loop = function() {

}