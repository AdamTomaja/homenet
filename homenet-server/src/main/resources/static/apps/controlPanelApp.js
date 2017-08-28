var app = angular.module('controlPanelApp', ['ngWebSocket']);

app.controller('ucusController', function($scope, $http, $websocket){
    var ws = $websocket(window.location.href.replace('http', 'ws') + '/ws');

    function loadUnits() {
        $http.get("/api/state/units").then(function(response){
                $scope.units = response.data;
                console.log("Units loaded");
            });
    }

    ws.onMessage(function(message) {
        var data = JSON.parse(message.data);
        switch(data.type) {
            case "SetValueRequest":
            data = data.message;
            $scope.units.forEach(function(unit){
                        unit.devices.forEach(function(device) {
                            if(unit.id == data.unitId && device.id == data.deviceId) {
                                device.value = data.value;
                                device.blocked = false;
                                $scope.$apply();
                            }
                        });
                    });
            break;
            case "ErrorNotification":
                $scope.units.forEach(function(unit){
                     if(unit.id == data.message.unitId) {
                        if(unit.errors == undefined) {
                            unit.errors = [];
                        }

                        unit.errors.push(data.message.error);
                        $scope.$apply();
                     }
                });
            break;
            case "ControlUnitPingMessage":
                 $scope.units.forEach(function(unit){
                     if(unit.id == data.message.unitId) {
                        unit.health = "HEALTHY";
                        $scope.$apply();
                     }
                });
            break;
        }

    });

    ws.onClose(function() {
        $scope.websocketConnectionClosed = true;
    });

    $scope.setValue = function(unit, device, value) {
        $http.post("/api/state/set", {unitId: unit.id, deviceId: device.id, value: value}).then(function(response){
            console.log("State set successfully")
            device.blocked = true;
        });
    }

    loadUnits();

});
