/*var app = angular.module('app', ['ngRoute','ngResource','ui.bootstrap','ngAnimate', 'ngSanitize']);*/
/*app.config(function($routeProvider,$locationProvider,$httpProvider){
	$routeProvider
	.when('/reset',{
		templateUrl: '/views/resetPassword.html',
		controller: 'passController'
	})
	.when('/login',{
		templateUrl: '/views/mainLogin.html',
		controller: 'passController'
	})
	.when('/workspace',{
		templateUrl: '/views/index.html',
		controller: 'passController',
	})
	.when('/addEvent',{
		templateUrl: '/views/addEvent.html',
		controller: 'eventController',
	})
	.when('/addBuilding',{
		templateUrl: '/views/addBuilding.html',
		controller: 'buildingController',
	})
	.when('/viewBuilding',{
		templateUrl: '/views/viewBuilding.html',
		controller: 'buildingController',
	})
	.when('/updateBuilding',{
		templateUrl: '/views/updateBuilding.html',
		controller: 'buildingController',
	})
	.when('/deleteBuilding',{
		templateUrl: '/views/deleteBuilding.html',
		controller: 'buildingController',
	})
	.when('/viewLevels',{
		templateUrl: '/views/viewLevels.html',
		controller: 'buildingController',
	})
	.when('/addLevel',{
		templateUrl: '/views/addLevel.html',
		controller: 'levelController',
	})
	.when('/updateLevel',{
		templateUrl: '/views/updateLevel.html',
		controller: 'levelController',
	})
	.when('/deleteLevel',{
		templateUrl: '/views/deleteLevel.html',
		controller: 'levelController',
	})
	.when('/resetPassword/:id/:token',{
		templateUrl: '/views/resetChangePass.html',
		controller: 'passController'
	})
	.when('/messageList',{
		templateUrl: '/message/message.html',
		controller: 'ListController'
	})
	.when('/view/:id', { 
		controller: 'DetailController',
		templateUrl: 'message/detail.html'
	})
	.when('/new', { 
		controller: 'NewMailController',
		templateUrl: 'message/new.html'
	})
	.when('/addClientOrg', { 
		controller: 'clientOrgController',
		templateUrl: '/views/addClientOrg.html'
	})   
	.when('/createFloorPlan', { 
		controller: 'MyCtrl',
		templateUrl: '/views/floorPlanAngular.html'
	})  
	//for event org (temp)

	.otherwise(
			{ redirectTo: '/login'}
	)
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});
 */
var app = angular.module('app', ['ngStorage', 'ngRoute','ngResource','ui.bootstrap','ngAnimate', 'ngSanitize','ui.router','500tech.simple-calendar','ngFileUpload'])
//Declaring Constants
.constant('USER_ROLES', {
	all: '*',
	admin: 'ROLE_ADMIN',
	event : 'ROLE_EVENT',
	user: 'ROLE_USER',
	superadmin: 'ROLE_SUPERADMIN',
	finance: 'ROLE_FINANCE',
	ticket: 'ROLE_TICKET',
	property: 'ROLE_PROPERTY',
	organiser: 'ROLE_EXTEVE'
})

