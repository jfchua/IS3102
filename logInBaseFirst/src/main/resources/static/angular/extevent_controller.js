//VIEW ALL EVENTS,
app.controller('eventExternalController', ['$scope', '$rootScope', '$http','$state','$routeParams','shareData', 'ModalService', function ($scope, $rootScope,$http,$state, $routeParams, shareData,ModalService) {


	angular.element(document).ready(function () {

		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)

		$http.get("//localhost:8443/event/viewAllEvents").then(function(response){
			$scope.order_item = "id";
			$scope.order_reverse = false;
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log($scope.events);

		},function(response){
			console.log("did not view all events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)
		$scope.IsFinanceSub = function(){
			var subz = sessionStorage.getItem('subscriptions');
			return (subz.indexOf("FINANCE") > -1);
		}

		$scope.checkDateBefore = function (dateString) {
			var daysAgo = new Date();
			//console.log("***");
			//console.log(new Date(dateString) < daysAgo);
			return (new Date(dateString) < daysAgo);
		}

		$scope.IsTicketingSub = function(){
			var subz = sessionStorage.getItem('subscriptions');
			return (subz.indexOf("TICKETING") > -1);
		}
		$scope.checkStatus = function (event) {
			var isTrue = true;
			//console.log("****");
			//console.log(event.approvalStatus == "CANCELLED");
			if(event.approvalStatus == "CANCELLED")
				isTrue = false;
			return isTrue;
		}


	});


	$scope.getEvent = function(event){	
		shareData.addData(event);
	}
	$scope.passEvent = function(event){
		shareData.addData(event);
		console.log(event);
	}
	$scope.passEventToTix = function(event){
		$rootScope.event = event;
		console.log("PASSING" + $rootScope.event);
		$state.go("IFMS.configureTicketsEx");
	}
	$scope.passEventToViewTix = function(id){
		shareData.addData(id);
	}



	$scope.requestForTicketSales = function(event){
		ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: 'Confirm ticket sales for '+event.event_title+'?' + " This action cannot be undone"
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					var tempObj ={eventId: event.id};
					console.log("fetch id ");
					console.log(tempObj);
					//var buildings ={name: $scope.name, address: $scope.address};
					$http.post("//localhost:8443/event/requestTickets", JSON.stringify(tempObj)).then(function(response){
						//$scope.buildings = response.data;
						console.log("REQUEST FOR TICKET SALES");
						$scope.requestedTicket = true;
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: "Ticket sales set successfully",
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go($state.current, {}, {reload: true}); 
							});
						});

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))

					},function(response){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: "Did not request ticket sales. Check if the event has been approved",
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





	$scope.getEventByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.event = shareData.getData();

		var url = "https://localhost:8443/event/deleteEvent";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event);
	}

	/*
	$scope.getBookings = function(id){	
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
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
			//$state.go("dashboard.viewAllEventsEx");
			console.log('GET Booking FAILED! ' + JSON.stringify(response));
		});

	}*/
}]);
//VIEW ALL APPROVED EVENTS
app.controller('viewApprovedEventsExController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {

	angular.element(document).ready(function () {
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		console.log("before view approved events");
		$http.get("//localhost:8443/event/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			console.log("did not view approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	});
	$scope.IsFinanceSub = function(){
		var subz = sessionStorage.getItem('subscriptions');
		return (subz.indexOf("FINANCE") > -1);
	}

	$scope.getEvent = function(event){	

		shareData.addData(event);


	}
	$scope.passEvent = function(event){
		shareData.addData(event);
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





	$scope.getEventByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.event = shareData.getData();

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
			$state.go("IFMS.viewAllEventsEx");
			console.log('GET Booking FAILED! ' + JSON.stringify(response));
		});

	}



}]);



