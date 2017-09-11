/** Consts **/
var adamsRoom = "Adam Room";
var buttonA = "Button A";
var buttonB = "Button B";
var lightRelay = "Light Relay";

setup = function() {
    this.addListener(adamsRoom, buttonA, function(value) {
        this.setValue(adamsRoom, lightRelay, 1);
    });

    this.addListener(adamsRoom, buttonB, function(value){
        this.setValue(adamsRoom, lightRelay, 0);
    });
}

loop = function() {
    //
}