//VIEW VENDOR, ADD VENDOR
app.controller('vendorController', ['$scope', '$http','$state','$routeParams','shareData', 'ModalService',function ($scope, $http,$state, $routeParams, shareData, ModalService) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/vendor/viewAllVendors").then(function(response){
			$scope.vendors = response.data;
			console.log("DISPLAY ALL vendors");
		},function(response){
			alert("did not view vendors");
		}	
		)	
	});
	$scope.passVendor = function(vendor){
		shareData.addData(vendor);
	}

	$scope.addVendor = function(){
		//alert("SUCCESS");
		$scope.data = {};
		
		if ( !$scope.vendor || !$scope.vendor.email || !$scope.vendor.name || !$scope.vendor.description || !$scope.vendor.contact){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered all fields",
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
		
		if ( $scope.vendor.contact.length < 3 || $scope.vendor.contact.length > 11 ||!(/^[0-9]+$/.test($scope.vendor.contact))  ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered a valid contact number",
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

			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Vendor saved successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewAllVendors");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL

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
			//END SHOWMODAL
		});
	};

}]);	
//UPDATE A VENDOR, DELETE A VENDOR
//VIEW VENDOR, ADD VENDOR
app.controller('updateVendorController', ['$scope', '$http','$state','$routeParams','shareData', 'ModalService',function ($scope, $http,$state, $routeParams, shareData,ModalService) {
	angular.element(document).ready(function () {
		$scope.vendor1 = shareData.getData();
		var dataObj = {			
				email: $scope.vendor1.email,
				name: $scope.vendor1.name,
				description: $scope.vendor1.description,
				contact: $scope.vendor1.contact,						
		};
		$scope.vendor = angular.copy($scope.vendor1)

		var url = "https://localhost:8443/vendor/updateVendor";
	});



	$scope.updateVendor = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.vendor.id);
		
		
		if ( !$scope.vendor || !$scope.vendor.email || !$scope.vendor.name || !$scope.vendor.description || !$scope.vendor.contact){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered all fields",
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
		
		if ( $scope.vendor.contact.length < 3 || $scope.vendor.contact.length > 11 ||!(/^[0-9]+$/.test($scope.vendor.contact))  ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered a valid contact number",
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Vendor saved successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewAllVendors");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
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
			//END SHOWMODAL
		});
	};	

	$scope.deleteVendor = function(){


		ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: 'Do you wish to delete vendor '+$scope.vendor.name+'?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					$scope.data = {};
					console.log("Start deleting vendor");
					//$scope.vendor = shareData.getData();
					console.log($scope.vendor.id);
					var tempObj ={vendorId:$scope.vendor.id};
					console.log("fetch id "+ tempObj);
					$http.post("//localhost:8443/vendor/deleteVendor", JSON.stringify(tempObj)).then(function(response){
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Vendor deleted successfully',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("dashboard.viewAllVendors");
							});
						});

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//END SHOWMODAL

					},function(data){
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
						//END SHOWMODAL
					}	
					)
				}
			});
		});

		$scope.dismissModal = function(result) {
			close(result, 200); // close, but give 200ms for bootstrap to animate

			console.log("in dissmiss");
		};

		//END SHOWMODAL

	}


}]);	

//DELETE MAINTENANCE
app.controller('deleteMaintenanceController', ['$scope',  '$timeout','$http','shareData','$state','ModalService', function ($scope,  $timeout,$http ,shareData,$state,ModalService) {
	angular.element(document).ready(function () {

		$scope.maintenance = shareData.getData();
	});
	$scope.deleteMaintenance = function(){
		
		ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: 'Do you wish to delete the maintenance of ID '+$scope.maintenance.id+'?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					var tempObj ={id:$scope.maintenance.id};
					$http.post("//localhost:8443/maintenance/deleteMaintenance", JSON.stringify(tempObj)).then(function(response){
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: "Maintenance successfully deleted. Going back to view maintenances."
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("dashboard.viewMaintenance");
							});
						});

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//END SHOWMODAL

					},function(response){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: "Server error in deleting the maintenance",
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
						//END SHOWMODAL
					}	
					)
				}
			});
		});

		$scope.dismissModal = function(result) {
			close(result, 200); // close, but give 200ms for bootstrap to animate

			console.log("in dissmiss");
		};

		//END SHOWMODAL		


	}

}])


