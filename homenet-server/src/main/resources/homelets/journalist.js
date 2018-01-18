/** Consts **/
var adamsRoom = "Door";
var motionSensor = "Motion Sensor";

setup = function() {
    this.addListener(adamsRoom, motionSensor, function(value) {
        if(value) {
            this.getService("journal").write("Adams Room Motion", "Motion detected!");
        }
    });
}

loop = function() {
    //
}