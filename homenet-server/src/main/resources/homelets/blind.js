/** Consts **/
var UP = 1;
var DOWN = 0;

setup = function() {

    var thizz = this;

    function getInstance() {
        return thizz.getParameter("instanceName");
    }

    function getPowerGpio() {
        return thizz.getParameter("powerGpioName");
    }

    function getDirectionGpio() {
        return thizz.getParameter("directionGpioName");
    }

    function getFullBindTime() {
        return parseInt(thizz.getParameter("fullBlindMovementTime"));
    }

    function getRelayTime() {
            return parseInt(thizz.getParameter("relayTime"));
        }

    function executeFullMovement(direction) {
        thizz.setValue(getInstance(), getPowerGpio(), 0);
        thizz.sleep(getRelayTime());
        thizz.setValue(getInstance(), getDirectionGpio(), direction);
        thizz.sleep(getRelayTime());
        thizz.setValue(getInstance(), getPowerGpio(), 1);
        thizz.sleep(getFullBindTime());
        thizz.setValue(getInstance(), getPowerGpio(), 0);
        thizz.setValue(getInstance(), getDirectionGpio(), 0);
    }

    this.addOperation("Open", function(parameters){
        thizz.loginfo("Open scheduled");
        executeFullMovement(UP);
        thizz.loginfo("Open finished");
    }, {"asynchronous": true});

    this.addOperation("Close", function(parameters){
        thizz.loginfo("Close scheduled");
        executeFullMovement(DOWN);
        thizz.loginfo("Close finished");

    }, {"asynchronous": true});
}

loop = function() {
    //
}