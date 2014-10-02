angular.module('team-bank', [
    'templates-app',
    'templates-common',
    'transaction',
    'ui.router',
    'logout'
])

    .run(function run() {
    })

    .controller('AppCtrl', function AppCtrl($scope, $location) {
        $scope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            if (angular.isDefined(toState.data.pageTitle)) {
                $scope.pageTitle = toState.data.pageTitle + ' | team-bank';
            }
        });
    });