.constant('AUTH_EVENTS', {
	loginSuccess: 'auth-login-success',
	loginFailed: 'auth-login-failed',
	logoutSuccess: 'auth-logout-success',
	sessionTimeout: 'auth-session-timeout',
	notAuthenticated: 'auth-not-authenticated',
	notAuthorized: 'auth-not-authorized'
})
//All UIRouters
app.config(
		function($stateProvider, $urlRouterProvider, $httpProvider, USER_ROLES) { 

			$stateProvider
			.state('/login',{
				url:'/login',
				templateUrl: '/views/mainlogin.html',
				controller: 'passController',
				data: {
					authorizedRoles: [USER_ROLES.all],
				}

			})
			.state('/reset',{
				url:'/reset',
				templateUrl: '/views/resetPassword.html',
				controller: 'passController',
				data: {
					authorizedRoles: [USER_ROLES.all]
				}
			})
			.state('/resetPassword',{
				url:'/resetPassword/:id/:token',
				templateUrl: '/views/resetChangePass.html',
				controller: 'passController',
				data: {
					authorizedRoles: [USER_ROLES.all]
				}
			})
			.state('/workspace',{
				url:'/workspace',
				templateUrl: '/views/index.html',
				controller: 'passController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/addBuilding',{
				url: '/addBuilding',
				templateUrl: '/views/addBuilding.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/viewBuilding',{
				url: '/viewBuilding',
				templateUrl: '/views/viewBuilding.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/updateBuilding',{
				url: '/updateBuilding',		
				templateUrl: '/views/updateBuilding.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/deleteBuilding',{
				url: '/deleteBuilding',
				templateUrl: '/views/deleteBuilding.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/viewLevels',{
				url: '/viewLevels',
				templateUrl: '/views/viewLevels.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/addLevel',{
				url: '/addLevel',
				templateUrl: '/views/addLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/updateLevel',{
				url: '/updateLevel',
				templateUrl: '/views/updateLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/deleteLevel',{
				url: '/deleteLevel',
				templateUrl: '/views/deleteLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})

			.state('/addEvent',{
				url:'/addEvent',
				templateUrl: '/views/addEvent.html',
				controller: 'eventController',
				data: {
					authorizedRoles: [USER_ROLES.event]
				}
			})
			.state('/uploadCompanyLogo',{
				url:'/uploadCompanyLogo',
				templateUrl: '/views/uploadCompanyLogo.html',
				controller: 'logoController',
				data: {
					authorizedRoles: [USER_ROLES.admin]
				}
			})

			.state('/messageList',{
				url:'/messageList',
				templateUrl: '/message/message.html',
				controller: 'ListController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/view/:id',{
				url:'/view/:id',
				templateUrl: '/message/detail.html',
				controller: 'DetailController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/new',{
				url:'/new',
				templateUrl: '/message/new.html',
				controller: 'NewMailController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/addClientOrg',{
				url:'/addClientOrg',
				templateUrl: '/views/addClientOrg.html',
				controller: 'clientOrgController',
				data: {
					authorizedRoles: [USER_ROLES.superadmin]
				}
			})	
			.state('/viewClientOrgs',{
				url:'/viewClientOrgs',
				templateUrl: '/views/viewClientOrgs.html',
				controller: 'viewClientOrgs',
				data: {
					authorizedRoles: [USER_ROLES.superadmin]
				}
			})
			.state('/createFloorPlan',{
				url:'/createFloorPlan',
				templateUrl: '/views/floorPlanAngular.html',
				controller: 'MyCtrl',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})	
			.state('/viewMaintenance',{
				url:'/viewMaintenance',	
				templateUrl: '/views/viewMaintenance.html',
				controller: 'maintenanceController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/viewBuildingMtn',{
				url:'/viewBuildingMtn',
				templateUrl: '/views/viewBuildingMtn.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.property]
				}
			})
			.state('/viewLevelsMtn',{
				url:'/viewLevelsMtn',
				templateUrl: '/views/viewLevelsMtn.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.property]
				}
			})
			.state('/viewFloorPlanMtn',{
				url:'/viewFloorPlanMtn',
				templateUrl: '/views/viewFloorPlanMtn.html',
				controller: 'MyCtrl',//hailing test hahahahha
				data:{
					authorizedRoles:[USER_ROLES.property]
				}
			})
			.state('/addMaintenance',{
				url:'/addMaintenance',	
				templateUrl: '/views/addMaintenance.html',
				controller: 'maintenanceController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/updateMaintenance',{
				url:'/updateMaintenance',
				templateUrl: '/views/updateMaintenance.html',
				controller: 'maintenanceController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/deleteMaintenance',{
				url:'/deleteMaintenance',
				templateUrl: '/views/deleteMaintenance.html',
				controller: 'maintenanceController',
				data:{
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/viewAllVendors',{
				url:'/viewAllVendors',
				templateUrl: '/views/viewAllVendors.html',
				controller: 'vendorController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/addVendor',{
				url:'/addVendor',	
				templateUrl: '/views/addVendor.html',
				controller: 'vendorController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/updateVendor',{
				url:'/updateVendor',
				templateUrl: '/views/updateVendor.html',
				controller: 'vendorController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/deleteVendor',{
				url:'/deleteVendor',
				templateUrl: '/views/deleteVendor.html',
				controller: 'vendorController',
				data:{
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/addEventEx',{
				url:'/addEventEx',
				templateUrl: '/views/addEventEx.html',
				controller: 'eventExternalController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/viewBuildingEx',{
				url:'/viewBuildingEx',
				templateUrl: '/views/viewBuildingEx.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/viewLevelsEx',{
				url:'/viewLevelsEx',
				templateUrl: '/views/viewLevelsEx.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/viewFloorPlanEx',{
				url:'/viewFloorPlanEx',
				templateUrl: '/views/viewFloorPlanEx.html',
				controller: 'MyCtrl',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/createUnitPlanEx',{
				url:'/createUnitPlanEx',
				templateUrl: '/views/createUnitPlanEx.html',
				controller: 'areaPlanController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/updateEventEx',{
				url:'/updateEventEx',
				templateUrl: '/views/updateEventEx.html',
				controller: 'eventExternalController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/cancelEventEx',{
				url:'/cancelEventEx',
				templateUrl: '/views/cancelEventEx.html',
				controller: 'eventExternalController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/viewAllEventsEx',{
				url:'/viewAllEventsEx',
				templateUrl: '/views/viewAllEventsEx.html',
				controller: 'eventExternalController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/viewApprovedEventsEx',{
				url:'/viewApprovedEventsEx',
				templateUrl: '/views/viewApprovedEventsEx.html',
				controller: 'eventExternalController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/viewToBeApprovedEvents',{
				url:'/viewToBeApprovedEvents',
				templateUrl: '/views/viewToBeApprovedEvents.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/viewNotifications',{
				url:'/viewNotifications',
				templateUrl: '/views/viewNotifications.html',
				controller: 'notificationController',
				data: {
					authorizedRoles: [USER_ROLES.event]
				}
			})
			.state('/updateEventStatus',{
				url:'/updateEventStatus',
				templateUrl: '/views/updateEventStatus.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/deleteEvent',{
				url:'/deleteEvent',
				templateUrl: '/views/deleteEvent.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/viewAllEvents',{
				url:'/viewAllEvents',
				templateUrl: '/views/viewAllEvents.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/viewApprovedEvents',{
				url:'/viewApprovedEvents',
				templateUrl: '/views/viewApprovedEvents.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/viewEventDetails',{
				url:'/viewEventDetails',
				templateUrl: '/views/viewEventDetails.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/viewEventDetailsApproved',{
				url:'/viewEventDetailsApproved',
				templateUrl: '/views/viewEventDetailsApproved.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/viewEventOrganizers',{
				url:'/viewEventOrganizers',
				templateUrl: '/views/viewEventOrganizers.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('/401',{
				url:'/401',
				templateUrl: '/views/401.html',
				controller: 'passController',
				data: {
					authorizedRoles: [USER_ROLES.all]
				}
			})	
			.state('/createNewUser',{
				url:'/createNewUser',
				templateUrl: '/views/createUser.html',
				controller: 'createNewUserController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})	
			.state('/viewUserList',{
				url:'/viewUserList',
				templateUrl: '/views/viewUserList.html',
				controller: 'viewUserList',
				data: {
					authorizedRoles: [USER_ROLES.admin]
				}
			})	
			.state('/viewAuditLog',{
				url:'/viewAuditLog',
				templateUrl: '/views/viewAuditLog.html',
				controller: 'auditLogController',
				data: {
					authorizedRoles: [USER_ROLES.admin]
				}
			})
			.state('/viewUserProfile',{
				url:'/viewUserProfile',
				templateUrl: '/views/viewUserProfile.html',
				controller: 'userProfileController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/editUserProfile',{
				url:'/editUserProfile',
				templateUrl: '/views/editUserProfile.html',
				controller: 'userProfileController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})

			$urlRouterProvider.otherwise('/login');


			/*$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';*/
		})
app.factory('httpAuthInterceptor', function ($q) {
		return {
		'response': function(response) {
		                    return response;
		                 },
		  'responseError': function (response) {
		    // NOTE: detect error because of unauthenticated user
		    if ([401, 403, 500].indexOf(response.status) >= 0) {
		      // redirecting to login page
		  	  event.preventDefault();
		  	  $location.path("/login");
		  	  alert("Session Timeout!");
		  	  $window.location.reload();
		      return response;
		    } else {
		      return $q.reject(response);
		    }
		  }
		};
})

app.config(function ($httpProvider) {
		$httpProvider.interceptors.push('httpAuthInterceptor');
});

		app.service('Session',  function () {
			this.create = function (sessionId, userId, userRole) {
				this.id = sessionId;
				this.userId = userId;
				this.userRole = userRole;
			};
			this.destroy = function () {
				this.id = null;
				this.userId = null;
				this.userRole = null;
			};
		})











		app.controller('ApplicationController', function ($scope,
				USER_ROLES,
				Auth) {
			$scope.currentUser = null;
			$scope.userRoles = USER_ROLES;
			$scope.isAuthorized = Auth.isAuthorized;

			$scope.setCurrentUser = function (user) {
				$scope.currentUser = user;
			};
		})
		
		/*    authorised: function(role){
	    for (i = 0; i<user.authorities.length;i++){
	    	console.log("Authority present is " + user.authorities[i].authority);
	    	if (role == user.authorities[i].authority){
	    		return true;	    	
	    	}
	    }
	    return false;
	    }

	}*/



		

		/*		app.controller('eventController', ["$scope",'$http', function ($scope, $http){
			$scope.viewEvents = function(event){

				console.log("start ALL events");
				$http.get('//localhost:8443/event/user/view').then(function(response){
					console.log("DISPLAY ALL events");
					return response.data;
				},function(response){
					alert("did not view");
					//console.log("response is : ")+JSON.stringify(response);
				}
				)
			}
		}])
		 */



		/*workspace*/
		app.controller('MyController', function ($scope, $http) {
			var urlBase = "https://localhost:8443/user";
			function findAllNotifications() {
				//get all notifications to display
				$http.get(urlBase + '/notifications/findAllNotifications')
				.then(function(res){
					$scope.notifications = res.data;
				});
			}
			/*       success(function (data) {
	                if (data._embedded != undefined) {
	                    $scope.notifications = data._embedded.notifications;
	                } else {
	                    $scope.notifications = [];
	                }
	                for (var i = 0; i < $scope.notifications.length; i++) {

	                }
	                $scope.taskName="";
	                $scope.taskDesc="";
	                $scope.taskPriority="";
	                $scope.taskStatus="";
	                $scope.toggle='!toggle';
	            }).then(function(res){
	            	$scope.notification = res.data;
	            });*/

			findAllNotifications();

			/*	$scope.notifications = [{
		Id: 1,
		Name: 'Kenneth',
		Selected: false
	}, {
		Id: 2,
		Name: 'Kok Hwee',
		Selected: true
	}, {
		Id: 3,
		Name: 'IS3102',
		Selected: false
	}];*/
		});

/* Precontent
 * CREATE NEW USER
 * VIEW ALL USERS
 * EDIT USER PROFILE
 * 
 * 
 * CONTENT
 * 1. TO DO LIST
 * 2. NOTIFICATIONS
 * - MESSAGE
 * 3. CALENDAR (WHEN IT IS DONE)
 * 4. PROPERTY
 * - BUILDING
 * - LEVELS
 * - FLOORPLAN
 * 5. EVENT
 * - EVENT MANAGER
 * 
 * 
 * 6. CLIENT ORG
 * 
 * 7. Audit Log
 */
//CREATING USER VERSION 2

app.controller("userCtrl",

		function userCtrl($scope) {



	$scope.model = {
			contacts: [{
				id: 1,
				name: "Ben",
				age: 28
			}, {
				id: 2,
				name: "Sally",
				age: 24
			}, {
				id: 3,
				name: "John",
				age: 32
			}, {
				id: 4,
				name: "Jane",
				age: 40
			}],
			selected: {}
	};
	//$scope.model = {};
	$scope.send = function(){
		console.log("REACHED HERE FOR FETCHING ALL USERS");

		var fetch = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/viewAllUsers',
			//forms user object
		});

		console.log("fetching the user list.......");
		fetch.success(function(response){

			$scope.model = response;
			//alert('FETCHED ALL USERS SUCCESS!!! ' + JSON.stringify(response));
		});
		fetch.error(function(response){
			alert('FETCH ALL USERS FAILED!!!');
		});

		//alert('done with viewing users');
	};

	// gets the template to ng-include for a table row / item
	$scope.getTemplate = function (contact) {
		if (contact.id === $scope.model.selected.id) return 'edit';
		else return 'display';
	};

	$scope.editContact = function (contact) {
		$scope.model.selected = angular.copy(contact);
	};

	$scope.saveContact = function (idx) {
		console.log("Saving contact");
		$scope.model.contacts[idx] = angular.copy($scope.model.selected);
		$scope.reset();
	};

	$scope.reset = function () {
		$scope.model.selected = {};
	};
});


//END OF CREATE NEW USER VER 2



//Create new user

app.controller('createNewUserController', function($scope, $http){

	$scope.genders=['ROLE_USER','ROLE_EVENT','ROlE_ADMIN','ROLE_PROPERTY','ROLE_FINANCE','ROLE_TICKETING','ROLE_EXTEVE'];
	$scope.selection=[];

	$scope.toggleSelection = function toggleSelection(gender) {
		var idx = $scope.selection.indexOf(gender);
		if (idx > -1) {
			// is currently selected
			$scope.selection.splice(idx, 1);
		}
		else {
			// is newly selected
			$scope.selected = [];
			$scope.selected.push(gender);
			//$scope.selection.push(gender);
		}
	};



	var role = $scope.roles;
	$scope.createNewUser = function(){

		var dataObj = {			
				email: $scope.ctrl.email,
				name: $scope.ctrl.name,
				roles: $scope.selection			
		};

		var create = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/addNewUser',
			data    :  dataObj//forms user object
		});
		create.success(function(){
			alert("Create New User SUCCESS   "  + JSON.stringify(dataObj))
		});
		create.error(function(){
			alert("Create New User FAILED" + JSON.stringify(dataObj));
		});
	}
});


//VIEW USER START
app.controller('viewUserList', ['$scope','$http','$location',
                                function($scope, $http,$location) {
	//assign roles
	$scope.genders=['ROLE_USER','ROLE_EVENT','ROlE_ADMIN','ROLE_PROPERTY','ROLE_FINANCE','ROLE_TICKETING','ROLE_EXTEVE'];

	$scope.selection=[];


	$scope.toggleSelection = function toggleSelection(gender) {
		var idx = $scope.selection.indexOf(gender);
		if (idx > -1) {
			// is currently selected
			$scope.selection.splice(idx, 1);
		}
		else {
			// is newly selected
			$scope.selection.push(gender);
		}
	};


	/*  $scope.checkAll = function() {
		    $scope.user.roles = Object.keys($scope.roles);
		  };
		  $scope.uncheckAll = function() {
		    $scope.user.roles = [];
		  };
		  $scope.checkFirst = function() {
		    $scope.user.roles.splice(0, $scope.user.roles.length); 
		    $scope.user.roles.push('a');
		  };*/

	//End of assign roles







	$scope.Profiles = [];

	$scope.send = function(){
		console.log("REACHED HERE FOR FETCHING ALL USERS");

		var fetch = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/viewAllUsers',
			//forms user object
		});

		console.log("fetching the user list.......");
		fetch.success(function(response){

			$scope.Profiles = response;
		//	alert('FETCHED ALL USERS SUCCESS!!! ' + JSON.stringify(response));
		});
		fetch.error(function(response){
			//alert('FETCH ALL USERS FAILED!!!');
		});

		//alert('done with viewing users');
		//$location.path('/viewUserList');
	}
	$scope.send();


	/*	$scope.Profiles = [
	                   {
	                	   name : "gede",
	                	   country : "indonesia",
	                	   editable : false
	                   },
	                   {
	                	   name : "made",
	                	   country : "thailand",
	                	   editable : false
	                   }
	                   ];*/

	$scope.entity = {}
	$scope.name = "";
	$scope.email = "";

	$scope.updateValue = function(name, email){
		$scope.name = name;
		$scope.email = email;
	};


	$scope.save = function(index){
		$scope.Profiles[index].editable = true;
		//$scope.entity = $scope.Profiles[index];
		$scope.entity = $scope.Profiles[index];
		//$scope.entity.index = index;
		$scope.entity.editable = true;
		console.log("USER: " + JSON.stringify($scope.entity));
		var Edit = {

				name: $scope.name,
				roles: $scope.selection,
				email: $scope.entity.email

		}


		var toEdit = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/updateUser',
			data 	: Edit
			//forms user object
		});

		console.log("fetching the user list......." + JSON.stringify(Edit));
		toEdit.success(function(response){

			alert('EDITING THE USERS SUCCESS!!! ');
			$location.path("/workspace");
		});
		toEdit.error(function(response){
			alert('EDITING THE USERS FAILED!!!');
		});




	}

	$scope.delete = function(index){
		//$scope.Profiles.splice(index,1);
		//send to db to delete
		//var index = $scope.Profiles[index];
		//console.log("DEX" + index);
		$scope.entity = $scope.Profiles[index];


		console.log(JSON.stringify($scope.entity));
		var toDel = {
				name: $scope.entity.name,
				email: $scope.entity.email

		}

		//var toDel = $scope.Profiles[index];
		console.log("ITEM TO DELETE: " + JSON.stringify(toDel));

		var del = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/deleteUser',
			data 	: toDel
			//forms user object
		});

		console.log("fetching the user list.......");
		del.success(function(response){
			//$scope.Profiles = response;
			alert('DELETED THE USERS SUCCESS!!! ');
		});
		del.error(function(response){
			alert('DELETED THE USERS FAILED!!!');
		});


		$scope.Profiles.splice(index, 1);
	}

	$scope.edit = function(index){
		$scope.Profiles[index].editable = true;
		//send to db to save

	}

	$scope.add = function(){
		$scope.Profiles.push({
			name : "",
			email : "",
			editable : true
		})
	}
}
]);
//EDIT USER PROFILE

//USER PROFILE CONTROLLER
app.controller('userProfileController', ['$scope', '$http', function ($scope, $http) {

	$scope.submit = function(){
		//alert("SUCCESS");
		$scope.data = {};
		$scope.dataRoles = {};

		var dataObj = {
				name: $scope.userProfile.name,
		};
		console.log("** Passing data object of " + dataObj);

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/editUserProfile',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE USER PROFILE");
		send.success(function(){
			alert('CHANGE USER PROFILE IS SAVED!');
		});
		send.error(function(){
			alert('Changing password GOT ERROR!');
		});
	};

	$scope.submitChangePass = function(){
		//alert("SUCCESS");
		if ( $scope.userProfile.password1 != $scope.userProfile.password2 ){
			alert("2 Passwords do not match!");
			return;
		}
		var dataObj = {
				password: $scope.userProfile.password1,
		};
		console.log("** Passing data object of " + dataObj);

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/changePassword',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE USER PROFILE");
		send.success(function(){
			alert('You have updated your profile');
		});
		send.error(function(){
			alert('Changing password GOT ERROR!');
		});
	};

	$scope.getUserProfileRoles = function(){
		//alert("SUCCESS");

		var send = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/getProfileRoles',
		});

		console.log("GETTING THE USER PROFILE");
		send.success(function(response){
			//alert('You have GOTTEN your profile');
			console.log("GOTTEN THE USER PROFILE AS " + JSON.stringify(response));
			$scope.dataRoles = response;
		});
		send.error(function(response){
			//alert('Getting Profile GOT ERROR!');
		});
	};
	$scope.getUserProfileDetails = function(){
		//alert("SUCCESS");

		var send = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/getProfileDetails',
		});

		send.success(function(response){
			//	alert('You have GOTTEN your profile' + JSON.stringify(response));
			$scope.data = response;
		});
		send.error(function(response){
			//	alert('Getting Profile GOT ERROR!');
		});
	};
	$scope.getUserProfileRoles();
	$scope.getUserProfileDetails();


}])

//END OF EDIT USER PROFILE

















/*1. TO DO LIST*/
app.controller('taskController', function($scope, $http, $route) {
	$scope.today = new Date();
	$scope.saved = localStorage.getItem('taskItems');
	$scope.taskItem = (localStorage.getItem('taskItems')!==null) ? 
			JSON.parse($scope.saved) : [ {description: "Why not add a task?", date: $scope.today, complete: false}];
			localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));

			var getTdList = function(){$http({
				method: 'GET',
				url: 'https://localhost:8443/todo/getToDoList'
			}).success(function (result) {
				$scope.saved = result;
			}).error(function(result){
				//do something
				console.log("ERROR GETTING TODO LIST");
			})
			}


			//$scope.save();
			getTdList();
			$scope.newTask = null;
			$scope.newTaskDate = null;
			$scope.categories = [
			                     {name: 'Personal'},
			                     {name: 'Work'},
			                     {name: 'School'},
			                     {name: 'Cleaning'},
			                     {name: 'Other'}
			                     ];
			$scope.newTaskCategory = $scope.categories;
			$scope.addNew = function () {
				$http({
					method : "POST",
					url : "https://localhost:8443/todo/addToDoTask",
					data: {task:$scope.newTask, date: $scope.newTaskDate}
				}).then(function mySuccess(response) {
					console.log("ADDED NEW TO DO LIST");
					getTdList();

					$route.reload();
					console.log("refresh page")





				}, function myError(response) {
					console.log(response.statusText);
				});
				$scope.newTask = '';
				$scope.newTaskDate = '';
				$scope.newTaskCategory = $scope.categories;
				localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));

			};
			$scope.deleteTask = function (id) {
				$http({
					method : "POST",
					url : "https://localhost:8443/todo/deleteToDoTask",
					data: id
				}).then(function mySuccess(response) {
					console.log("DELETED TASK WITH ID: " + id);
					getTdList();
				}, function myError(response) {
					console.log(response.statusText);
				});
				getTdList();


			};
});

app.controller('DemoCtrl', function ($scope, $http) {
	$scope.selectedNotifications = null;
	$scope.testNotifications = [];

	$http({
		method: 'GET',
		url: 'https://localhost:8443/todo/getToDoList'
	}).success(function (result) {
		$scope.var = result;
		$scope.selected = result;
		console.log(JSON.stringify(result));
	}).error(function(result){
		//do something
		console.log("ERROR");
	})
});

//2. ALERT NOTIFICATIONS

app.factory('datfactory', function ($http, $q){

	this.getlist = function(){            
		return $http({
			method: 'GET',
			url: 'https://localhost:8443/user/notifications/findAllNotifications'
		}).then(function(response) {
			//console.log("RESPONSE:    " + response.data.subject); //I get the correct items, all seems ok here
			console.log("JSON PARSING:  " +JSON.stringify(response.data));
			return response.data;


		});            
	}
	return this;
});

app.controller('AlertDemoCtrl', function ($scope, datfactory, $http){
	datfactory.getlist()
	.then(function(arrItems){
		//$scope.alerts = arrItems;
		//$scope.alerts = [];
		$scope.data = arrItems
		/* angular.forEach($scope.data, function(item){
    		  console.log("COME IN HERE");
              console.log(item.task);
              $scope.alerts.push(item.task + " HELLO");
          })*/
		$scope.alerts = [];
		$scope.getId = [];
		for(var key in arrItems){

			$scope.alerts.push(arrItems[key].senderName + ": "  + arrItems[key].subject + " - " + arrItems[key].message);
			$scope.getId.push(arrItems[key].id);
			console.log($scope.alerts);
		}

	});
	$scope.closeAlert = function(index) {

		console.log("ID: " + $scope.getId[index]);
		var del = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/deleteNotification',
			data    :  $scope.getId[index] //forms user object
		});
		del.success(function(){
			alert("DELETE NOTIFICATION SUCCESS")
		});
		del.error(function(){
			alert("DELETE NOTIFICATION FAILED");
		});
		$scope.alerts.splice(index, 1);
	};
});

//MESSAGE

//Message
app.controller('ListController', ['$scope', '$http', function ($scope, $http) {
	//populate this with stored message, http get from db
	//return with array of messages


	$scope.messages = function(){
		//alert("SUCCESS");

		$scope.msg = {};
		var getMsg = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/notifications/findAllNotifications',

		});

		console.log("GETTING THE MESSAGES");
		getMsg.success(function(response){
			$scope.msg = response;
			for(var key in response){
				//$scope.selectedContacts
				$scope.alerts.push(arrItems[key].message);
				$scope.getId.push(arrItems[key].id);
				console.log($scope.alerts);
			}
			//alert('Message Gotten');
		});
		getMsg.error(function(){
			//alert('Get msg error!!!!!!!!!!');
		});
	};
}]);

app.controller('DetailController', function($scope, $routeParams){
	$scope.message = messages[$routeParams.id];
});

app.controller('NewMailController', function($scope, $location, $http){
	var getMsg = $http({
		method  : 'GET',
		url     : 'https://localhost:8443/getUsersToSendTo',
	});
	console.log("GETTING THE MESSAGES");
	getMsg.success(function(response){
		$scope.contacts = response;
		$scope.selectedContacts = [];
		console.log("RESPONSE IS" + JSON.stringify(response));

		/*for(var key in response){
			$scope.contacts.push(response[key]);
		}*/
		console.log('Senders Gotten');
	});
	getMsg.error(function(){
		//alert('Get sender error!!!!!!!!!!');
	});

	$scope.currentlySelected = null;

	$scope.selectRecipient = function(){
		$scope.selectedContacts.push($scope.currentlySelected);
	}

	$scope.deleteRecipient = function(recipient){
		$scope.selectedContacts.splice(recipient,1);
	}

	$scope.send = function(){
		var dataObj = {
				subject: $scope.mail.subject,
				recipient: $scope.selectedContacts,
				//recipient: $scope.currentlySelected,
				message: $scope.message,

		};

		console.log("REACHED HERE FOR SUBMIT BUILDING " + JSON.stringify(dataObj));

		var sendMsg = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/notifications/sendNotification',
			data    : dataObj //forms user object
		});

		console.log("Sending the message");
		sendMsg.success(function(){
			alert('MESSAGE IS SENT!');
		});
		sendMsg.error(function(){
			alert('SENT MESSAGE ERROR!');
		});

		//alert('message sent');
		$location.path('/workspace');
	};
});

//===========================================================================
//fake emails:
//===========================================================================
messages = [
            { id : 0,
            	sender : 'jean@someCompany.com',
            	subject : 'Hi there, old friend',
            	date : 'Dec 7, 2013 12:32:00',
            	recipients : ['greg@someCompany.com'],
            	message : 'Hey, we should get together for lunch sometime and catch up. There are many things we should collaborate on this year.'
            },
            { id : 1,
            	sender : 'maria@someCompany.com',
            	subject : 'Where did you leave my laptop?',
            	date : 'Dec 7, 2013 08:15:12',
            	recipients : ['greg@someCompany.com'],
            	message : 'I thought you would put it in my desk drawer. But it does not seem to be there.'
            },
            { id : 2,
            	sender : 'bill@someCompany.com',
            	subject : 'Lost python',
            	date : 'Dec 6, 2013 20:35:02',
            	recipients : ['greg@someCompany.com'],
            	message : 'Nobody panic, but my pet python is missing from its cage. She doesnt move too fast, so just call me if you see her.'
            }
            ];

contacts = [
            { id : 0,
            	email : 'jean@somecompany.com'
            },
            { id : 1,
            	email : 'maria@somecompany.com'
            },
            { id : 2,
            	email : 'bill@somecompany.com'
            },
            ];







/*3. CALENDAR HERE */



/*       4. BUILDING         */
/////////////////////////////////////////////////////////////////////////////////////////////////

app.controller('buildingController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	$scope.submit1 = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {
				name: $scope.building.name,
				numFloor: $scope.building.numFloor,
				address: $scope.building.address,
				city: $scope.building.city,
				postalCode: $scope.building.postalCode,
				filePath: $scope.building.filePath
		};

		console.log("REACHED HERE FOR SUBMIT BUILDING " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/building/addBuilding',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE BUILDING");
		send.success(function(){
			alert('BUILDING IS SAVED!');
		});
		send.error(function(){
			alert('SAVING BUILDING GOT ERROR!');
		});
	};

	//view building
	$scope.viewBuilding = function(building){
		$scope.data = {};
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/building/viewBuildings").then(function(response){
			$scope.buildings = response.data;
			console.log("DISPLAY ALL BUILDINGS");
			console.log("LEVELS DATA ARE OF THE FOLLOWING: " + $scope.buildings);

		},function(response){
			alert("did not view");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}
	$scope.viewLevelsByBuildingId = function(id){

		$scope.dataToShare = [];

		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		//populate levels from a building of the specific ID
		$scope.url = "https://localhost:8443/level/viewLevels/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE LEVELS")
		var getLevels = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/level/viewLevels/' + id

		});

		console.log("Getting the levels using the url: " + $scope.url);
		getLevels.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET LEVELS SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//shareData.addDataId(JSON.stringify(id));
			//$location.path("/viewLevels");
		});
		getLevels.error(function(response){
			$location.path("/viewBuilding");
			console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
		});
	};	

	$scope.getBuilding = function(id){		
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/building/getBuilding/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE BUILDING INFO")
		var getBuilding = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/building/getBuilding/' + id        
		});
		console.log("Getting the building using the url: " + $scope.url);
		getBuilding.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET Building SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getBuilding.error(function(response){
			$location.path("/viewBuilding");
			console.log('GET BUILDING FAILED! ' + JSON.stringify(response));
		});

	}


	$scope.getBuildingById= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.building1 = JSON.parse(shareData.getData());
		var dataObj = {
				name: $scope.building1.name,
				numFloor: $scope.building1.numFloor,
				address: $scope.building1.address,
				city: $scope.building1.city,
				postalCode: $scope.building1.postalCode,
				filePath: $scope.building1.filePath
		};
		$scope.building = angular.copy($scope.building1)
		var url = "https://localhost:8443/building/updateBuilding";
		console.log("BUILDING DATA ARE OF THE FOLLOWING: " + $scope.building);
	}

	$scope.updateBuilding = function(){
		$scope.data = {};
		//$scope.building = JSON.parse(shareData.getData());
		var dataObj = {					
				id: $scope.building.id,
				name: $scope.building.name,
				numFloor: parseInt($scope.building.numFloor),
				address: $scope.building.address,
				city: $scope.building.city,
				postalCode: $scope.building.postalCode,
				filePath: $scope.building.filePath
		};		
		console.log("REACHED HERE FOR SUBMIT BUILDING " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/building/updateBuilding',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE BUILDING");
		send.success(function(){
			alert('BUILDING IS SAVED!');
		});
		send.error(function(){
			alert('UPDATING BUILDING GOT ERROR!');
		});
	};	

	$scope.deleteBuilding = function(){
		$scope.data = {};
		console.log("Start deleting");
		$scope.building = JSON.parse(shareData.getData());
		var tempObj ={id:$scope.building.id};
		console.log("fetch id "+ tempObj);
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.post("//localhost:8443/building/deleteBuilding", JSON.stringify(tempObj)).then(function(response){
			//$scope.buildings = response.data;
			console.log("Delete the BUILDING");
		},function(response){
			alert("DID NOT DELETE");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}


}]);

//service class to connect

app.service('shareData', function($window) {
	var KEY = 'App.SelectedValue';
	var data = {};
	var addData = function(newObj) {
		var mydata = $window.sessionStorage.getItem(KEY);
		data = newObj;
		if (mydata) {
			mydata = JSON.parse(mydata);
			//console.log(mydata);
		} else {
			mydata = [];
		}
		mydata.push(newObj);
		//console.log("hailing test");
		//console.log(mydata);
		$window.sessionStorage.setItem(KEY, JSON.stringify(mydata));
	};

	var getData = function(){
		var mydata = $window.sessionStorage.getItem(KEY);
		return data;
		if (mydata) {
			mydata = JSON.parse(mydata);
		}
		return mydata || [];
	};

	return {
		addData: addData,
		getData: getData
	};
});



//CONNECT BUILDING TO LEVELS
app.controller('levelController', ['$scope', '$http','shareData', function ($scope, $http, shareData) {
	$scope.viewLevels = function(){
		console.log("VIEWING LEVELS BY BUILDING ID :" + JSON.stringify(shareData.getData()));

		$scope.levels = JSON.parse(shareData.getData()); //gets the response data from building controller 
		//console.log($scope.levels.length);

		var url = "https://localhost:8443/level/viewLevels";
		console.log("LEVELS DATA ARE OF THE FOLLOWING: " + $scope.levels);
	};

	$scope.addLevel = function(){
		//alert("SUCCESS");
		console.log("start adding");
		$scope.data = {};
		$scope.building = JSON.parse(shareData.getData());
		console.log($scope.building);
		console.log($scope.building.id);
		var dataObj = {
				id: $scope.building.id,
				levelNum: $scope.level.levelNum,
				length: $scope.level.length,
				width: $scope.level.width,
				filePath: $scope.level.filePath
		};

		console.log("REACHED HERE FOR SUBMIT LEVEL " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/level/addLevel',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE LEVEL");
		send.success(function(){
			alert('LEVEL IS SAVED!');
		});
		send.error(function(){
			alert('SAVING LEVEL GOT ERROR!');
		});
	};

	$scope.updateLevel = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.building = JSON.parse(shareData.getData());
		//console.log($scope.building.id);
		//$scope.level = JSON.parse(shareData.getDataId());
		console.log($scope.level.id);
		var dataObj = {					
				levelId: $scope.level.id,
				//buildingId:$scope.building.id,
				levelNum: $scope.level.levelNum,
				length: $scope.level.length,
				width: $scope.level.width,
				filePath: $scope.level.filePath
		};		
		//console.log("level: "+$scope.level.id+" building: "+$scope.building.id);
		console.log("REACHED HERE FOR SUBMIT LEVEL " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/level/updateLevel',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE LEVEL");
		send.success(function(){
			alert('LEVEL IS SAVED!');
		});
		send.error(function(){
			alert('UPDATING LEVEL GOT ERROR!');
		});
	};	

	$scope.getLevel = function(id){		
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/level/getLevel/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE Level INFO")
		var getLevel = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/level/getLevel/' + id        
		});
		console.log("Getting the levelusing the url: " + $scope.url);
		getLevel.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET LEVEL SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getLevel.error(function(response){
			$location.path("/viewLevels");
			console.log('GET LEVEL FAILED! ' + JSON.stringify(response));
		});

	}

	$scope.getLevelById= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.level1 = JSON.parse(shareData.getData());
		var dataObj = {
				levelNum: $scope.level1.levelNum,
				length: $scope.level1.length,
				width: $scope.level1.width,
				filePath: $scope.level1.filePath
		};
		$scope.level = angular.copy($scope.level1)

		var url = "https://localhost:8443/level/updateLevel";
		console.log("LEVEL DATA ARE OF THE FOLLOWING: " + $scope.level);
	}

	$scope.getLevelByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.level = JSON.parse(shareData.getData());

		var url = "https://localhost:8443/level/deleteLevel";
		console.log("LEVEL DATA ARE OF THE FOLLOWING: " + $scope.level);
	}

	$scope.deleteLevel = function(){
		$scope.data = {};
		console.log("Start deleting");
		$scope.level = JSON.parse(shareData.getData());
		console.log($scope.level.id);
		var tempObj ={levelId:$scope.level.id};
		console.log("fetch id "+ tempObj);
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.post("//localhost:8443/level/deleteLevel", JSON.stringify(tempObj)).then(function(response){
			//$scope.buildings = response.data;
			console.log("Delete the LEVEL");
		},function(response){
			alert("DID NOT DELETE LEVEL");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}

}]);	


app.controller('vendorController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	$scope.viewAllVendors = function(){
		$scope.data = {};	
		$http.get("//localhost:8443/vendor/viewAllVendors").then(function(response){
			$scope.vendors = response.data;
			console.log("DISPLAY ALL vendors");
		},function(response){
			alert("did not view vendors");
		}	
		)	
	}

	$scope.getVendor = function(id){		
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/vendor/getVendor/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE Vendor INFO")
		var getVendor = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/vendor/getVendor/' + id        
		});
		console.log("Getting the vendor using the url: " + $scope.url);
		getVendor.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET EVENT SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getVendor.error(function(response){
			$location.path("/viewAllVendors");
			console.log('GET Vendor FAILED! ' + JSON.stringify(response));
		});			
	}

	$scope.getVendorById= function(){			
		$scope.vendor1 = JSON.parse(shareData.getData());
		var dataObj = {			
				email: $scope.vendor1.email,
				name: $scope.vendor1.name,
				description: $scope.vendor1.description,
				contact: $scope.vendor1.contact,						
		};
		$scope.vendor = angular.copy($scope.vendor1)

		var url = "https://localhost:8443/vendor/updateVendor";
		//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}

	$scope.updateVendor = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.vendor.id);
		var dataObj = {				
				id: $scope.vendor.id,
				email: $scope.vendor.email,
				name: $scope.vendor.name,
				description: $scope.vendor.description,
				contact: $scope.vendor.contact,		};		
		//console.log(dataObj.event_approval_status);
		console.log("REACHED HERE FOR SUBMIT VENDOR " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/vendor/updateVendor',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE VENDOR");
		send.success(function(){
			alert('VENDOR IS SAVED!');
		});
		send.error(function(){
			alert('UPDATING VENDOR GOT ERROR!');
		});
	};	

	$scope.deleteVendor = function(){
		$scope.data = {};
		console.log("Start deleting vendor");
		$scope.vendor = JSON.parse(shareData.getData());
		console.log($scope.vendor.id);
		var tempObj ={vendorId:$scope.vendor.id};
		console.log("fetch id "+ tempObj);
		$http.post("//localhost:8443/vendor/deleteVendor", JSON.stringify(tempObj)).then(function(response){
			console.log("Cancel the VENDOR");
		},function(response){
			alert("DID NOT Cancel VENDOR");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}
	$scope.getVendorByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.vendor = JSON.parse(shareData.getData());

		var url = "https://localhost:8443/vendor/deleteVendor";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.vendor);
	}

	$scope.addVendor = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {			
				email: $scope.vendor.email,
				name: $scope.vendor.name,
				description: $scope.vendor.description,
				contact: $scope.vendor.contact,						
		};

		console.log("REACHED HERE FOR SUBMIT Vendor " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/vendor/addVendor',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE vendor");
		send.success(function(){
			alert('vendor IS SAVED!');
		});
		send.error(function(){
			alert('SAVING vendor GOT ERROR!');
		});
	};

}]);	


app.controller('maintenanceController',['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	$scope.viewMaintenance = function(){
		$scope.data = {};	
		$http.get("//localhost:8443/maintenance/viewMaintenance").then(function(response){
			$scope.maintenance_requests = response.data;
			console.log("DISPLAY ALL maintenance requests");
		},function(response){
			alert("did not view maintenance requests");
		}	
		)	
	}

	$scope.getMaintenance = function(id){		
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/maintenance/getMaintenance/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE MAINTENANCE INFO")
		var getVendor = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/maintenance/getMaintenance/' + id        
		});
		console.log("Getting the maintenance using the url: " + $scope.url);
		getVendor.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET MAINTENANCE SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getVendor.error(function(response){
			$location.path("/viewMaintenance");
			console.log('GET MAINTENANCE FAILED! ' + JSON.stringify(response));
		});			
	}




	$scope.getMaintenanceById= function(){			
		$scope.maintenance1 = JSON.parse(shareData.getData());
		$scope.maintenance1.start = new Date($scope.maintenance1.start);
		$scope.maintenance1.end = new Date($scope.maintenance1.end);
		var dataObj = {			
				vendors: $scope.maintenance1.vendors,
				start: $scope.maintenance1.start,
				end: $scope.maintenance1.end,
				description: $scope.maintenance1.description,

		};
		$scope.maintenance = angular.copy($scope.maintenance1)

		var url = "https://localhost:8443/maintenance/updateMaintenance";
		//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}

	$scope.updateMaintenance = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.maintenance.id);
		var dataObj = {				
				id: $scope.maintenance.id,
				vendors: $scope.maintenance.vendors,
				start: $scope.maintenance.start,
				end: $scope.maintenance.end,
				description: $scope.maintenance.description,	
		};		
		//console.log(dataObj.start);
		console.log("REACHED HERE FOR SUBMIT maintenance " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/maintenance/updateMaintenance',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE MAINT");
		send.success(function(){
			alert('MAINT IS SAVED!');
		});
		send.error(function(){
			alert('UPDATING MAINT GOT ERROR!');
		});
	};	

	$scope.deleteMaintenance = function(){
		$scope.data = {};
		console.log("Start deleting maintenance");
		$scope.maintenance = JSON.parse(shareData.getData());
		console.log($scope.maintenance.id);
		var tempObj ={id:$scope.maintenance.id};
		$http.post("//localhost:8443/maintenance/deleteMaintenance", JSON.stringify(tempObj)).then(function(response){
			console.log("delete the maintenance");
		},function(response){
			alert("DID NOT delete maintenance");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}
	$scope.getMaintenanceByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.maintenance = JSON.parse(shareData.getData());

		var url = "https://localhost:8443/maintenance/deleteMaintenance";
		console.log("Maintenance DATA ARE OF THE FOLLOWING: " + $scope.maintenance);
	}

	$scope.addMaintenance = function(){
		//alert("SUCCESS");
		var unitIdsString="";
		var unitIdsObj = JSON.parse(shareData.getData());
		unitIdsString+=unitIdsObj.units;
		console.log("test hailing");
		console.log(unitIdsString);
		$scope.data = {};
		var dataObj = {
				units: unitIdsString,
				vendors: $scope.maintenance.vendorId,
				start: ($scope.maintenance.start).toString(),
				end: ($scope.maintenance.end).toString(),
				description: $scope.maintenance.description,
		};

		console.log("REACHED HERE FOR SUBMIT MAINTENANCE " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/maintenance/addMaintenance',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE BUILDING");
		send.success(function(){
			alert('MAINTENANCE IS SAVED!');
		});
		send.error(function(){
			alert('MAINTENANCE IS NOT SAVED BECAUSE IT IS NOT AVAILABLE!');
		});
	};

}]);


