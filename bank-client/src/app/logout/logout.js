angular.module("logout", ["ui.router"])

    .controller("LogoutCtrl", ["$scope", '$http', function ($scope, $http) {

        $scope.logout = function () {
            $http.post("/logout")
                .success(function (data) {
                    window.location.href = data;
                });
        };
    }]
);