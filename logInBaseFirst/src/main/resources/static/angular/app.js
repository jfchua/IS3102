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
			.state('/viewIcon',{
				url: '/viewIcon',
				templateUrl: '/views/viewIcon.html',
				controller: 'iconController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/addIcon',{
				url: '/addIcon',
				templateUrl: '/views/addIcon.html',
				controller: 'addIconController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/updateIcon',{
				url: '/updateIcon',
				templateUrl: '/views/updateIcon.html',
				controller: 'updateIconController',
				data: {
					authorizedRoles: [USER_ROLES.property]
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
				controller: 'updateBuildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/deleteBuilding',{
				url: '/deleteBuilding',
				templateUrl: '/views/deleteBuilding.html',
				controller: 'deleteBuildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/viewLevels',{
				url: '/viewLevels',
				templateUrl: '/views/viewLevels.html',
				controller: 'viewLevelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/addLevel',{
				url: '/addLevel',
				templateUrl: '/views/addLevel.html',
				controller: 'addLevelController',
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
            .state('/addSpecialRate',{
				url: '/addSpecialRate',
				templateUrl: '/views/addSpecialRate.html',
				controller: 'rateController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/viewAllRates',{
				url: '/viewAllRates',
				templateUrl: '/views/viewAllRates.html',
				controller: 'rateController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/updateSpecialRate',{
				url: '/updateSpecialRate',		
				templateUrl: '/views/updateSpecialRate.html',
				controller: 'updateRateController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/deleteSpecialRate',{
				url: '/deleteSpecialRate',
				templateUrl: '/views/deleteSpecialRate.html',
				controller: 'deleteRateController',
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
				controller: 'floorPlanController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})	
			.state('/viewFloorPlan',{
 			url:'/viewFloorPlan',
 				templateUrl: '/views/viewFloorPlan.html',
 				controller: 'viewFloorPlanController',
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
				controller: 'floorPlanController',//hailing test hahahahha
				data:{
					authorizedRoles:[USER_ROLES.property]
				}
			})
			.state('/addMaintenance',{
				url:'/addMaintenance',	
				templateUrl: '/views/addMaintenance.html',
				controller: 'addMaintenanceController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/updateMaintenance',{
				url:'/updateMaintenance',
				templateUrl: '/views/updateMaintenance.html',
				controller: 'updateMaintenanceController',
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
				controller: 'updateVendorController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/deleteVendor',{
				url:'/deleteVendor',
				templateUrl: '/views/deleteVendor.html',
				controller: 'updateVendorController',
				data:{
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('/addEventEx',{
				url:'/addEventEx',
				templateUrl: '/views/addEventEx.html',
				//controller: 'eventExternalController',
				controller: 'addEController',
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
				controller: 'floorPlanController',
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
				//controller: 'eventExternalController',
				controller: 'updateEController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('/cancelEventEx',{
				url:'/cancelEventEx',
				templateUrl: '/views/cancelEventEx.html',
				controller: 'deleteEventExController',
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
				controller: 'viewApprovedEventsController',
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
			.state('/viewBookingEx',{
 				url:'/viewBookingEx',
 				templateUrl: '/views/viewBookingEx.html',
 				controller: 'bookingController',
 				data:{
 					authorizedRoles:[USER_ROLES.organiser]
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
			.state('/viewAllPaymentPlans',{
				url:'/viewAllPaymentPlans',
				templateUrl: '/views/viewAllPaymentPlans.html',
				controller: 'paymentController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('/addPaymentPlan',{
				url:'/addPaymentPlan',
				templateUrl: '/views/addPaymentPlan.html',
				controller: 'paymentController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
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

//===========================================================================
//5. Events.
//===========================================================================



//========Test================

//========Event================
app.controller('addEController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData){
	console.log("start selecting venue");
	var getBuild = $http({
		method  : 'GET',
		url     : 'https://localhost:8443/building/viewBuildings',
	});
	console.log("GETTING THE BUILDINGS");
	getBuild.success(function(response){
		$scope.buildings = response;
		$scope.selectedBuilding;
		console.log("RESPONSE IS" + JSON.stringify(response));

		console.log('Buildings Gotten');
	});
	getBuild.error(function(){
		alert('Get building error!!!!!!!!!!');
	});
	$scope.currentlySelectedBuilding;
	$scope.selectBuild = function(){
		$scope.selectedBuilding=$scope.currentlySelectedBuilding;
	}
	console.log("finish selecting building");

	$scope.getLevel = function(id){
		$scope.dataToShare = [];
		//var id = $scope.currentlySelected;
		$scope.url = "https://localhost:8443/level/viewLevels/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE ALL LEVELS INFO")
		var getLevels = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/level/viewLevels/'+id,
	});
		console.log("Getting the levels using the url: " + $scope.url);
		getLevels.success(function(response){
			$scope.levels = response;
			$scope.selectedLevel;
			console.log("RESPONSE IS" + JSON.stringify(response));

			console.log('Levels Gotten');
		});
		getLevels.error(function(){
			alert('Get levels error!!!!!!!!!!');
		});		
		$scope.currentlySelectedLevel;
		$scope.selectLevel = function(){
			$scope.selectedLevel=$scope.currentlySelectedLevel;
		}
		console.log("finish selecting level");		
	}
	
	$scope.selectedUnits=[];
	$scope.getUnit = function(levelId){
		//$scope.url = "https://localhost:8443/property/viewUnits/";
		
		$scope.levelID = levelId; 
		var dataObj = {id: $scope.levelID};
		console.log("GETTING THE ALL UNITS INFO")
		var getUnits = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/property/viewUnits/',
			data    : dataObj,
	});
		console.log("REACHED HERE FOR SUBMIT LEVEL " + JSON.stringify(dataObj));
		getUnits.success(function(response){
			$scope.units = response;
			console.log("RESPONSE IS" + JSON.stringify(response));

			console.log('Units Gotten');
		});
		getUnits.error(function(){
			alert('Get units error!!!!!!!!!!');
		});		
		
		$scope.currentlySelectedUnit;
		$scope.selectUnit = function(){
			$scope.selectedUnits.push($scope.currentlySelectedUnit);
		}

		$scope.deleteUnit = function(unit){
			var index = $scope.selectedUnits.indexOf(unit);
			$scope.selectedUnits.splice(index, 1);  
		}
		console.log("finish selecting units");		
	}
	
	
	$scope.addEvent = function(){
		console.log("start adding");
		$scope.data = {};

		var dataObj = {
				units: $scope.selectedUnits,
				event_title: $scope.event.event_title,
				event_content: $scope.event.event_content,
				event_description: $scope.event.event_description,
				event_approval_status: "processing",
				event_start_date: ($scope.event.event_start_date).toString(),
				event_end_date: ($scope.event.event_end_date).toString(),
				filePath: $scope.event.filePath,
		};
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/event/addEvent',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE Event");
		send.success(function(){
			alert('Event IS SAVED!');
		});
		send.error(function(){
			alert('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
		});
	};
	
	
}]);

app.controller('updateEController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData){
	$scope.init= function(){
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
	
	console.log("start selecting venue");
	var getBuild = $http({
		method  : 'GET',
		url     : 'https://localhost:8443/building/viewBuildings',
	});
	console.log("GETTING THE BUILDINGS");
	getBuild.success(function(response){
		$scope.buildings = response;
		$scope.selectedBuilding;
		console.log("RESPONSE IS" + JSON.stringify(response));

		console.log('Buildings Gotten');
	});
	getBuild.error(function(){
		alert('Get building error!!!!!!!!!!');
	});
	$scope.currentlySelectedBuilding;
	$scope.selectBuild = function(){
		$scope.selectedBuilding=$scope.currentlySelectedBuilding;
	}
	console.log("finish selecting building");

	$scope.getLevel = function(id){
		$scope.dataToShare = [];
		//var id = $scope.currentlySelected;
		$scope.url = "https://localhost:8443/level/viewLevels/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE ALL LEVELS INFO")
		var getLevels = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/level/viewLevels/'+id,
	});
		console.log("Getting the levels using the url: " + $scope.url);
		getLevels.success(function(response){
			$scope.levels = response;
			$scope.selectedLevel;
			console.log("RESPONSE IS" + JSON.stringify(response));

			console.log('Levels Gotten');
		});
		getLevels.error(function(){
			alert('Get levels error!!!!!!!!!!');
		});		
		$scope.currentlySelectedLevel;
		$scope.selectLevel = function(){
			$scope.selectedLevel=$scope.currentlySelectedLevel;
		}
		console.log("finish selecting level");		
	}
	
	$scope.selectedUnits=[];
	$scope.getUnit = function(levelId){
		//$scope.url = "https://localhost:8443/property/viewUnits/";
		
		$scope.levelID = levelId; 
		var dataObj = {id: $scope.levelID};
		console.log("GETTING THE ALL UNITS INFO")
		var getUnits = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/property/viewUnits/',
			data    : dataObj,
	});
		console.log("REACHED HERE FOR SUBMIT LEVEL " + JSON.stringify(dataObj));
		getUnits.success(function(response){
			$scope.units = response;
			console.log("RESPONSE IS" + JSON.stringify(response));

			console.log('Units Gotten');
		});
		getUnits.error(function(){
			alert('Get units error!!!!!!!!!!');
		});		
		
		$scope.currentlySelectedUnit;
		$scope.selectUnit = function(){
			$scope.selectedUnits.push($scope.currentlySelectedUnit);
		}

		$scope.deleteUnit = function(unit){
			var index = $scope.selectedUnits.indexOf(unit);
			$scope.selectedUnits.splice(index, 1);  
		}
		console.log("finish selecting units");		
	}
	
	$scope.getUnitsId = function(){
		var dataObj ={id: $scope.selectedUnits};
		console.log("units to be get are "+JSON.stringify(dataObj));
		$scope.shareMyData = function (myValue) {
		}		
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/property/getUnitsId',
			data    : dataObj,
		});
		send.success(function(response){
			console.log('GET Unit IDS SUCCESS! ' + JSON.stringify(response));
			shareData.addData(JSON.stringify(response));
		});
		send.error(function(response){
			$location.path("/viewAllEventsEx");
			console.log('GET UNITS ID FAILED! ' + JSON.stringify(response));
		});
	}

	$scope.updateEvent = function(){
		console.log("Start updating");
		var unitIdsString="";
		var unitIdsObj = JSON.parse(shareData.getData());
		unitIdsString+=unitIdsObj;
		console.log("test hailing");
		console.log(unitIdsString);
		$scope.data = {};
		console.log($scope.event.id);
		var dataObj = {	
				id: $scope.event.id,
				units: $scope.event.units,
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
}]);



app.controller('eventExternalController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {


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


$scope.getBookings = function(id){		
	$scope.dataToShare = [];	  
	$scope.shareMyData = function (myValue) {
		//$scope.dataToShare = myValue;
		//shareData.addData($scope.dataToShare);
	}
	$scope.url = "https://localhost:8443/booking/viewAllBookings/"+id;
	//$scope.dataToShare = [];
	console.log("GETTING THE EVENT INFO")
	var getBookings = $http({
		method  : 'GET',
		url     : 'https://localhost:8443/booking/viewAllBookings/' + id        
	});
	console.log("Getting the bookings using the url: " + $scope.url);
	getBookings.success(function(response){
		//$scope.dataToShare.push(id);
		//$location.path("/viewLevels/"+id);
		console.log('GET Booking SUCCESS! ' + JSON.stringify(response));
		console.log("ID IS " + id);
		$scope.bookings = response.data;
		shareData.addData(JSON.stringify(response));
		//$location.path("/viewLevels");
	});
	getBookings.error(function(response){
		$location.path("/viewAllEventsEx");
		console.log('GET Booking FAILED! ' + JSON.stringify(response));
	});

}



}]);


app.controller('bookingController', ['$scope','$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
$scope.viewBookings = function(){
	$scope.data = {};
	//var tempObj= {id:1};
	//console.log(tempObj)
	console.log("DISPLAY ALL BOOKINGS");
	$scope.bookings = JSON.parse(shareData.getData());
	var url = "https://localhost:8443/booking/viewAllBookings";	
    console.log("BOOKING DATA ARE OF THE FOLLOWING: " + $scope.bookings);	
};

$scope.deleteBooking = function(id){
    var r = confirm("Confirm cancel? \nEither OK or Cancel.");
    if (r == true) {
    	$scope.url = "https://localhost:8443/booking/deleteBooking/"+id;
    	var deleteBooking = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/booking/deleteBooking/' + id        
		});
    	console.log("Deleting the event using the url: " + $scope.url);
		deleteBooking.success(function(response){
			alert('DELETE BOOKING SUCCESS! ');
			console.log("ID IS " + id);
		});
		deleteBooking.error(function(response){
			alert('DELETE BOOKING FAIL! ');
			$location.path("/viewAllEventsEx");
			console.log('DELETE BOOKING FAILED! ' + JSON.stringify(response));
		});
    } else {
        alert("Cancel deleting booking");
    }
    //document.getElementById("demo").innerHTML = txt;	
	/*
	$scope.data = {};
	console.log("Start deleting event");
	$scope.url = "https://localhost:8443/booking/deleteBooking/"+id;
	console.log("GETTING THE EVENT INFO")
	var deleteBooking = $http({
		method  : 'POST',
		url     : 'https://localhost:8443/booking/deleteBooking/' + id        
	});
	console.log("Deleting the event using the url: " + $scope.url);
	deleteBooking.success(function(response){
		console.log('DELETE BOOKING SUCCESS! ' + JSON.stringify(response));
		console.log("ID IS " + id);
	});
	deleteBooking.error(function(response){
		$location.path("/viewAllEventsEx");
		console.log('DELETE BOOKING FAILED! ' + JSON.stringify(response));
	});*/
			
	/*
	$scope.event = JSON.parse(shareData.getData());
	console.log($scope.event.id);
	var tempObj ={eventId:$scope.event.id};
	console.log("fetch id "+ tempObj);
	
	$http.post("//localhost:8443/event/deleteEvent", JSON.stringify(tempObj)).then(function(response){
		
		console.log("Cancel the EVENT");
	},function(response){
		alert("DID NOT Cancel EVENT");
		
	}	
	)*/
}
}])


//=======End of test===========






app.controller('notificationController', ['$scope', '$http','shareData', function ($scope, $http, shareData) {		

	$scope.viewNotifications = function(){
		$scope.data = {};
		$scope.notifications = JSON.parse(shareData.getData()); //gets the response data from building controller 
		//console.log($scope.levels.length);			
		//var url = "https://localhost:8443/event/viewLevels";

	}

}]);





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

