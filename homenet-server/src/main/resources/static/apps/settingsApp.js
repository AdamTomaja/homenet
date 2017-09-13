var app = angular.module('settingsApp', ['ngWebSocket']);

app.controller('settingsController', function($scope, $http, $websocket){

    var container = document.getElementById("jsoneditor");
    var options = {};
    var editor = new JSONEditor(container, options);

    // set json
    var json = {
        "Array": [1, 2, 3],
        "Boolean": true,
        "Null": null,
        "Number": 123,
        "Object": {"a": "b", "c": "d"},
        "String": "Hello World"
    };

    $http.get("/api/settings").then(function(response){
         editor.set(response.data);
    });


    $http.get("/api/homelets").then(function(response){
        $scope.homelets = response.data;
    });

    $scope.saveHomelet = function(homelet) {
       $http.post("/api/homelet", homelet).then(function(response){
            console.log("State set successfully")
       });
    }

    $scope.removeHomelet = function(homelet) {
        console.log(homelet);
    }

    $scope.callOperation = function(homelet, operation) {
        $http.post("/api/homelet/operation", {id: homelet.id, operation: operation, parameters: {a: "b"}}).then(function(response){
            console.log("State set successfully")
        });
    }

    // get json
    var json = editor.get();
});
