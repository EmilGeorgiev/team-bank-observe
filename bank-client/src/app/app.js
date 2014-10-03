angular.module('team-bank', [
    'templates-app',
    'templates-common',
    'transaction',
    'ui.router',
    'logout'
])

    .config(['$urlRouterProvider', function($urlRouterProvider) {

        $urlRouterProvider.otherwise('transaction');

    }])

    .run(function() {

    })

    .controller('AppCtrl', ["$scope", function ($scope) {

        $scope.$on('$stateChangeSuccess', function (event, toState) {
            if (angular.isDefined(toState.data.pageTitle)) {
                $scope.pageTitle = toState.data.pageTitle + ' | team-bank';
            }
        });

    }])



;



