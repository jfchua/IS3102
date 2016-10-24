
//VIEW DELET ICON
app.controller('iconController', function ($scope, $http,shareData) {
	
	angular.element(document).ready(function () {
		
		
		//RETRIEVE ICON WHEN LOADED
		$http.get("//localhost:8443/property/viewIcons").then(function(response){
			
			console.log(angular.fromJson(response.data));
			$scope.icons = response.data;

		},function(response){
			alert("DID NOT VIEW ICONS");
			
		})
		
	});

	$scope.remove = function(icon) { 
		if (confirm('CONFIRM TO DELETE ICON'+icon.id+'?')) {
			var index = $scope.icons.indexOf(icon);
			$scope.icons.splice(index, 1);  
			var dataObj={id:icon.id};
			$http.post('/property/deleteIcon', JSON.stringify(dataObj)).then(function(response){
		},function(response){//else is not saved successfully
			alert("Error, " + response);
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
app.controller('addIconController', ['$scope', 'Upload', '$timeout','$http','$state',function ($scope, Upload, $timeout,$http,$state ) {
	//for selection of icon types in add a icon form
	
	
	angular.element(document).ready(function () {
		$scope.iconTypes=[{'name':'Customised','iconType':'CUST'},
		                  {'name':'Entry','iconType':'ENTRY'},
		                  {'name':'Escalator','iconType':'ESCALATOR'},
		                  {'name':'Exit','iconType':'EXIT'},
		                  {'name':'Lift','iconType':'LIFT'},
		                  {'name':'Service Lift','iconType':'SERVICELIFT'},
		                  {'name':'Staircase','iconType':'STAIRCASE'},
		                  {'name':'Toilet','iconType':'TOILET'},
		                  {'name':'Unit','iconType':'RECT'}];
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
				alert('ICON IS CREATED. GOING BACK TO VIEW ICONS...')
				      $state.go("dashboard.viewIcon");
				
			});
		}, function (response) {
			if (response.status > 0){
				alert(response.data);
				$scope.errorMsg = response.status + ': ' + response.data;
			}
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})
	
	} 
}])



//UPDATE ICON
app.controller('updateIconController', ['$scope', 'Upload', '$timeout','$http','shareData','$state', function ($scope, Upload, $timeout,$http ,shareData,$state) {
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
		                  {'name':'Toilet','iconType':'TOILET'} ,
		                  {'name':'Unit','iconType':'RECT'}];
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
				alert('Icon updated successfully. Going back to viewing icons')
				      $state.go("workspace.viewIcon");
				
			});
		}, function (response) {
			if (response.status > 0){
				$scope.errorMsg = response.status + ': ' + response.data;
				alert("Error, " + response.data);
			
			}
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})
	
	} 
}])




//UPLOAD CSV
app.controller('csvController', function ($scope, $http,shareData) {
	
	angular.element(document).ready(function () {
		
		console.log("csvController ready");
		
	});
	    var _lastGoodResult = '';
	    $scope.toPrettyJSON = function (json, tabWidth) {
				var objStr = JSON.stringify(json);
				var obj = null;
				try {
					obj = $parse(objStr)({});
				} catch(e){
					// eat $parse error
					return _lastGoodResult;
				}

				var result = JSON.stringify(obj, null, Number(tabWidth));
				_lastGoodResult = result;

				return result;
	    };
	    $scope.attributeTypes=[];
	    $scope.updatedView=false;
	    $scope.csvCallback = function (result) {
	    	console.log($scope.csv.result);
	    	$scope.datas=$scope.csv.result;
	    	var contentString=$scope.csv.content;
	    	var contentStrings=contentString.split('\n');
	    	var headerString=contentStrings[0];
			
			$scope.typeStrings=headerString.split(',');
	    	}
	    
	   $scope.updateTableHeader=function(){
		   
		   var index = 0;
		    angular.forEach($scope.typeStrings, function() {
		    	$scope.attributeTypes.push({header:$scope.typeStrings[index]});
		        	index = index + 1;
		    });
		    console.log($scope.attributeTypes);
		    $scope.updatedView=true;
	   };
	   
	    $scope.saveData =function(){	
	    		console.log("START SAVING CSV");
	    	var dataObj = {
	    			datas:{
	    				data:$scope.datas
		            },
		            attributeTypes:{
		            	attributeType:$scope.attributeTypes
			            }      
	    };//END DATA OBJ
	    	$http.post('//localhost:8443/property/saveDatas', JSON.stringify(dataObj)).then(function(response){
	    		 alert("CSV FILE IS SAVED.");
		  },function(response){
			  console.log(response);
		        console.log("DID NOT SAVE");
	      })
	    }
	    $scope.csv = {
	    		content: null,
	    		header: true,
	    		headerVisible: true,
	    		separator: ',',
	    		separatorVisible: true,
	    		result: null,
	    		encoding: 'ISO-8859-1',
	    		encodingVisible: true,
	    		accept: true,
	    		callback: 'csvCallback'
	    		};
	
})

