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
var app = angular.module('app', ['ngRoute','ngResource','ui.bootstrap','ngAnimate', 'ngSanitize','ui.router'])
//Declaring Constants
.constant('USER_ROLES', {
	all: '*',
	admin: 'ROLE_ADMIN',
	event : 'ROLE_EVENT',
	user: 'ROLE_USER',
	superadmin: 'ROLE_SUPERADMIN',
	property: 'ROLE_PROPERTY'
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
				url: '/viewlevels',
				templateUrl: '/views/viewLevels.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/addLevel',{
				url: '/addlevel',
				templateUrl: '/views/addLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/updateLevel',{
				url: '/updatelevel',
				templateUrl: '/views/updateLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('/deleteLevel',{
				url: '/deleteLevel',
				templateUrl: '/views/deleteLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.user]
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
					authorizedRoles: [USER_ROLES.user]
				}
			})	
			.state('/createFloorPlan',{
				url:'/createFloorPlan',
				templateUrl: '/views/floorPlanAngular.html',
				controller: 'MyCtrl',
				data: {
					authorizedRoles: [USER_ROLES.user]
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
			.state('/viewClientOrgs',{
				url:'/viewClientOrgs',
				templateUrl: '/views/viewClientOrgs.html',
				controller: 'viewClientOrgs',
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
			.state('/editUserProfile',{
				url:'/editUserProfile',
				templateUrl: '/views/editUserProfile.html',
				controller: 'userProfileController',
				data: {
					authorizedRoles: [USER_ROLES.user]
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

			$urlRouterProvider.otherwise('/login');


			/*$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';*/
		})

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
		app.factory('Auth', function(){
			var user;
			var authenticated;
			var userRoles = new Array();
			var authService = {};
			var state;

			authService.setUser = function(aUser){
				user = aUser;
				console.log("User " + user.name +" is set");

			},
			authService.isAuthenticated = function(){

				if (user == null) {
					console.log("User is null");
					authenticated = false;
				}
				else if (authenticated == null){
					console.log("authenticated is null");
					authenticated = user.authenticated;
				}
				//	console.log(user.authenticated);
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
				console.log("Returning unauthorized");
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



		app.run(['$rootScope', 'AUTH_EVENTS', 'Auth' ,'$location','$window', function ($rootScope, AUTH_EVENTS, Auth, $location, $window) {

			$rootScope.$on('$stateChangeStart', function (event, next) {
				var authorizedRoles = next.data.authorizedRoles;
				console.log("Next authorized roles are " + authorizedRoles);
				//no idea how to put multiple conditions together, && and || doesnt seem to work
				if ($location.path() != '/login'){
					if ( $location.path() != '/#/login') {
						if ($location.path() != '/reset') {
							if (  $location.path().indexOf('/resetPassword/') == -1) {
								if (!Auth.isAuthorized(authorizedRoles)) {
									console.log("role is authorizing")
									event.preventDefault();
									if (Auth.isAuthenticated()) {
										// user is not allowed
										console.log("notauthorised");
										//  $window.location.href = '/#/401';
										//   $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
									} else {
										// user is not logged in
										console.log("notauthenticated");
										// $location.path('/login');
										$window.location.href = '/#/login';
									}
									//  $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
								}
							}
						}}}}

			);

		}])


		app.controller('eventController', ["$scope",'$http', function ($scope, $http){
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
 * 
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
//Create new user

app.controller('createNewUserController', function($scope, $http){

	$scope.genders=['ROLE_USER','ROLE_EVENT','ROlE_ADMIN'];
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
			alert("Create New User SUCCESS    "  + JSON.stringify(dataObj))
		});
		create.error(function(){
			alert("Create New User FAILED" + JSON.stringify(dataObj));
		});
	}
});

app.controller('viewUserList', ['$scope','$http',
                                function($scope, $http) {
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
			alert('FETCHED ALL USERS SUCCESS!!! ' + JSON.stringify(response));
		});
		fetch.error(function(response){
			alert('FETCH ALL USERS FAILED!!!');
		});

		alert('done with viewing users');
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

	$scope.save = function(index){
		//$scope.entity = $scope.Profiles[index];
		$scope.entity = $scope.Profiles[index];
		//$scope.entity.index = index;
		$scope.entity.editable = true;
		console.log("USER: " + JSON.stringify($scope.entity));
		var Edit = {
				name: $scope.entity.name,
				email: $scope.entity.email 			
		}


		var toEdit = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/updateUser',
			data 	: Edit
			//forms user object
		});

		console.log("fetching the user list.......");
		toEdit.success(function(response){
			$scope.Profiles = response;
			alert('EDITING THE USERS SUCCESS!!! ' + JSON.stringify(response));
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


/*1. TO DO LIST*/
app.controller('taskController', function($scope, $http) {
	$scope.today = new Date();
	$scope.saved = localStorage.getItem('taskItems');
	$scope.taskItem = (localStorage.getItem('taskItems')!==null) ? 
			JSON.parse($scope.saved) : [ {description: "Why not add a task?", date: $scope.today, complete: false}];
			localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));
			//$scope.save();

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
					data: {task:"task", date: "2016-09-18"}
				}).then(function mySucces(response) {
					console.log("ADDED NEW TO DO LIST!!!");
				}, function myError(response) {
					$scope.myWelcome = response.statusText;
				});
				if ($scope.newTaskDate == null || $scope.newTaskDate == '') {
					$scope.taskItem.push({
						description: $scope.newTask,
						date: "No deadline",
						complete: false,
						category: $scope.newTaskCategory.name
					}) 
				} else {
					$scope.taskItem.push({
						description: $scope.newTask,
						date: $scope.newTaskDate,
						complete: false,
						category: $scope.newTaskCategory.name
					})
				};
				$scope.newTask = '';
				$scope.newTaskDate = '';
				$scope.newTaskCategory = $scope.categories;
				localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));

			};
			$scope.deleteTask = function () {
				var completedTask = $scope.taskItem;
				$scope.taskItem = [];
				angular.forEach(completedTask, function (taskItem) {
					if (!taskItem.complete) {
						$scope.taskItem.push(taskItem);
					}
				});
				localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));
			};

			$scope.save = function () {


				localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));

			}
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

			$scope.alerts.push(arrItems[key].senderName + ": "  + arrItems[key].subject + "\n\n" + arrItems[key].message);
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
			alert('Message Gotten');
		});
		getMsg.error(function(){
			alert('Get msg error!!!!!!!!!!');
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
		alert('Get sender error!!!!!!!!!!');
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

		alert('message sent');
		$location.path('/messageList');
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
				postalCode: parseInt($scope.building.postalCode),
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
		} else {
			mydata = [];
		}
		mydata.push(newObj);
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







//===========================================================================
//6. Create client organisation.
//===========================================================================
app.controller('clientOrgController', ['$scope', '$http', function ($scope, $http) {
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



























//FLOORPLAN
app.directive('draggable', function() {
	return {
		// A = attribute, E = Element, C = Class and M = HTML Comment
		restrict:'M',
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
				containment: '#glassbox'

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
}).directive('resizable', function () {

	return {

		restrict: 'M',
		scope: {

			callback: '&onResize'
		},
		link: function postLink(scope, element, attrs) {
			element.resizable();
			element.on('resize', function (unit,evt, ui) {
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







//flooxrplan
app.controller('MyCtrl', function ($scope, $http,shareData) {
	//$scope.units=[];
	//var app = angular.module('myApp', ['angularResizable']);
	//$scope.level = JSON.parse(shareData.getData());//copied from level contorller
	var levelIdObj;
	var levelId;
	$scope.units=[];
	$scope.viewUnit=function(){
		alert("viewing existing units");
		var level = JSON.parse(shareData.getData());
		console.log("test hailing");
		console.log(level.id);
		levelId=level.id;
		//var levelId=1;//need to pass level id here

		//get level id from previous page
		levelIdObj={
				id:levelId
		}
		//retrieve units when page loaded
		$http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
			console.log("pure response is "+response.data);
			console.log("test"+JSON.stringify(response.data.records));//how to pass set of objects back to javascript controller
			console.log("test anglar.fromJon"+angular.fromJson(response.data));
			$scope.units=angular.fromJson(response.data);

		},function(response){
			console.log("DID NOT view");
			console.log("response is "+angularfromJson(response.data).error);
		})


	}

	$scope.addUnit = function () {  
		alert("adding unit");

		$scope.units.push({"id": 0,"unitNumber": "#unit","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "coral","type": "rect"}});
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
});

//===========================================================================
//7. Audit Logs
//===========================================================================
app.controller('auditLogController', ['$scope', '$http', function ($scope, $http) {
	$scope.submit = function(){
		//alert("SUCCESS");
		$scope.data = {};
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
		alert('Get sender error!!!!!!!!!!');
	});

}]);

////VIEW CLIENT ORGS//////////

app.controller('viewClientOrgs', ['$scope','$http',
                                function($scope, $http) {
	$scope.Profiles = [];

	$scope.send = function(){
		console.log("REACHED HERE FOR FETCHING ALL USERS");

		var fetch = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/viewClientOrgs',
			//forms user object
		});

		console.log("fetching the orgs list.......");
		fetch.success(function(response){

			$scope.Profiles = response;
			alert('FETCHED ALL orgs SUCCESS!!! ' + JSON.stringify(response));
		});
		fetch.error(function(response){
			alert('FETCH ALL orgs FAILED!!!');
		});

		alert('done with viewing users');
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

	$scope.save = function(index){
		//$scope.entity = $scope.Profiles[index];
		$scope.entity = $scope.Profiles[index];
		//$scope.entity.index = index;
		$scope.entity.editable = true;
		console.log("USER: " + JSON.stringify($scope.entity));
		var Edit = {
				name: $scope.entity.name,
				email: $scope.entity.email 			
		}


		var toEdit = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/updateUser',
			data 	: Edit
			//forms user object
		});

		console.log("fetching the user list.......");
		toEdit.success(function(response){
			$scope.Profiles = response;
			alert('EDITING THE USERS SUCCESS!!! ' + JSON.stringify(response));
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
			alert('CLIENT ORG IS SAVED!');
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


}]);




////////ASSIGNING FUNCTIONS BASED ON ROLES

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

$scope.IsUser = function(){
	return Auth.hasRoles('ROLE_USER');
}

$scope.IsSuperAdmin = function(){
	return Auth.hasRoles('ROLE_SUPERADMIN');
}

});
