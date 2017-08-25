var dailyRoom = "Daily Room";
var kitchen = "Kitchen";
var button = "Button";
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

    this.addListener(kitchen, button, function(value) {
        thizz.loginfo("Value changed kitchen :)))");

         if(value == 0) {
            this.setValue(kitchen, "Relay2", 0);
        } else {
            this.setValue(kitchen, "Relay2", 1);
        }
     });
}

loop = function() {

}