'use strict';

app.factory('Auth', function($window, $sessionStorage){
			var user;
			var storageUser; 
			var authenticated = false;
			var userRoles = new Array();
			var authService = {};
			//Variable for 'for' loop
			var state;
			var i;
			var j;
			
			authService.setUser = function(aUser){
				user = aUser;
				//console.log("User " + user.name +" is set");
				  sessionStorage.setItem('user', JSON.stringify(user));
				  sessionStorage.setItem('clientOrg', (user.principal.user.clientOrganisation.organisationName));
				  sessionStorage.setItem('subscriptions', (user.principal.user.clientOrganisation.systemSubscriptions));
				  console.log("token is set");
				  authenticated = true;
				  for (i = 0; i<user.authorities.length;i++) {
						userRoles [i] = user.authorities[i].authority;
						console.log("Authority present is " + userRoles[i]);
					}
				  //var token = jwt.sign(user, secret, { expiresInMinutes: 60*5 });


			},
			
			authService.getUser = function() {
				  
				  return user;
				  
				}
			authService.remove= function() {
				console.log("cookies removed");
				sessionStorage.clear();
			    this.user = null;
			    authenticated = false;
			},
			authService.isAuthenticated = function(){

				if (user == null) {
					console.log("User is null");
					authenticated = false;
				}
				else if (authenticated == null){
					console.log("authenticated is null");
					if (user.authenticated == undefined)
					authenticated = false;
				}
					//console.log(user.authenticated);
				return(authenticated);
			},
			authService.isAuthorized = function (authorizedRoles) {
				
				if ( authService.hasRoles(authorizedRoles)){
					return true;
				}
				//console.log("Returning unauthorized");
				return false;


			}

			authService.hasRoles = function(authorizedRoles){
				for (j = 0; j < userRoles.length; j++) {
					if (userRoles[j] == authorizedRoles) {
						//console.log("Returning authorized");
						return true;
					}
				}
			}
			return authService;
		} )
		
app.controller('AccountController', function($scope, Auth) {
	$scope.IsAdmin = function(){
		return Auth.hasRoles('ROLE_ADMIN');
	}
	$scope.IsEvent = function(){
		return Auth.hasRoles('ROLE_EVENT');
	}
	$scope.IsProperty = function(){
		return Auth.hasRoles('ROLE_PROPERTY');
	}
	$scope.IsExtEve = function(){
		return Auth.hasRoles('ROLE_EXTEVE');
	}
	$scope.IsFinance = function(){
		return Auth.hasRoles('ROLE_FINANCE');
	}
	$scope.IsUser = function(){
		return Auth.hasRoles('ROLE_USER');
	}

	$scope.IsSuperAdmin = function(){
		return Auth.hasRoles('ROLE_SUPERADMIN');
	}
	$scope.IsTicketing = function(){
		return Auth.hasRoles('ROLE_TICKETING');
	}
	$scope.IsManager = function(){
		return Auth.hasRoles('ROLE_HIGHER');
	}

});
