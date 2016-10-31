/*       4. BUILDING         */
/////////////////////////////////////////////////////////////////////////////////////////////////
//VIEW BUILDINGS, ADD A BUILDING
app.controller('buildingController', ['$scope', '$http','$state','$routeParams','shareData','ModalService','Upload', '$timeout', function ($scope, $http,$state, $routeParams, shareData,ModalService,Upload,$timeout) {



	$scope.submit1 = function(){
		//alert("SUCCESS");
		$scope.data = {};

		if ( !angular.isDefined($scope.building) ){
			return;
		}




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
		send.success(function(buildingId){
			console.log("save building ok");
			if ($scope.picFile != null && $scope.picFile != "") {
				$scope.buildingId = buildingId;
				$scope.picFile.upload = Upload.upload({
					url: 'https://localhost:8443//building/saveBuildingImage',
					data: { file: $scope.picFile,buildingId:$scope.buildingId},
				});

				$scope.picFile.upload.then(function (response) {
					$timeout(function () {
						$scope.picFile.result = response.data;
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Building and its associated image has been saved successfully',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("dashboard.viewBuilding");
							});
						});

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//END SHOWMODAL

					});
				}, function (response) {
					if (response.status > 0){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: response.data,
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
							});
						});

						//END SHOWMODAL

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};

						$scope.errorMsg = response.status + ': ' + response.data;
					}
				}, function (evt) {
					// Math.min is to fix IE which reports 200% sometimes
					$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
				})
				/////////////////////////
				return;
			}



			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Building has been saved successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewBuilding");
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

			//END SHOWMODAL

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};


		});
	};
	angular.element(document).ready(function () {
		//retrieve buildings when page loaded
		$scope.data = {};
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/building/viewBuildings").then(function(response){
			$scope.buildings = response.data;
			console.log("DISPLAY ALL BUILDINGS");
			console.log("test view buildings");
			console.log( $scope.buildings);

		},function(response){
			alert("did not view buildings, server error");
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
			$state.go("dashboard.viewBuilding");
			console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
		});
	};	
	/*
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
			alert('Error getting building, ' + JSON.stringify(response));
		});

	}

	 */
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
app.controller('updateBuildingController', ['$scope',  '$timeout','$http','shareData','$state','ModalService','Upload',function ($scope,  $timeout,$http ,shareData,$state,ModalService,Upload) {

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

		if ( $scope.building.address == null || $scope.building.name == null || $scope.building.numFloor == null || $scope.building.city == null || $scope.building.postalCode == null){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Please make sure you have entered all fields",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});
			return;
		}

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
		send.success(function(buildingId){


			if ($scope.picFile != null) {
				$scope.buildingId = buildingId;
				$scope.picFile.upload = Upload.upload({
					url: 'https://localhost:8443//building/saveBuildingImage',
					data: { file: $scope.picFile,buildingId:$scope.buildingId},
				});

				$scope.picFile.upload.then(function (response) {
					$timeout(function () {
						$scope.picFile.result = response.data;
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Building and its associated image has been saved successfully',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("dashboard.viewBuilding");
							});
						});

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//END SHOWMODAL

					});
				}, function (response) {
					if (response.status > 0){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: response.data,
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
							});
						});

						//END SHOWMODAL

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};

						$scope.errorMsg = response.status + ': ' + response.data;
					}
				}, function (evt) {
					// Math.min is to fix IE which reports 200% sometimes
					$scope.picFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
				})
				/////////////////////////
				return;
			}



			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Building successfully updated',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go('dashboard.viewBuilding');
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
}])

//DELETE BUILDING
app.controller('deleteBuildingController', ['$scope',  '$timeout','$http','shareData','$state','ModalService', function ($scope,  $timeout,$http ,shareData,$state,ModalService) {

	angular.element(document).ready(function () {

		$scope.building = shareData.getData();
		console.log("DISPLAYING BUILDING FOR DELETE");
		console.log($scope.building);
	});

	$scope.deleteBuilding = function(){


		ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: "Do you wish to delete " +$scope.building.name+'?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					console.log("START DELETE");
					$scope.data = {};
					var tempObj ={id:$scope.building.id};
					console.log("fetch id "+ tempObj);

					$http.post("//localhost:8443/building/deleteBuilding", JSON.stringify(tempObj)).then(function(response){
						//$scope.buildings = response.data;
						console.log("Delete the BUILDING");
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Building successfully deleted',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("dashboard.viewBuilding");
							});
						});
						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//END SHOWMODAL
						//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
						//$location.path("/viewBuilding");


					},function(response){
						alert("Error, " + response);
						//console.log("response is : ")+JSON.stringify(response);
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




	};	
}])


