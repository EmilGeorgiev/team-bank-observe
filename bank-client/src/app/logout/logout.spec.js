describe("logout behavior", function () {

    var scope, httpBackend, http, controller, _windowService;

    beforeEach(module('logout'));

    beforeEach(inject(function ($rootScope, $httpBackend, $http, $controller) {
        httpBackend = $httpBackend;

        scope = $rootScope.$new();

        http = $http;

        _windowService = {windowService: {redirect: jasmine.createSpy}};

        controller = $controller('LogoutCtrl', {
            '$scope': scope,
            '$http': http,
            'windowService': _windowService
        });
    }));

    xit("should get path to login page as response from server", function () {
        httpBackend.expectPOST("/logout").respond("/login");

        scope.logout();

        httpBackend.flush();

        expect(_windowService.redirect()).toHaveBeenCalled();

    });
});