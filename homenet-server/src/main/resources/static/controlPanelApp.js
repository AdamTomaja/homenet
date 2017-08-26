var app = angular.module('controlPanelApp', ['ngWebSocket']);

app.controller('ucusController', function($scope, $http, $websocket){
    var ws = $websocket(window.location.href.replace('http', 'ws') + '/ws');

    function loadUnits() {
        $http.get("/state/units").then(function(response){
                $scope.units = response.data;
                console.log("Units loaded");
            });
    }

    ws.onMessage(function(message) {
        loadUnits();
    });

    $scope.setValue = function(unit, device, value) {
        $http.post("/state/set", {unitId: unit.id, deviceId: device.id, value: value}).then(function(response){
            console.log("ok");
            console.log(response);
        });
    }

    loadUnits();

});

console.log("siema");