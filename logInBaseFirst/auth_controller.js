'use strict';

app.factory('Auth', function($window, $localStorage){
			var user;
			var storageUser; 
			var authenticated;
			var userRoles = new Array();
			var authService = {};
			//Variable for 'for' loop
			var state;
			var i;
			var j;
			
			authService.setUser = function(aUser){
				user = aUser;
				//console.log("User " + user.name +" is set");
				$window.localStorage.setItem('user', JSON.stringify(user));
				//console.log("User " + user.name +" is set in localstorage");

			},
			
			authService.getUser = function() {
				  if (this.currentUser) {
				      return this.currentUser;
				  }
				  var storageUser = $window.localStorage.getItem('user');
				  if (storageUser) {
				    try {
				      this.user = JSON.parse(storageUser);
				    } catch (e) {
				      $window.localStorage.removeItem('user');
				    }
				  }
				  return this.currentUser;
				}
			authService.remove= function() {
			    $window.localStorage.removeItem('user');
			    this.user = null;
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
				//console.log("In isAuthorized " + authorizedRoles);
				if (user == null || user == undefined){
					console.log("User is not logged in!");
					return false;
				}
				for (i = 0; i<user.authorities.length;i++) {
					userRoles [i] = user.authorities[i].authority;
					console.log("Authority present is " + userRoles[i]);
				}
				if ( authService.hasRoles(authorizedRoles)){
					return true;
				}
				//console.log("Returning unauthorized");
				return false;


			}

			authService.hasRoles = function(authorizedRoles){
				for (j = 0; j < userRoles.length; j++) {
					if (userRoles[j] == authorizedRoles) {
						console.log("Returning authorized");
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
	$scope.IsUser = function(){
		return Auth.hasRoles('ROLE_USER');
	}

	$scope.IsSuperAdmin = function(){
		return Auth.hasRoles('ROLE_SUPERADMIN');
	}

});
