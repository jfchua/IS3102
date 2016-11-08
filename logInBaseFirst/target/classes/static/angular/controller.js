app.controller('usersController', function($scope) {
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});
app.controller('passController', function($scope) {
    $scope.headingTitle = "Pass List";
});

app.controller('dashboardStateController', ['$state', '$stateParams', '$sessionStorage', '$location', '$window','Auth', function($state, $stateParams, $sessionStorage, $location, $window,Auth) {
    //$scope.headingTitle = "Pass List";
		console.log($stateParams);
		console.log($stateParams.param);
		if (Auth.isAuthenticated()) {
		//this handles changing client organisation forcefully
		    if ($stateParams.param != sessionStorage.getItem('clientOrg')){
				event.preventDefault();
				console.log(sessionStorage.getItem('clientOrg'));
				console.log($stateParams.param);
		    	$stateParams.param = sessionStorage.getItem('clientOrg');
		    	//$state.reload();
		    	console.log($stateParams.param);
		    	alert('Error attempting to access beyond your organisation');
				//$location.path('/dashboard/'+ ($stateParams.param)+'/workspace');
				console.log("session at error attempting... in controller");
				console.log($location.path());
				$state.reload();	
		    		}
				}
		else{
			event.preventDefault();
	    	alert('You are not logged in!');
	    	$state.go('/login');
			
		}
			
			
	}
]);