//===========================================================================
//5. Events.
//===========================================================================



app.controller('eventExternalController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {


	$scope.addEvent = function(){
		var unitIdsString="";
		var unitIdsObj = JSON.parse(shareData.getData());
		unitIdsString+=unitIdsObj.units;
		console.log("test hailing");
		console.log(unitIdsString);

		console.log("start adding");
		$scope.data = {};

		var dataObj = {
				units:unitIdsString,
				event_title: $scope.event.event_title,
				event_content: $scope.event.event_content,
				event_description: $scope.event.event_description,
				event_approval_status: "processing",
				//x.setHours(x.getHours() - x.getTimezoneOffset() / 60);			
				//event_start_date: $scope.event.event_start_date.setHours($scope.event.event_start_date.getHours()-$scope.event.event_start_date.getTimezoneOffset() / 60),
				event_start_date: ($scope.event.event_start_date).toString(),
				event_end_date: ($scope.event.event_end_date).toString(),
				//event_period: $scope.event.event_period,
				filePath: $scope.event.filePath,
		};
		//console.log(JSON.parse($scope.event.event_start_date));
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
		//Date.prototype.toJSON = function(){ return moment(this).format(); }
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/event/addEvent',
			data    : dataObj //forms user object
		});

		//shareData.addData(JSON.stringify(response));	 

		console.log("SAVING THE Event");
		send.success(function(){
			alert('Event IS SAVED!');
		});
		send.error(function(){
			alert('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
		});
	};

	$scope.viewEvents = function(){
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		$http.get("//localhost:8443/event/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			alert("did not view all events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}

	$scope.viewApprovedEvents = function(){
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		$http.get("//localhost:8443/event/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			alert("did not view approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}

	$scope.getEvent = function(id){		
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/event/getEvent1/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENT INFO")
		var getEvent = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/getEvent1/' + id        
		});
		console.log("Getting the event using the url: " + $scope.url);
		getEvent.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET EVENT SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getEvent.error(function(response){
			$location.path("/viewAllEventsEx");
			console.log('GET Event FAILED! ' + JSON.stringify(response));
		});

	}

	$scope.getEventById= function(){

		$scope.event1 = JSON.parse(shareData.getData());
		$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
		$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
		var dataObj = {			
				units:$scope.event1.units,
				event_title: $scope.event1.event_title,
				event_content: $scope.event1.event_content,
				event_description: $scope.event1.event_description,
				event_approval_status: $scope.event1.event_approval_status,						
				event_start_date: $scope.event1.event_start_date,						
				event_end_date: $scope.event1.event_end_date,
				filePath: $scope.event1.filePath,
		};
		$scope.event = angular.copy($scope.event1)

		var url = "https://localhost:8443/event/updateEvent";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}

	$scope.updateEvent = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.building = JSON.parse(shareData.getData());
		//console.log($scope.building.id);
		//$scope.level = JSON.parse(shareData.getDataId());
		console.log($scope.event.id);
		var dataObj = {	
				id: $scope.event.id,
				units:$scope.event.units,
				event_title: $scope.event.event_title,
				event_content: $scope.event.event_content,
				event_description: $scope.event.event_description,
				event_approval_status: "processing",
				event_start_date: ($scope.event.event_start_date).toString(),
				event_end_date: ($scope.event.event_end_date).toString(),
				//event_period: $scope.event.event_period,
				filePath: $scope.event.filePath,
		};		
		console.log(dataObj.units);
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/event/updateEvent',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE EVENT");
		send.success(function(){
			alert('EVENT IS SAVED!');
		});
		send.error(function(){
			alert('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
		});
	};	


	$scope.deleteEvent = function(){
		$scope.data = {};
		console.log("Start deleting event");
		$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var tempObj ={eventId:$scope.event.id};
		console.log("fetch id "+ tempObj);
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.post("//localhost:8443/event/deleteEvent", JSON.stringify(tempObj)).then(function(response){
			//$scope.buildings = response.data;
			console.log("Cancel the EVENT");
		},function(response){
			alert("DID NOT Cancel EVENT");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}
	$scope.getEventByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.event = JSON.parse(shareData.getData());

		var url = "https://localhost:8443/event/deleteEvent";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event);
	}
	/*
$scope.buildings=[];
$scope.viewBuilding = function(building){
			$scope.data = {};
			$http.get("//localhost:8443/building/viewBuildings").then(function(response){
				$scope.buildings = response.data;
				console.log("DISPLAY ALL BUILDINGS");
				console.log("LEVELS DATA ARE OF THE FOLLOWING: " + $scope.buildings);

			},function(response){
				alert("did not view");
			}	
			)	
		}



$scope.viewLevelsByBuildingId = function(id){
		$scope.buildingId=id;
		console.log($scope.buildingId);

	}

$scope.levels;	
$scope.viewLevels=function(){
	//populate levels from a building of the specific ID
	var id=$scope.buildingId
	$scope.url = "https://localhost:8443/level/viewLevels/"+id;

	console.log("GETTING THE LEVELS")
	var getLevels = $http({
         method  : 'GET',
         url     : 'https://localhost:8443/level/viewLevels/' + id

       });

	console.log("Getting the levels using the url: " + $scope.url);
	getLevels.success(function(response){

	console.log('GET LEVELS SUCCESS! ' + angular.fromJson(response));
	console.log( angular.fromJson(response));
	console.log("ID IS " + id);
	// need to check how to pass in 
	 $scope.levels=angular.fromJson(response);
	 console.log($scope.levels);

	});

	getLevels.error(function(response){
		$location.path("/viewBuildingEx");
		console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
	});
			}
	 */

}]);




