app.run(['$rootScope', 'AUTH_EVENTS', 'Auth' ,'$location','$window', '$sessionStorage','$state','$stateParams','$http', function ($rootScope, AUTH_EVENTS, Auth, $location, $window , $sessionStorage, $state, $stateParams, $http) {

			$rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
				
				var authorizedRoles = toState.data.authorizedRoles;
				console.log("Next authorized roles are " + authorizedRoles);
				if (fromState.name !== '/login' && toState.name !== '/login' 
					&& toState.name !== 'IFMS' && fromState.name !== 'IFMS'
						&& fromState.name !== '/'&& fromState.name !== '404') {
					
					if (sessionStorage.getItem('user') && $stateParams.param === undefined) {
						console.log('statechange sessionStorage get item is not null');					
					 	Auth.setUser(JSON.parse(sessionStorage.getItem('user')));	
					}
							
				 	if ($stateParams.param != sessionStorage.getItem('clientOrg')){
						event.preventDefault();
						$stateParams.param = sessionStorage.getItem('clientOrg');
						$state.go(toState.name, {param : $stateParams.param}, {reload :true});					
					}
				 	if (!Auth.isAuthorized(authorizedRoles)) {
						console.log("role is authorizing")
						event.preventDefault();
						if (Auth.isAuthenticated()) {
							// user is not allowed
							console.log("notauthorised");
							$window.location.reload();
							alert('You are not authorised to view this page!');
							$state.go('IFMS.workspace', {param :sessionStorage.getItem('clientOrg')}, {reload :true});

						} else {
							// user is not logged in
							console.log("notauthenticated");
							// $location.path('/login');
							alert('You are not logged in!');
							$state.go('/login');
						}
				 	}
				}
			}

			
			);

		}])