//VIEW ALL APPROVED EVENTS
app.controller('viewToBeApprovedEventsExController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {

	angular.element(document).ready(function () {
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		$http.get("//localhost:8443/event/viewToBeApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			console.log("did not view to be approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	});

	$scope.IsFinanceSub = function(){
		var subz = sessionStorage.getItem('subscriptions');
		return (subz.indexOf("FINANCE") > -1);
	}

	$scope.getEvent = function(event){	

		shareData.addData(event);


	}
	$scope.passEvent = function(event){
		shareData.addData(event);
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





	$scope.getEventByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.event = shareData.getData();

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
			$state.go("IFMS.viewAllEventsEx");
			console.log('GET Booking FAILED! ' + JSON.stringify(response));
		});

	}



}]);




//DELETE EVENT
app.controller('deleteEventExController', ['$scope',  '$timeout','$http','shareData','$state','ModalService', function ($scope,  $timeout,$http ,shareData,$state,ModalService) {
	angular.element(document).ready(function () {

		$scope.event = shareData.getData();
	});
	$scope.deleteEvent = function(){
		if(confirm('CONFIRM TO DELETE EVENT '+$scope.event.event_title+'?')){

			var tempObj ={eventId:$scope.event.id};
			console.log("fetch id "+ tempObj);
			//var buildings ={name: $scope.name, address: $scope.address};
			$http.post("//localhost:8443/event/deleteEvent", JSON.stringify(tempObj)).then(function(response){
				//$scope.buildings = response.data;
				console.log("Cancel the EVENT");
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "Event successfully deleted. Going back to view events",
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						$state.go("IFMS.viewAllEventsEx");
					});
				});

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};


			},function(response){
				console.log("DID NOT CANCEL EVENT");
			}	
			)
		}


	}

}])

