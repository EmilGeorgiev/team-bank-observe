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

        $httpProvider.interceptors.push('unauthorisedInterceptor');
    }])

    .factory('unauthorisedInterceptor', ['$q', 'windowService', function ($q, windowService) {

        return {
            'responseError': function (rejection) {
                if (rejection.status === 401) {
                    windowService.redirect();
                }

                return $q.reject(rejection);
            }

        };

    }])

    .factory('windowService', ['$window', function ($window) {
        return {
            redirect: function () {
                $window.location.replace("/login");
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

    .service('bankService', ['$http', '$q', function ($http, $q) {

        return {
            fetchCurrentAmount: function () {
                var defer = $q.defer();
                $http.get("/bankService/getAmount").success(function (amount) {
                    defer.resolve(amount);
                });
                return defer.promise;
            },

            deposit: function (amount) {
                var defer = $q.defer();
                $http.post('/bankService/deposit', {amount: amount}).success(function (amount) {
                    defer.resolve(amount);
                });
                return defer.promise;
            },

            withdraw: function (amount) {
                var defer = $q.defer();
                $http.post("/bankService/withdraw", {amount: amount}).success(function (amount) {
                    defer.resolve(amount);
                });
                return defer.promise;
            }
        };

    }])

    .directive('amountValidator', function () {

        var regexp = /^[1-9][0-9]*(\.[0-9]{1,2})?$/;

        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    if (regexp.test(viewValue)) {
                        ctrl.$setValidity('float', true);
                        return parseFloat(viewValue.replace(',', '.'));
                    }
                    else {
                        ctrl.$setValidity('float', false);
                        return undefined;
                    }
                });
            }
        };
    });