app.controller('eventController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	$scope.viewEvents = function(){
		$scope.data = {};

		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/eventManager/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT fir event manager");
			//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.buildings);

		},function(response){
			alert("did not view all events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}

	$scope.viewApprovedEvents = function(){
		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			alert("did not view approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}

	$scope.viewToBeApprovedEvents = function(){
		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewToBeApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			alert("did not view to-be-approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}

	$scope.viewEventOrganizers = function(){
		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewEventOrganizers").then(function(response){
			$scope.eventOrgs = response.data;
			console.log("DISPLAY ALL EVENT ORGANIZERS");
			console.log("EVENT ORGS DATA ARE OF THE FOLLOWING: " + $scope.eventOrgs);

		},function(response){
			alert("did not view eventOrgs");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}

	$scope.getEvent = function(id){		
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/eventManager/getEvent/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENT INFO")
		var getEvent = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/getEvent/' + id        
		});
		console.log("Getting the event using the url: " + $scope.url);
		getEvent.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET EVENT SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getEvent.error(function(response){
			$location.path("/viewAllEvents");
			console.log('GET Event FAILED! ' + JSON.stringify(response));
		});			
	}


	$scope.getEventById= function(){			
		$scope.event1 = JSON.parse(shareData.getData());
		$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
		$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
		var dataObj = {			
				event_title: $scope.event1.event_title,
				event_content: $scope.event1.event_content,
				event_description: $scope.event1.event_description,
				event_approval_status: $scope.event1.event_approval_status,						
				event_start_date: $scope.event1.event_start_date,				
				event_end_date: $scope.event1.event_end_date,
				//event_period: $scope.event1.event_period,
				filePath: $scope.event1.filePath,
		};
		$scope.event = angular.copy($scope.event1)

		var url = "https://localhost:8443/eventManager/updateEventStatus";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}

	$scope.getEventDetailsById= function(){			
		$scope.event1 = JSON.parse(shareData.getData());
		$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
		$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
		var dataObj = {			
				event_title: $scope.event1.event_title,
				event_content: $scope.event1.event_content,
				event_description: $scope.event1.event_description,
				event_approval_status: $scope.event1.event_approval_status,						
				event_start_date: $scope.event1.event_start_date,				
				event_end_date: $scope.event1.event_end_date,
				//event_period: $scope.event1.event_period,
				filePath: $scope.event1.filePath,
		};
		$scope.event = angular.copy($scope.event1)

		var url = "https://localhost:8443/eventManager/viewEventDetails";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}


	$scope.updateEventStatus = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var dataObj = {				
				id: $scope.event.id,
				event_approval_status: $scope.event.event_approval_status,
		};		
		console.log(dataObj.event_approval_status);
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/eventManager/updateEventStatus',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE EVENT");
		send.success(function(){
			alert('EVENT IS SAVED!');
		});
		send.error(function(){
			alert('UPDATING EVENT GOT ERROR!');
		});
	};	

	$scope.approveEvent = function(){
		$scope.data = {};
		console.log("Start approving event");
		$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var tempObj ={eventId:$scope.event.id};
		console.log("fetch id "+ tempObj);
		$http.post("//localhost:8443/eventManager/approveEvent", JSON.stringify(tempObj)).then(function(response){
			console.log("Approve the EVENT");
		},function(response){
			alert("DID NOT approve EVENT");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}

	$scope.deleteEvent = function(){
		$scope.data = {};
		console.log("Start deleting event");
		$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var tempObj ={eventId:$scope.event.id};
		console.log("fetch id "+ tempObj);
		$http.post("//localhost:8443/eventManager/deleteEvent", JSON.stringify(tempObj)).then(function(response){
			console.log("Cancel the EVENT");
		},function(response){
			alert("DID NOT Cancel EVENT");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}
	$scope.getEventByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.event = JSON.parse(shareData.getData());

		var url = "https://localhost:8443/eventManager/deleteEvent";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event);
	}

	
	$scope.getNotifications = function(id){		
		$scope.dataToShare = [];	  
		console.log(id);
		$scope.shareMyData = function (myValue) {
		    //$scope.dataToShare = myValue;
		    //shareData.addData($scope.dataToShare);
		  }
		$scope.url = "https://localhost:8443/eventManager/getNotifications/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE NOTIFICATIONS")
		var getNotifications = $http({
	           method  : 'GET',
	           url     : 'https://localhost:8443/eventManager/getNotifications/' + id        
	         });
		console.log("Getting the event organizer using the url: " + $scope.url);
		getNotifications.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET NOTIFCATIONS SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getNotifications.error(function(response){
			$location.path("/viewEventOrganizers");
			console.log('GET NOTIFICATIONS FAILED! ' + JSON.stringify(response));
		});
		
	}
	
	
	

}]);


app.controller('notificationController', ['$scope', '$http','shareData', function ($scope, $http, shareData) {		

	$scope.viewNotifications = function(){
		$scope.data = {};
		$scope.notifications = JSON.parse(shareData.getData()); //gets the response data from building controller 
		//console.log($scope.levels.length);			
		//var url = "https://localhost:8443/event/viewLevels";

	}

}]);

//FLOORPLAN
app.directive('draggable', function() {
	console.log("test draggable directive");
	jQuery = window.jQuery;
	console.log(jQuery);
	console.log(jQuery.ui);
	return {
		// A = attribute, E = Element, C = Class and M = HTML Comment
		restrict:'A',
		controller: 'MyCtrl',

		/*
		      scope: {

		            callback: '&onDrag'
		        },
		 */
		//The link function is responsible for registering DOM listeners as well as updating the DOM.
		//link: function postLink(scope, element, attrs) {
		link: function (scope, element, attrs) {
			element.draggable({
				//revert:'invalid'
				containment: '#glassbox',
				
				 obstacle: "#1",
				    preventCollision: true
			
				

			});
			element.on('drag', function (unit,evt, ui) {
				//console.log(evt);
				//  console.log(ui);
				scope.$apply(function() {
					if (scope.callback) { 
						scope.callback({$evt: evt, $ui: ui }); 
					}                
				})
			});
		}
	};
})

app.directive('resizable', function () {
	console.log("test resiable directive");
	// jQuery = window.jQuery;
	//console.log(jQuery);
	console.log("test jquery ui");
	console.log(jQuery.ui);
	return {

		restrict: 'A',
		controller: 'MyCtrl',
		scope: {

			callback: '&onResize'

		},
		link: function postLink(scope, element, attrs) {
			element.resizable({
				containment: '#glassbox'
			});
			console.log("test jquery ui");
			element.on('resize', function (unit,evt, ui) {
				//console.log(evt);
				//  console.log(ui);
				scope.$apply(function() {
					if (scope.callback) { 
						console.log("test jquery ui");
						scope.callback({$evt: evt, $ui: ui }); 
					}                
				})
			});
		}
	};
})




//floorplan
app.controller('MyCtrl', function ($scope, $http,shareData) {
	//jQuery = window.jQuery;
	//console.log(jQuery);
	//console.log(jQuery.ui);
	var levelIdObj;
	var levelId;
	var level;
	angular.element(document).ready(function () {
		level = JSON.parse(shareData.getData());
	    console.log("test, hailing, after ready");
	    $scope.levelLength;
		$scope.levelWidth;
		
		//console.log("test hailing");
		console.log(level);
		if(!level.id){
		
			level=level[0];
			console.log("level");
		}
		$scope.levelLength=900;
		$scope.levelWidth=parseInt((level.width)*900/(level.length));
		levelId=level.id;
		levelIdObj={
				id:levelId
		}
		
		//retrieve units when page loaded
		$http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
			console.log("pure response is "+response.data);

			console.log("test anglar.fromJon"+angular.fromJson(response.data));
			$scope.units=angular.fromJson(response.data);

		},function(response){
			console.log("DID NOT view");
			console.log("response is "+angular.fromJson(response.data).error);
		})
		
	});
	//$scope.units=[];
	//var app = angular.module('myApp', ['angularResizable']);
	//$scope.level = JSON.parse(shareData.getData());//copied from level contorller
	var levelIdObj;
	var levelId;
	//$scope.levelLength=700;
	//$scope.levelWidth=600;
	$scope.units=[];
	$scope.viewUnit=function(){
		$scope.units=[];
		alert("viewing existing units");
		var level = JSON.parse(shareData.getData());
		//console.log("test hailing");
		console.log("view units hailing");
		console.log(level.id);
		levelId=level.id;
	
		
		// levelId=1;//need to pass level id here nedd to delete this part

		//get level id from previous page
		levelIdObj={
				id:levelId
		}
		//retrieve units when page loaded
		$http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
			console.log("pure response is "+response.data);

			console.log("test anglar.fromJon"+angular.fromJson(response.data));
			$scope.units=angular.fromJson(response.data);

		},function(response){
			console.log("DID NOT view");
			console.log("response is "+angular.fromJson(response.data).error);
		})


	}

	$scope.addUnit = function () {  
		$scope.units.push({"id": 0,"unitNumber": "#unit","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "coral","type": "./svg/rect.svg"}});
		console.log("test "+JSON.stringify($scope.units));

	} 
	$scope.specialType;
	$scope.addSpecialUnit = function (type) {  	
		$scope.units.push({"id": 0,"unitNumber": "","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "transparent","type": type}});
		console.log("test "+JSON.stringify($scope.units));

	} 

	$scope.saveUnits = function () {   


		console.log("Test: start saving units");
		var saveUnits=$scope.units;
		var unitsString=angular.toJson(saveUnits);
		console.log(unitsString);

		var dataObj = {
				id: levelId,
				Units:{
					Unit:saveUnits
				}
		};



		console.log(dataObj);

		$http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
			console.log("pure response is "+JSON.stringify(response.data));
			alert("Saved. Please click on \"Start\" button to view.");
			//if saved successfully, retrieve and view the new units of this level
			/*$http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(responseView){
	    	    		console.log("pure response is "+responseView.data);
	    	    		console.log("test"+JSON.stringify(responseView.data.records));//how to pass set of objects back to javascript controller
	    	    		console.log("test anglar.fromJon"+angular.fromJson(responseView.data));
	    	    		$scope.units=angular.fromJson(responseView.data);

	        		},function(responseView){
	    	    		console.log("DID NOT view");
	    	    		console.log("response is "+angularfromJson(responseView.data).error);
	    	    	})
			 */
			//just press the view Units button
		},function(response){//else is not saved successfully
			console.log("DID NOT SAVE");
			console.log("response is "+JSON.stringify(response.data));
		})


	} 

	$scope.viewLevelsByBuildingId = function(){
		var buildingId;
		$scope.dataToShare = [];
		//get building id from levelId
		$http.post('/level/getBuildingId', JSON.stringify(levelIdObj)).then(function(response){
			console.log('GET BUILDING SUCCESS! ' + JSON.stringify(response));
			var buildingJson=response.data;
			console.log(buildingJson);
			//var temp=JSON.stringify(buildingJson)
			buildingId=buildingJson.buildingId;

			console.log("Building ID IS " + buildingId);

		},function(response){
			console.log('GET BUILDING ID FAILED! ' + JSON.stringify(response));
		}).then(function() {

			//get levels of building id and then save to share data
			$scope.url = "https://localhost:8443/level/viewLevels/"+buildingId;
			//$scope.dataToShare = [];
			console.log("GETTING THE LEVELS")
			var getLevels = $http({
				method  : 'GET',
				url     : 'https://localhost:8443/level/viewLevels/' + buildingId

			});

			console.log("Getting the levels using the url: " + $scope.url);
			getLevels.success(function(response){
				//$scope.dataToShare.push(id);
				//$location.path("/viewLevels/"+id);
				console.log('GET LEVELS SUCCESS! ' + JSON.stringify(response));
				console.log("ID IS " + buildingId);
				shareData.addData(JSON.stringify(response));
				//shareData.addDataId(JSON.stringify(id));
				//$location.path("/viewLevels");
			});
			getLevels.error(function(response){
				$location.path("/viewBuilding");//not sure
				console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
			});

		})





	}
	//$scope.showDetail="test";
	 $scope.showDetails= function (thisUnit) {   
		 console.log(thisUnit);
		// console.log(event);
		// console.log(event.target.classList);
		 $scope.showDetail="unitNumber: "+thisUnit.unitNumber  +  " Unit Width: " + parseInt((thisUnit.square.height)*(level.length)/900) + "meter, Unit Length: " + parseInt((thisUnit.square.width)*(level.length)/900) +"meter";
		 console.log($scope.showDetail);
	    } 

	$scope.resize = function(unit,evt,ui) {

		console.log("resize");

		unit.square.width = evt.size.width;//working restrict A
		unit.square.height = evt.size.height;
		unit.square.left = parseInt(evt.position.left);
		unit.square.top = parseInt(evt.position.top);
		
		

		
	}
	$scope.drag = function(unit,evt,ui) {

		console.log(evt);
		console.log("DRAGGING");

		// $scope.units[x.id].square.width = ui.size.width;
		//$scope.units[x.id].square.height = ui.size.height;
		//$scope.units[unit.id-1].square.width = evt.size.width;
		//$scope.units[unit.id-1].square.height = evt.size.height;
		// console.log( evt.target);
		// console.log(  evt.helper.context.clientHeight); //working
		// console.log(  evt.helper[0].clientHeight); //working restrict E
		unit.square.left = parseInt(evt.position.left);
		unit.square.top = parseInt(evt.position.top);
		unit.square.width = evt.helper.context.clientWidth;
		unit.square.height = evt.helper.context.clientHeight;
	}

	$scope.remove = function(unit) { 
		var index = $scope.units.indexOf(unit);
		$scope.units.splice(index, 1);     
	}

//	for external event organisers
	var unitIds="";
	$scope.addToUnitIds=function(unitId){
		unitIds+=(unitId+" ");
		console.log(unitIds);
	}
//	pass selected units ids to share data
	$scope.passUnitIds=function(){
		var stringToPassUnit=unitIds.substring(0,unitIds.length-1);
		console.log(stringToPassUnit);
		var objToPassUnit={'units':stringToPassUnit};
		shareData.addData(JSON.stringify(objToPassUnit));
		console.log(JSON.stringify(objToPassUnit));
	}


})







