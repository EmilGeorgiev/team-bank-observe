angular.module("logout", ["ui-router"])

    .controller("logoutController", ["$scope",'$http', function ($scope, $http) {

        $scope.logout = function () {
            $http.post("/logout")
                .success(function (data) {
                    console.log(data);
                });
        };
    }]
);