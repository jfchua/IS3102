app.run(['$rootScope', 'AUTH_EVENTS', 'Auth' ,'$location','$window', '$sessionStorage','$state','$stateParams','$http', function ($rootScope, AUTH_EVENTS, Auth, $location, $window , $sessionStorage, $state, $stateParams, $http) {

			$rootScope.$on('$stateChangeStart', function (event, next) {
			
				var authorizedRoles = next.data.authorizedRoles;
				console.log("Next authorized roles are " + authorizedRoles);
				//no idea how to put multiple conditions together, && and || doesnt seem to work
				if (sessionStorage.getItem('user')) {
					console.log('statechange sessionStorage get item is not null');					
					Auth.setUser(JSON.parse(sessionStorage.getItem('user')));	
				}
				//can refresh, but cant refresh into change url
					if ($location.path() != '/login') {
						console.log($rootScope.userInfo+" gathering /login details");
						//if ($location.path().indexOf($stateParams.org) != 11){
					if (($rootScope.userInfo !== undefined) && ($rootScope.userInfo !== null)) {
						
						console.log($rootScope.userInfo+" gathering /login details");
						console.log($rootScope.userInfo.client+ ' in second part');
							if ($stateParams.org != $rootScope.userInfo.client){
								event.preventDefault();
								$stateParams.org = $rootScope.userInfo.client;
								alert('Error attempting to access beyond your organisation');
								$location.path('/dashboard/'+$rootScope.userInfo.client+'/workspace');
								$state.go('dashboard.workspace');
							}
						}
					else {

						$http.get("//localhost:8443/user/viewCurrentUser").then(function(responseUser){
							$rootScope.userInfo = angular.fromJson(responseUser.data);
							
						})
					}
					}
			//	})
			
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
			
				
			
			
				}
			
			);

		}])