//Unit Plan of Event used by event organiser
app.controller('areaPlanController', function ($scope, $http,shareData) {
	jQuery = window.jQuery;
	console.log(jQuery);
	console.log(jQuery.ui);


	var eventIdObj;
	var eventId;
	$scope.areas=[];
	$scope.viewArea=function(){
		$scope.areas=[];
		alert("viewing existing areas");
		var event = JSON.parse(shareData.getData());
		console.log("test hailing");
		console.log(event.id);
		eventId=event.id;

		//get event id from previous page
		eventIdObj={
				id:eventId
		}
		//retrieve areas when page loaded
		$http.post('//localhost:8443/event/viewAreas', JSON.stringify(eventIdObj)).then(function(response){
			console.log("pure response is "+response.data);

			console.log("test anglar.fromJon"+angular.fromJson(response.data));
			$scope.areas=angular.fromJson(response.data);

		},function(response){
			console.log("DID NOT view");
			console.log("response is "+angularfromJson(response.data).error);
		})


	}

	$scope.addArea = function () {  
		alert("Please edit information of the new area in the table below.");
		$scope.areas.push({"id": 0,"areaName": "#area","description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "coral","type": "rect"}});
		console.log("test "+JSON.stringify($scope.areas));

	} 

	$scope.saveAreas = function () {   

		console.log("Test: start saving areas");
		var saveAreas=$scope.areas;
		var areasString=angular.toJson(saveAreas);
		console.log(areasString);

		var dataObj = {
				id: eventId,
				Areas:{
					Area:saveAreas
				}
		};

		console.log(dataObj);

		$http.post('/event/saveAreas', JSON.stringify(dataObj)).then(function(response){
			console.log("pure response is "+JSON.stringify(response.data));
			alert("Saved. Please click on \"Start\" button to view.");

		},function(response){//else is not saved successfully
			console.log("DID NOT SAVE");
			console.log("response is "+JSON.stringify(response.data));
		})


	} 

	$scope.remove = function(area) { 
		var index = $scope.areas.indexOf(area);
		$scope.areas.splice(index, 1);     
	}

	$scope.showDetails= function (thisArea) {   
		//console.log(thisArea.id); 

		$scope.showDetail="id: "+ thisArea.id+", areaName: " + thisArea.areaName+", description: " + thisArea.description+"left: " + thisArea.square.left + ", top: " +  thisArea.square.top+ ", height: " + thisArea.square.height + ", width: " + thisArea.square.width;    

	} 
	$scope.resize = function(area,evt,ui) {

		console.log("resize");

		area.square.width = evt.size.width;//working restrict A
		area.square.height = evt.size.height;
		area.square.left = parseInt(evt.position.left);
		area.square.top = parseInt(evt.position.top);
	}
	$scope.drag = function(area,evt,ui) {

		console.log(evt);
		console.log("DRAGGING");
		area.square.left = parseInt(evt.position.left);
		area.square.top = parseInt(evt.position.top);
		area.square.width = evt.helper.context.clientWidth;
		area.square.height = evt.helper.context.clientHeight;
	}



	/*
	    var areaIds="";
	    $scope.addToAreaIds=function(areaId){
	        areaIds+=(areaId+" ");
	        console.log(areaIds);
	    }

	    $scope.passAreaIds=function(){
	    	var stringToPassArea=areaIds.substring(0,areaIds.length-1);
	    	console.log(stringToPassArea);
	    	var objToPassArea={'areas':stringToPassArea};
	    	shareData.addData(JSON.stringify(objToPassArea));
	    	console.log(JSON.stringify(objToPassArea));
	    }
	 */

})



//===========================================================================
//6. Create client organisation.
//===========================================================================
app.controller('clientOrgController', ['$scope', '$http','$location', function ($scope, $http, $location) {
	$scope.genders=['Property System','Event Management System','Finance System'];
	$scope.selection=[];

	$scope.toggleSelection = function toggleSelection(gender) {
		var idx = $scope.selection.indexOf(gender);
		if (idx > -1) {
			// is currently selected
			$scope.selection.splice(idx, 1);
		}
		else {
			// is newly selected
			$scope.selection.push(gender);
		}
	};


	$scope.submit = function(){
		//alert("SUCCESS");
		$scope.data = {};
		console.log("** Passing data object of " + dataObj);
		var dataObj = {
				name: $scope.clientOrg.name,
				email: $scope.clientOrg.email,
				subscription: $scope.selection,
				nameAdmin: $scope.clientOrg.nameAdmin
		};

		console.log("REACHED HERE FOR SUBMIT BUILDING " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/addClientOrganisation',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE CLIENT ORG");
		send.success(function(){
			alert('CLIENT ORG IS SAVED!');
			$location.path("/workspace");
		});
		send.error(function(){
			alert('SAVING CLIENT ORG GOT ERROR!');
		});
	};

	/*$scope.view = function(){
			var dataObj = {
					name: $scope.building.name,
					numFloor: $scope.building.numFloor,
					address: $scope.building.address,
					city: $scope.building.city,
					postalCode: $scope.building.postalCode,
					filePath: $scope.building.filePath
			}
		};*/

}]);





//////////VIEW CLIENT ORGS//////////

app.controller('viewClientOrgs', ['$scope','$http', '$location',
                                  function($scope, $http,$location) {

	$scope.genders=['Property System','Event Management System','Finance System'];
	$scope.selection=[];

	$scope.toggleSelection = function toggleSelection(gender) {
		var idx = $scope.selection.indexOf(gender);
		if (idx > -1) {
			// is currently selected
			$scope.selection.splice(idx, 1);
		}
		else {
			// is newly selected
			$scope.selection.push(gender);
		}
	};
	// END OF SUB SYSTEM ASSIGNMENT

	$scope.Profiles = [];

	$scope.send = function(){
		console.log("REACHED HERE FOR FETCHING ALL ORGS");

		var fetch = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/viewClientOrgs',
			//forms user object
		});

		console.log("fetching the orgs list.......");
		fetch.success(function(response){

			$scope.Profiles = response;
		//	alert('FETCHED ALL orgs SUCCESS!!! ' + JSON.stringify(response));
		});
		fetch.error(function(response){
		alert('FETCH ALL orgs FAILED!!!');
		});

		//alert('done with viewing users');
		//$location.path('/viewUserList');
	}
	$scope.send();


	$scope.entity = {};
	$scope.name = "";


	$scope.updateValue = function(name){
		$scope.name = name;

	};
	console.log("SENDING THE NAME: " + $scope.name);
	$scope.save = function(index){
		$scope.Profiles[index].editable = true;
		//$scope.entity = $scope.Profiles[index];
		$scope.entity = $scope.Profiles[index];
		//$scope.entity.index = index;
		$scope.entity.editable = true;


		$scope.updateValue = function(name){
		//alert("addgin " + name + " to " + $scope.name);
			$scope.name = name;
		};


		var Edit = {
				newname: $scope.name,
				name: $scope.entity.organisationName,
				subsys: $scope.selection		
		}


		var toEdit = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/updateClientOrg',
			data 	: Edit
			//forms user object
		});

		console.log("fetching the user list.......");
		toEdit.success(function(response){
			$scope.Profiles = response;
			alert('EDITING THE ORGANIZATIONS SUCCESS');
			$location.path("/workspace");
		});
		toEdit.error(function(response){
			alert('EDITING THE ORGANIZATIONS FAILED!!!');
		});

	}

	$scope.delete = function(index){
		//$scope.Profiles.splice(index,1);
		//send to db to delete
		//var index = $scope.Profiles[index];
		//console.log("DEX" + index);
		$scope.entity = $scope.Profiles[index];


		console.log(JSON.stringify($scope.entity));
		var toDel = {
				id: $scope.entity.id,
		}

		//var toDel = $scope.Profiles[index];
		console.log("ITEM ID TO DELETE: " + JSON.stringify(toDel));

		var del = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/deleteClientOrg',
			data 	: toDel
			//forms user object
		});

		console.log("fetching the user list.......");
		del.success(function(response){
			//$scope.Profiles = response;
			alert('DELETED THE client Org SUCCESS!!! ');
		});
		del.error(function(response){
			alert('DELETED THE client Org FAILED!!!');
		});


		$scope.Profiles.splice(index, 1);
	}

	$scope.edit = function(index){
		$scope.Profiles[index].editable = true;
		//send to db to save

	}

	$scope.add = function(){
		$scope.Profiles.push({
			name : "",
			email : "",
			editable : true
		})
	}
}
]);
//////END VIEW CLIENT ORGS/////////////












