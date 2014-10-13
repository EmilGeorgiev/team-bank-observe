/**
 * Created by emil on 14-9-27.
 */
describe('Transaction module', function () {

    beforeEach(module('transaction'));

    describe('Transaction state', function () {

        var state, scope;

        beforeEach(inject(function ($rootScope, $controller) {


            state = {go: jasmine.createSpy()};

            scope = $rootScope.$new();

            $controller('TransactionCtrl', {'$scope': scope, '$state': state});

        }));

        it('should change view to state "transaction"', function () {

            scope.navigateToTransaction();

            expect(state.go).toHaveBeenCalledWith('transaction');

        });

    });

    describe('requestService', function() {

        var httpBackend, requestService;

        beforeEach(function() {

            inject(function($injector) {

                httpBackend = $injector.get('$httpBackend');

                requestService = $injector.get('requestService');

            });

        });

        it('should send request', function() {

            httpBackend.expectPOST('/deposit').respond({amount: 140});

            requestService.sendRequest('POST', '/deposit', {amount: 120});

            httpBackend.flush();

        });

        it("should simulate promise", inject(function($q, $rootScope) {

            var deferred = $q.defer();

            var promise = deferred.promise;

            var resolvedValue;

            promise.then(function(value) { resolvedValue = value; });

            expect(resolvedValue).toBeUndefined();

            deferred.resolve(123);

            expect(resolvedValue).toBeUndefined();

            $rootScope.$apply();

            expect(resolvedValue).toEqual(123);


        }));

    });

    describe("bankService ", function () {

        var mock, bankService;

        beforeEach(function () {

            mock = {sendRequest: jasmine.createSpy()};

            module(function($provide) {

                $provide.value('requestService', mock);

            });

            inject(function($injector) {

                bankService = $injector.get('bankService');

            });

        });

        it('should get current amount on user', function () {

            bankService.fetchCurrentAmount();

            expect(mock.sendRequest).toHaveBeenCalledWith("GET", "/bankService/getAmount");

        });

        it('should deposit amount in account on current user', function () {

            bankService.deposit(20);

            expect(mock.sendRequest).toHaveBeenCalledWith('POST', '/bankService/deposit', {amount: 20});

        });

        it('should withdraw amount from user account', function () {

            bankService.withdraw(56);

            expect(mock.sendRequest).toHaveBeenCalledWith('POST', '/bankService/withdraw', {amount: 56});

        });

    });

    describe("authorizationInterceptor", function () {

        var $q, windowService, authorizationInterceptor;

        beforeEach(function () {

            windowService = {redirect: jasmine.createSpy()};

            $q = {reject: jasmine.createSpy()};

            module(function ($provide) {
                $provide.value('windowService', windowService);
                $provide.value('$q', $q);
            });

            inject(function ($injector) {
                authorizationInterceptor = $injector.get('authorizationInterceptor');
            });

        });

        it('should redirect to "/login"', function () {

            authorizationInterceptor.responseError({status: 401});

            expect($q.reject).toHaveBeenCalledWith({status: 401});

            expect(windowService.redirect).toHaveBeenCalled();

        });

        it('should not redirect', function() {

            authorizationInterceptor.responseError({status: 404});

            expect($q.reject).toHaveBeenCalledWith({status: 404});

            expect(windowService.redirect).not.toHaveBeenCalled();

        });
    });

    describe('windowService', function() {

        var $window, windowService;

        beforeEach(function() {

            $window = {location: {href: jasmine.createSpy().and.returnValue('/login')}};

            module(function($provide) {

                $provide.value('$window', $window);

            });

            inject(function($injector) {

                windowService = $injector.get('windowService');

            });

        });

        it('should redirect to login page', function() {

            windowService.redirect();

            expect($window.location.href).toBe('/login');

        });

    });

    xdescribe('TransactionCtrl ', function() {

       var controller, scope, _bankService, state;

       beforeEach(inject(function($rootScope, $controller, bankService, $state) {

           scope = $rootScope.$new();

           _bankService = bankService;

           state = $state;

           $controller('TransactionCtrl', {'$scope': scope, '$state': state, 'bankService': _bankService});

       }));

       it('should have navigateToTransaction set', function() {

            expect(scope.navigateToTransaction).toBeDefined();

       });

       it('should have deposit set', function() {
             expect(scope.deposit).toBeDefined();
       });

       it('should have withdraw set', function() {
           expect(scope.withdraw).toBeDefined();
       });

   });
});