//VIEW LEVELS
app.controller('viewLevelController', ['$scope', 'Upload', '$timeout','$http','$state','shareData','ModalService',function ($scope, Upload, $timeout,$http,$state ,shareData,ModalService) {
	var building;
	//VIEW LEVELS WHEN PAGE LOADED
	angular.element(document).ready(function () {
		//console.log("VIEWING LEVELS OF BUILDING :" + shareData.getData());
		$scope.building = shareData.getData();
		building=$scope.building;
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
			$state.go("dashboard.viewBuilding");
			console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
		});
		var url = "https://localhost:8443/level/viewLevels";
		console.log("LEVELS DATA ARE OF THE FOLLOWING: " + $scope.levels);
	});

	//PASS LEVEL TO SHAREDATA
	$scope.passLevel = function(level){
		var obj={building:building,
				level:level
		}
		shareData.addData(obj);
	}
}])

//ADD A LEVEL,UPDATE A LEVEL
app.controller('addLevelController', ['$scope', '$http','shareData','$state','ModalService', function ($scope, $http, shareData,$state,ModalService) {
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Level successfully saved',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewBuilding");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))


		});
		send.error(function(){
			$scope.addLevelFail={status:true};
			//alert('SAVING LEVEL GOT ERROR! LEVEL NUMBER IS OUT OF RANGE');
		});
	};
	$scope.addLevelFail={status:false};


}])