//===========================================================================
//7. Audit Logs
//===========================================================================
app.controller('auditLogController', ['$scope', '$http', function ($scope, $http) {
	$scope.submit = function(){
		//alert("SUCCESS");
		$scope.data = {};
		//$scope.audit.startDate.setDate($scope.audit.startDate.getDate() + 1);
		$scope.audit.endDate.setDate($scope.audit.endDate.getDate() + 1);
		var dataObj = {
				username: $scope.selectedUser,
				system: $scope.selectedSystem,
				startDate: $scope.audit.startDate,
				endDate: $scope.audit.endDate
		};

		console.log("REACHED HERE FOR AUDIT LOG " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/downloadAuditLog',
			data    : dataObj, //forms user object
			responseType: 'arraybuffer'
		});

		console.log("DOWNLOADING");
		send.success(function(data){
			console.log(JSON.stringify(data));
			var file = new Blob([data], {type: 'application/pdf'});
			var fileURL = URL.createObjectURL(file);
			window.open(fileURL);
			alert('DOWNLOADED!');
		});
		send.error(function(data){
			alert('DOWNLOAD GOT ERROR!');
		});
	};

	$scope.selectRecipient = function(){
		$scope.selectedUser = $scope.currentlySelected;
	}

	$scope.selectSystem = function(){
		$scope.selectedSystem = $scope.currentlySelectedSystem;
	}

	var getUsers = $http({
		method  : 'GET',
		url     : 'https://localhost:8443/getUsersToSendTo',
	});
	console.log("GETTING THE MESSAGES");
	getUsers.success(function(response){
		$scope.contacts = response;
		$scope.selectedContacts = "";
		console.log("RESPONSE IS" + JSON.stringify(response));

		/*for(var key in response){
			$scope.contacts.push(response[key]);
		}*/
		console.log('Senders Gotten');
	});
	getUsers.error(function(){
		alert('Get sender error!');
	});

}]);


