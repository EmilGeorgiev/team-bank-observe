/**
 * Created by emil on 14-9-27.
 */
describe('Transaction module', function() {

    beforeEach(module('transaction'));

    describe('TransactionCtrl', function() {

        var state, scope;

        beforeEach(inject(function($rootScope, $controller) {

            state = {go: jasmine.createSpy()};

            scope = $rootScope.$new();

            $controller('TransactionCtrl', {'$scope': scope, '$state': state});

        }));

        it('should change view to state "transaction"', function() {

            scope.navigateToTransaction();

            expect(state.go).toHaveBeenCalledWith('transaction');

        });
    });

});