//DELETE A LEVEL,UPDATE A LEVEL
app.controller('levelController', ['$scope', '$http','shareData','$state','ModalService', function ($scope, $http, shareData,$state,ModalService) {
	var building;

	//VIEW LEVELS WHEN PAGE LOADED
	angular.element(document).ready(function () {
		$scope.obj = shareData.getData();
		obj=shareData.getData();
		$scope.level=obj.level
		building=obj.buliding;
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Level '+$scope.level.levelNum+' successfully updated! Going back to view levels'
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewLevels");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};

			//add go back to view levels when ready
		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Error in updating level'
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

	$scope.deleteLevel = function(){
		if(confirm('CONFIRM TO DELETE LEVEL '+$scope.level.levelNum+'?')){
			$scope.data = {};
			console.log("Start deleting");
			//$scope.level = shareData.getData();
			console.log($scope.level.id);
			var tempObj ={levelId:$scope.level.id};
			console.log("fetch id "+ tempObj);
			//var buildings ={name: $scope.name, address: $scope.address};
			$http.post("//localhost:8443/level/deleteLevel", JSON.stringify(tempObj)).then(function(response){
				//$scope.buildings = response.data;
				console.log("Delete the LEVEL");
				shareData.addData(building); 
				console.log(building);
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Level '+$scope.level.levelNum+' deleted successfully! Going back to view levels'
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						$state.go("dashboard.viewLevels");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
				$state.go("dashboard.viewLevels");
			},function(response){
				ModalService.showModal({

					templateUrl: "views/errorMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Error in deleting level'
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						$state.go("dashboard.viewLevels");
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


	}

}]);	


app.controller('rateController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService){
	$scope.myDropDown = 'period';

	$scope.months = [{'name':'JANUARY','month':'JAN'},{'name':'FEBRUARY','month':'FEB'},
	                 {'name':'MARCH', 'month':'MAR'},{'name':'APRIL','month':'APR'},
	                 {'name':'MAY','month':'MAY'},{'name':'JUNE','month':'JUN'},
	                 {'name':'JULY','month':'JUL'},{'name':'AUGUST','month':'AUG'},
	                 {'name':'SEPTEMBER','month':'SEP'},{'name':'OCTOBER','month':'OCT'}, 
	                 {'name':'NOVEMBER','month':'NOV'}, {'name':'DECEMBER','month':'DEC'}];

	$scope.weekends = [{'name':'yes', 'weekend':'weekend'}];

	$scope.addRate = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {
				rate: ($scope.special.rate).toString(),
				description: $scope.special.description,
				period: ($scope.special.period).toString(),
		};
		console.log("period is" + $scope.special.period);
		console.log("REACHED HERE FOR SUBMIT RATE " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/rate/addSpecialRate',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE RATE");
		send.success(function(){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Rate saved successfully. Going back to view rates'
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewLevels");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			$state.go("dashboard.viewAllRates");

		});
		send.error(function(){
			alert('SAVING RATE GOT ERROR!');
		});
	};

	angular.element(document).ready(function () {
		//retrieve buildings when page loaded
		$scope.data = {};
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/rate/viewAllRates").then(function(response){
			$scope.rates = response.data;
			console.log("READY TO DISPLAY ALL RATES");
			console.log("DISPLAY ALL RATES: "+ $scope.rates);
		},function(response){
			alert("did not view");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)
	});
	$scope.updateSpecialRate = function(rate){

		shareData.addData(rate);
	}

	$scope.deleteSpecialRate = function(rate) { 
		shareData.addData(rate);		   
	}

	$scope.passRate = function(rate){
		shareData.addData(rate);	
	}

	$scope.getRate = function(id){		
		$scope.dataToShare = [];	  
		$scope.url = "https://localhost:8443/rate/getRate/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE RATE INFO")
		var getRate = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/rate/getRate/' + id        
		});
		console.log("Getting the rate using the url: " + $scope.url);
		getRate.success(function(response){
			console.log('GET RATE SUCCESS! ' + JSON.stringify(response));
			shareData.addData(JSON.stringify(response));
		});
		getRate.error(function(response){
			$state.go("dashboard.viewAllRates");
			console.log('GET RATE FAILED! ' + JSON.stringify(response));
		});

	}

	$scope.getRateById= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.special1 = JSON.parse(shareData.getData());
		var dataObj = {
				rate: $scope.special1.rate,
				description: $scope.special1.description,
				period: $scope.special1.period,
		};
		$scope.special = angular.copy($scope.special1)
		var url = "https://localhost:8443/rate/updateRate";
		console.log("RATE DATA ARE OF THE FOLLOWING: " + $scope.special);
	}
}]);

