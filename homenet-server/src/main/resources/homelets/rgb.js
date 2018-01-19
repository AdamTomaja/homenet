/** Consts **/
var UNDERDESK = "UnderDesk";
var ONDESK = "OnDesk";

var RGBSTRIP = "RGBStrip";


function hexToRgb(hex) {
    // Expand shorthand form (e.g. "03F") to full form (e.g. "0033FF")
    var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
    hex = hex.replace(shorthandRegex, function(m, r, g, b) {
        return r + r + g + g + b + b;
    });

    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        "r": parseInt(result[1], 16),
        "g": parseInt(result[2], 16),
        "b": parseInt(result[3], 16)
    } : null;
}




setup = function() {
    function setColor(api, r, g, b) {
        var color = {"r": r, "g": g, "b": b};
        api.setValue(UNDERDESK, RGBSTRIP, color);
        api.setValue(ONDESK, RGBSTRIP, color);
    }

    var api = this;

    this.addOperation("LightsOff", function(parameters){
           setColor(this, 0, 0, 0);
        });

        this.addOperation("Blue", function(parameters){
            setColor(this, 0, 49, 214);
        });

        this.addOperation("Sleep", function(parameters){
             setColor(this, 0, 2, 15);
        });

        this.addOperation("Animate", function(parameters){
            for(var i = 0; i < 255; i+=1) {
                 setColor(this, api.toInteger(i), api.toInteger(i), api.toInteger(i));
                 api.sleep(200);
            }
        }, {"asynchronous": true});

}

loop = function() {

}