app.run(['$rootScope', 'AUTH_EVENTS', 'Auth' ,'$location','$window', '$sessionStorage','$state','$stateParams','$http', function ($rootScope, AUTH_EVENTS, Auth, $location, $window , $sessionStorage, $state, $stateParams, $http) {

			$rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
			
				var authorizedRoles = toState.data.authorizedRoles;
				console.log("Next authorized roles are " + authorizedRoles);
				//no idea how to put multiple conditions together, && and || doesnt seem to work
				if (fromState.name !== '/login' && toState.name !== '/login' 
					&& toState.name !== 'dashboard' && fromState.name !== 'dashboard') {
					
					console.log(fromState.name);
					console.log(toState.name);
							if (sessionStorage.getItem('user') && $stateParams.param === undefined) {
								console.log('statechange sessionStorage get item is not null');					
								Auth.setUser(JSON.parse(sessionStorage.getItem('user')));	
							}
							
							if ($stateParams.param != sessionStorage.getItem('clientOrg')){
								console.log(toParams);
								console.log(fromParams);
								console.log(fromState.name);
								console.log(toState.name);
								event.preventDefault();
								console.log($stateParams.param);
							    $stateParams.param = sessionStorage.getItem('clientOrg');
								console.log(sessionStorage.getItem('clientOrg'));
								//alert('Error attempting to access beyond your organisation');
								//$location.path('/dashboard/'+ (sessionStorage.getItem('clientOrg'))+'/workspace');
								console.log("session at error attempting...");
								$state.go(toState.name, {param : $stateParams.param});					
												//}
						//}
				
							}
					}
				}
			//	})
			
				/*	if ($location.path() != '/login'){
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
							}}}*/
			
				
			
			
				
			
			);

		}])