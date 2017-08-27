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
        $scope.units.forEach(function(unit){
            unit.devices.forEach(function(device) {
                if(unit.id == data.unitId && device.id == data.deviceId) {
                    device.value = data.value;
                    device.blocked = false;
                    $scope.$apply();
                }
            });
        });
    });

    $scope.setValue = function(unit, device, value) {
        $http.post("/api/state/set", {unitId: unit.id, deviceId: device.id, value: value}).then(function(response){
            console.log("State set successfully")
            device.blocked = true;
        });
    }

    loadUnits();

});