app.controller('addEController', ['$scope', '$http','$state','$routeParams','shareData','ModalService','Upload','$timeout', function ($scope, $http,$state, $routeParams, shareData,ModalService,Upload,$timeout){

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
					$scope.event.event_end_date = '';
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
					message: "Event start date should not be before today",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.event.event_start_date = '';
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
		console.log('Get building error!!!!!!!!!!');
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
			console.log('Get levels error!!!!!!!!!!');
		});	
		$scope.currentlySelectedLevel;
		$scope.selectLevel = function(){
			$scope.selectedLevel=$scope.currentlySelectedLevel;
		}
		console.log("finish selecting level");	
	}

	$scope.selectedUnits=[];
	$scope.getUnit = function(levelId){

		$scope.levelID = levelId; 
		var dataObj = {id: $scope.levelID};
		console.log("GETTING THE ALL UNITS INFO")
		var getUnits = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/property/viewUnitsWithBookings/',
			data    : dataObj,
		});
		console.log("REACHED HERE FOR SUBMIT LEVEL " + JSON.stringify(dataObj));
		getUnits.success(function(response){
			$scope.units = response;
			console.log("RESPONSE IS" + JSON.stringify(response));
			console.log($scope.units);
		});
		getUnits.error(function(){
			console.log('Get units error!!!!!!!!!!');
		});		

		$scope.currentlySelectedUnit;
		$scope.selectUnit = function(){
			//ADD EVENTS AND MAINTS OF CURRENTLY SELECT UNIT
			$scope.haha.length=0;
			console.log("currently selected unit bookings");
			console.log($scope.currentlySelectedUnit.bookings);

			getEvents($scope.currentlySelectedUnit.bookings);
			console.log("currently selected unit schedules");
			console.log($scope.currentlySelectedUnit.schedule);
			getMaints($scope.currentlySelectedUnit.schedule);//put here or after for loop
			//CHECK AND PREVENT DOUBLE SELECTION OF THE SAME UNIT
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


	}

	$scope.checkAvail = function(){
		$scope.avail = "";
		console.log("start checking availability");
		$scope.data = {};	
		if ( !$scope.event || !$scope.event.event_start_date || !$scope.event.event_end_date||$scope.selectedUnits.length == 0 ){
			$scope.avail = "";
		}
		else{
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



	$scope.checkRent = function(){
		$scope.components={};
		$scope.totalRent = 0.00;
		$scope.totalRentAfter = 0.00;
		console.log("start checking rent");
		$scope.data = {};
		if ( !$scope.event || !$scope.event.event_start_date || !$scope.event.event_end_date ||$scope.selectedUnits.length == 0 ){
			$scope.components={};
			$scope.totalRent = 0.00;
			$scope.totalRentAfter = 0.00;
		}
		else{
			var dataObj = {
					units: $scope.selectedUnits,
					event_start_date: ($scope.event.event_start_date).toString(),
					event_end_date: ($scope.event.event_end_date).toString(),
			};
			console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));

			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/event/checkComponents',
				data    : dataObj //forms user object
			});
			send.success(function(response){
				$scope.components = response;
				$scope.order_item = "num";
				$scope.order_reverse = false;
				console.log($scope.components);
				console.log("OOOOO");
			});
			send.error(function(response){
				//alert("get component failure");
			});

			var send1 = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/event/checkRent',
				data    : dataObj //forms user object
			});
			send1.success(function(response){
				$scope.totalRent = response;
				//$scope.components = response.data;
				$scope.totalRentAfter = response*1.07;
				console.log($scope.totalRent);
			});
			send1.error(function(response){
				$scope.totalRent = response;
				$scope.totalRentAfter = response*1.07;
				console.log($scope.totalRent);
			});	
		}
	}

	$scope.eventTypes=[{'name':'Concert','eventType':'CONCERT'},
	                   {'name':'Conference','eventType':'CONFERENCE'},
	                   {'name':'Fair','eventType':'FAIR'},
	                   {'name':'Family Entertainment','eventType':'FAMILY'},
	                   {'name':'Lifestyle/Leisure','eventType':'LIFESTYLE'},
	                   {'name':'Seminar/Workshop','eventType':'SEMINAR'}];
	$scope.eventType=$scope.eventTypes[0].eventType;

	$scope.addEvent = function(){

		console.log("start adding");
		$scope.data = {};

		if ( $scope.selectedUnits == null || $scope.event.event_title == null || $scope.event.event_description == null || $scope.eventType == null ||
				$scope.event.event_start_date == null || $scope.event.event_end_date == null){
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


		var dataObj = {
				units: $scope.selectedUnits,
				event_title: $scope.event.event_title,
				event_content: $scope.eventType,
				event_description: $scope.event.event_description,
				event_approval_status: "PROCESSING",
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
		send.success(function(eventId){
			console.log("eventId "+eventId);
			if ($scope.picFile != null && $scope.picFile != "") {

				$scope.picFile.upload = Upload.upload({
					url: 'https://localhost:8443/event/saveEventImage',
					data: { file: $scope.picFile,eventId:eventId},
				});

				$scope.picFile.upload.then(function (response) {
					$timeout(function () {
						$scope.picFile.result = response.data;
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Event and its associated image has been updated successfully',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("IFMS.viewAllEventsEx");
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
					message: "Event saved successfully",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewAllEventsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};


		});
		send.error(function(){
			console.log('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
		});
	};
	$scope.uiConfig = {
			calendar:{
				width: 300,
				editable:false,
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


	$scope.haha=[];
	//RETRIEVE EVENTS
	//$scope.eventsFormated=[];
	var getEvents = function(bookings){
		//need to changed to same as workspace calendar view all events with status success approved,processing	
		var index=0;
		angular.forEach(bookings, function() {

			var booking=[{start: bookings[index].event_start_date_time,
				end: bookings[index].event_end_date_time,	         
				title:'Booked',
				allDay: false,
				color: 'IndianRed',
				overlap:false
			}];

			$scope.haha.push(booking);
			index = index + 1;
		});	




	}
	// getEvents(); 


	//RETRIEVE MAINTENANCES
	//$scope.eventsFormated=[];
	var getMaints = function(schedules){
		var index=0;
		angular.forEach(schedules, function() {

			var maint=[{start: schedules[index].start_time,
				end: schedules[index].end_time,	         
				title:"Maintenance",
				allDay: false,
				color: 'SteelBlue'
			}];

			$scope.haha.push(maint);
			index = index + 1;
		});
		//var buildings ={name: $scope.name, address: $scope.address};
		console.log( $scope.haha);


	}



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


app.controller('updateEController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService){
	$scope.selectedBookingsUnits = {};
	angular.element(document).ready(function () {
		//VIEW EVENT
		$scope.event1 = shareData.getData();
		$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
		$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
		var dataObj = {	
				units:$scope.event1.units,
				event_title: $scope.event1.event_title,
				event_type: $scope.event1.event_type,
				event_description: $scope.event1.event_description,
				event_approval_status: $scope.event1.approvalStatus,	
				event_start_date: $scope.event1.event_start_date,	
				event_end_date: $scope.event1.event_end_date,
				filePath: $scope.event1.filePath,
		};

		$scope.event = angular.copy($scope.event1);
		//$scope.event.eventType = $scope.event1.eventType
		var url = "https://localhost:8443/event/updateEvent";
		console.log("***********************");
		console.log($scope.event.eventType);
		//GET SELECTED UNITS
		var id=$scope.event.id;

		$scope.url = "https://localhost:8443/booking/viewAllSelectedUnits/"+id;
		console.log("GETTING THE EVENT INFO")
		var getBookings = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/booking/viewAllSelectedUnits/' + id        
		});
		console.log("Getting the bookings Units using the url: " + $scope.url);
		getBookings.success(function(response){
			console.log('GET Selected Units SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			console.log("test hailing");
			console.log(response);
			$scope.selectedBookingsUnits1 = response;
			$scope.selectedBookingsUnits=angular.copy($scope.selectedBookingsUnits1);

		});
		getBookings.error(function(response){
			$state.go("IFMS.viewAllEventsEx");
			console.log('GET Selected Units FAILED! ' + JSON.stringify(response));
		});	


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
			console.log('Get building error!!!!!!!!!!');
		});
		$scope.currentlySelectedBuilding;
		$scope.selectBuild = function(){
			$scope.selectedBuilding=$scope.currentlySelectedBuilding;
		}
		console.log("finish selecting building");

	});
	//RESET SELECTED BOOKINGS UNITS TO LAST SAVED EVENT
	$scope.resetBookings = function(){
		$scope.event = angular.copy($scope.event1);
		$scope.selectedBookingsUnits=angular.copy($scope.selectedBookingsUnits1);
	}
	//DELETE BOOKING UNIT FROM SELECTED BOOKING  UNIT
	$scope.deleteBookingUnit = function(unit){
		var index = $scope.selectedBookingsUnits.indexOf(unit);
		$scope.selectedBookingsUnits.splice(index, 1);  
	}

	$scope.getLevel = function(id){
		$scope.dataToShare = [];	
		$scope.url = "https://localhost:8443/level/viewLevels/"+id;
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
			console.log('Get levels error!!!!!!!!!!');
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
			url     : 'https://localhost:8443/property/viewUnitsWithBookings/',
			data    : dataObj,
		});
		console.log("REACHED HERE FOR SUBMIT LEVEL " + JSON.stringify(dataObj));
		getUnits.success(function(response){
			$scope.units = response;
			console.log("RESPONSE IS" + JSON.stringify(response));

			console.log('Units Gotten');
		});
		getUnits.error(function(){
			console.log('Get units error!!!!!!!!!!');
		});	

		$scope.currentlySelectedUnit;
		$scope.selectUnit = function(){
			$scope.haha.length=0;
			getEvents($scope.currentlySelectedUnit.bookings);
			console.log("currently selected unit schedules");
			console.log($scope.currentlySelectedUnit.schedule);
			getMaints($scope.currentlySelectedUnit.schedule);//put here or after for loop
			var duplicate = false;
			var index = 0;
			angular.forEach($scope.selectedBookingsUnits, function() {
				if(duplicate==false&&$scope.currentlySelectedUnit.id == $scope.selectedBookingsUnits[index].id)
					duplicate = true;
				else
					index = index + 1;
			});
			console.log(duplicate);
			if(!duplicate){
				$scope.selectedBookingsUnits.push($scope.currentlySelectedUnit);
			}

		}

		$scope.deleteUnit = function(unit){
			var index = $scope.selectedBookingsUnits.indexOf(unit);
			$scope.selectedBookingsUnits.splice(index, 1);  
		}
		console.log("finish selecting units");	
	}

	$scope.checkAvail = function(){
		$scope.avail = "";
		console.log("start checking availability");
		$scope.data = {};
		if ( !$scope.event || !$scope.event.event_start_date || !$scope.event.event_end_date ||$scope.selectedBookingsUnits.length == 0 ){
			$scope.avail = "";
		}
		else{
			var dataObj = {	
					eventId: $scope.event.id,
					units: $scope.selectedBookingsUnits,
					event_start_date: ($scope.event.event_start_date).toString(),
					event_end_date: ($scope.event.event_end_date).toString(),
			};
			//console.log($scope.event.id);
			//console.log($scope.event.event_start_date);
			console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/event/checkAvailabilityForUpdate',
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

	$scope.checkRent = function(){
		$scope.components={};
		$scope.totalRent = 0.00;
		$scope.totalRentAfter = 0.00;
		console.log("start checking rent");
		$scope.data = {};
		if ( !$scope.event || !$scope.event.event_start_date || !$scope.event.event_end_date ||$scope.selectedBookingsUnits.length == 0 ){
			$scope.components={};
			$scope.totalRent = 0.00;
			$scope.totalRentAfter = 0.00;
		}
		else{
			var dataObj = {
					units: $scope.selectedBookingsUnits,
					event_start_date: ($scope.event.event_start_date).toString(),
					event_end_date: ($scope.event.event_end_date).toString(),
			};
			console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/event/checkComponents',
				data    : dataObj //forms user object
			});
			//$scope.avail = "";	
			send.success(function(response){
				$scope.components = response;
				$scope.order_item = "num";
				$scope.order_reverse = false;
				console.log($scope.components);
				console.log("get component success");
			});
			send.error(function(response){
				console.log("get component failure");
			});


			var send1 = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/event/checkRent',
				data    : dataObj //forms user object
			});
			send1.success(function(response){
				$scope.totalRent = response;
				$scope.totalRentAfter = response*1.07;
				console.log($scope.totalRent);
			});
			send1.error(function(response){
				$scope.totalRent = response;
				$scope.totalRentAfter = response*1.07;
				console.log($scope.totalRent);
			});	
		}
	}


	$scope.eventTypes=[{'name':'Concert','eventType':'CONCERT'},
	                   {'name':'Conference','eventType':'CONFERENCE'},
	                   {'name':'Fair','eventType':'FAIR'},
	                   {'name':'Family Entertainment','eventType':'FAMILY'},
	                   {'name':'Lifestyle/Leisure','eventType':'LIFESTYLE'},
	                   {'name':'Seminar/Workshop','eventType':'SEMINAR'}];
	//$scope.eventType=$scope.eventTypes[0].eventType;

	$scope.updateEvent = function(){
		console.log("Start updating");
		var unitIdsString="";
		//var unitIdsObj=shareData.getData();
		//console.log(unitIdsObj);
		//var unitIdsObj = JSON.parse(shareData.getData());
		//unitIdsString+=unitIdsObj;
		//console.log("test hailing");
		//console.log(unitIdsString);
		$scope.data = {};
		console.log($scope.event.id);
		var dataObj = {	
				id: $scope.event.id,
				units: $scope.selectedBookingsUnits,		
				event_title: $scope.event.event_title,
				event_content: $scope.event.event_type,
				event_description: $scope.event.event_description,
				event_approval_status: "PROCESSING",
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Event saved successfully",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewAllEventsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};

		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Did not save event, unit is unavailable",
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

	$scope.uiConfig = {
			calendar:{
				width: 300,
				editable:false,
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



	$scope.haha=[];
	//RETRIEVE EVENTS
	//$scope.eventsFormated=[];
	var getEvents = function(bookings){
		//need to changed to same as workspace calendar view all events with status success approved,processing	
		var index=0;
		angular.forEach(bookings, function() {

			var booking=[{start: bookings[index].event_start_date_time,
				end: bookings[index].event_end_date_time,	         
				title:'Booked',
				allDay: false,
				color: 'IndianRed',
				overlap:false
			}];

			$scope.haha.push(booking);
			index = index + 1;
		});	




	}
	// getEvents(); 


	//RETRIEVE MAINTENANCES
	//$scope.eventsFormated=[];
	var getMaints = function(schedules){
		var index=0;
		angular.forEach(schedules, function() {

			var maint=[{start: schedules[index].start_time,
				end: schedules[index].end_time,	         
				title:"Maintenance",
				allDay: false,
				color: 'SteelBlue'
			}];

			$scope.haha.push(maint);
			index = index + 1;
		});
		//var buildings ={name: $scope.name, address: $scope.address};
		console.log( $scope.haha);


	}

}]);





app.controller('bookingController', ['$scope','$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService) {
	angular.element(document).ready(function () {	
		//console.log(tempObj)
		console.log("DISPLAY ALL BOOKINGS");
		$scope.event = shareData.getData();
		var id=$scope.event.id;

		$scope.bookings = {};
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
			$scope.bookings = response;

			//$location.path("/viewLevels");
		});
		getBookings.error(function(response){
			$state.go("IFMS.viewAllEventsEx");
			console.log('GET Booking FAILED! ' + JSON.stringify(response));
		});

		var url = "https://localhost:8443/booking/viewAllBookings";	
		console.log("BOOKING DATA ARE OF THE FOLLOWING: " + $scope.bookings);	
	});

	$scope.checkDateBefore = function (dateString) {
		var daysAgo = new Date();
		return (new Date(dateString) > daysAgo);
	}

	$scope.checkBookings = function () {
		//console.log("*************");
		//console.log($scope.bookings.length);
		return ($scope.bookings.length >1);
	}

	$scope.deleteBooking = function(id){

		ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: "Confirm deletion?",
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					$scope.url = "https://localhost:8443/booking/deleteBooking/"+id;
					var deleteBooking = $http({
						method  : 'POST',
						url     : 'https://localhost:8443/booking/deleteBooking/' + id        
					});
					deleteBooking.success(function(response){
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Booking successfully deleted',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								//console.log("OK");
								$state.go("IFMS.viewAllEventsEx");
							});
						});
						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							//console.log("in dissmiss");
						};
						//END SHOWMODAL

					});
					deleteBooking.error(function(response){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Error deleting booking',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								//console.log("OK");
								$state.go("IFMS.viewAllEventsEx");
							});
						});
						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							//console.log("in dissmiss");
						};
						//END SHOWMODAL
					});

				}
			});
		});

		$scope.dismissModal = function(result) {
			close(result, 200); // close, but give 200ms for bootstrap to animate

			//console.log("in dissmiss");
		};


	}

	$scope.passBooking=function(booking){
		console.log(booking);
		var obj={
				event:$scope.event,
				booking:booking
		};
		shareData.addData(obj);
	}
}])

