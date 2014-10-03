describe("logout behavior", function () {

    var scope, http, httpBackend, controller;

    beforeEach(module("logout"));
    beforeEach(inject(function ($rootScope, $http, $httpBackend, $controller) {
        httpBackend = $httpBackend;
        scope = $rootScope.$new();
        http = $http;

        controller = $controller('LogoutCtrl', {
            $scope: scope,
            $http: http
        });
    }));

    it("should get path to login page as response from server", function () {
        httpBackend.whenPOST("/logout").respond(200);
        scope.logout();
        expect().toBe();
        httpBackend.flush();

    });
});