app.controller('addMaintenanceController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService){
	
	$scope.checkDateErr = function(startDate,endDate) {
		$scope.errMessage = '';
		var curDate = new Date();

		if(new Date(startDate) > new Date(endDate)){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "End Date should be greater than start date",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.maintenance.end = '';
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};

			return false;
		}
		if(new Date(startDate) < curDate){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Maintenance start date should not be before today",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.maintenance.start = '';
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
			return false;
		}
	};
	
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
			var duplicate = false;
			var index = 0;
			angular.forEach($scope.selectedUnits, function() {
				if(duplicate==false&&$scope.currentlySelectedUnit.id == $scope.selectedUnits[index].id){
					duplicate = true;
				}else
					index = index + 1;
			});
			console.log(duplicate);
			if(!duplicate){
				$scope.selectedUnits.push($scope.currentlySelectedUnit);
			}

		}

		$scope.deleteUnit = function(unit){
			var index = $scope.selectedUnits.indexOf(unit);
			$scope.selectedUnits.splice(index, 1);  
		}
		console.log("finish selecting units");		
	}
	$scope.checkAvail = function(){
		console.log("start checking availability");
		$scope.data = {};

		if ( !$scope.maintenance|| !$scope.maintenance.start|| !$scope.maintenance.start){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Please make sure you have entered the starting and ending dates to check for availability and rent calculation",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.selectedUnits = [];
					$scope.currentlySelectedUnit = '';
				});
			});
			return;
		}
		var dataObj = {
				units: $scope.selectedUnits,
				start: ($scope.maintenance.start).toString(),
				end: ($scope.maintenance.start).toString(),
		};
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/maintenance/checkAvailability',
			data    : dataObj //forms user object
		});
		$scope.avail = "";
		send.success(function(){
			$scope.avail = "AVAILABLE!";
			console.log($scope.avail);
		});
		send.error(function(){
			$scope.avail = "NOT AVAILABLE!";
			console.log($scope.avail);
		});
	}
	/*
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
	}*/

	$scope.selectedVendors=[];
	//$scope.getAllVendors = function(){
	console.log("GETTING THE ALL UNITS INFO")
	var getVendors = $http({
		method  : 'GET',
		url     : 'https://localhost:8443/vendor/viewAllVendors/',
	});
	getVendors.success(function(response){
		$scope.vendors = response;
		console.log("RESPONSE IS" + JSON.stringify(response));

		console.log('Vendors Gotten');
	});
	getVendors.error(function(){
		alert('Error getting vendors');
	});		

	$scope.currentlySelectedVendor;
	$scope.selectVendor = function(){

		var duplicate = false;
		var index = 0;
		angular.forEach($scope.selectedVendors, function() {
			if(duplicate==false&&$scope.currentlySelectedVendor.id == $scope.selectedVendors[index].id)
				duplicate = true;
			else
				index = index + 1;
		});
		console.log(duplicate);
		if(!duplicate){
			$scope.selectedVendors.push($scope.currentlySelectedVendor);
		}


	}

	$scope.deleteVendor = function(vendor){
		var index = $scope.selectedVendors.indexOf(vendor);
		$scope.selectedVendors.splice(index, 1);  
	}
	console.log("finish selecting vendors");		
//	}

	$scope.addMaintenance = function(){

		console.log("start adding");
		$scope.data = {};
/*
		if ( !$scope.maintenance || !$scope.selectedUnits || !$scope.selectedVendors || !$scope.maintenance.start ||
				!$scope.maintenance.end || !$scope.maintenance.description){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Please make sure you have entered all the fields",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.selectedUnits = [];
					$scope.currentlySelectedUnit = '';
				});
			});
			return;
		}*/
		//EEPTOINjava.lang.NumberFormatException: For input string: ""   For input string: ""
		
		var dataObj = {
				units: $scope.selectedUnits,
				vendors: $scope.selectedVendors,
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


		send.success(function(){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Maintenance successfully saved",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewMaintenance");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL

		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Maintenance not added due to unavailability",
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
			//END SHOWMODAL
		});
	};

}]);