app.controller('paymentHistoryExController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		//$scope.org = shareData.getData();
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$scope.url = "https://localhost:8443/event/getPaymentHistory/";
		console.log("GETTING THE PAYMENT HISTORY");
		var getPayments = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/getPaymentHistory/',

		});
		console.log("Getting the payments using the url: " + $scope.url);
		getPayments.success(function(response){
			console.log('GET PAYMENTS SUCCESS! ');
			console.log(JSON.stringify(response));
			console.log(response);
			$scope.payments = response;
			$scope.order_item = "id";
			$scope.order_reverse = false;
		});
		getPayments.error(function(response){
			$state.go("IFMS.viewPaymentPlansEx");
			console.log('GET PAYMENTS FAILED! ');
		});
	});
}]);


app.controller('paymentExController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		console.log("START:):):):)");
		$http.get("//localhost:8443/event/viewAllPayments").then(function(response){
			$scope.plans = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
			console.log($scope.plans);
		},function(response){
			console.log("did not view plans");
		}	
		)

		$http.get("//localhost:8443/event/getTotal").then(function(response){
			$scope.totalAmount = response.data;
			console.log("DISPLAY TOTAL BALANCE");
			console.log($scope.totalAmount);
		},function(response){
			console.log("did not view plans");
		}	
		)
	});
	$scope.paymentPlanNull = function(paymentPlan){
		return !(paymentPlan === null)
	}
	$scope.passPaymentPlan = function(plan){
		shareData.addData(plan);
	}

}]);

