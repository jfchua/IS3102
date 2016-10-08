/*       4. BUILDING         */
/////////////////////////////////////////////////////////////////////////////////////////////////
//VIEW BUILDINGS, ADD A BUILDING
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
			alert('BUILDING IS SAVED! GOING BACK TO VIEW BUILDINGS');
			$location.path("/viewBuilding");
			
		});
		send.error(function(){
			alert('SAVING BUILDING GOT ERROR!');
		});
	};
	angular.element(document).ready(function () {
		//retrieve buildings when page loaded
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
	});
	$scope.updateBuilding = function(building){
		
		shareData.addData(building);
	}
	
	$scope.deleteBuilding = function(building) { 
		shareData.addData(building);		   
	}
	
	$scope.passBuilding = function(building){
		shareData.addData(building);	
	}
	$scope.addLevel = function(building){
		shareData.addData(building);	
	}
	$scope.viewLevelsByBuildingId = function(id){

		$scope.dataToShare = [];

		
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
			console.log("LEVELS OF BUILDING" + id);
			//console.log(JSON.stringify(response));
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
			//console.log("ID IS " + id);
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

	


}]);

//UPDATE BUILDING
app.controller('updateBuildingController', ['$scope',  '$timeout','$http','shareData','$location', function ($scope,  $timeout,$http ,shareData,$location) {
	
	angular.element(document).ready(function () {

		$scope.building1 = shareData.getData();
			
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

	

	});

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
			 $location.path("/viewBuilding");
		});
		send.error(function(){
			alert('UPDATING BUILDING GOT ERROR!');
		});
	};	
}])

//DELETE BUILDING
app.controller('deleteBuildingController', ['$scope',  '$timeout','$http','shareData','$location', function ($scope,  $timeout,$http ,shareData,$location) {
	
	angular.element(document).ready(function () {

		$scope.building = shareData.getData();
		console.log("DISPLAYING BUILDING FOR DELETE");
		console.log($scope.building);
	});

	$scope.deleteBuilding = function(){
		console.log("START DELETE");
		$scope.data = {};
		var tempObj ={id:$scope.building.id};
		console.log("fetch id "+ tempObj);
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.post("//localhost:8443/building/deleteBuilding", JSON.stringify(tempObj)).then(function(response){
			//$scope.buildings = response.data;
			console.log("Delete the BUILDING");
			alert('BUILDING IS DELETED! GOING BACK TO VIEW BUILDINGS...');
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
			$location.path("/viewBuilding");
			
		},function(response){
			alert("DID NOT DELETE");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)
	};	
}])


//VIEW LEVELS
app.controller('viewLevelController', ['$scope', 'Upload', '$timeout','$http','$location','shareData',function ($scope, Upload, $timeout,$http,$location ,shareData) {
	
	//VIEW LEVELS WHEN PAGE LOADED
	angular.element(document).ready(function () {
		//console.log("VIEWING LEVELS OF BUILDING :" + shareData.getData());
		$scope.building = shareData.getData();
		console.log($scope.building);
		//populate levels from a building of the specific ID
		$scope.url = "https://localhost:8443/level/viewLevels/"+$scope.building.id;
		//$scope.dataToShare = [];
		console.log("GETTING THE LEVELS")
		var getLevels = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/level/viewLevels/' + $scope.building.id

		});

		console.log("Getting the levels using the url: " + $scope.url);
		getLevels.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET LEVELS SUCCESS! ' + JSON.stringify(response));
			console.log("LEVELS OF BUILDING" + $scope.building.id);
			//console.log(JSON.stringify(response));
			console.log(response);
			$scope.levels = response; //gets the response data from building controller 
			//shareData.addDataId(JSON.stringify(id));
			//$location.path("/viewLevels");
		});
		getLevels.error(function(response){
			$location.path("/viewBuilding");
			console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
		});
		var url = "https://localhost:8443/level/viewLevels";
		console.log("LEVELS DATA ARE OF THE FOLLOWING: " + $scope.levels);
	});

	//PASS LEVEL TO SHAREDATA
	$scope.passLevel = function(level){
		shareData.addData(level);
	}
}])

//ADD A LEVEL,UPDATE A LEVEL
app.controller('addLevelController', ['$scope', '$http','shareData','$location', function ($scope, $http, shareData,$location) {
	$scope.addLevel = function(){
		//alert("SUCCESS");
		console.log("start adding");
		$scope.data = {};
		$scope.building = shareData.getData();
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
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
			      $location.path("/viewBuilding");
			
		});
		send.error(function(){
			alert('SAVING LEVEL GOT ERROR! LEVEL NUMBER IS OUT OF RANGE');
		});
	};



}])

//DELETE A LEVEL,UPDATE A LEVEL
app.controller('levelController', ['$scope', '$http','shareData','$location', function ($scope, $http, shareData,$location) {
		var building;
	
	//VIEW LEVELS WHEN PAGE LOADED
	angular.element(document).ready(function () {
		$scope.level = shareData.getData();
		
		var url = "https://localhost:8443/level/updateLevel";
		console.log("LEVEL DATA ARE OF THE FOLLOWING: " + $scope.level);
		
		//$scope.getBuilding;
		   
		   var levelIdObj={id:$scope.level.id}
		    //get building from levelId
		    $http.post('/level/getBuilding', JSON.stringify(levelIdObj)).then(function(response){
		      console.log('GET BUILDING SUCCESS! ' + JSON.stringify(response));
		     building=response.data;
		      console.log(building);
		      console.log("Building ID IS " + building.id);
		    },function(response){
		      console.log('GET BUILDING ID FAILED! ' + JSON.stringify(response));
		    });	
		console.log(building);
	});
	
	  $scope.passBuilding = function(){
		  shareData.addData(building);
	  }
	  /*
	$scope.addLevel = function(){
		//alert("SUCCESS");
		console.log("start adding");
		$scope.data = {};
		$scope.building = shareData.getData();
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
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
			      $location.path("/viewBuilding");
			
		});
		send.error(function(){
			alert('SAVING LEVEL GOT ERROR! LEVEL NUMBER IS OUT OF RANGE');
		});
	};
*/
	$scope.updateLevel = function(){
		console.log("Start updating");
		$scope.data = {};
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
			shareData.addData(building); 
			alert('LEVEL '+$scope.level.levelNum+' IS UPDATED! GOING BACK TO VIEW LEVELS');
			$location.path("/viewLevels");
			//add go back to view levels when ready
		});
		send.error(function(){
			alert('UPDATING LEVEL GOT ERROR!');
		});
	}
/*
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
	*/
/*
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
*/
	$scope.deleteLevel = function(){
		if(confirm('CONFIRM TO DELETE LEVEL '+$scope.level.levelNum+'?')){
			$scope.data = {};
			console.log("Start deleting");
			$scope.level = shareData.getData();
			console.log($scope.level.id);
			var tempObj ={levelId:$scope.level.id};
			console.log("fetch id "+ tempObj);
			//var buildings ={name: $scope.name, address: $scope.address};
			$http.post("//localhost:8443/level/deleteLevel", JSON.stringify(tempObj)).then(function(response){
				//$scope.buildings = response.data;
				console.log("Delete the LEVEL");
				shareData.addData(building); 
				console.log(building);
				alert('LEVEL '+$scope.level.levelNum+' IS DELETED! GOING BACK TO VIEW LEVELS');
				$location.path("/viewLevels");
			},function(response){
				alert("DID NOT DELETE LEVEL");
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)
		}
		

	}

}]);	
