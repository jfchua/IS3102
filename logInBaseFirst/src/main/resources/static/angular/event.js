app.controller('eventController', ['$scope', '$http','$state','$routeParams','shareData','ModalService','$filter', function ($scope, $http,$state, $routeParams, shareData,ModalService,$filter) {
	$scope.eventFilter={
			event:{
				approvalStatus:"SUCCESSFUL"
			}
	};
	angular.element(document).ready(function () {
		console.log("test succes");
		console.log($scope.events);
		$scope.data = {};

		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/eventManager/viewAllEvents").then(function(response){
			$scope.events = response.data;
			$scope.order_item = "id";
			$scope.order_reverse = false;
			console.log($scope.events);
			console.log("DISPLAY ALL EVENT fir event manager");
			
			$scope.currentPage = 1;
			var eventsSorted
			//SORT EVENTS BY ID
			var order = function () {
				eventsSorted = $filter('orderBy')($scope.events, 'id');
			}
			order();
			//SET PAGINATION TO BE 10 PER PAGE
			$scope.changePage=function(currentPage){
				var begin = (($scope.currentPage - 1) * 10)
				var  end = begin + 10;
				$scope.displayEvents= eventsSorted.slice(begin, end);
			}
			$scope.changePage(1);
			
			$scope.eventsS=[];
			$scope.eventsC=[];
			$scope.eventsA=[];
			$scope.eventsP=[];
			//GET SUCCESS EVENTS
			 angular.forEach($scope.events, function(event){   
					if(event.approvalStatus==="SUCCESSFUL")
					 $scope.eventsS.push(event);
					else if(event.approvalStatus==="CANCELLED")
						$scope.eventsC.push(event);
					else if(event.approvalStatus==="APPROVED")
						$scope.eventsA.push(event);
					else if(event.approvalStatus==="PROCESSING")
						$scope.eventsP.push(event);
					else
						console.log("EVENT "+event.event_title+" HAS UNKNOWN STATUS" );
				})
				
				
			//SET PAGE FOR SUCCESS EVENTS
				$scope.currentPageSuccess = 1;
				var eventsSortedS
				//SORT EVENTS BY ID
				order = function () {
					eventsSortedS = $filter('orderBy')($scope.eventsS, 'id');
				}
				order();
				//SET PAGINATION TO BE 10 PER PAGE
				$scope.changePageSuccess=function(currentPageS){
					var begin = ((currentPageS - 1) * 10)
					var  end = begin + 10;
					$scope.eventsSuccess= eventsSortedS.slice(begin, end);
				}
				$scope.changePageSuccess(1);
				
				
			//SET PAGE FOR CANCELLED EVENTS
				$scope.currentPageCancel = 1;
				var eventsSortedC
				//SORT EVENTS BY ID
				order = function () {
					eventsSortedC = $filter('orderBy')($scope.eventsC, 'id');
				}
				order();
				//SET PAGINATION TO BE 10 PER PAGE
				$scope.changePageCancel=function(currentPageC){
					var begin = ((currentPageC - 1) * 10)
					var  end = begin + 10;
					$scope.eventsCancelled= eventsSortedC.slice(begin, end);
				}
				$scope.changePageCancel(1);
				
				
				
			//SET PAGE FOR APPROVED EVENTS
				$scope.currentPageApproved = 1;
				var eventsSortedA
				//SORT EVENTS BY ID
				order = function () {
					eventsSortedA = $filter('orderBy')($scope.eventsA, 'id');
				}
				order();
				//SET PAGINATION TO BE 10 PER PAGE
				$scope.changePageApproved=function(currentPageA){
					var begin = ((currentPageA - 1) * 10)
					var  end = begin + 10;
					$scope.eventsApproved= eventsSortedA.slice(begin, end);
				}
				$scope.changePageApproved(1);
				
				//SET PAGE FOR PROCESSING EVENTS
				$scope.currentPageProcess = 1;
				var eventsSortedP
				//SORT EVENTS BY ID
				order = function () {
					eventsSortedP = $filter('orderBy')($scope.eventsP, 'id');
				}
				order();
				//SET PAGINATION TO BE 10 PER PAGE
				$scope.changePageProcess=function(currentPageP){
					var begin = ((currentPageP - 1) * 10)
					var  end = begin + 10;
					$scope.eventsProcess= eventsSortedP.slice(begin, end);
				}
				$scope.changePageProcess(1);
		},function(response){
			console.log(response);
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
		
	});
	


	$scope.viewEvents = function(){
		$scope.data = {};

		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/eventManager/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT fir event manager");
			//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.buildings);

		},function(response){
			console.log(response);
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}
	$scope.passEvent = function(event){
		shareData.addData(event);
	}
	$scope.viewApprovedEvents = function(){
		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			console.log(response);
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
		$scope.url = "https://localhost:8443/eventManager/getEvent/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENT INFO")
		var getEvent = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/getEvent/' + id        
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
			//$state.go("dashboard.viewAllEvents");
			//alert("Error, " + response);
			console.log('GET Event FAILED! ' + JSON.stringify(response));
		});			
	}


	$scope.getEventById= function(){			
		$scope.event1 = JSON.parse(shareData.getData());
		$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
		$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
		var dataObj = {			
				event_title: $scope.event1.event_title,
				event_content: $scope.event1.event_content,
				event_description: $scope.event1.event_description,
				event_approval_status: $scope.event1.approvalStatus,						
				event_start_date: $scope.event1.event_start_date,				
				event_end_date: $scope.event1.event_end_date,
				//event_period: $scope.event1.event_period,
				filePath: $scope.event1.filePath,
		};
		$scope.event = angular.copy($scope.event1)

		var url = "https://localhost:8443/eventManager/updateEventStatus";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}

	$scope.getEventDetailsById= function(){			
		$scope.event1 = JSON.parse(shareData.getData());
		$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
		$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
		var dataObj = {			
				event_title: $scope.event1.event_title,
				event_content: $scope.event1.event_content,
				event_description: $scope.event1.event_description,
				event_approval_status: $scope.event1.approvalStatus,						
				event_start_date: $scope.event1.event_start_date,				
				event_end_date: $scope.event1.event_end_date,
				//event_period: $scope.event1.event_period,
				filePath: $scope.event1.filePath,
		};
		$scope.event = angular.copy($scope.event1)

		var url = "https://localhost:8443/eventManager/viewEventDetails";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
	}










	$scope.getEventByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.event = JSON.parse(shareData.getData());

		var url = "https://localhost:8443/eventManager/deleteEvent";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event);
	}


	$scope.getNotifications = function(id){		
		$scope.dataToShare = [];	  
		console.log(id);
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/eventManager/getNotifications/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE NOTIFICATIONS")
		var getNotifications = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/eventManager/getNotifications/' + id        
		});
		console.log("Getting the event organizer using the url: " + $scope.url);
		getNotifications.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET NOTIFCATIONS SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			shareData.addData(JSON.stringify(response));
			//$location.path("/viewLevels");
		});
		getNotifications.error(function(response){
			$state.go("dashboard.viewEventOrganizers");
			console.log('GET NOTIFICATIONS FAILED! ' + JSON.stringify(response));
		});

	}


	$scope.complexResult = null;
	$scope.showAModal = function(event) {
		console.log(event);
		// Just provide a template url, a controller and call 'showModal'.
		ModalService.showModal({

			templateUrl: "views/updateEventStatusTemplate.html",
			controller: "updateEventStatusController",
			inputs: {
				title: "Update Event Status",
				event:event
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				$scope.event  = result.event;
				$scope.updateEventStatus();
			});
		});

	};

	$scope.dismissModal = function(result) {
		close(result, 200); // close, but give 200ms for bootstrap to animate
	};

	$scope.updateEventStatus = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var dataObj = {				
				id: $scope.event.id,
				event_approval_status: $scope.event.approvalStatus,
		};		
		console.log(dataObj.event_approval_status);
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/eventManager/updateEventStatus',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE EVENT");
		send.success(function(){
			//alert('Successfully saved event status, going back to viewing all approved events');

			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Event status is saved successfully. Going back to view events'
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.reload();
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};

		});
		send.error(function(data){
			//alert(data);
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

app.controller('successfulEventController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	$scope.eventFilter={
			event:{
				approvalStatus:"SUCCESSFUL"
			}
	};
	angular.element(document).ready(function () {
		$scope.data = {};

		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/eventManager/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT fir event manager");
			//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.buildings);
			
		},function(response){
			console.log(response);
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	

	});
	$scope.passEvent = function(event){
		shareData.addData(event);
	}
}]);
app.controller('canceledEventController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	$scope.eventFilter={
			event:{
				approvalStatus:"CANCELLED"
			}
	};
	angular.element(document).ready(function () {
		$scope.data = {};

		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/eventManager/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT fir event manager");
			//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.buildings);

		},function(response){
			console.log(response);
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	

	});
	$scope.passEvent = function(event){
		shareData.addData(event);
	}
}]);
//DELETE EVENT
app.controller('deleteEventController', ['$scope',  '$timeout','$http','shareData','$state','ModalService', function ($scope,  $timeout,$http ,shareData,$state,ModalService) {
	angular.element(document).ready(function () {

		$scope.event = shareData.getData();
	});
	$scope.deleteEvent = function(){

	    ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: "Do you wish to delete " +$scope.event.event_title+'?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					$scope.data = {};
					console.log("Start deleting event");
					//$scope.event = JSON.parse(shareData.getData());
					console.log($scope.event.id);
					var tempObj ={eventId:$scope.event.id};
					console.log("fetch id "+ tempObj);
					$http.post("//localhost:8443/eventManager/deleteEvent", JSON.stringify(tempObj)).then(function(response){
					
						console.log("Cancel the EVENT");
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Event successfully cancelled',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("dashboard.viewAllEvents");
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
								message: response.data,
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("dashboard.viewAllEvents");
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

	}

}])

//VIEW APPROVED EVENTS
app.controller('viewApprovedEventController', ['$scope', '$http','$state','$routeParams','shareData', 'ModalService','$filter',function ($scope, $http,$state, $routeParams, shareData,ModalService,$filter) {
	angular.element(document).ready(function () {
		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);
			$scope.currentPage = 1;
			var eventsSorted
			//SORT EVENTS BY ID
			$scope.order = function () {
				eventsSorted = $filter('orderBy')($scope.events, 'id');
			}
			$scope.order();
			//SET PAGINATION TO BE 10 PER PAGE
			$scope.changePage=function(currentPage){
				var begin = (($scope.currentPage - 1) * 10)
				var  end = begin + 10;
				$scope.displayEvents= eventsSorted.slice(begin, end);
			}
			$scope.changePage(1);
		},function(response){
			console.log("did not view approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	


	});


	$scope.passEvent = function(event){
		shareData.addData(event);
	}
	$scope.complexResult = null;
	$scope.showAModal = function(event) {
		console.log(event);
		// Just provide a template url, a controller and call 'showModal'.
		ModalService.showModal({

			templateUrl: "views/updateEventStatusTemplate.html",
			controller: "updateEventStatusController",
			inputs: {
				title: "Update Event Status",
				event:event
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				$scope.event  = result.event;
				$scope.updateEventStatus();
			});
		});

	};

	$scope.dismissModal = function(result) {
		close(result, 200); // close, but give 200ms for bootstrap to animate
	};

	$scope.updateEventStatus = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var dataObj = {				
				id: $scope.event.id,
				event_approval_status: $scope.event.approvalStatus,
		};		
		console.log(dataObj.event_approval_status);
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/eventManager/updateEventStatus',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE EVENT");
		send.success(function(){
			//alert('Successfully saved event status, going back to viewing all approved events');

			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Event status is saved successfully. Going back to view events'
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.reload();
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		send.error(function(data){
			//alert(data);
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

app.filter('successfulEventsFilter', [function($filter) {
	return function(inputArray, searchCriteria, txnStatus){         
		if(!angular.isDefined(searchCriteria) || searchCriteria == ''){
			return inputArray;
		}         
		var data=[];
		angular.forEach(inputArray, function(item){             
			if(event.approvalStatus == txnStatus){	
				data.push(item);
			}
		});      
		return data;
	};
}]);
//VIEW TO BE APPROVED EVENTS
app.controller('viewToBeApprovedEventController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {


		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewToBeApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			console.log("did not view to-be-approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	


	});


	$scope.passEvent = function(event){
		shareData.addData(event);
	}

}]);

//VIEW EVENT DETAILS OF TO BE APPROVED EVENTS / APPROVED EVENTS AND UPDATE STATUS
app.controller('viewEventDetailsController', ['$scope', '$http','$state','$routeParams','shareData','ModalService',function ($scope, $http,$state, $routeParams, shareData,ModalService) {
	angular.element(document).ready(function () {



		$scope.event1 = shareData.getData();
		$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
		$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
		var dataObj = {			
				event_title: $scope.event1.event_title,
				event_content: $scope.event1.event_content,
				event_description: $scope.event1.event_description,
				event_approval_status: $scope.event1.approvalStatus,						
				event_start_date: $scope.event1.event_start_date,				
				event_end_date: $scope.event1.event_end_date,
				//event_period: $scope.event1.event_period,
				filePath: $scope.event1.filePath,
		};
		$scope.event = angular.copy($scope.event1)

	//	var url = "https://localhost:8443/eventManager/viewEventDetails";
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);



	});

	$scope.approveEvent = function(){
		$scope.data = {};
		console.log("Start approving event");

		var tempObj ={eventId:$scope.event.id};
		console.log("fetch id "+ tempObj);
		$http.post("//localhost:8443/eventManager/approveEvent", JSON.stringify(tempObj)).then(function(response){
			console.log("Approve the EVENT");
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Successfully approved event, going back to view events'
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("dashboard.viewAllEvents");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};

			//alert("Successfully approved event, going back to viewing to be approved events");
			
		},function(response){
			console.log(response);
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}
	/*
	$scope.updateEventStatus = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var dataObj = {				
				id: $scope.event.id,
				event_approval_status: $scope.event.approvalStatus,
		};		
		console.log(dataObj.event_approval_status);
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/eventManager/updateEventStatus',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE EVENT");
		send.success(function(){
			alert('Successfully saved event status, going back to viewing all approved events');
			$state.go("dashboard.viewAllEvents");
		});
		send.error(function(data){
			alert(data);
		});
	};
	*/
}]);


//UPDATE EVENT STATUS MODAL
app.controller('updateEventStatusController', ['$scope', '$element', 'title', 'close', 'event',
                                               function($scope, $element, title, close,event) {

	//UPDATE MODAL

	$scope.title = title;
	$scope.event=event;
	console.log(title);
	console.log(close);
	console.log($element);
	//  This close function doesn't need to use jQuery or bootstrap, because
	//  the button has the 'data-dismiss' attribute.
	$scope.close = function() {
		close({
			event:$scope.event
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
//VIEW EVENT ORGANISERS
app.controller('viewEventOrganiserController', ['$scope',  '$timeout','$http','shareData','$state', function ($scope,  $timeout,$http ,shareData,$state) {
	angular.element(document).ready(function () {

		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewEventOrganizers").then(function(response){
			$scope.eventOrgs = response.data;
			console.log("DISPLAY ALL EVENT ORGANIZERS");
			console.log("EVENT ORGS DATA ARE OF THE FOLLOWING: " + $scope.eventOrgs);

		},function(response){
			console.log("did not view eventOrgs");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	

	});
	$scope.passEventOrg = function(eventOrg){
		shareData.addData(eventOrg);
	}

}])

//VIEW NOTIFICATIONS
app.controller('notificationController', ['$scope', '$http','shareData','$state', function ($scope, $http, shareData,$state) {		

	angular.element(document).ready(function () {

		$scope.eventOrg = shareData.getData(); //gets the response data from building controller 
		console.log($scope.eventOrg);
		var id=$scope.eventOrg.id;
		$scope.url = "https://localhost:8443/eventManager/getNotifications/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE NOTIFICATIONS")
		var getNotifications = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/eventManager/getNotifications/' + id        
		});
		console.log("Getting the event organizer using the url: " + $scope.url);
		getNotifications.success(function(response){
			//$scope.dataToShare.push(id);
			//$location.path("/viewLevels/"+id);
			console.log('GET NOTIFCATIONS SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			$scope.notifications=response;
			//$location.path("/viewLevels");
		});
		getNotifications.error(function(response){
			$state.go("dashboard.viewEventOrganizers");
			console.log('GET NOTIFICATIONS FAILED! ' + JSON.stringify(response));
		});		
	})
}]);

app.controller('ticketSalesController', ['$scope', '$http','$state','$routeParams','shareData', 'ModalService',function ($scope, $http,$state, $routeParams, shareData,ModalService) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/payment/viewAllEventsWithTicket").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENTS");
		},function(response){
			console.log("did not view events");
		}	
		)	
	});
	$scope.passEvent = function(id){
		shareData.addData(id);
	}
}]);



app.controller('ticketSaleDetailsController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	$scope.payment={};
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.eventId = shareData.getData();
		$scope.url = "https://localhost:8443/eventManager/getEvent/"+$scope.eventId;
		//$scope.dataToShare = [];
		console.log("GETTING THE Event");
		var getEvent = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/eventManager/getEvent/' + $scope.eventId,

		});
		console.log("Getting the event using the url: " + $scope.url);
		getEvent.success(function(response){
			console.log('GET PAYMENT PLAN SUCCESS! ');
			console.log(response);
			$scope.event = response;
		});
		getEvent.error(function(response){
			$state.go("dashboard.viewTicketSales");
			console.log('GET PAYMENT FAILED! ');
		});


		$scope.order_item = "cat";
		$scope.order_reverse = false;
		$scope.url1 = "https://localhost:8443/eventManager/getTicketSales/"+$scope.eventId;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENTS");
		var getSales = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/eventManager/getTicketSales/' + $scope.eventId,

		});
		console.log("Getting the events using the url: " + $scope.url1);
		getSales.success(function(response){
			console.log('GET EVENTS SUCCESS! ');
			console.log(JSON.stringify(response));
			console.log(response);
			$scope.sales = response;
		});
		getSales.error(function(response){
			$state.go("dashboard.viewTicketSales");
			console.log('GET EVENTS FAILED! ');
		});


	});

}]);