//TODOLIST CALENDAR////

app.controller('UsersIndexController', ['$scope','$http', function($scope,$http) {
	// ... code omitted ...
	// Dates can be passed as strings or Date objects 
	$scope.events = [];
	$scope.calendarOptions = {
			defaultDate: new Date(),
			minDate: new Date(),
			maxDate: new Date([2020, 12, 31]),
			dayNamesLength: 1, // How to display weekdays (1 for "M", 2 for "Mo", 3 for "Mon"; 9 will show full day names; default is 1)
			multiEventDates: true, // Set the calendar to render multiple events in the same day or only one event, default is false
			maxEventsPerDay: 1, // Set how many events should the calendar display before showing the 'More Events' message, default is 3;
			eventClick: function(date){
				var counter = 0;
				var arrayTasks = [];
				for(var key in date){
					if ( counter > 2 ){
						for ( var key2 in date[key]){
							//alert(date[key][key2].task);
							arrayTasks.push(date[key][key2].task);
						}
						//alert(JSON.stringify(date[key]));
					}
					counter++;					
				}
				alert("Tasks for this day: " + arrayTasks);

			},
			dateClick: function(){
				var arrayTasks = [];
				//alert("called event click" + JSON.stringify(date));
				for(var i=0; i < date.length; i++){
					console.log(date[i].task);
				}
			}
	};


	var getListFromServer = $http({
		method  : 'GET',
		url     : 'https://localhost:8443/todo/getToDoList',
	});
	getListFromServer.success(function(data){
		console.log("gotten to do list");
		$scope.events = data;
	});
	getListFromServer.error(function(data){
		console.log("GETTING LIST FAILED" + JSON.stringify(data));
	});




}]);



