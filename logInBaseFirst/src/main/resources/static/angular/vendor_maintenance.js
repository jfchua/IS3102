/*       4. BUILDING         */
/////////////////////////////////////////////////////////////////////////////////////////////////
//vendor
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



app.controller('addMaintenanceController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData){
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
			alert('Get vendors error!!!!!!!!!!');
		});		
		
		$scope.currentlySelectedVendor;
		$scope.selectVendor = function(){
			$scope.selectedVendors.push($scope.currentlySelectedVendor);
		}

		$scope.deleteVendor = function(vendor){
			var index = $scope.selectedVendors.indexOf(vendor);
			$scope.selectedVendors.splice(index, 1);  
		}
		console.log("finish selecting vendors");		
//}
	
	$scope.addMaintenance = function(){
		console.log("test hailing");

		console.log("start adding");
		$scope.data = {};

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

		console.log("SAVING THE BUILDING");
		send.success(function(){
			alert('MAINTENANCE IS SAVED!');
		});
		send.error(function(){
			alert('MAINTENANCE IS NOT SAVED BECAUSE IT IS NOT AVAILABLE!');
		});
	};
	
}]);

app.controller('updateMaintenanceController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData){
	$scope.init= function(){
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
	
	
	
	$scope.updateMaintenance = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.maintenance.id);
		var dataObj = {				
				id: $scope.maintenance.id,
				units: $scope.maintenance.units,
				vendors: $scope.maintenance.vendors,
				units: $scope.maintenance.units,
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
				units: $scope.maintenance1.units,
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
				units: $scope.maintenance.units,
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


}]);



