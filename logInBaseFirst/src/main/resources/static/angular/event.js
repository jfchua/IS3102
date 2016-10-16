app.controller('eventController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};

		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/eventManager/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT fir event manager");
			//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.buildings);

		},function(response){
			alert(response);
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
			alert(response);
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
			alert(response);
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
			$location.path("/viewAllEvents");
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
				event_approval_status: $scope.event1.event_approval_status,						
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
				event_approval_status: $scope.event1.event_approval_status,						
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
			$location.path("/viewEventOrganizers");
			console.log('GET NOTIFICATIONS FAILED! ' + JSON.stringify(response));
		});
		
	}
	
	
	

}]);


//DELETE EVENT
app.controller('deleteEventController', ['$scope',  '$timeout','$http','shareData','$location', function ($scope,  $timeout,$http ,shareData,$location) {
	angular.element(document).ready(function () {

		$scope.event = shareData.getData();
	});
	$scope.deleteEvent = function(){
		
		if(confirm('CONFIRM TO DELETE EVENT '+$scope.event.event_title+'?')){
			$scope.data = {};
			console.log("Start deleting event");
			//$scope.event = JSON.parse(shareData.getData());
			console.log($scope.event.id);
			var tempObj ={eventId:$scope.event.id};
			console.log("fetch id "+ tempObj);
			$http.post("//localhost:8443/eventManager/deleteEvent", JSON.stringify(tempObj)).then(function(response){
				console.log("Cancel the EVENT");
			},function(response){
				alert(response);
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)
		}
	}

}])

//VIEW APPROVED EVENTS
app.controller('viewApprovedEventController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};
		$http.get("//localhost:8443/eventManager/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);
		},function(response){
			alert("did not view approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
		
		
	});

	
	$scope.passEvent = function(event){
		shareData.addData(event);
	}

}]);

//VIEW TO BE APPROVED EVENTS
app.controller('viewToBeApprovedEventController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	angular.element(document).ready(function () {
	
	
			$scope.data = {};
			$http.get("//localhost:8443/eventManager/viewToBeApprovedEvents").then(function(response){
				$scope.events = response.data;
				console.log("DISPLAY ALL EVENT");
				console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

			},function(response){
				alert("did not view to-be-approved events");
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)	
		
		
	});

	
	$scope.passEvent = function(event){
		shareData.addData(event);
	}

}]);

//VIEW EVENT DETAILS OF TO BE APPROVED EVENTS / APPROVED EVENTS AND UPDATE STATUS
app.controller('viewEventDetailsController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	angular.element(document).ready(function () {
		
	
		
			$scope.event1 = shareData.getData();
			$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
			$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
			var dataObj = {			
					event_title: $scope.event1.event_title,
					event_content: $scope.event1.event_content,
					event_description: $scope.event1.event_description,
					event_approval_status: $scope.event1.event_approval_status,						
					event_start_date: $scope.event1.event_start_date,				
					event_end_date: $scope.event1.event_end_date,
					//event_period: $scope.event1.event_period,
					filePath: $scope.event1.filePath,
			};
			$scope.event = angular.copy($scope.event1)

			var url = "https://localhost:8443/eventManager/viewEventDetails";
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.event1.event_title);
		
		
		
	});

	$scope.approveEvent = function(){
		$scope.data = {};
		console.log("Start approving event");
		
		var tempObj ={eventId:$scope.event.id};
		console.log("fetch id "+ tempObj);
		$http.post("//localhost:8443/eventManager/approveEvent", JSON.stringify(tempObj)).then(function(response){
			console.log("Approve the EVENT");
			alert("Successfully approved event, going back to viewing to be approved events");
			$location.path("/viewToBeApprovedEvents");
		},function(response){
			alert(response);
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}
	$scope.updateEventStatus = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var dataObj = {				
				id: $scope.event.id,
				event_approval_status: $scope.event.event_approval_status,
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
			$location.path("/viewApprovedEvents");
		});
		send.error(function(data){
			alert(data);
		});
	};

}]);

//VIEW EVENT ORGANISERS
app.controller('viewEventOrganiserController', ['$scope',  '$timeout','$http','shareData','$location', function ($scope,  $timeout,$http ,shareData,$location) {
	angular.element(document).ready(function () {
	
			$scope.data = {};
			$http.get("//localhost:8443/eventManager/viewEventOrganizers").then(function(response){
				$scope.eventOrgs = response.data;
				console.log("DISPLAY ALL EVENT ORGANIZERS");
				console.log("EVENT ORGS DATA ARE OF THE FOLLOWING: " + $scope.eventOrgs);

			},function(response){
				alert("did not view eventOrgs");
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)	
		
	});
	$scope.passEventOrg = function(eventOrg){
		shareData.addData(eventOrg);
	}

}])

//VIEW NOTIFICATIONS
app.controller('notificationController', ['$scope', '$http','shareData','$location', function ($scope, $http, shareData,$location) {		

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
				$location.path("/viewEventOrganizers");
				console.log('GET NOTIFICATIONS FAILED! ' + JSON.stringify(response));
			});
			
		
	})


}]);