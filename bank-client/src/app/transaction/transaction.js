/**
 * Created by emil on 14-9-26.
 */
angular.module('transaction', ['ui.router'])

    .config(['$stateProvider', function($stateProvider) {
        $stateProvider.state('transaction', {
            url: '/transaction',
            views: {
                "main": {
                    templateUrl: 'transaction/transaction.tpl.html',
                    controller: 'TransactionCtrl'
                }
            },
            data: {pageTitle: 'Transaction'}
        });
    }])

.controller('TransactionCtrl', ['$scope', '$state', 'bankService', function($scope, $state, bankService) {

        $scope.navigateToTransaction = function() {
            $state.go('transaction');
        };


        bankService.fetchCurrentAmount()
            .success(function(data) {
                $scope.currentAmount = data.amount;
            })
            .error(function(resp) {
                console.log("error");
                $scope.errorMessage = resp;
            });

        $scope.deposit = function(amount) {
            bankService.deposit(amount)
                .success(function(data) {
                    $scope.currentAmount = data.amount;
                })
                .error(function(resp) {
                    $scope.errorMessage = resp;
                });
        };

        $scope.withdraw = function(amount) {

            bankService.withdraw(amount)
                .success(function(data) {
                    $scope.currentAmount = data.amount;
                    console.log($scope.currentAmount);
                })
                .error(function(reason) {
                    $scope.errorMessage = reason;
                });
        };
    }])

.factory('bankService', ['$http', function($http) {

        return {
            fetchCurrentAmount: function() {
                return $http.post("/bankService/getAmount");
            },

            deposit: function(amount) {
                return $http.post('/bankService/deposit', {amount: amount});
            },

            withdraw: function(amount) {
                return $http.post("/bankService/withdraw", {amount: amount});
            }
        };

    }]);