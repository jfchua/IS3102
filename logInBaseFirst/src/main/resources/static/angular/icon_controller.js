
//VIEW DELET ICON
app.controller('iconController', function ($scope, $http,shareData) {
	
	angular.element(document).ready(function () {
		
		
		//retrieve icons when page loaded
		$http.get("//localhost:8443/property/viewIcons").then(function(response){
			
			console.log(angular.fromJson(response.data));
			$scope.icons = response.data;

		},function(response){
			alert("did not view");
			//console.log("response is : ")+JSON.stringify(response);
		})
		
	});

	$scope.remove = function(icon) { 
		if (confirm('Are you sure you want to delete this?')) {
			var index = $scope.icons.indexOf(icon);
			$scope.icons.splice(index, 1);  
			var dataObj={id:icon.id};
			$http.post('/property/deleteIcon', JSON.stringify(dataObj)).then(function(response){
		},function(response){//else is not saved successfully
			console.log("DID NOT DELETE");
		})

		}
		   
	}
	
	$scope.updateIcon = function(icon){
		//push icon to shareData
		shareData.addData(icon);
		//console.log(shareData.getData());
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
	/*
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
	*/
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
				alert("ICON IS CREATED");
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



//ADD ICON
app.controller('updateIconController', ['$scope', 'Upload', '$timeout','$http','shareData', function ($scope, Upload, $timeout,$http ,shareData) {
	//for selection of icon types in add a icon form
	//get icon from sharedata
	
	angular.element(document).ready(function () {
		console.log(shareData.getData());
		$scope.icon = shareData.getData();
		$scope.iconTypes=[{'name':'Customised','iconType':'CUST'},
		                  {'name':'Entry','iconType':'ENTRY'},
		                  {'name':'Escalator','iconType':'ESCALATOR'},
		                  {'name':'Exit','iconType':'EXIT'},
		                  {'name':'Lift','iconType':'LIFT'},
		                  {'name':'Service Lift','iconType':'SERVICELIFT'},
		                  {'name':'Staircase','iconType':'STAIRCASE'},
		                  {'name':'Toilet','iconType':'TOILET'} ];
		$scope.iconType=$scope.icon.iconType;
	});

	$scope.picFile;
	
	$scope.updateIcon = function () {   
		console.log("start saving icon");
		console.log($scope.icon.id);
		console.log($scope.picFile);
		$scope.picFile.upload = Upload.upload({
			url: 'https://localhost:8443//property/updateIcon',
			data: { file: $scope.picFile,id:$scope.icon.id},
		});

		$scope.picFile.upload.then(function (response) {
			$timeout(function () {
				$scope.picFile.result = response.data;
				alert("Icon update is successful");
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





