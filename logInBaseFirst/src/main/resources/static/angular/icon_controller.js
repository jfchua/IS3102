
//VIEW DELET ICON
app.controller('iconController',['$scope','$http','shareData', 'ModalService', function ($scope, $http,shareData,ModalService) {

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
		
        ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: "Do you wish to delete " +icon.id+'?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					var dataObj={id:icon.id};

					$http.post('/property/deleteIcon', JSON.stringify(dataObj)).then(function(response){
						console.log("Deleted the icon");
                       // var index = $scope.icons.indexOf(icon);
						//$scope.icons.splice(index, 1);  
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Icon successfully deleted',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
                            $state.go($state.current, {}, {reload: true}); 
							});
						});
						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate
						};
						//END SHOWMODAL
						//$scope.units=[];
		  				 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
		  				       // console.log("pure response is "+response.data);
		  				
		  				       // console.log("test anglar.fromJon"+angular.fromJson(response.data));
		  				        $scope.units=angular.fromJson(response.data);
		  				
		  				      },function(response){
		  				        console.log("DID NOT view");
		  				       // console.log("response is "+angular.fromJson(response.data).error);
		  				      })


					},function(response){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: "Did not delete icon, " + response.data,
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								
							});
						});
						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

						
						};
					}	
					)
				}
			});
		});

		$scope.dismissModal = function(result) {
		};

		//END SHOWMODAL
		      
		
		   
	}//END REMOVE

	$scope.updateIcon = function(icon){
		//push icon to shareData
		shareData.addData(icon);
		//console.log(shareData.getData());
	}

}])


//ADD ICON
app.controller('addIconController', ['$scope', 'Upload', '$timeout','$http','$state','ModalService', function ($scope, Upload, $timeout,$http,$state,ModalService ) {
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
			url: 'https://localhost:8443/property/saveIcon',
			data: { file: $scope.picFile,iconType:$scope.iconType},
		});

		$scope.picFile.upload.then(function (response) {
			$timeout(function () {
				$scope.picFile.result = response.data;
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "Icon created successfully."
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						$state.go("IFMS.viewIcon");
					});
				});

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};


			});
		}, function (response) {
			if (response.status > 0){
				//alert(response.data);
				$scope.errorMsg = response.status + ': ' + response.data;
				ModalService.showModal({

					templateUrl: "views/errorMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "Did not manage to upload icon, " + response.data,
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
			}
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})

	} 
}])



//UPDATE ICON
app.controller('updateIconController', ['$scope', 'Upload', '$timeout','$http','shareData','$state','ModalService', function ($scope, Upload, $timeout,$http ,shareData,$state,ModalService) {
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
			url: 'https://localhost:8443/property/updateIcon',
			data: { file: $scope.picFile,id:$scope.icon.id},
		});

		$scope.picFile.upload.then(function (response) {
			$timeout(function () {
				$scope.picFile.result = response.data;
				
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "Icon updated successfully. Going back to viewing icons."
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						$state.go("IFMS.viewIcon");
					});
				});

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};


			});
		}, function (response) {
			if (response.status > 0){
				$scope.errorMsg = response.status + ': ' + response.data;
				ModalService.showModal({

					templateUrl: "views/errorMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "Did not manage to update icon, " + response.data,
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

			}
		}, function (evt) {
			// Math.min is to fix IE which reports 200% sometimes
			$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		})

	} 
}])




//UPLOAD CSV
app.controller('csvController', function ($scope, $http,shareData,ModalService,$state) {

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
		//console.log($scope.csv.result);
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "CSV file saved successfully."
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
			$scope.datas=[];
			$scope.attributeTypes=[];
			$scope.updatedView=false;
			$state.reload();
			
		},function(response){
			console.log(response);
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: response.data
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
			$scope.datas=[];
			$scope.attributeTypes=[];
			$scope.updatedView=false;
			$state.reload();
			
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

