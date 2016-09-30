app.run(['$rootScope', 'AUTH_EVENTS', 'Auth' ,'$location','$window', function ($rootScope, AUTH_EVENTS, Auth, $location, $window) {

			$rootScope.$on('$stateChangeStart', function (event, next) {
			//	
				var authorizedRoles = next.data.authorizedRoles;
				console.log("Next authorized roles are " + authorizedRoles);
				//no idea how to put multiple conditions together, && and || doesnt seem to work
				if ($location.path() != '/login'){
					if ( $location.path() != '/#/login') {
						if ($location.path() != '/reset') {
							if (  $location.path().indexOf('/resetPassword/') == -1) {
								//if (Auth.getUser() != null) {
								if (!Auth.isAuthorized(authorizedRoles)) {
									console.log("role is authorizing")
									event.preventDefault();
									if (Auth.isAuthenticated()) {
										// user is not allowed
										console.log("notauthorised");
										$window.location.reload();
										alert('You are not authorised to view this page!');
										//  $window.location.href = '/#/401';
										//   $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
									} else {
										// user is not logged in
										console.log("notauthenticated");
										// $location.path('/login');
										alert('You are not logged in!');
										$window.location.href = '/#/login';
									}
									//  $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
								}
								
									
							}
						}}}
				
			}		//}

			);

		}])

