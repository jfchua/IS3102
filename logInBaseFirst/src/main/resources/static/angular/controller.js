app.controller('usersController', function($scope) {
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});
app.controller('passController', function($scope, $stateParams) {
    $scope.headingTitle = "Pass List";
});

