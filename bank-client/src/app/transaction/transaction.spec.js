/**
 * Created by emil on 14-9-27.
 */
describe('Transaction module', function () {

    beforeEach(module('transaction'));

    describe('TransactionCtrl', function () {

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

    describe("bankService send request to", function () {

        var httpBackend, authRequestHandler, scope;

        beforeEach(inject(function ($httpBackend, $rootScope, $controller) {

            httpBackend = $httpBackend;

            authRequestHandler = httpBackend.expectGET('/bankService/getAmount');
            authRequestHandler.respond('50');

            scope = $rootScope.$new();

            $controller('TransactionCtrl', {'$scope': scope});

        }));

        afterEach(function () {
            httpBackend.verifyNoOutstandingExpectation();
            httpBackend.verifyNoOutstandingRequest();
        });

        it('"/bankService/getAmount" service for get current amount on the client', function () {
            httpBackend.flush();

            expect(scope.currentAmount).toBe('50');
        });

        it('"/bankService/getAmount" service should fail', function () {
            authRequestHandler.respond(404, "Not found");

            httpBackend.flush();

        });

        it('"/bankService/deposit" for deposit amount', function () {

            httpBackend.flush();

            expect(scope.currentAmount).toBe('50');

            httpBackend.expectPOST('/bankService/deposit', {amount: 70}).respond({amount: 120});

            scope.deposit(70);

            httpBackend.flush();

            expect(scope.currentAmount).toBe(120);

        });

        it('"/bankService should fail"', function () {
            httpBackend.expectPOST('/bankService/deposit', {amount: 34}).respond(404, "Not found");

            scope.deposit(34);

            httpBackend.flush();

        });

        it('"/bankService/withdraw" for withdraw amount', function () {

            httpBackend.expectPOST('/bankService/withdraw', {amount: 60}).respond({amount: 30});

            scope.withdraw(60);

            httpBackend.flush();

            expect(scope.currentAmount).toBe(30);

        });

        it('"/bankService/withdraw" should withdraw more amount when we have', function () {

            httpBackend.expectPOST('/bankService/withdraw', {amount: 60}).respond(400, "Not enough many in account");

            scope.withdraw(60);

            httpBackend.flush();

            expect(scope.currentAmount).toBe('50');

        });

    });

    describe("unauthorisedInterceptors", function () {

        var $window, windowService;

        beforeEach(function () {

            $window = {location: {replace: jasmine.createSpy()}};

            module(function ($provide) {
                $provide.value('$window', $window);
            });

            inject(function ($injector) {
                windowService = $injector.get('windowService');
            });

        });

        it('replace should redirect to "/login"', function () {
            windowService.redirect();

            expect($window.location.replace).toHaveBeenCalledWith("/login");
        });
    });

    describe("directive for amount validation", function () {

        var compile, element, contents, rootScope;
        beforeEach(module('transaction'));
        beforeEach(inject(function (_$compile_, $rootScope) {
            compile = _$compile_;
            rootScope = $rootScope;
        }));

        it('should return undefined after giving to input wrong value', function () {
            element = angular.element('<input amount-validator ng-model="amount">');

            compile(element)(rootScope);
            rootScope.amount = '1';
            contents = element.contents();
            rootScope.$apply();
            expect(element.val()).toBe('1');
        });
    });
});