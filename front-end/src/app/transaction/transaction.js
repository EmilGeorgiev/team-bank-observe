/**
 * Created by emil on 14-9-26.
 */
angular.module('transaction', ['ui.router'])

    .config(['$stateProvider', '$httpProvider', function ($stateProvider, $httpProvider) {
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

        $httpProvider.interceptors.push('authorizationInterceptor');
    }])

    .service('requestService', ['$http', '$q', function($http, $q) {

        return {

            sendRequest: function(method, url, config) {

                var defer = $q.defer();

                $http({method: method, url: url, data: config}).success(function(data) {

                    console.log("data before defer.resolve" + data);

                    defer.resolve(data);

                    console.log("data after defer.resolve" + data);
                });

                return defer.promise;

            }
        };

    }])

    .service('bankService', ['requestService', function (requestService) {

        return {
            fetchCurrentAmount: function () {
                return requestService.sendRequest('GET', "/amount");
            },

            deposit: function (amount) {
                return requestService.sendRequest('POST', '/amount/deposit', {amount: amount});
            },

            withdraw: function (amount) {
                return requestService.sendRequest("POST", "/amount/withdraw", {amount: amount});
            }
        };

    }])

    .service('authorizationInterceptor', ['$q', 'windowService', function ($q, windowService) {

        return {
            'responseError': function (rejection) {
                if (rejection.status === 401) {
                    windowService.redirect();
                }

                return $q.reject(rejection);
            }

        };

    }])

    .service('windowService', ['$window', function ($window) {
        return {
            redirect: function () {
                $window.location.href ="/login";
            }
        };
    }])

    .controller('TransactionCtrl', ['$scope', '$state', 'bankService', function ($scope, $state, bankService) {

        $scope.navigateToTransaction = function () {
            $state.go('transaction');
        };

        bankService.fetchCurrentAmount().then(function (amount) {

            $scope.currentAmount = amount;

        });

        $scope.deposit = function (amount) {
            bankService.deposit(amount).then(function (data) {

                $scope.currentAmount = data.amount;
            });
        };

        $scope.withdraw = function (amount) {

            bankService.withdraw(amount).then(function (data) {
                $scope.currentAmount = data.amount;
            });
        };
    }])


;

