app.controller('usersController', function($scope) {
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});
app.controller('passController', function($scope) {
    $scope.headingTitle = "Pass List";
});

app.controller('dashboardStateController', ['$state', '$stateParams', '$sessionStorage', '$location',  function($state, $stateParams, $sessionStorage, $location) {
		//this handles changing client organisation forcefully
	    if ($stateParams.param != sessionStorage.getItem('clientOrg')){
			event.preventDefault();
			console.log(sessionStorage.getItem('clientOrg'));
			console.log($stateParams.param);
	    	$stateParams.param = sessionStorage.getItem('clientOrg');
	    	console.log($stateParams.param);
	    	alert('Error attempting to access beyond your organisation');
			console.log("session at error attempting... in controller");
			console.log($location.path());
			$state.reload();	
	    	}
	}
]);
