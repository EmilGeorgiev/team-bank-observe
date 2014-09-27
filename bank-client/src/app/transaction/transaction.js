/**
 * Created by emil on 14-9-27.
 */
angular.module('transaction', ['ui.router'])

    .controller('TransactionCtrl', ['$scope', '$state', function($scope, $state) {

        $scope.navigateToTransaction = function() {
            $state.go('transaction');
        };

    }])
;