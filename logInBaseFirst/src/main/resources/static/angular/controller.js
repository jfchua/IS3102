app.controller('usersController', function($scope) {
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});
app.controller('passController', function($scope) {
    $scope.headingTitle = "Pass List";
});

app.controller('dashboardStateController', ['$state', '$stateParams', '$sessionStorage', function($state, $stateParams, $sessionStorage) {
    //$scope.headingTitle = "Pass List";
		console.log($stateParams);
		console.log($stateParams.param);


	    if ($stateParams.param != sessionStorage.getItem('clientOrg')){
			event.preventDefault();
			console.log(sessionStorage.getItem('clientOrg'));
			console.log($stateParams.param);
	    	$stateParams.param = sessionStorage.getItem('clientOrg');
	    	//$state.reload();
	    	
	    }
	    $state.go('dashboard.workspace');
	  }
]);