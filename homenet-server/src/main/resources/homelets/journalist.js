setup = function() {
    this.addListener("OnDesk", "Motion Sensor", function(value) {
        this.getService("journal").saveMeasurement("motion", "adamroom", {"value": value});
    });

    this.addListener("Door", "Light Relay", function(value) {
        this.getService("journal").saveMeasurement("light", "adamroom", {"value": value});
    });
}

loop = function() {
    //
}