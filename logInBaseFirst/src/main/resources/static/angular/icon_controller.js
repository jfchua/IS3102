
//VIEW DELET ICON
app.controller('iconController', function ($scope, $http,shareData) {
	
	angular.element(document).ready(function () {
		
		
		//retrieve icons when page loaded
		$http.get("//localhost:8443/property/viewIcons").then(function(response){
			$scope.buildings = response;
			console.log("DISPLAY ALL BUILDINGS");
			console.log("LEVELS DATA ARE OF THE FOLLOWING: " + $scope.buildings);

		},function(response){
			alert("did not view");
			//console.log("response is : ")+JSON.stringify(response);
		})
		
	});

	
	$scope.addUnit = function () {  
		$scope.units.push({"id": 0,"unitNumber": "#unit","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "coral","type": "./svg/rect.svg"}});
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
		
		},function(response){//else is not saved successfully
			console.log("DID NOT SAVE");
			console.log("response is "+JSON.stringify(response.data));
		})


	} 
	$scope.remove = function(unit) { 
		var index = $scope.units.indexOf(unit);
		$scope.units.splice(index, 1);     
	}

})


//ADD ICON
app.controller('addIconController', ['$scope', 'Upload', '$timeout','$http', function ($scope, Upload, $timeout,$http ) {
	//for selection of icon types in add a icon form
	
	
	angular.element(document).ready(function () {
		$scope.iconTypes=[{'name':'Customised','iconType':'CUST'},
		                  {'name':'Entry','iconType':'ENTRY'},
		                  {'name':'Escalator','iconType':'ESCALATOR'},
		                  {'name':'Exit','iconType':'EXIT'},
		                  {'name':'Lift','iconType':'LIFT'},
		                  {'name':'Service Lift','iconType':'SERVICELIFT'},
		                  {'name':'Staircase','iconType':'STAIRCASE'},
		                  {'name':'Toilet','iconType':'TOILET'} ];
		$scope.iconType=$scope.iconTypes[0].iconType;
	});

	$scope.picFile;
	$scope.uploadIcon = function () {   
		console.log("start saving icon");
		console.log($scope.iconType);
		console.log($scope.picFile);
		$scope.picFile.upload = Upload.upload({
			url: 'https://localhost:8443//property/uploadIcon',
			data: { file: $scope.picFile},
		});

		$scope.picFile.upload.then(function (response) {
			$timeout(function () {
				$scope.picFile.result = response.data;
				alert("is success " + JSON.stringify(response.data));
			});
		}, function (response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})
	
	} 
	
	$scope.saveIcon = function () {   
		console.log("start saving icon");
		console.log($scope.iconType);
		console.log($scope.picFile);
		$scope.picFile.upload = Upload.upload({
			url: 'https://localhost:8443//property/saveIcon',
			data: { file: $scope.picFile,iconType:$scope.iconType},
		});

		$scope.picFile.upload.then(function (response) {
			$timeout(function () {
				$scope.picFile.result = response.data;
				alert("is success " + JSON.stringify(response.data));
			});
		}, function (response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})
	
	} 
}])