//END TODOLIST CALENDAR//


//UPLOAD LOGO CONTROLLER//
app.controller('logoController', ['$scope', 'Upload', '$timeout','$http', function ($scope, Upload, $timeout,$http ) {
	/* $scope.submit = function() {
	      if ($scope.form.file.$valid && $scope.file) {
	        $scope.upload($scope.file);
	      }
	    };

	    // upload on file select or drop
	    $scope.upload = function (file) {
	        Upload.upload({
	            url: 'https://localhost:8443/receiveFile',
	            data: {file: file}
	        }).then(function (resp) {
	            console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
	        }, function (resp) {
	            console.log('Error status: ' + resp.status);
	        }, function (evt) {
	            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
	            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
	        });
	    };*/

	$scope.logo = "";
	var getLogo = function getLogo(){

		$http({
			method: 'GET',
			url: 'https://localhost:8443/getCompanyLogo'
		}).success(function (result) {
			$scope.logo = result;
			console.log("LOGO: " + JSON.stringify(result));
		}).error(function(result){
			//do something
			console.log("LOGOERROR: " + JSON.stringify(result));
		});



	}
	getLogo();




	$scope.uploadPic = function(file) {
		file.upload = Upload.upload({
			url: 'https://localhost:8443/receiveLogoFile',
			data: { file: file},
		});

		file.upload.then(function (response) {
			$timeout(function () {
				file.result = response.data;
				alert("is success " + JSON.stringify(response.data));
			});
		}, function (response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})
	};
}]);


////////ASSIGNING FUNCTIONS BASED ON ROLES