app.controller('paymentDetailsExController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};
		$scope.plan= shareData.getData();
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$scope.url = "https://localhost:8443/event/viewPaymentDetails/"+$scope.plan;
		console.log("GETTING THE EVENTS");
		var getPayments = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/viewPaymentDetails/' + $scope.plan,

		});
		console.log("Getting the payments using the url: " + $scope.url);
		getPayments.success(function(response){
			console.log('GET PAYMENTS SUCCESS! ');
			console.log(JSON.stringify(response));
			console.log(response);
			$scope.paymentPlan = response;
		});
		getPayments.error(function(response){
			$state.go("IFMS.viewPaymentPlansEx");
			console.log('GET PAYMENTS FAILED! ');
		});	
	});

}]);




app.controller('ticketSaleExController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	$scope.payment={};
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.eventId = shareData.getData();
		$scope.url = "https://localhost:8443/event/getEvent1/"+$scope.eventId;
		//$scope.dataToShare = [];
		console.log("GETTING THE Event");
		var getEvent = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/getEvent1/' + $scope.eventId,

		});
		console.log("Getting the event using the url: " + $scope.url);
		getEvent.success(function(response){
			console.log('GET PAYMENT PLAN SUCCESS! ');
			console.log(response);
			$scope.event = response;
		});
		getEvent.error(function(response){
			$state.go("IFMS.viewTicketSales");
			console.log('GET PAYMENT FAILED! ');
		});


		$scope.order_item = "cat";
		$scope.order_reverse = false;
		$scope.url1 = "https://localhost:8443/event/getTicketSales/"+$scope.eventId;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENTS");
		var getSales = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/getTicketSales/' + $scope.eventId,

		});
		console.log("Getting the events using the url: " + $scope.url1);
		getSales.success(function(response){
			console.log('GET EVENTS SUCCESS! ');
			console.log(JSON.stringify(response));
			console.log(response);
			$scope.sales = response;
		});
		getSales.error(function(response){
			$state.go("IFMS.viewAllEventsEx");
			console.log('GET EVENTS FAILED! ');
		});


	});

}]);

app.controller('feedbackController', ['$scope','$rootScope','$http','$state','shareData','ModalService', function ($scope, $rootScope, $http,$state, shareData,ModalService) {
	angular.element(document).ready(function () {
		$scope.event = shareData.getData();
		var dataObj={id: $scope.event.id};
		console.log(dataObj);
		console.log("!!!");
		$http.post("//localhost:8443/tixGetFeedback", JSON.stringify(dataObj)).then(function(response){
			$scope.feedbacks = response.data;

		},function(response){
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
					$state.go("IFMS.viewAllEventsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	})
}])



