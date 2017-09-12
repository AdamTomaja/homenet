/** Consts **/
var adamsRoom = "Adam Room";
var buttonA = "Button A";
var buttonB = "Button B";

setup = function() {
    this.addListener(adamsRoom, buttonA, function(value) {
        this.setValue(this.getParameter("instanceName"), this.getParameter("gpioName"), 1);
    });

    this.addListener(adamsRoom, buttonB, function(value){
        this.setValue(this.getParameter("instanceName"), this.getParameter("gpioName"), 0);
    });
}

loop = function() {
    //
}