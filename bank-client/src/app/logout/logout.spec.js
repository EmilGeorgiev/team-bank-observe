describe("logout behavior", function () {

    var scope, httpBackend, http, controller, _windowService, $window;

    beforeEach(module('transaction'));
    beforeEach(module('logout'));

    beforeEach(inject(function ($rootScope, $httpBackend, $http, $controller, $injector) {
        httpBackend = $httpBackend;

        scope = $rootScope.$new();

        http = $http;

        _windowService = $injector.get('windowService');

        spyOn(_windowService, 'redirect');

        controller = $controller('LogoutCtrl', {
            '$scope': scope,
            '$http': http,
            'windowService': _windowService
        });
    }));

    it("should be redirected to login page", function () {

        scope.logout();

        httpBackend.whenPOST("/logout").respond("/login");

        httpBackend.flush();

        expect(_windowService.redirect()).toHaveBeenCalled();
    });
});