app.controller('updateMaintenanceController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService){
	angular.element(document).ready(function () {
		//VIEW MAINTENANCES
		$scope.maintenance1 = shareData.getData();
		console.log($scope.maintenance1);
		$scope.maintenance1.start = new Date($scope.maintenance1.start);
		$scope.maintenance1.end = new Date($scope.maintenance1.end);
		var dataObj = {			
				vendors: $scope.maintenance1.vendors,
				units: $scope.maintenance1.units,
				start: $scope.maintenance1.start,
				end: $scope.maintenance1.end,
				description: $scope.maintenance1.description,

		};
		$scope.maintenance = angular.copy($scope.maintenance1);

		var url = "https://localhost:8443/maintenance/updateMaintenance";

		//GET SELECTED UNITS
		var id=$scope.maintenance.id;

		$scope.url = "https://localhost:8443/maintenance/viewAllSelectedUnits/"+id;
		console.log("GETTING THE EVENT INFO")
		var getBookings = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/maintenance/viewAllSelectedUnits/' + id        
		});
		console.log("Getting the maintenance schedule Units using the url: " + $scope.url);
		getBookings.success(function(response){
			console.log('GET Selected Units SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			console.log("test MS *******");
			console.log(response);
			$scope.selectedSchedulesUnits1 = response;
			$scope.selectedSchedulesUnits=angular.copy($scope.selectedSchedulesUnits1);

		});
		getBookings.error(function(response){
			$state.go("dashboard.viewMaintenance");
			alert("FAILED");
			console.log('GET Selected Units FAILED! ' + JSON.stringify(response));
		});		

		$scope.selectedVendors=[];
		//$scope.getAllVendors = function(){
		console.log("GETTING THE ALL UNITS INFO")
		var getVendors = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/vendor/viewAllVendors/',
		});
		getVendors.success(function(response){
			$scope.vendors = response;
			console.log("RESPONSE IS" + JSON.stringify(response));

			console.log('Vendors Gotten');
		});
		getVendors.error(function(){
			alert('Error getting vendors');
		});		

		$scope.selectedVendors = $scope.maintenance.vendors;
		$scope.currentlySelectedVendor;
		$scope.selectVendor = function(){
			var duplicate = false;
			var index = 0;
			angular.forEach($scope.selectedVendors, function() {
				if(duplicate==false&&$scope.currentlySelectedVendor.id == $scope.selectedVendors[index].id)
					duplicate = true;
				else
					index = index + 1;
			});
			console.log(duplicate);
			if(!duplicate){
				$scope.selectedVendors.push($scope.currentlySelectedVendor);
			}
		}

		$scope.deleteVendor = function(vendor){
			var index = $scope.selectedVendors.indexOf(vendor);
			$scope.selectedVendors.splice(index, 1);  
		}
		console.log("finish selecting vendors");	

		//FOR SELECTING BULDING
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

	})
	//DELETE VENDOR
	$scope.deleteVendor = function(vendor){
		var index = $scope.maintenance.vendors.indexOf(vendor);
		$scope.maintenance.vendors.splice(index, 1);  
	}

	//DELETE SCHEDULE UNIT FROM SELECTED SCHEDULE UNIT
	$scope.deleteSchedulesUnit = function(unit){
		var index = $scope.selectedSchedulesUnits.indexOf(unit);
		$scope.selectedSchedulesUnits.splice(index, 1);  
	}
	//RESET SELECTED SCHEDULES UNITS TO LAST SAVED MAINTENANCE
	$scope.resetMaintSchedules = function(){
		$scope.maintenance = angular.copy($scope.maintenance1);
		$scope.selectedSchedulesUnits=angular.copy($scope.selectedSchedulesUnits1);
	}


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

	//$scope.selectedUnits=[];
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
			//$scope.haha.length=0;
			var duplicate = false;
			var index = 0;
			angular.forEach($scope.selectedBookingsUnits, function() {
				if(duplicate==false&&$scope.currentlySelectedUnit.id == $scope.selectedSchedulesUnits[index].id)
					duplicate = true;
				else
					index = index + 1;
			});
			console.log(duplicate);
			if(!duplicate){
			$scope.selectedSchedulesUnits.push($scope.currentlySelectedUnit);
			}
		}

		$scope.deleteUnit = function(unit){
			var index = $scope.selectedSchedulesUnits.indexOf(unit);
			$scope.selectedSchedulesUnits.splice(index, 1);  
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
			$state.go("dashboard.viewAllEventsEx");
			console.log('GET UNITS ID FAILED! ' + JSON.stringify(response));
		});
	}	

	$scope.checkAvail = function(){
		console.log("start checking availability");
		$scope.data = {};

		if ( !$scope.maintenance|| !$scope.maintenance.start|| !$scope.maintenance.start){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Please make sure you have entered the starting and ending dates to check for availability and rent calculation",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.selectedUnits = [];
					$scope.currentlySelectedUnit = '';
				});
			});
			return;
		}
		var dataObj = {
				units: $scope.selectedSchedulesUnits,
				start: ($scope.maintenance.start).toString(),
				end: ($scope.maintenance.start).toString(),
		};
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/maintenance/checkAvailabilityForUpdate',
			data    : dataObj //forms user object
		});
		$scope.avail = "";
		send.success(function(){
			$scope.avail = "AVAILABLE!";
			console.log($scope.avail);
		});
		send.error(function(){
			$scope.avail = "NOT AVAILABLE!";
			console.log($scope.avail);
		});
	}

	$scope.updateMaintenance = function(){
		console.log("Start updating");
		/*
		if ( !$scope.maintenance || !$scope.selectedUnits || !$scope.selectedVendors || !$scope.maintenance.start ||
				!$scope.maintenance.end || !$scope.maintenance.description){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Please make sure you have entered all the fields",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.selectedUnits = [];
					$scope.currentlySelectedUnit = '';
				});
			});
			return;
		}*/
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.maintenance.id);
		console.log($scope.selectedSchedulesUnits);
		var dataObj = {				
				id: $scope.maintenance.id,			
				vendors: $scope.maintenance.vendors,
				units: $scope.selectedSchedulesUnits,
				start: ($scope.maintenance.start).toString(),
				end:( $scope.maintenance.end).toString(),
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Maintenance saved successfully",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewMaintenance");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL

		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Server error in updating maintenance"
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
			//END SHOWMODAL
		});
	};	



}]);


