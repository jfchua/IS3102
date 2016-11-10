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

//                                Declaring Constants
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
//                                All UIRouters
 app.config(
		 function($stateProvider, $urlRouterProvider, $httpProvider, USER_ROLES) { 
											 $stateProvider
											  .state('/',{
												  url:'/',
												  templateUrl: '/views/mainlogin.html',
												  controller: 'usersController',
												  data: {
													  authorizedRoles: [USER_ROLES.all],
												  }
								
											  })
                                			  $stateProvider
                                			  .state('/login',{
                                				  url:'/login',
                                				  templateUrl: '/views/mainlogin.html',
                                				  controller: 'usersController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.all],
                                				  }

                                			  })
                                			  .state('/reset',{
                                				  url:'/reset',
                                				  templateUrl: '/views/resetPassword.html',
                                				  controller: 'UserController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.all]
                                				  }
                                			  })
                                			  .state('/resetPassword',{
                                				  url:'/resetPassword/:id/:token',
                                				  templateUrl: '/views/resetChangePass.html',
                                				  controller: 'usersController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.all]
                                				  }
                                			  })
                                			  .state('IFMS',{
                                				  url:'/IFMS/:param',
                                				  templateUrl: 'views/index.html',
                                				  controller: 'dashboardStateController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }

                                			  })
                                			  .state('IFMS.workspace',{
                                				  url:'/workspace',
                                				  templateUrl: 'views/workspace.html',
                                				  controller: 'workspaceController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.viewIcon',{
                                				  url: '/viewIcon',
                                				  templateUrl: '/views/viewIcon.html',
                                				  controller: 'iconController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addIcon',{
                                				  url: '/addIcon',
                                				  templateUrl: '/views/addIcon.html',
                                				  controller: 'addIconController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.updateIcon',{
                                				  url: '/updateIcon',
                                				  templateUrl: '/views/updateIcon.html',
                                				  controller: 'updateIconController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.uploadCSV',{
                                				  url: '/uploadCSV',
                                				  templateUrl: '/views/uploadCSV.html',
                                				  controller: 'csvController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.updateRent',{
                                				  url: '/updateRent',
                                				  templateUrl: '/views/updateRent.html',
                                				  controller: 'rentController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addBuilding',{
                                				  url: '/addBuilding',
                                				  templateUrl: '/views/addBuilding.html',
                                				  controller: 'buildingController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewBuilding',{
                                				  url: '/viewBuilding',
                                				  templateUrl: '/views/viewBuilding.html',
                                				  controller: 'buildingController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.updateBuilding',{
                                				  url: '/updateBuilding',		
                                				  templateUrl: '/views/updateBuilding.html',
                                				  controller: 'updateBuildingController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.deleteBuilding',{
                                				  url: '/deleteBuilding',
                                				  templateUrl: '/views/deleteBuilding.html',
                                				  controller: 'deleteBuildingController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewLevels',{
                                				  url: '/viewLevels',
                                				  templateUrl: '/views/viewLevels.html',
                                				  controller: 'viewLevelController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addLevel',{
                                				  url: '/addLevel',
                                				  templateUrl: '/views/addLevel.html',
                                				  controller: 'addLevelController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.updateLevel',{
                                				  url: '/updateLevel',
                                				  templateUrl: '/views/updateLevel.html',
                                				  controller: 'levelController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.deleteLevel',{
                                				  url: '/deleteLevel',
                                				  templateUrl: '/views/deleteLevel.html',
                                				  controller: 'levelController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addSpecialRate',{
                                				  url: '/addSpecialRate',
                                				  templateUrl: '/views/addSpecialRate.html',
                                				  controller: 'rateController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewAllRates',{
                                				  url: '/viewAllRates',
                                				  templateUrl: '/views/viewAllRates.html',
                                				  controller: 'rateController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.updateSpecialRate',{
                                				  url: '/updateSpecialRate',		
                                				  templateUrl: '/views/updateSpecialRate.html',
                                				  controller: 'updateRateController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.deleteSpecialRate',{
                                				  url: '/deleteSpecialRate',
                                				  templateUrl: '/views/deleteSpecialRate.html',
                                				  controller: 'deleteRateController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addEvent',{
                                				  url:'/addEvent',
                                				  templateUrl: '/views/addEvent.html',
                                				  controller: 'eventController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.uploadCompanyLogo',{
                                				  url:'/uploadCompanyLogo',
                                				  templateUrl: '/views/uploadCompanyLogo.html',
                                				  controller: 'logoController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.admin]
                                				  }
                                			  })

                                			  .state('IFMS.messageList',{
                                				  url:'/messageList',
                                				  templateUrl: '/message/message.html',
                                				  controller: 'ListController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.view/:id',{
                                				  url:'/view/:id',
                                				  templateUrl: '/message/detail.html',
                                				  controller: 'DetailController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.new',{
                                				  url:'/new',
                                				  templateUrl: '/message/new.html',
                                				  controller: 'NewMailController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.addClientOrg',{
                                				  url:'/addClientOrg',
                                				  templateUrl: '/views/addClientOrg.html',
                                				  controller: 'clientOrgController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.superadmin]
                                				  }
                                			  })	
                                			  .state('IFMS.viewClientOrgs',{
                                				  url:'/viewClientOrgs',
                                				  templateUrl: '/views/viewClientOrgs.html',
                                				  controller: 'viewClientOrgs',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.superadmin]
                                				  }
                                			  })
                                			  .state('IFMS.viewDataVisual',{
                                				  url:'/viewDataVisual',
                                				  templateUrl: '/views/viewDataVisual.html',
                                				  controller: 'ChartCtrl',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.viewTestData',{
                                				  url:'/viewTestData',
                                				  templateUrl: '/views/viewTestData.html',
                                				  controller: 'LineChartCtrl',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.createFloorPlan',{
                                				  url:'/createFloorPlan',
                                				  templateUrl: '/views/floorPlanAngular.html',
                                				  controller: 'floorPlanController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })	
                                			  .state('IFMS.viewFloorPlan',{
                                				  url:'/viewFloorPlan',
                                				  templateUrl: '/views/viewFloorPlan.html',
                                				  controller: 'viewFloorPlanController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.createUnitPlanDefault',{
                                				  url:'/createUnitPlanDefault',
                                				  templateUrl: '/views/createUnitPlanDefault.html',
                                				  controller: 'defaultUnitPlanController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })	
                                			  .state('IFMS.viewUnitPlanDefault',{
                                				  url:'/viewUnitPlanDefault',
                                				  templateUrl: '/views/viewUnitPlanDefault.html',
                                				  controller: 'viewDefaultUnitPlanController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewMaintenance',{
                                				  url:'/viewMaintenance',	
                                				  templateUrl: '/views/viewMaintenance.html',
                                				  controller: 'maintenanceController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewMaintenanceSchedule',{
                                				  url:'/viewMaintenanceSchedule',	
                                				  templateUrl: '/views/viewMaintenanceSchedule.html',
                                				  controller: 'scheduleController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewBuildingMtn',{
                                				  url:'/viewBuildingMtn',
                                				  templateUrl: '/views/viewBuildingMtn.html',
                                				  controller: 'buildingController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewLevelsMtn',{
                                				  url:'/viewLevelsMtn',
                                				  templateUrl: '/views/viewLevelsMtn.html',
                                				  controller: 'buildingController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewFloorPlanMtn',{
                                				  url:'/viewFloorPlanMtn',
                                				  templateUrl: '/views/viewFloorPlanMtn.html',
                                				  controller: 'floorPlanController',//hailing test hahahahha
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addMaintenance',{
                                				  url:'/addMaintenance',	
                                				  templateUrl: '/views/addMaintenance.html',
                                				  controller: 'addMaintenanceController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.updateMaintenance',{
                                				  url:'/updateMaintenance',
                                				  templateUrl: '/views/updateMaintenance.html',
                                				  controller: 'updateMaintenanceController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.deleteMaintenance',{
                                				  url:'/deleteMaintenance',
                                				  templateUrl: '/views/deleteMaintenance.html',
                                				  controller: 'deleteMaintenanceController',
                                				  data:{
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.viewAllVendors',{
                                				  url:'/viewAllVendors',
                                				  templateUrl: '/views/viewAllVendors.html',
                                				  controller: 'vendorController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addVendor',{
                                				  url:'/addVendor',	
                                				  templateUrl: '/views/addVendor.html',
                                				  controller: 'vendorController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.updateVendor',{
                                				  url:'/updateVendor',
                                				  templateUrl: '/views/updateVendor.html',
                                				  controller: 'updateVendorController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.deleteVendor',{
                                				  url:'/deleteVendor',
                                				  templateUrl: '/views/deleteVendor.html',
                                				  controller: 'updateVendorController',
                                				  data:{
                                					  authorizedRoles: [USER_ROLES.property]
                                				  }
                                			  })
                                			  .state('IFMS.addEventEx',{
                                				  url:'/addEventEx',
                                				  templateUrl: '/views/addEventEx.html',
                                				  //controller: 'eventExternalController',
                                				  controller: 'addEController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewBuildingEx',{
                                				  url:'/viewBuildingEx',
                                				  templateUrl: '/views/viewBuildingEx.html',
                                				  controller: 'viewBuildingExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewLevelsEx',{
                                				  url:'/viewLevelsEx',
                                				  templateUrl: '/views/viewLevelsEx.html',
                                				  controller: 'viewviewLevelsExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })

                                			  .state('IFMS.viewBuildingEx.viewFloorPlanEx',{
                                				  url:'/viewFloorPlanEx',
                                				  params: {
                                					  param1: null
                                				  },
                                				  templateUrl: '/views/viewFloorPlanEx.html',
                                				  controller: 'viewFloorPlanExController',

                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })

                                			  .state('IFMS.viewBuildingEx.viewFloorPlanEx.viewUnitPlanDefaultEx',{
                                				  url:'/viewUnitPlanDefaultEx',
                                				  params: {
                                					  param2: null
                                				  },
                                				  templateUrl: '/views/viewUnitPlanDefaultEx.html',
                                				  controller: 'viewDefaultUnitPlanExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.createUnitPlanEx',{
                                				  url:'/createUnitPlanEx',
                                				  templateUrl: '/views/createUnitPlanEx.html',
                                				  controller: 'areaPlanController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewUnitPlanEx',{
                                				  url:'/viewUnitPlanEx',
                                				  templateUrl: '/views/viewUnitPlanEx.html',
                                				  controller: 'viewAreaPlanController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.updateEventEx',{
                                				  url:'/updateEventEx',
                                				  templateUrl: '/views/updateEventEx.html',
                                				  //controller: 'eventExternalController',
                                				  controller: 'updateEController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.cancelEventEx',{
                                				  url:'/cancelEventEx',
                                				  templateUrl: '/views/cancelEventEx.html',
                                				  controller: 'deleteEventExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.updateCategoryEx',{
                                				  url:'/updateCategoryEx',
                                				  templateUrl: '/views/updateCategoryEx.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.configureDiscountsEx',{
                                				  url:'/configureDiscountsEx',
                                				  templateUrl: '/views/configureDiscountsEx.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.updateDiscount',{
                                				  url:'/updateDiscount',
                                				  templateUrl: '/views/updateDiscount.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.addDiscount',{
                                				  url:'/addDiscount',
                                				  templateUrl: '/views/addDiscount.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			   .state('IFMS.configureBeaconsEx',{
                                				  url:'/configureBeaconsEx',
                                				  templateUrl: '/views/configureBeaconsEx.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.updateBeaconEx',{
                                				  url:'/updateBeaconEx',
                                				  templateUrl: '/views/updateBeaconEx.html',
                                				  controller: 'updateBeaconController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.addBeaconEx',{
                                				  url:'/addBeaconEx',
                                				  templateUrl: '/views/addBeaconEx.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.feedbackEx',{
                                				  url:'/viewFeedbackEx',
                                				  templateUrl: '/views/viewFeedback.html',
                                				  controller: 'feedbackController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.addCategoryEx',{
                                				  url:'/addCategoryEx',
                                				  templateUrl: '/views/addCategoryEx.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.configureTicketsEx',{
                                				  url:'/configureTicketsEx',
                                				  templateUrl: '/views/configureTicketsEx.html',
                                				  controller: 'configureTicketsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewAllEventsEx',{
                                				  url:'/viewAllEventsEx',
                                				  templateUrl: '/views/viewAllEventsEx.html',
                                				  controller: 'eventExternalController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewApprovedEventsEx',{
                                				  url:'/viewApprovedEventsEx',
                                				  templateUrl: '/views/viewApprovedEventsEx.html',
                                				  controller: 'viewApprovedEventsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewToBeApprovedEvents',{
                                				  url:'/viewToBeApprovedEvents',
                                				  templateUrl: '/views/viewToBeApprovedEvents.html',
                                				  controller: 'viewToBeApprovedEventController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewBookingEx',{
                                				  url:'/viewBookingEx',
                                				  templateUrl: '/views/viewBookingEx.html',
                                				  controller: 'bookingController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewPaymentPlansEx',{
                                				  url:'/viewPaymentPlansEx',
                                				  templateUrl: '/views/viewPaymentPlansEx.html',
                                				  controller: 'paymentExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewPaymentHistoryEx',{
                                				  url:'/viewPaymentHistoryEx',
                                				  templateUrl: '/views/viewPaymentHistoryEx.html',
                                				  controller: 'paymentHistoryExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewPaymentDetailsEx',{
                                				  url:'/viewPaymentDetailsEx',
                                				  templateUrl: '/views/viewPaymentDetailsEx.html',
                                				  controller: 'paymentDetailsExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewTicketSalesEx',{
                                				  url:'/viewTicketSalesEx',
                                				  templateUrl: '/views/viewTicketSalesEx.html',
                                				  controller: 'ticketSaleExController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.organiser]
                                				  }
                                			  })
                                			  .state('IFMS.viewNotifications',{
                                				  url:'/viewNotifications',
                                				  templateUrl: '/views/viewNotifications.html',
                                				  controller: 'notificationController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.updateEventStatus',{
                                				  url:'/updateEventStatus',
                                				  templateUrl: '/views/updateEventStatus.html',
                                				  controller: 'viewEventDetailsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.deleteEvent',{
                                				  url:'/deleteEvent',
                                				  templateUrl: '/views/deleteEvent.html',
                                				  controller: 'deleteEventController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewAllEvents',{
                                				  url:'/viewAllEvents',
                                				  templateUrl: '/views/viewAllEvents.html',
                                				  controller: 'eventController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewApprovedEvents',{
                                				  url:'/viewApprovedEvents',
                                				  templateUrl: '/views/viewApprovedEvents.html',
                                				  controller: 'viewApprovedEventController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewEventDetails',{
                                				  url:'/viewEventDetails',
                                				  templateUrl: '/views/viewEventDetails.html',
                                				  controller: 'viewEventDetailsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewEventDetailsApproved',{
                                				  url:'/viewEventDetailsApproved',
                                				  templateUrl: '/views/viewEventDetailsApproved.html',
                                				  controller: 'viewEventDetailsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewEventOrganizers',{
                                				  url:'/viewEventOrganizers',
                                				  templateUrl: '/views/viewEventOrganizers.html',
                                				  controller: 'viewEventOrganiserController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewTicketSales',{
                                				  url:'/viewTicketSales',
                                				  templateUrl: '/views/viewTicketSales.html',
                                				  controller: 'ticketSalesController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewTicketSaleDetailsEvent',{
                                				  url:'/viewTicketSaleDetailsEvent',
                                				  templateUrl: '/views/viewTicketSaleDetailsEvent.html',
                                				  controller: 'ticketSaleDetailsController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.event]
                                				  }
                                			  })
                                			  .state('IFMS.viewAllPaymentPlans',{
                                				  url:'/viewAllPaymentPlans',
                                				  templateUrl: '/views/viewAllPaymentPlans.html',
                                				  controller: 'paymentController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.addPaymentPlan',{
                                				  url:'/addPaymentPlan',
                                				  templateUrl: '/views/addPaymentPlan.html',
                                				  controller: 'addPaymentController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.updatePaymentPlan',{
                                				  url:'/updatePaymentPlan',
                                				  templateUrl: '/views/updatePaymentPlan.html',
                                				  controller: 'updatePaymentController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.viewPaymentPolicy',{
                                				  url:'/viewPaymentPolicy',
                                				  templateUrl: '/views/viewPaymentPolicy.html',
                                				  controller: 'policyController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.addPaymentPolicy',{
                                				  url:'/addPaymentPolicy',
                                				  templateUrl: '/views/addPaymentPolicy.html',
                                				  controller: 'policyController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.updatePaymentPolicy',{
                                				  url:'/updatePaymentPolicy',
                                				  templateUrl: '/views/updatePaymentPolicy.html',
                                				  controller: 'updatePolicyController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.viewAllOutstandingBalance',{
                                				  url:'/viewAllOutstandingBalance',
                                				  templateUrl: '/views/viewAllOutstandingBalance.html',
                                				  controller: 'balanceController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.viewListOfEvents',{
                                				  url:'/viewListOfEvents',
                                				  templateUrl: '/views/viewListOfEvents.html',
                                				  controller: 'eventListController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.updateReceivedPayment',{
                                				  url:'/updateReceivedPayment',
                                				  templateUrl: '/views/updateReceivedPayment.html',
                                				  controller: 'receivedPController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.viewEventsWithTicketSales',{
                                				  url:'/viewEventsWithTicketSales',
                                				  templateUrl: '/views/viewEventsWithTicketSales.html',
                                				  controller: 'eventWithTicketController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.updateTicketRevenue',{
                                				  url:'/updateTicketRevenue',
                                				  templateUrl: '/views/updateTicketRevenue.html',
                                				  controller: 'ticketRController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.updateOutgoingPayment',{
                                				  url:'/updateOutgoingPayment',
                                				  templateUrl: '/views/updateOutgoingPayment.html',
                                				  controller: 'outgoingController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.viewPaymentHistory',{
                                				  url:'/viewPaymentHistory',
                                				  templateUrl: '/views/viewPaymentHistory.html',
                                				  controller: 'paymentHistoryController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.generateInvoice',{
                                				  url:'/generateInvoice',
                                				  templateUrl: '/views/generateInvoice.html',
                                				  controller: 'invoiceController',
                                				  data:{
                                					  authorizedRoles:[USER_ROLES.finance]
                                				  }
                                			  })
                                			  .state('IFMS.createNewUser',{
                                				  url:'/createNewUser',
                                				  templateUrl: '/views/createUser.html',
                                				  controller: 'createNewUserController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })	
                                			  .state('IFMS.viewUserList',{
                                				  url:'/viewUserList',
                                				  templateUrl: '/views/viewUserList.html',
                                				  controller: 'viewUserList',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.admin]
                                				  }
                                			  })	
                                			  .state('IFMS.viewAuditLog',{
                                				  url:'/viewAuditLog',
                                				  templateUrl: '/views/viewAuditLog.html',
                                				  controller: 'auditLogController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.admin]
                                				  }
                                			  })
                                			  .state('IFMS.viewUserProfile',{
                                				  url:'/viewUserProfile',
                                				  templateUrl: '/views/viewUserProfile.html',
                                				  controller: 'userProfileController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.editUserProfile',{
                                				  url:'/editUserProfile',
                                				  templateUrl: '/views/editUserProfile.html',
                                				  controller: 'userProfileController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('IFMS.dataVisual',{
                                				  url:'/dataVisual',
                                				  templateUrl: 'views/datavisual.html',
                                				  controller: 'dataVisualController',
                                				  data: {
                                					  authorizedRoles: [USER_ROLES.user]
                                				  }
                                			  })
                                			  .state('404',{
                                				  url:'/404.html',
                                				  templateUrl: 'views/404.html',
					                              controller: 'usersController',
					                              data: {
													authorizedRoles: [USER_ROLES.all]
					                              }
                                			  })

                                			  $urlRouterProvider.otherwise('404.html');

                                		  })
                                		  app.factory('httpAuthInterceptor', function ($q, $location, $injector) {
                                			  return {
                                				  'response': function(response) {
                                					  return response;
                                				  },
                                				  'responseError': function (response) {
                                					  event.preventDefault();
                                					  if (response.status === 500 && $location.path() === '//Expo/workspace'){
                                    					  event.preventDefault();
                                						  console.log(response); 
                                						  console.log(response.status);
                                						  alert("Your session has timeout! Please log in again!");
                                						  $injector.get('$state').transitionTo('/login');
                                					  }
                                					  return $q.reject(response);
                                				  }
                                			  };
                                		  })

app.config(function ($httpProvider) {
	$httpProvider.interceptors.push('httpAuthInterceptor');
 });












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




app.controller('MyController',['$scope','$http','ModalService', function ($scope, $http,ModalService) {
	var urlBase = "https://localhost:8443/user";
	function findAllNotifications() {
		//get all notifications to display
		$http.get(urlBase + '/notifications/findAllNotifications')
		.then(function(res){
			$scope.notifications = res.data;
		});
	}
	

	findAllNotifications();

	
}]);

app.controller("userCtrl",['$scope','ModalService', function userCtrl($scope,ModalService) {
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
	
	$scope.send = function(){
		console.log("REACHED HERE FOR FETCHING ALL USERS");

		var fetch = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/viewAllUsers',
		});

		console.log("fetching the user list.......");
		fetch.success(function(response){

			$scope.model = response;
		});
		fetch.error(function(response){
			console.log('Failure to fetch all users!');
		});

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
}]);



//Create new user

app.controller('createNewUserController', ['$scope','$http','$state','ModalService',function($scope, $http, $state, ModalService){


	var arr = ['ROLE_USER','ROLE_EVENT','ROLE_ADMIN','ROLE_PROPERTY','ROLE_EXTEVE'];

	var subz = sessionStorage.getItem('subscriptions');
	if (subz.indexOf("TICKETING") > -1) {
		arr.push('ROLE_TICKETING');
		
	} 
	if (subz.indexOf("FINANCE") > -1) {
		arr.push('ROLE_FINANCE');
	} 
	if (subz.indexOf("BI") > -1) {
		arr.push('ROLE_HIGHER');
	} 


	$scope.genders= arr;
	$scope.selection=[];

	$scope.toggleSelection = function toggleSelection(gender) {
		var idx = $scope.selection.indexOf(gender);
		//alert("index selected gender: " + idx);
		if (idx > -1) {
			// is currently selected
			$scope.selection.splice(idx, 1);
		}
		else {
			// is newly selected
			//$scope.selected = [];
			$scope.selection.push(gender);
			//$scope.selection.push(gender);
		}
		//alert(JSON.stringify($scope.selection));
	};



	var role = $scope.roles;
	$scope.createNewUser = function(){

		var dataObj = {			
				email: $scope.ctrl.email,
				name: $scope.ctrl.name,
				roles: $scope.selection			
		};
//alert(JSON.stringify(dataObj));
		var create = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/addNewUser',
			data    :  dataObj//forms user object
		});
		create.success(function(){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'New user successfully created',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewUserList");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		create.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: data,
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	}
}]);


//VIEW USER START
app.controller('viewUserList', ['$scope','$http','$location','ModalService',
                                function($scope, $http,$location,ModalService) {
	//assign roles
	var arr = ['ROLE_USER','ROLE_EVENT','ROLE_ADMIN','ROLE_PROPERTY','ROLE_EXTEVE'];

	var subz = sessionStorage.getItem('subscriptions');
	if (subz.indexOf("TICKETING") > -1) {
		arr.push('ROLE_TICKETING');
		
	} 
	if (subz.indexOf("FINANCE") > -1) {
		arr.push('ROLE_FINANCE');
	} 
	if (subz.indexOf("BI") > -1) {
		arr.push('ROLE_HIGHER');
	} 


	$scope.genders= arr;

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
		});
		fetch.error(function(response){
			//alert('FETCH ALL USERS FAILED!!!');
		});

	}
	$scope.send();

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

			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'User successfully updated',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			$scope.send();

		});
		toEdit.error(function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Server error in updating user",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'User deleted successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		del.error(function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: response,
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
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
app.controller('userProfileController', ['$scope', '$http','ModalService','$state', function ($scope, $http,ModalService,$state) {

	$scope.question = [
	                   {question: "What is your Mother\'s Maiden name"},
	                   {question: "What is your favourite number"},
	                   {question: "What is your favourite food"},
	                   {question: "What is your favourite animal"},
	                   {question: "What is your favourite colour"}
	                   ];


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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'User profile successfully updated',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					//console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				//console.log("in dissmiss");
			};
		});
		send.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: data,
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	};

	$scope.submitChangePass = function(){
		//alert("SUCCESS");
		if ( $scope.userProfile.password1 != $scope.userProfile.password2 ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'The 2 passwords do not match',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			return;
		}
		else{
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
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Password successfully updated',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
			});
			send.error(function(data){
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: data,
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
			});
		};
	}

	$scope.submitChangeSecurity = function(){
		//alert("SUCCESS");
		if ( $scope.userProfile.security1 != $scope.userProfile.security2 ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'The security answers do not match',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			return;
		}
		else{
			var dataObj = {
					security: $scope.userProfile.security1,
					oldsecurity: $scope.userProfile.security0,
					question: $scope.selectedQuestion,
			};


			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/user/changeSecurity',
				data    : dataObj //forms user object
			});

			send.success(function(){
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Security Question and Answer successfully updated',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dismiss");
				};
			});
			send.error(function(data){
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: data,
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
			});
		};
	}

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
app.controller('AlertDemoCtrl',[ '$scope','datfactory','$http','ModalService', function ($scope, datfactory, $http,ModalService){
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

	//MODAL FOR VIEWING ONE MESSAGE
	$scope.complexResult = null;
	$scope.showModal = function(message) {
		console.log(message);
		// Just provide a template url, a controller and call 'showModal'.
		ModalService.showModal({

			templateUrl: "views/viewMessageTemplate.html",
			controller: "viewMessageController",
			inputs: {
				title: "View Message",
				message:message
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				console.log("FINISHED VIEWING UNIT");
			});
		});

	};//END SHOWMODAL

	$scope.dismissModal = function(result) {
		close(result, 200); // close, but give 200ms for bootstrap to animate

		console.log("in dissmiss");
	};


	$scope.closeAlert = function(index) {

		console.log("ID: " + $scope.getId[index]);
		var del = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/deleteNotification',
			data    :  $scope.getId[index] //forms user object
		});
		del.success(function(){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Notification deleted successsfully!',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			$scope.alerts.splice(index, 1);
		});
		del.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: data,
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});

	};
}]);

//VIEW MESSAGE MODAL
app.controller('viewMessageController', ['$scope', '$element', 'title', 'close', 'message',
                                         function($scope, $element, title, close,message) {


	$scope.title = title;
	$scope.message=message;
	console.log(title);
	console.log(close);
	console.log($element);
	//  This close function doesn't need to use jQuery or bootstrap, because
	//  the button has the 'data-dismiss' attribute.
	$scope.close = function() {
		close({

		}, 500); // close, but give 500ms for bootstrap to animate
	};

	//  This cancel function must use the bootstrap, 'modal' function because
	//  the doesn't have the 'data-dismiss' attribute.
	$scope.cancel = function() {

		//  Manually hide the modal.
		$element.modal('hide');

		//  Now call close, returning control to the caller.
		close({

		}, 500); // close, but give 500ms for bootstrap to animate
	};




}])

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

app.controller('NewMailController',['$scope','$state','$http','ModalService', function($scope, $state, $http,ModalService){
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
		ModalService.showModal({

			templateUrl: "views/errorMessageTemplate.html",
			controller: "errorMessageModalController",
			inputs: {
				message: data,
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				console.log("OK");
			});
		});
		$scope.dismissModal = function(result) {
			close(result, 200); // close, but give 200ms for bootstrap to animate

			console.log("in dissmiss");
		};
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

		if ( $scope.selectedContacts < 1 ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Please select at least 1 user to send a message to',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			}; 
			return;
		}
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Message sent successsfully!',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		sendMsg.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: data,
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});

		//alert('message sent');
		$state.go('IFMS.workspace');
	};
}]);

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


app.controller('eventExternalController', ['$scope', '$http','$location','$routeParams','shareData', 'ModalService',function ($scope, $http,$location, $routeParams, shareData,ModalService) {


	$scope.viewEvents = function(){
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		$http.get("//localhost:8443/event/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			console.log("did not view all events");
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
			console.log("did not view approved events");
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
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Error in cancelling event!',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
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


app.controller('bookingController', ['$scope','$http','$location','$routeParams','shareData','ModalService', function ($scope, $http,$location, $routeParams, shareData,ModalService) {
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
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Booking deleted successsfully!',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
				console.log("ID IS " + id);
			});
			deleteBooking.error(function(response){
				ModalService.showModal({

					templateUrl: "views/errorMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Did not manage to delete the booking',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
				$location.path("/viewAllEventsEx");
				console.log('DELETE BOOKING FAILED! ' + JSON.stringify(response));
			});
		} else {
			console.log("Cancel deleting booking");
		}
	}
}])


//===========================================================================
//7. Audit Logs
//===========================================================================
app.controller('auditLogController', ['$scope', '$http','ModalService', function ($scope, $http,ModalService) {
	
	var subz = sessionStorage.getItem('subscriptions');
	$scope.IsTicketingSub = function(){
		//console.log("ISTICKETING: " + (subz.indexOf("TICKETING") > -1));
		return (subz.indexOf("TICKETING") > -1);
	}

	$scope.IsFinanceSub = function(){
		//console.log("ISFINANCE: " + (subz.indexOf("FINANCE") > -1));
		return (subz.indexOf("FINANCE") > -1);
	}

	$scope.IsBISub = function(){
		return (subz.indexOf("BI") > -1);
	}
	
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Download successful!',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		send.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Error in downloading',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
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
		console.log('Get sender error!');
	});

}]);


//TODOLIST CALENDAR////

app.controller('UsersIndexController', ['$scope','$http','ModalService', function($scope,$http,ModalService) {
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
				//alert("Tasks for this day: " + arrayTasks);

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

//ERROR MESSAGE MODAL TEMPLATE:
app.controller('errorMessageModalController', ['$scope','message','close',
                                               function($scope, message,close) {
	$scope.message = message;
	$scope.close = function() {
		close({
			unit:$scope.unit
		}, 399); // close, but give 500ms for bootstrap to animate
	};

	//  This cancel function must use the bootstrap, 'modal' function because
	//  the doesn't have the 'data-dismiss' attribute.
	$scope.cancel = function() {

		//  Manually hide the modal.
		$element.modal('hide');

		//  Now call close, returning control to the caller.
		close({
			unit:$scope.unit
		}, 399); // close, but give 500ms for bootstrap to animate
	};



}
])

app.controller('YesNoController', ['$scope', 'close','message', function($scope, close,message) {
	$scope.message = message;
	$scope.close = function(result) {
		close(result, 500); // close, but give 500ms for bootstrap to animate
	};

}])




//UPLOAD LOGO CONTROLLER//
app.controller('logoController', ['$scope', 'Upload', '$timeout','$http','ModalService' ,function ($scope, Upload, $timeout,$http,ModalService ) {

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
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Upload successful!',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Theme saved successsfully!',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		send.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Theme could not be saved!',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	}

}]);


////////ASSIGNING FUNCTIONS BASED ON ROLES