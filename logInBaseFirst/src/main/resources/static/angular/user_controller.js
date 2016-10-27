'use strict';


app.controller('UserController', ['$scope', 'UserService','$stateParams', '$routeParams','$rootScope', '$http', '$location', 'Auth','$state', '$timeout', 
                                  function($scope, UserService, $stateParams, $routeParams,$rootScope,$http, $location, Auth,$state, $timeout) {
	var self = this;
	self.email = '';
	self.pass1  = '';
	self.pass2  = '';
	self.user={id:null,username:'',address:'',email:''};
	self.users=[];
	self.changePasswordInfo={id:parseInt($stateParams.id),token:$stateParams.token,password:''};



	self.submitChangePass = submitChangePass;
	self.submitResetPass = submitResetPass;
	self.edit = edit;
	self.remove = remove;
	self.reset = reset;

	


	var authenticate = function(credentials, callback) {
		console.log("AUTHENTICATION FUNCTION CALLED");

		var headers = credentials ? {authorization : "Basic "
			+ btoa(credentials.username + ":" + credentials.password)
		} : {};
		//alert(JSON.stringify(headers));
		var headerJson = JSON.stringify(headers);
		console.log("REACHED HERE BEFORE HTTP GET " +  JSON.stringify(headers));





		$http.get('//localhost:8443/user/loginVerify', {headers: headers}).success(function(response) {
		//	$http.get('//172.20.10.3:8443/user/loginVerify', {headers: headers}).success(function(response) {
				
			console.log("RESPONSE IS" + JSON.stringify(response));
			console.log("RESPONSE NAME IS " + JSON.stringify(response.name));

			if (response.name) {
				console.log("VERIFIED WITH NAME: " + response.name);
				$rootScope.authenticated = true;
				Auth.setUser(response);
				//$location.path("/workspace");
				$state.go("dashboard");
				//return true;
			} else {
				console.log("NO VERIFIED");
				$rootScope.authenticated = false;
				if ( $location.path() != "/reset" && !$location.path().startsWith("/resetPassword/") ){
					$location.path("/login");
				}
				//return false;
			}
			console.log("pre callback");
			callback && callback();
		}, function(response) {
			console.log("BAD RESPONSE AUTHENTICATION");
			//alert(JSON.stringify(response));
			$rootScope.authenticated = false;
			alert("Please log in again");
			reset();

			if ( $location.path() != "/reset" && !$location.path().startsWith("/resetPassword/") ){
				$location.path("/login");
			}     
			callback && callback();
			return false
		});

	}

	//authenticate();
	self.credentials = {};
	self.login = function() {
		console.log("preauth");
		authenticate(self.credentials, function() {
			console.log("got true or false from authenticate");
			if ($rootScope.authenticated) {
				//$location.path("/dashboard/workspace");
				console.log("LOGGED IN");
				self.error = false;
				 $timeout(function() {
				      $state.go('dashboard.workspace');
				      }, 500);
			} else {
				console.log("NOT LOGGED IN");
				$location.path("/login");

				self.error = true;
			}
		});
	};
	
	/* fetchAllUsers();*/

	function fetchAllUsers(){
		UserService.fetchAllUsers()
		.then(
				function(d) {
					self.users = d;
				},
				function(errResponse){
					console.error('Error while fetching Users');
				}
		);
	}

	function createUser(user){
		UserService.createUser(user)
		.then(
				fetchAllUsers,
				function(errResponse){
					console.error('Error while creating User');
				}
		);
	}
	function resetPassword(userEmail){
		UserService.resetPassword(userEmail)
		.then(
				function(response) {
					alert("Password reset link has been sent to your email:" + response.data);
				},
				function(errResponse){
					console.log(errResponse);
					alert("This email does not exist!" + errResponse.data);
				}
		);
	}
	function submitResetPass() {
		if($scope.ctrl.email==null || $scope.ctrl.email == ''){
			console.log("Not Resetted password");
			reset();
		}else{
			console.log("Resetting... password for email: " + $scope.ctrl.email);
			resetPassword($scope.ctrl.email);
			console.log("Resetted password for email: " + $scope.ctrl.email);
			$scope.ctrl.email = '';
		}

	}
	function changePassword(info){
		UserService.changePassword(info)
		.then(
				function(response) {
					alert("Password changed");
					$location.path("/login");
				},
				function(errResponse){
					console.log(errResponse);
					alert("Error changing password" + errResponse.data);
				}
		);
	}


	function updateUser(user, id){
		UserService.updateUser(user, id)
		.then(
				fetchAllUsers,
				function(errResponse){
					console.error('Error while updating User');
				}
		);
	}

	function deleteUser(id){
		UserService.deleteUser(id)
		.then(
				fetchAllUsers,
				function(errResponse){
					console.error('Error while deleting User');
				}
		);
	}


	function submitChangePass() {
		console.log($scope.ctrler.pass1);
		if($scope.ctrler.pass1==null || $scope.ctrler.pass2 == null || $scope.ctrler.pass1 != $scope.ctrler.pass2){
			alert("Check your 2 passwords again!");
			reset();
		}else{
			console.log("Changing... password");
			self.changePasswordInfo.password=$scope.ctrler.pass1;	
			changePassword(self.changePasswordInfo);
			("Changed password for email");
			reset();
		}

	}


	function edit(id){
		console.log('id to be edited', id);
		for(var i = 0; i < self.users.length; i++){
			if(self.users[i].id === id) {
				self.user = angular.copy(self.users[i]);
				break;
			}
		}
	}

	function remove(id){
		console.log('id to be deleted', id);
		if(self.user.id === id) {//clean form if the user to be deleted is shown there.
			reset();
		}
		deleteUser(id);
	}

	$scope.logout = function() {
		$http.post('https://localhost:8443/logout', {}).finally(function() {
			console.log("LOGGED OUT BY QUERYING POST TO LOGOUT");
			$rootScope.authenticated = false;
			//I added these 2 lines
			credentials.password = '';
			credentials.username ='';
			Auth.remove();
			$location.path("/login");
		});
	}



	function reset(){
		//self.email='';
		//self.password='';
		//$scope.ctrler.email = '';
		//$scope.ctrler.pass1 = '';
		//$scope.ctrler.pass2 = '';
		//$scope.controller.credentials.username = '';
		//$scope.controller.credentials.password = '';
		/*if ( typeof $scope.ctrler.pass1 !== 'undefined' ){
			$scope.ctrler.pass1 = '';
		}
		if (undefined != $scope.ctrler.pass1 ){
			$scope.ctrler.pass1 = '';*/
	}



	 	



}]);