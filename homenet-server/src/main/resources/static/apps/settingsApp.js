var app = angular.module('settingsApp', ['ngWebSocket']);

app.controller('settingsController', function($scope, $http, $websocket){

    $http.get("/api/homelets").then(function(response){
        $scope.homelets = response.data;
    });

    $scope.saveHomelet = function(homelet) {
       $http.post("/api/homelet", homelet).then(function(response){
            console.log("State set successfully")
       });
    }

    $scope.removeHomelet = function(homelet) {
        $http.delete("/api/homelet/" + homelet.id).then(function(data){
            $scope.homelets.splice($scope.homelets.indexOf(homelet), 1);
        });
    }

    $scope.callOperation = function(homelet, operation) {
        $http.post("/api/homelet/operation", {homeletId: homelet.id, operation: operation, parameters: {a: "b"}}).then(function(response){
            console.log("State set successfully")
        });
    }
});
