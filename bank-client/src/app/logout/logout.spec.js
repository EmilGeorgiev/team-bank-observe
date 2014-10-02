describe("logout behavior", function () {

    var scope, http, httpBackend;

    beforeEach(module("logout"));
    beforeEach(inject(function ($rootScope, $http, $httpBackend) {
        httpBackend = $httpBackend;
        scope = $rootScope.$new();
        http = $http;
    }));

    it("should get path to login page as response from server", function () {
        httpBackend.whenPOST("/logout").respond(200);
    });

});