app.controller('updateRateController', ['$scope',  '$timeout','$http','shareData','$state','ModalService', function ($scope,  $timeout,$http ,shareData,$state,ModalService) {

	angular.element(document).ready(function () {

		$scope.special1 = shareData.getData();

		var dataObj = {
				rate: ($scope.special1.rate).toString(),
				description: $scope.special1.description,
				period: $scope.special1.period,
		};
		$scope.special = angular.copy($scope.special1)
		var url = "https://localhost:8443/rate/updateRate";
		console.log("RATE DATA ARE OF THE FOLLOWING: " + $scope.special);



	});

	$scope.myDropDown = 'period';

	$scope.months = [{'name':'JANUARY','month':'JAN'},{'name':'FEBRUARY','month':'FEB'},
	                 {'name':'MARCH', 'month':'MAR'},{'name':'APRIL','month':'APR'},
	                 {'name':'MAY','month':'MAY'},{'name':'JUNE','month':'JUN'},
	                 {'name':'JULY','month':'JUL'},{'name':'AUGUST','month':'AUG'},
	                 {'name':'SEPTEMBER','month':'SEP'},{'name':'OCTOBER','month':'OCT'}, 
	                 {'name':'NOVEMBER','month':'NOV'}, {'name':'DECEMBER','month':'DEC'}];

	$scope.weekends = [{'name':'yes', 'weekend':'weekend'}];

	$scope.updateRate = function(){
		$scope.data = {};
		//$scope.building = JSON.parse(shareData.getData());
		var dataObj = {					
				id: $scope.special.id,
				rate: ($scope.special.rate).toString(),
				description: $scope.special.description,
				period: ($scope.special.period).toString(),
		};		
		console.log("REACHED HERE FOR SUBMIT RATE " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/rate/updateRate',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE RATE");
		send.success(function(){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Rate updated successfully!'
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewLevels");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			$state.go("dashboard.viewAllRates");
		});
		send.error(function(){
			alert('UPDATING RATE GOT ERROR!');
		});
	};	
}])

//DELETE BUILDING
app.controller('deleteRateController', ['$scope',  '$timeout','$http','shareData','$state','ModalService', function ($scope,  $timeout,$http ,shareData,$state,ModalService) {

	angular.element(document).ready(function () {

		$scope.special = shareData.getData();
		console.log("DISPLAYING RATE FOR DELETE");
		console.log($scope.special);
	});

	$scope.deleteRate = function(){
		if(confirm('CONFIRM TO DELETE SPECIAL RATE '+$scope.special.description+'?')){
			console.log("START DELETE");
			$scope.data = {};
			var tempObj ={id:$scope.special.id};
			console.log("fetch id "+ tempObj);

			$http.post("//localhost:8443/rate/deleteRate", JSON.stringify(tempObj)).then(function(response){
				//$scope.buildings = response.data;
				console.log("Delete the RATE");
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Special rate deleted successfully!'
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						$state.go("dashboard.viewLevels");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
				//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
				$state.go("dashboard.viewAllRates");

			},function(response){
				alert("DID NOT DELETE");
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)
		}

	};	
}])


//VIEW RENTS, UPDATE RENT
app.controller('rentController', ['$scope',  '$timeout','$http','shareData','$state','ModalService', function ($scope,  $timeout,$http ,shareData,$state,ModalService) {

	angular.element(document).ready(function () {
		$scope.units =[];
		$scope.allUnits=[];
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/property/getAllUnits").then(function(response){
			$scope.units = response.data;
			$scope.allUnits= response.data;
			console.log("DISPLAY ALL UNITS OF CLIENT");
			console.log(angular.fromJson(response.data));


		},function(response){
			alert("DID NOT VIEW UNITS");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)
		console.log($scope.units);
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
				console.log($scope.units);
			});
			getUnits.error(function(){
				alert('Get units error!!!!!!!!!!');
			});		

			$scope.currentlySelectedUnit;
			$scope.selectUnit = function(){

				var duplicate = false;
				var index = 0;
				angular.forEach($scope.selectedUnits, function() {
					if(duplicate==false&&$scope.currentlySelectedUnit.id == $scope.selectedUnits[index].id)
						duplicate = true;
					else
						index = index + 1;
				});
				console.log(duplicate);
				if(!duplicate){
					$scope.selectedUnits.push($scope.currentlySelectedUnit);
				}



				console.log("Hailing test:");
				console.log($scope.currentlySelectedUnit);
				console.log($scope.selectedUnits);
			}

			$scope.deleteUnit = function(unit){
				var index = $scope.selectedUnits.indexOf(unit);
				$scope.selectedUnits.splice(index, 1);  
			}
			console.log("finish selecting units");	

			$scope.checkAvail = function(){
				console.log("start checking availability");
				$scope.data = {};

				var dataObj = {
						units: $scope.selectedUnits,
						event_start_date: ($scope.event.event_start_date).toString(),
						event_end_date: ($scope.event.event_end_date).toString(),
				};
				console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
				var send = $http({
					method  : 'POST',
					url     : 'https://localhost:8443/event/checkAvailability',
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
		}
	});
	$scope.viewAllUnits=function(){
		$scope.units=$scope.allUnits;
	}
	$scope.save = function(unit){
		unit.editable=false;
		if(confirm('CONFIRM TO EDIT RENT OF '+unit.unitNumber+'?')){

			var tempObj ={unit:unit};

			$http.post("//localhost:8443/property/updateRent", JSON.stringify(tempObj)).then(function(response){
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Rent updated successfully!'
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						$state.go("dashboard.viewLevels");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};

			},function(response){
				ModalService.showModal({

					templateUrl: "views/errorMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "DID NOT UPDATE RENT OF UNIT "+unit.unitNumber
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						$state.go("dashboard.viewLevels");
					});
				});
				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};

			}	
			)
		}

	};	

	$scope.edit = function(unit){
		unit.editable=true;

	};	
}])


