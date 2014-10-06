/**
 * Created by emil on 14-9-26.
 */
angular.module('transaction', ['ui.router'])

    .config(['$stateProvider', '$httpProvider', function($stateProvider, $httpProvider) {
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

        $httpProvider.interceptors.push('unauthorisedInterceptor');
    }])

    .factory('unauthorisedInterceptor', ['$q', 'windowService', function($q, windowService) {

        return {
            'responseError': function(rejection) {
                if(rejection.status === 401) {
                   windowService.redirect();
                }

                return $q.reject(rejection);
            }

        };

    }])

    .factory('windowService', ['$window', function($window) {
        return {
            redirect: function() {
                $window.location.replace("/login");
            }
        };
    }])

.controller('TransactionCtrl', ['$scope', '$state', 'bankService', function($scope, $state, bankService) {

        $scope.navigateToTransaction = function() {
            $state.go('transaction');
        };


        bankService.fetchCurrentAmount()
            .success(function(data) {
                $scope.currentAmount = data.amount;
            });

        $scope.deposit = function(amount) {
            bankService.deposit(amount)
                .success(function(data) {
                    $scope.currentAmount = data.amount;
                });
        };

        $scope.withdraw = function(amount) {

            bankService.withdraw(amount)
                .success(function(data) {
                    $scope.currentAmount = data.amount;
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

    }])

.directive('amountValidator', function () {

        var regexp = /^[1-9][0-9]*(\.[0-9]{1,2})?$/;

        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    if(regexp.test(viewValue)){
                        ctrl.$setValidity('float', true);
                        return parseFloat(viewValue.replace(',', '.'));
                    }
                    else{
                        ctrl.$setValidity('float', false);
                        return undefined;
                    }
                });
            }
        };
    });