app.filter('orderObjectBy', function() {
	  return function(items, field, reverse) {
	    var filtered = [];
	    angular.forEach(items, function(item) {
	      filtered.push(item);
	    });
	    filtered.sort(function (a, b) {
	      return (a[field] > b[field] ? 1 : -1);
	    });
	    if(reverse) filtered.reverse();
	    return filtered;
	  };
	});




app.controller('maintenanceController',['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {

		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/maintenance/viewMaintenance").then(function(response){
			$scope.maintenance_requests = response.data;
			console.log("DISPLAY ALL maintenance requests");
		},function(response){
			alert("did not view maintenance requests");
		}	
		)
		
		$scope.checkDateBefore = function (dateString) {
	    var daysAgo = new Date();
        return (new Date(dateString) > daysAgo);
	}
	});
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
			$state.go("dashboard.viewMaintenance");
			console.log('GET MAINTENANCE FAILED! ' + JSON.stringify(response));
		});			
	}


	$scope.passMaintenance = function(maint){
		shareData.addData(maint);
	}

	$scope.getMaintenanceById= function(){			
		$scope.maintenance1 = JSON.parse(shareData.getData());
		$scope.maintenance1.start = new Date($scope.maintenance1.start);
		$scope.maintenance1.end = new Date($scope.maintenance1.end);
		var dataObj = {			
				vendors: $scope.maintenance1.vendors,
				units: $scope.maintenance1.units,
				start: $scope.maintenance1.start,
				end: $scope.maintenance1.end,
				description: $scope.maintenance1.description,

		};
		$scope.maintenance = angular.copy($scope.maintenance1)

		var url = "https://localhost:8443/maintenance/updateMaintenance";
		//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}

}]);

app.controller('scheduleController', ['$scope','$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService) {
	angular.element(document).ready(function () {	
		//console.log(tempObj)
		console.log("DISPLAY ALL MAINTENANCES");
		$scope.maint = shareData.getData();
		var id=$scope.maint.id;
		$scope.schedules = {};
		$scope.url = "https://localhost:8443/maintenance/viewAllSchedules/"+id;
		//$scope.dataToShare = [];
		$scope.order_item = "id";
		$scope.order_reverse = false;
		var getBookings = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/maintenance/viewAllSchedules/' + id        
		});
		console.log("Getting the maintenances using the url: " + $scope.url);
		getBookings.success(function(response){
			console.log('GET MAINTENANCES SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			$scope.schedules = response;
		});
		getBookings.error(function(response){
			$state.go("dashboard.viewMaintenance");
			console.log('GET MAINTENANCES FAILED! ' + JSON.stringify(response));
		});

		//var url = "https://localhost:8443/maintenance/viewAllBookings";	
		//console.log("BOOKING DATA ARE OF THE FOLLOWING: " + $scope.bookings);	
	});

	$scope.deleteSchedule = function(id){
		var r = confirm("Confirm cancel? \nEither OK or Cancel.");
		if (r == true) {
			$scope.url = "https://localhost:8443/maintenance/deleteSchedule/"+id;
			var deleteBooking = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/maintenance/deleteSchedule/' + id        
			});
			console.log("Deleting the maintenance using the url: " + $scope.url);
			deleteBooking.success(function(response){
				alert('DELETE SCHEDULE SUCCESS! ');
				console.log("ID IS " + id);
				$state.go("dashboard.viewMaintenance");
			});
			deleteBooking.error(function(response){
				alert('DELETE SCHEDULE FAIL! ');
				$state.go("dashboard.viewMaintenance");
				console.log('DELETE BOOKING FAILED! ' + JSON.stringify(response));
			});
		} else {
			alert("Cancel deleting");
		}
	}

	$scope.checkDateBefore = function (dateString) {
	    var daysAgo = new Date();
	    return (new Date(dateString) > daysAgo);
	}

	$scope.checkSchedule = function () {
	    return ($scope.schedules.length > 1);
	}
	
	
	$scope.passSchedule=function(booking){
		console.log(booking);
		var obj={
				event:$scope.event,
				booking:booking
		};
		shareData.addData(obj);
	}
}])




