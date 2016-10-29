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
	.when('/dashboard',{
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
var app = angular.module('app', [ 'ui.router',
                                  'ngAnimate', 
                                  'ui.calendar',
                                 'ngStorage', 
                                  'ngRoute',
                                  'ngResource',
                                  'chart.js',
                                  'textAngular',
                                  'gridshore.c3js.chart', 
                                  'angular-growl',
                                  'growlNotifications',   
                                  'angular-loading-bar',
                                  'angular-progress-button-styles',
                                  'pascalprecht.translate',
                                  'ui.bootstrap',                          
                                  'ngSanitize',                                                       
                                  'ngFileUpload',
                                  'ui.bootstrap.contextMenu',
                                  'angularModalService',
                                  'ui.bootstrap.tabs',
                                  'ngCsvImport',
                                  'color.picker',
                                  'gridster'
                                  ])
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
			.state('dashboard',{
				url:'/dashboard',
				templateUrl: 'views/index.html',
				controller: 'passController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('dashboard.workspace',{
				url:'/workspace',
				templateUrl: 'views/workspace.html',
				controller: 'workspaceController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('dashboard.viewIcon',{
				url: '/viewIcon',
				templateUrl: '/views/viewIcon.html',
				controller: 'iconController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.addIcon',{
				url: '/addIcon',
				templateUrl: '/views/addIcon.html',
				controller: 'addIconController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.updateIcon',{
				url: '/updateIcon',
				templateUrl: '/views/updateIcon.html',
				controller: 'updateIconController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.uploadCSV',{
				url: '/uploadCSV',
				templateUrl: '/views/uploadCSV.html',
				controller: 'csvController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.updateRent',{
				url: '/updateRent',
				templateUrl: '/views/updateRent.html',
				controller: 'rentController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.addBuilding',{
				url: '/addBuilding',
				templateUrl: '/views/addBuilding.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.viewBuilding',{
				url: '/viewBuilding',
				templateUrl: '/views/viewBuilding.html',
				controller: 'buildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.updateBuilding',{
				url: '/updateBuilding',		
				templateUrl: '/views/updateBuilding.html',
				controller: 'updateBuildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.deleteBuilding',{
				url: '/deleteBuilding',
				templateUrl: '/views/deleteBuilding.html',
				controller: 'deleteBuildingController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.viewLevels',{
				url: '/viewLevels',
				templateUrl: '/views/viewLevels.html',
				controller: 'viewLevelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.addLevel',{
				url: '/addLevel',
				templateUrl: '/views/addLevel.html',
				controller: 'addLevelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.updateLevel',{
				url: '/updateLevel',
				templateUrl: '/views/updateLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.deleteLevel',{
				url: '/deleteLevel',
				templateUrl: '/views/deleteLevel.html',
				controller: 'levelController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
            .state('dashboard.addSpecialRate',{
				url: '/addSpecialRate',
				templateUrl: '/views/addSpecialRate.html',
				controller: 'rateController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.viewAllRates',{
				url: '/viewAllRates',
				templateUrl: '/views/viewAllRates.html',
				controller: 'rateController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.updateSpecialRate',{
				url: '/updateSpecialRate',		
				templateUrl: '/views/updateSpecialRate.html',
				controller: 'updateRateController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.deleteSpecialRate',{
				url: '/deleteSpecialRate',
				templateUrl: '/views/deleteSpecialRate.html',
				controller: 'deleteRateController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.addEvent',{
				url:'/addEvent',
				templateUrl: '/views/addEvent.html',
				controller: 'eventController',
				data: {
					authorizedRoles: [USER_ROLES.event]
				}
			})
			.state('dashboard.uploadCompanyLogo',{
				url:'/uploadCompanyLogo',
				templateUrl: '/views/uploadCompanyLogo.html',
				controller: 'logoController',
				data: {
					authorizedRoles: [USER_ROLES.admin]
				}
			})

			.state('dashboard.messageList',{
				url:'/messageList',
				templateUrl: '/message/message.html',
				controller: 'ListController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('dashboard.view/:id',{
				url:'/view/:id',
				templateUrl: '/message/detail.html',
				controller: 'DetailController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('dashboard.new',{
				url:'/new',
				templateUrl: '/message/new.html',
				controller: 'NewMailController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('dashboard.addClientOrg',{
				url:'/addClientOrg',
				templateUrl: '/views/addClientOrg.html',
				controller: 'clientOrgController',
				data: {
					authorizedRoles: [USER_ROLES.superadmin]
				}
			})	
			.state('dashboard.viewClientOrgs',{
				url:'/viewClientOrgs',
				templateUrl: '/views/viewClientOrgs.html',
				controller: 'viewClientOrgs',
				data: {
					authorizedRoles: [USER_ROLES.superadmin]
				}
			})
			.state('dashboard.viewDataVisual',{
				url:'/viewDataVisual',
				templateUrl: '/views/viewDataVisual.html',
				controller: 'ChartCtrl',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('dashboard.createFloorPlan',{
				url:'/createFloorPlan',
				templateUrl: '/views/floorPlanAngular.html',
				controller: 'floorPlanController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})	
			.state('dashboard.viewFloorPlan',{
 			url:'/viewFloorPlan',
 				templateUrl: '/views/viewFloorPlan.html',
 				controller: 'viewFloorPlanController',
 				data: {
 					authorizedRoles: [USER_ROLES.property]
 				}
 		})
			.state('dashboard.viewMaintenance',{
				url:'/viewMaintenance',	
				templateUrl: '/views/viewMaintenance.html',
				controller: 'maintenanceController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.viewBuildingMtn',{
				url:'/viewBuildingMtn',
				templateUrl: '/views/viewBuildingMtn.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.property]
				}
			})
			.state('dashboard.viewLevelsMtn',{
				url:'/viewLevelsMtn',
				templateUrl: '/views/viewLevelsMtn.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.property]
				}
			})
			.state('dashboard.viewFloorPlanMtn',{
				url:'/viewFloorPlanMtn',
				templateUrl: '/views/viewFloorPlanMtn.html',
				controller: 'floorPlanController',//hailing test hahahahha
				data:{
					authorizedRoles:[USER_ROLES.property]
				}
			})
			.state('dashboard.addMaintenance',{
				url:'/addMaintenance',	
				templateUrl: '/views/addMaintenance.html',
				controller: 'addMaintenanceController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.updateMaintenance',{
				url:'/updateMaintenance',
				templateUrl: '/views/updateMaintenance.html',
				controller: 'updateMaintenanceController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.deleteMaintenance',{
				url:'/deleteMaintenance',
				templateUrl: '/views/deleteMaintenance.html',
				controller: 'deleteMaintenanceController',
				data:{
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.viewAllVendors',{
				url:'/viewAllVendors',
				templateUrl: '/views/viewAllVendors.html',
				controller: 'vendorController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.addVendor',{
				url:'/addVendor',	
				templateUrl: '/views/addVendor.html',
				controller: 'vendorController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.updateVendor',{
				url:'/updateVendor',
				templateUrl: '/views/updateVendor.html',
				controller: 'updateVendorController',
				data: {
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.deleteVendor',{
				url:'/deleteVendor',
				templateUrl: '/views/deleteVendor.html',
				controller: 'updateVendorController',
				data:{
					authorizedRoles: [USER_ROLES.property]
				}
			})
			.state('dashboard.addEventEx',{
				url:'/addEventEx',
				templateUrl: '/views/addEventEx.html',
				//controller: 'eventExternalController',
				controller: 'addEController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.viewBuildingEx',{
				url:'/viewBuildingEx',
				templateUrl: '/views/viewBuildingEx.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.viewLevelsEx',{
				url:'/viewLevelsEx',
				templateUrl: '/views/viewLevelsEx.html',
				controller: 'buildingController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.viewFloorPlanEx',{
				url:'/viewFloorPlanEx',
				templateUrl: '/views/viewFloorPlanEx.html',
				controller: 'floorPlanController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.createUnitPlanEx',{
				url:'/createUnitPlanEx',
				templateUrl: '/views/createUnitPlanEx.html',
				controller: 'areaPlanController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.updateEventEx',{
				url:'/updateEventEx',
				templateUrl: '/views/updateEventEx.html',
				//controller: 'eventExternalController',
				controller: 'updateEController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.cancelEventEx',{
				url:'/cancelEventEx',
				templateUrl: '/views/cancelEventEx.html',
				controller: 'deleteEventExController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.updateCategoryEx',{
				url:'/updateCategoryEx',
				templateUrl: '/views/updateCategoryEx.html',
				controller: 'configureTicketsController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.feedbackEx',{
				url:'/viewFeedbackEx',
				templateUrl: '/views/viewFeedback.html',
				controller: 'eventExternalController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.addCategoryEx',{
				url:'/addCategoryEx',
				templateUrl: '/views/addCategoryEx.html',
				controller: 'configureTicketsController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.configureTicketsEx',{
				url:'/configureTicketsEx',
				templateUrl: '/views/configureTicketsEx.html',
				controller: 'configureTicketsController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.viewAllEventsEx',{
				url:'/viewAllEventsEx',
				templateUrl: '/views/viewAllEventsEx.html',
				controller: 'eventExternalController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.viewApprovedEventsEx',{
				url:'/viewApprovedEventsEx',
				templateUrl: '/views/viewApprovedEventsEx.html',
				controller: 'viewApprovedEventsController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.viewToBeApprovedEvents',{
				url:'/viewToBeApprovedEvents',
				templateUrl: '/views/viewToBeApprovedEvents.html',
				controller: 'viewToBeApprovedEventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewBookingEx',{
 				url:'/viewBookingEx',
 				templateUrl: '/views/viewBookingEx.html',
 				controller: 'bookingController',
 				data:{
 					authorizedRoles:[USER_ROLES.organiser]
 				}
 			})
 			.state('dashboard.viewPaymentPlansEx',{
 				url:'/viewPaymentPlansEx',
 				templateUrl: '/views/viewPaymentPlansEx.html',
 				controller: 'paymentExController',
 				data:{
 					authorizedRoles:[USER_ROLES.organiser]
 				}
 			})
 			.state('dashboard.viewPaymentHistoryEx',{
 				url:'/viewPaymentHistoryEx',
 				templateUrl: '/views/viewPaymentHistoryEx.html',
 				controller: 'paymentHistoryExController',
 				data:{
 					authorizedRoles:[USER_ROLES.organiser]
 				}
 			})
 			.state('dashboard.viewPaymentDetailsEx',{
 				url:'/viewPaymentDetailsEx',
 				templateUrl: '/views/viewPaymentDetailsEx.html',
 				controller: 'paymentDetailsExController',
 				data:{
 					authorizedRoles:[USER_ROLES.organiser]
 				}
 			})
 			.state('dashboard.viewTicketSalesEx',{
				url:'/viewTicketSalesEx',
				templateUrl: '/views/viewTicketSalesEx.html',
				controller: 'ticketSaleExController',
				data:{
					authorizedRoles:[USER_ROLES.organiser]
				}
			})
			.state('dashboard.viewNotifications',{
				url:'/viewNotifications',
				templateUrl: '/views/viewNotifications.html',
				controller: 'notificationController',
				data: {
					authorizedRoles: [USER_ROLES.event]
				}
			})
			.state('dashboard.updateEventStatus',{
				url:'/updateEventStatus',
				templateUrl: '/views/updateEventStatus.html',
				controller: 'viewEventDetailsController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.deleteEvent',{
				url:'/deleteEvent',
				templateUrl: '/views/deleteEvent.html',
				controller: 'deleteEventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewAllEvents',{
				url:'/viewAllEvents',
				templateUrl: '/views/viewAllEvents.html',
				controller: 'eventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewApprovedEvents',{
				url:'/viewApprovedEvents',
				templateUrl: '/views/viewApprovedEvents.html',
				controller: 'viewApprovedEventController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewEventDetails',{
				url:'/viewEventDetails',
				templateUrl: '/views/viewEventDetails.html',
				controller: 'viewEventDetailsController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewEventDetailsApproved',{
				url:'/viewEventDetailsApproved',
				templateUrl: '/views/viewEventDetailsApproved.html',
				controller: 'viewEventDetailsController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewEventOrganizers',{
				url:'/viewEventOrganizers',
				templateUrl: '/views/viewEventOrganizers.html',
				controller: 'viewEventOrganiserController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewTicketSales',{
				url:'/viewTicketSales',
				templateUrl: '/views/viewTicketSales.html',
				controller: 'ticketSalesController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewTicketSaleDetailsEvent',{
				url:'/viewTicketSaleDetailsEvent',
				templateUrl: '/views/viewTicketSaleDetailsEvent.html',
				controller: 'ticketSaleDetailsController',
				data:{
					authorizedRoles:[USER_ROLES.event]
				}
			})
			.state('dashboard.viewAllPaymentPlans',{
				url:'/viewAllPaymentPlans',
				templateUrl: '/views/viewAllPaymentPlans.html',
				controller: 'paymentController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.addPaymentPlan',{
				url:'/addPaymentPlan',
				templateUrl: '/views/addPaymentPlan.html',
				controller: 'addPaymentController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.updatePaymentPlan',{
				url:'/updatePaymentPlan',
				templateUrl: '/views/updatePaymentPlan.html',
				controller: 'updatePaymentController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.viewPaymentPolicy',{
				url:'/viewPaymentPolicy',
				templateUrl: '/views/viewPaymentPolicy.html',
				controller: 'policyController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.addPaymentPolicy',{
				url:'/addPaymentPolicy',
				templateUrl: '/views/addPaymentPolicy.html',
				controller: 'policyController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.updatePaymentPolicy',{
				url:'/updatePaymentPolicy',
				templateUrl: '/views/updatePaymentPolicy.html',
				controller: 'updatePolicyController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.viewAllOutstandingBalance',{
				url:'/viewAllOutstandingBalance',
				templateUrl: '/views/viewAllOutstandingBalance.html',
				controller: 'balanceController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.viewListOfEvents',{
				url:'/viewListOfEvents',
				templateUrl: '/views/viewListOfEvents.html',
				controller: 'eventListController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
            .state('dashboard.updateReceivedPayment',{
				url:'/updateReceivedPayment',
				templateUrl: '/views/updateReceivedPayment.html',
				controller: 'receivedPController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.viewEventsWithTicketSales',{
				url:'/viewEventsWithTicketSales',
				templateUrl: '/views/viewEventsWithTicketSales.html',
				controller: 'eventWithTicketController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.updateTicketRevenue',{
				url:'/updateTicketRevenue',
				templateUrl: '/views/updateTicketRevenue.html',
				controller: 'ticketRController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.updateOutgoingPayment',{
				url:'/updateOutgoingPayment',
				templateUrl: '/views/updateOutgoingPayment.html',
				controller: 'outgoingController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.viewPaymentHistory',{
				url:'/viewPaymentHistory',
				templateUrl: '/views/viewPaymentHistory.html',
				controller: 'paymentHistoryController',
				data:{
					authorizedRoles:[USER_ROLES.finance]
				}
			})
			.state('dashboard.generateInvoice',{
				url:'/generateInvoice',
				templateUrl: '/views/generateInvoice.html',
				controller: 'invoiceController',
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
			.state('dashboard.createNewUser',{
				url:'/createNewUser',
				templateUrl: '/views/createUser.html',
				controller: 'createNewUserController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})	
			.state('dashboard.viewUserList',{
				url:'/viewUserList',
				templateUrl: '/views/viewUserList.html',
				controller: 'viewUserList',
				data: {
					authorizedRoles: [USER_ROLES.admin]
				}
			})	
			.state('dashboard.viewAuditLog',{
				url:'/viewAuditLog',
				templateUrl: '/views/viewAuditLog.html',
				controller: 'auditLogController',
				data: {
					authorizedRoles: [USER_ROLES.admin]
				}
			})
			.state('dashboard.viewUserProfile',{
				url:'/viewUserProfile',
				templateUrl: '/views/viewUserProfile.html',
				controller: 'userProfileController',
				data: {
					authorizedRoles: [USER_ROLES.user]
				}
			})
			.state('dashboard.editUserProfile',{
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
		    if ([401, 403].indexOf(response.status) >= 0) {
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



		/*dashboard*/
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
		create.error(function(data){
			alert("Error, " + data);
		});
	}
});


//VIEW USER START
app.controller('viewUserList', ['$scope','$http','$location','ModalService',
                                function($scope, $http,$location,ModalService) {
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



	$scope.checkRole =function(role,profile){
		     var roles=profile.roles;
			 var hasRole=false;
		     var index = 0;
			  angular.forEach(roles, function(item){             
			   if(hasRole==false&&role == roles[index].name){	
					 hasRole=true;
					  console.log(hasRole);
			   }else{
				   index = index + 1;
			   }
			  });      
			  return hasRole;
			
	} ;

	
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
			console.log($scope.Profiles);
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
		console.log(name);
		console.log(email);
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

			alert('Successfully updated the user');
			$scope.send();

		});
		toEdit.error(function(response){
			alert('Error, ');
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
			alert('Succesfully deleted the user');
		});
		del.error(function(response){
			alert('Error deleting user, ' + response);
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
			alert('User profile successfully changed');
		});
		send.error(function(data){
			alert('Error, ' + data);
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
				oldpassword: $scope.userProfile.password0,
		};
		console.log("** Passing data object of " + dataObj);

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/changePassword',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE USER PROFILE");
		send.success(function(){
			alert('You have successfully changed your password');
		});
		send.error(function(data){
			alert('Error, ' + data);
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
app.controller('taskController', function($scope, $http, $route,$state) {
	
	  
});
/*
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
		alert(result);
	})
});
*/
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

// DIRECTIVE AND CONTROLLER FOR UI
app.directive('topnav',function(){
	return {
    templateUrl:'views/topnav.html?v='+window.app_version,
    restrict: 'E',
    replace: true,
    controller: function($scope){

    	$scope.showMenu = function(){

	        $('.dashboard-page').toggleClass('push-right');

    	}
    	$scope.changeTheme = function(setTheme){

			$('<link>')
			  .appendTo('head')
			  .attr({type : 'text/css', rel : 'stylesheet'})
			  .attr('href', 'styles/app-'+setTheme+'.css?v='+window.app_version);

			// $.get('/api/change-theme?setTheme='+setTheme);

		}
		$scope.rightToLeft = function(){
			// alert('message');
			$('body').toggleClass('rtl');

			// var t = $('body').hasClass('rtl');
			// console.log(t);
			
			if($('body').hasClass('rtl')) {
				$('.stat').removeClass('hvr-wobble-horizontal');
			}
			

		}


		
    }
}
});

app.directive('sidebar',function(){
	return {
    templateUrl:'views/sidebar.html',
    restrict: 'E',
    replace: true,

    controller: function($scope){

		setTimeout(function(){
			$('.sidenav-outer').perfectScrollbar();
		}, 100);
		
	}
}
});
app.directive('menubar',function(){
		return {
        templateUrl:'views/menu-bar.html',
        restrict: 'E',
        replace: true,
    }
})
app.directive('sidebarwidgets',function(){
		return {
        templateUrl:'views/sidebar-widgets.html',
        restrict: 'E',
        replace: true,
	}
});
app.directive('sidebarProfile',function(){
		return {
        templateUrl:'views/sidebar-profile.html',
        restrict: 'E',
        replace: true,
    	}
	});
app	.directive('sidebarCalendar',function(){
		return {
        templateUrl:'views/sidebar-calendar.html',
        restrict: 'E',
        replace: true,
    	}
	});
app.directive('sidebarNewsfeed',function(){
		return {
        templateUrl:'views/sidebar-newsfeed.html',
        restrict: 'E',
        replace: true,
    	}
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
		$scope.alertsForTopNav=[];
		for(var key in arrItems){

			$scope.alerts.push(arrItems[key].senderName + ": "  + arrItems[key].subject + " - " + arrItems[key].message);
			$scope.alertsForTopNav.push({'senderName':arrItems[key].senderName,
											'subject':arrItems[key].subject,
											'message':arrItems[key].message
										});
			
			$scope.getId.push(arrItems[key].id);
			console.log($scope.alerts);
			
			//$scope.numberOfNotification=$scope.alerts.length;
		}
		$scope.numberOfNotification=$scope.alerts.length;

	});
	$scope.closeAlert = function(index) {

		console.log("ID: " + $scope.getId[index]);
		var del = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/deleteNotification',
			data    :  $scope.getId[index] //forms user object
		});
		del.success(function(){
			alert("Notification deleted")
			$scope.alerts.splice(index, 1);
		});
		del.error(function(data){
			alert(data);
		});

	};
});
app.controller('DropdownCtrl', function ($scope, $log) {
	  $scope.items = [
	                  'The first choice!',
	                  'And another choice for you.',
	                  'but wait! A third!'
	                ];

	                $scope.status = {
	                  isopen: false
	                };

	                $scope.toggled = function(open) {
	                  $log.log('Dropdown is now: ', open);
	                };

	                $scope.toggleDropdown = function($event) {
	                  $event.preventDefault();
	                  $event.stopPropagation();
	                  $scope.status.isopen = !$scope.status.isopen;
	                };
	              });
app.controller('sidenavCtrl', function($scope, $location){
	$scope.selectedMenu = 'dashboard';
	$scope.collapseVar = 0;

	$scope.check = function(x){

		if(x==$scope.collapseVar)
			$scope.collapseVar = 0;
		else
			$scope.collapseVar = x;
	};
	$scope.multiCheck = function(y){

		if(y==$scope.multiCollapseVar)
			$scope.multiCollapseVar = 0;
		else
			$scope.multiCollapseVar = y;
	};
});

app.controller('ButtonsCtrl', function ($scope) {
	  $scope.singleModel = 1;

	  $scope.radioModel = 'Middle';

	  $scope.checkModel = {
	    left: false,
	    middle: true,
	    right: false
	  };
	});
app.controller('progressCtrl', function($scope) {
	[].slice.call( document.querySelectorAll( 'button.progress-button' ) ).forEach( function( bttn ) {
		new ProgressButton( bttn, {
			callback : function( instance ) {
				var progress = 0,
				interval = setInterval( function() {
					progress = Math.min( progress + Math.random() * 0.1, 1 );
					instance._setProgress( progress );
					if( progress === 1 ) {
						instance._stop(1);
						clearInterval( interval );
					}
				}, 80 );
			}
		});
	});
});	
app.controller('ProgressDemoCtrl', function ($scope) {
	  $scope.max = 200;

	  $scope.random = function() {
	    var value = Math.floor((Math.random() * 100) + 1);
	    var type;

	    if (value < 25) {
	      type = 'success';
	    } else if (value < 50) {
	      type = 'info';
	    } else if (value < 75) {
	      type = 'warning';
	    } else {
	      type = 'danger';
	    }

	    $scope.showWarning = (type === 'danger' || type === 'warning');

	    $scope.dynamic = value;
	    $scope.type = type;
	  };
	  $scope.random();

	  $scope.randomStacked = function() {
	    $scope.stacked = [];
	    var types = ['success', 'info', 'warning', 'danger'];

	    for (var i = 0, n = Math.floor((Math.random() * 4) + 1); i < n; i++) {
	        var index = Math.floor((Math.random() * 4));
	        $scope.stacked.push({
	          value: Math.floor((Math.random() * 30) + 1),
	          type: types[index]
	        });
	    }
	  };
	  $scope.randomStacked();
	});
app.controller('CollapseDemoCtrl', function ($scope) {
  $scope.isCollapsed = false;
});

app.directive('todolist',function(){
		return {
	    templateUrl:'views/to-do.html?v='+window.app_version,
	    restrict: 'E',
	    replace: true,
    	controller: function($scope){

			setTimeout(function(){
    			$('.todo-list-wrap').perfectScrollbar();
			}, 100);

        }
	}
});
app.controller('calendarCtrl', function ($scope,$http) {
	var today = new Date();
	var next = new Date();
	next.setDate(next.getDate() + 3); 

	   //$scope.eventSources =[[{start:today,title:"haha"}]];
	  // $scope.eventSources.push([{start:today,end:next,title:"haha",allDay: false}]);
	   //console.log($scope.eventSources);
	   $scope.uiConfig = {
			      calendar:{
			        width: 600,
			        editable: true,
			        header:{
			          left: 'month agendaWeek agendaDay',
			          center: 'title',
			          right: 'today prev,next'
			        },
			        eventClick: $scope.alertEventOnClick,
			        eventDrop: $scope.alertOnDrop,
			        eventResize: $scope.alertOnResize
			      }
			    };
	    
	});

//FOR MOCK UP DATA VISUAL//NEED TO DELETE LATER
app.controller('ChartCtrl', ['$scope', '$timeout', function ($scope, $timeout) {
	$scope.menuOptions = [
	                      
	                              // Dividier
	                        ['All 6 Levels', function ($itemScope, $event, modelValue, text, $li) {
	                           
	                        }],
	                      null, 
	                        ['Level 1', function ($itemScope, $event, modelValue, text, $li) {
	                           
	                        }],
	                        ['Level 2', function ($itemScope, $event, modelValue, text, $li) {                     
	                           
	                        }],
	                        ['Level 3', function ($itemScope, $event, modelValue, text, $li) {                      
	                            
	                        }],
	                        ['Level 4', function ($itemScope, $event, modelValue, text, $li) {                      
	                           
	                        }],
	                        ['Level 5', function ($itemScope, $event, modelValue, text, $li) {                        
	                            
	                        }],
	                        ['Level 6', function ($itemScope, $event, modelValue, text, $li) {                  
	                            
	                        }],
	                    ];
	
	$scope.xaxis=[
                  
                  // null,        // Dividier
                  
            
                   ['Units', function ($itemScope, $event, modelValue, text, $li) {
                      
                   }],
                   ['Time', function ($itemScope, $event, modelValue, text, $li) {                     
                      
                   }],
               ];
    $scope.line = {
	    labels: ['1', '2', '3', '4', '5', '6'],
	          data: [
	      [65, 59, 80, 81, 56, 55, 80]
	      
	    ],
	    colours: ['#3CA2E0','#F0AD4E','#7AB67B','#D9534F','#3faae3'],
	    onClick: function (points, evt) {
	      console.log(points, evt);
	    }

    };

    $scope.bar = {
	    labels: ['2006', '2007', '2008', '2009', '2010', '2011', '2012'],
		data: [
		   [65, 59, 80, 81, 56, 55, 40],
		   [28, 48, 40, 19, 86, 27, 90]
		],
		colours: ['#3CA2E0','#F0AD4E','#7AB67B','#D9534F','#3faae3']
    	
    };

    $scope.donut = {
    	labels: ["Download Sales", "In-Store Sales", "Mail-Order Sales"],
    	      data: [300, 500, 100],
    	      colours: ['#3CA2E0','#F0AD4E','#7AB67B','#D9534F','#3faae3']
    };

     $scope.pie = {
    	labels : ["Download Sales", "In-Store Sales", "Mail-Order Sales"],
    	      data : [300, 500, 100],
    	      colours: ['#3CA2E0','#F0AD4E','#7AB67B','#D9534F','#3faae3']
    };


    $scope.datapoints=[{"x":10,"top-1":10,"top-2":15},
                       {"x":20,"top-1":100,"top-2":35},
                       {"x":30,"top-1":15,"top-2":75},
                       {"x":40,"top-1":50,"top-2":45}];
    $scope.datacolumns=[{"id":"top-1","type":"spline"},
                        {"id":"top-2","type":"spline"}];
    $scope.datax={"id":"x"};

    
}]);


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

app.controller('NewMailController', function($scope, $state, $http){
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
	getMsg.error(function(data){
		alert(data);
	});

	$scope.currentlySelected = null;

	$scope.selectRecipient = function(){
		
		var duplicate = false;
		var index = 0;
	    angular.forEach($scope.selectedContacts, function() {
	        if(duplicate==false&&$scope.currentlySelected == $scope.selectedContacts[index])
	        	duplicate = true;
	        else
	        	index = index + 1;
	    });
	    console.log(duplicate);
		if(!duplicate){
			$scope.selectedContacts.push($scope.currentlySelected);
		}
		
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
			alert('Message sent successfully!');
		});
		sendMsg.error(function(data){
			alert(data);
		});

		//alert('message sent');
		$state.go('dashboard.workspace');
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

app.controller('AppCtrl', function ($scope, $timeout, $mdSidenav, $log) {
    $scope.toggleLeft = buildDelayedToggler('left');
    $scope.toggleRight = buildToggler('right');
    $scope.isOpenRight = function(){
      return $mdSidenav('right').isOpen();
    };

    /**
     * Supplies a function that will continue to operate until the
     * time is up.
     */
    function debounce(func, wait, context) {
      var timer;

      return function debounced() {
        var context = $scope,
            args = Array.prototype.slice.call(arguments);
        $timeout.cancel(timer);
        timer = $timeout(function() {
          timer = undefined;
          func.apply(context, args);
        }, wait || 10);
      };
    }

    /**
     * Build handler to open/close a SideNav; when animation finishes
     * report completion in console
     */
    function buildDelayedToggler(navID) {
      return debounce(function() {
        // Component lookup should always be available since we are not using `ng-if`
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }, 200);
    }

    function buildToggler(navID) {
      return function() {
        // Component lookup should always be available since we are not using `ng-if`
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }
    }
  })
  .controller('LeftCtrl', function ($scope, $timeout, $mdSidenav, $log) {
    $scope.close = function () {
      // Component lookup should always be available since we are not using `ng-if`
      $mdSidenav('left').close()
        .then(function () {
          $log.debug("close LEFT is done");
        });

    };
  })
  .controller('RightCtrl', function ($scope, $timeout, $mdSidenav, $log) {
    $scope.close = function () {
      // Component lookup should always be available since we are not using `ng-if`
      $mdSidenav('right').close()
        .then(function () {
          $log.debug("close RIGHT is done");
        });
    };
  });


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
//FLOORPLAN



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
		send.success(function(data){
			alert('Client organisation successfully saved' );
			$location.path("/dashboard");
		});
		send.error(function(data){
			alert(data);
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
			alert('Succesfully updated the client organisation');
			$scope.send();
		});
		toEdit.error(function(response){
			alert('Error, ');
		});

	}
	

	$scope.checkRole =function(role,profile){
		     var roles=profile.systemSubscriptions;
		     console.log(roles);
			 var hasRole=false;
		     var index = 0;
			  angular.forEach(roles, function(item){             
			   if(hasRole==false&&role == roles[index]){	
					 hasRole=true;
					  console.log(hasRole);
			   }else{
				   index = index + 1;
			   }
			  });      
			  return hasRole;
			
	} ;
	

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
			alert('Successfully delete ');
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
			minDate: new Date([2016, 09, 10]),
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
				getLogo();
			});
		}, function (response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})
	};
	

	$scope.themeSelected;
	$scope.changeTheme = function(setTheme){
		$scope.themeSelected=setTheme;
		$('<link>')
		  .appendTo('head')
		  .attr({type : 'text/css', rel : 'stylesheet'})
		  .attr('href', 'css/styles/app-'+setTheme+'.css');

		// $.get('/api/change-theme?setTheme='+setTheme);

	}
	
	$scope.saveTheme = function(){

		var dataObj={theme:$scope.themeSelected};
		
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443//saveTheme',
			data    : dataObj, //forms user object
			responseType: 'arraybuffer'
		});

		console.log("SAVING THEME");
		send.success(function(data){
			alert('THEME IS SAVED');
		});
		send.error(function(data){
			alert('THEME IS NOT SAVED!');
		});
	}
	
}]);


////////ASSIGNING FUNCTIONS BASED ON ROLES

