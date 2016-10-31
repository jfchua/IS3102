//VIEW ALL EVENTS,
app.controller('eventExternalController', ['$scope', '$rootScope', '$http','$state','$routeParams','shareData', function ($scope, $rootScope,$http,$state, $routeParams, shareData) {
	
	
	angular.element(document).ready(function () {

	$scope.data = {};
	//var tempObj= {id:1};
	//console.log(tempObj)
	$http.get("//localhost:8443/tixGetFeedback").then(function(response){
		$scope.feedbacks = response.data;
		console.log("DISPLAY ALL EVENT");
		console.log($scope.feedbacks);

	},function(response){
		alert("did not view all events");
		//console.log("response is : ")+JSON.stringify(response);
	}	
	)
	
	$http.get("//localhost:8443/event/viewAllEvents").then(function(response){
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$scope.events = response.data;
		console.log("DISPLAY ALL EVENT");
		console.log($scope.events);

	},function(response){
		alert("did not view all feedback");
		//console.log("response is : ")+JSON.stringify(response);
	}	
	)
	
	
});

$scope.viewApprovedEvents = function(){
	$scope.data = {};
	//var tempObj= {id:1};
	//console.log(tempObj)
	$http.get("//localhost:8443/event/viewApprovedEvents").then(function(response){
		$scope.events = response.data;
		console.log("DISPLAY ALL EVENT");
		console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

	},function(response){
		alert("did not view approved events");
		//console.log("response is : ")+JSON.stringify(response);
	}	
	)	
}

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
	$state.go("dashboard.configureTicketsEx");
}
$scope.passEventToViewTix = function(id){
	shareData.addData(id);
}

$scope.requestForTicketSales = function(event){
	if(confirm('Confirm ticket sales for '+event.event_title+'?' + " This action cannot be undone")){

		var tempObj ={eventId: event.id};
		console.log("fetch id ");
		console.log(tempObj);
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.post("//localhost:8443/event/requestTickets", JSON.stringify(tempObj)).then(function(response){
			//$scope.buildings = response.data;
			console.log("REQUEST FOR TICKET SALES");
			$scope.requestedTicket = true;
			alert('Successfully set ticket sales');
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
			$state.go($state.current, {}, {reload: true}); 
		},function(response){
			alert("Did not request ticket sales. Check if the event has been approved");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)
	}	
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
		$state.go("dashboard.viewAllEventsEx");
		console.log('GET Booking FAILED! ' + JSON.stringify(response));
	});

}
}]);
//VIEW ALL APPROVED EVENTS
app.controller('viewApprovedEventsController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {

	angular.element(document).ready(function () {
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		$http.get("//localhost:8443/event/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			alert("did not view approved events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)
	
});


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
		$state.go("dashboard.viewAllEventsEx");
		console.log('GET Booking FAILED! ' + JSON.stringify(response));
	});

}



}]);
//DELETE EVENT
app.controller('deleteEventExController', ['$scope',  '$timeout','$http','shareData','$state', function ($scope,  $timeout,$http ,shareData,$state) {
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
				alert('EVENT IS DELETED! GOING BACK TO VIEW EVENTS...');
				//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
				$state.go("dashboard.viewAllEventsEx");
			},function(response){
				alert("DID NOT CANCEL EVENT");
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)
		}
		

	}

}])

app.controller('addEController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData){
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
			alert('Get units error!!!!!!!!!!');
		});		
		
		$scope.currentlySelectedUnit;
		$scope.selectUnit = function(){
			console.log("currently selected unit bookings");
			console.log($scope.currentlySelectedUnit.bookings);
			
			 getEvents($scope.currentlySelectedUnit.bookings);
			 console.log("currently selected unit schedules");
			 console.log($scope.currentlySelectedUnit.schedule);
			 getMaints($scope.currentlySelectedUnit.schedule);//put here or after for loop
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
	$scope.components={};
	$scope.checkRent = function(){
		console.log("start checking rent");
		$scope.data = {};

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
		//$scope.avail = "";	
		send.success(function(response){
			$scope.components = response;
			$scope.order_item = "id";
			$scope.order_reverse = false;
			console.log($scope.components);
			alert("get component success");
		});
		send.error(function(response){
			alert("get component failure");
		});
		
		
		var send1 = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/event/checkRent',
			data    : dataObj //forms user object
		});
		send1.success(function(response){
			$scope.totalRent = response;
			$scope.components = response.data;
			$scope.totalRentAfter = response*1.07;
			console.log($scope.totalRent);
		});
		send1.error(function(response){
			$scope.totalRent = response;
			console.log($scope.totalRent);
		});		
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
		send.success(function(){
			alert('Event IS SAVED!');
			$state.go("dashboard.viewAllEventsEx");
		});
		send.error(function(){
			alert('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
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

app.controller('updateEController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData){
	angular.element(document).ready(function () {
		//VIEW EVENT
			$scope.event1 = shareData.getData();
			$scope.event1.event_start_date = new Date($scope.event1.event_start_date);
			$scope.event1.event_end_date = new Date($scope.event1.event_end_date);
			var dataObj = {			
					units:$scope.event1.units,
					event_title: $scope.event1.event_title,
					event_content: $scope.event1.event_type,
					event_description: $scope.event1.event_description,
					event_approval_status: $scope.event1.approvalStatus,						
					event_start_date: $scope.event1.event_start_date,						
					event_end_date: $scope.event1.event_end_date,
					filePath: $scope.event1.filePath,
			};
			
			$scope.event = angular.copy($scope.event1)
			console.log($scope.event1);
			var url = "https://localhost:8443/event/updateEvent";
			console.log($scope.event1.event_title);
			
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
				$state.go("dashboard.viewAllEventsEx");
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
				alert('Get building error!!!!!!!!!!');
			});
			$scope.currentlySelectedBuilding;
			$scope.selectBuild = function(){
				$scope.selectedBuilding=$scope.currentlySelectedBuilding;
			}
			console.log("finish selecting building");
		
	});
	 //RESET SELECTED BOOKINGS UNITS TO LAST SAVED EVENT
	$scope.resetBookings = function(){
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
			alert('Get levels error!!!!!!!!!!');
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
		/*
		$scope.checkAvail = function(){
			console.log("start checking availability");
			$scope.data = {};

			var dataObj = {
					units: $scope.selectedBookingsUnits,
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
		}	*/
	}
	
	$scope.checkAvail = function(){
		console.log("start checking availability");
		$scope.data = {};

		var dataObj = {			
				id: $scope.event.id,
				units: $scope.selectedBookingsUnits,
				event_start_date: ($scope.event.event_start_date).toString(),
				event_end_date: ($scope.event.event_end_date).toString(),
		};
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

	$scope.checkRent = function(){
		console.log("start checking rent");
		$scope.data = {};

		var dataObj = {
				units: $scope.selectedBookingsUnits,
				event_start_date: ($scope.event.event_start_date).toString(),
				event_end_date: ($scope.event.event_end_date).toString(),
		};
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/event/checkRent',
			data    : dataObj //forms user object
		});
		//$scope.avail = "";
		send.success(function(response){
			$scope.totalRent = response;
			$scope.totalRentAfter = response*1.07;
			console.log($scope.totalRent);
		});
		send.error(function(response){
			$scope.totalRent = response;
			console.log($scope.totalRent);
		});
	}

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
				event_content: $scope.event.event_content,
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
			alert('EVENT IS SAVED! GOING BACK TO VIEW ALL EVENTS');
			$state.go("dashboard.viewAllEventsEx");
		});
		send.error(function(){
			alert('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
		});
	};	
}]);





app.controller('bookingController', ['$scope','$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {	
	//console.log(tempObj)
	console.log("DISPLAY ALL BOOKINGS");
	$scope.event = shareData.getData();
	var id=$scope.event.id;

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
		$state.go("dashboard.viewAllEventsEx");
		console.log('GET Booking FAILED! ' + JSON.stringify(response));
	});
	
	var url = "https://localhost:8443/booking/viewAllBookings";	
    console.log("BOOKING DATA ARE OF THE FOLLOWING: " + $scope.bookings);	
	});

$scope.deleteBooking = function(id){
    var r = confirm("Confirm cancel? \nEither OK or Cancel.");
    if (r == true) {
    	$scope.url = "https://localhost:8443/booking/deleteBooking/"+id;
    	var deleteBooking = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/booking/deleteBooking/' + id        
		});
    	console.log("Deleting the event using the url: " + $scope.url);
		deleteBooking.success(function(response){
			alert('DELETE BOOKING SUCCESS! ');
			console.log("ID IS " + id);
			$state.go("dashboard.viewAllEventsEx");
		});
		deleteBooking.error(function(response){
			alert('DELETE BOOKING FAIL! ');
			$state.go("dashboard.viewAllEventsEx");
			console.log('DELETE BOOKING FAILED! ' + JSON.stringify(response));
		});
    } else {
        alert("Cancel deleting booking");
    }
    //document.getElementById("demo").innerHTML = txt;	
	/*
	$scope.data = {};
	console.log("Start deleting event");
	$scope.url = "https://localhost:8443/booking/deleteBooking/"+id;
	console.log("GETTING THE EVENT INFO")
	var deleteBooking = $http({
		method  : 'POST',
		url     : 'https://localhost:8443/booking/deleteBooking/' + id        
	});
	console.log("Deleting the event using the url: " + $scope.url);
	deleteBooking.success(function(response){
		console.log('DELETE BOOKING SUCCESS! ' + JSON.stringify(response));
		console.log("ID IS " + id);
	});
	deleteBooking.error(function(response){
		$location.path("/viewAllEventsEx");
		console.log('DELETE BOOKING FAILED! ' + JSON.stringify(response));
	});*/
			
	/*
	$scope.event = JSON.parse(shareData.getData());
	console.log($scope.event.id);
	var tempObj ={eventId:$scope.event.id};
	console.log("fetch id "+ tempObj);
	
	$http.post("//localhost:8443/event/deleteEvent", JSON.stringify(tempObj)).then(function(response){
		
		console.log("Cancel the EVENT");
	},function(response){
		alert("DID NOT Cancel EVENT");
		
	}	
	)*/
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
		});
		getPayments.error(function(response){
			$state.go("dashboard.viewPaymentPlansEx");
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
			alert("did not view plans");
		}	
		)
		
		$http.get("//localhost:8443/event/getTotal").then(function(response){
			$scope.totalAmount = response.data;
			console.log("DISPLAY TOTAL BALANCE");
			console.log($scope.totalAmount);
		},function(response){
			alert("did not view plans");
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
			$state.go("dashboard.viewPaymentPlansEx");
			console.log('GET PAYMENTS FAILED! ');
		});		
	});
	
}]);



//Unit Plan of Event used by event organiser
app.controller('areaPlanController', function ($scope, $http,shareData,ModalService) {
	  var widthForAreaPlan;
	  var meter;
	  var unit;
	  var scale;
	  var bookingIdObj;
	angular.element(document).ready(function () {
		var obj=shareData.getData();
		$scope.booking=obj.booking;
		$scope.event=obj.event;
		console.log($scope.booking);
		console.log($scope.event);
		unit=obj.booking.unit;
		bookingIdObj={id:$scope.booking.id};
	 //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;    
	      console.log(widthForFloorPlan);
	      meter=parseInt((widthForFloorPlan-40)/(unit.sizeX));
	      $scope.unitLengthGrid=meter*(unit.sizeX);
		  $scope.unitWidthGrid=meter*(unit.sizeY);	
		  scale=meter/2;//one grid represent 0.5m
		  console.log( $scope.unitLengthGrid+" "+$scope.unitWidthGrid);
		//get event id from previous page
		  			var getAreas= $http({
		  				method  : 'POST',
		  				url     : 'https://localhost:8443/area/viewAreas/',
		  				data    : bookingIdObj,
		  		});
		  			console.log("REACHED HERE FOR VIEWING AREAS " + JSON.stringify(bookingIdObj));
		  			getAreas.success(function(response){
		  				console.log(response);
		  				$scope.areas = response;
		  				
		  			});
		  			getAreas.error(function(){
		  				
		  			});		
		  			
		  			
		  		  //RETRIEVE ICON WHEN LOADED
					$http.get("//localhost:8443/property/viewIcons").then(function(response){			
						//console.log(response.data);
						$scope.icons = response.data;
						console.log($scope.icons);
						//console.log($scope.icons[0]);
						//console.log($scope.icons[0].iconType);
						//console.log($scope.icons[0].iconPath);
						$scope.icon=$scope.icons[0];
					},function(response){
						alert("DID NOT VIEW ICONS");
						
					})
					
					
					
					
					//GET ICON MENU
					$http.get("//localhost:8443/property/getIconsMenu").then(function(response){			
						console.log(response);
						var data = angular.fromJson(response.data);
						console.log(data);
						//$scope.iconMenu=[];
						var index=0;
						 angular.forEach(data, function(item){             
							   	var iconMenuRow=[];
							   	iconMenuRow.push(data[index].name);
							   
							   	eval( 'var func = ' +data[index].funct ); //working
							   	console.log(data[index].funct);
							   	iconMenuRow.push(func);//working
							   	$scope.menuOptions.push(iconMenuRow);//working
							    // $scope.iconMenu.push([data[index]]);
							     index++;
							     console.log(iconMenuRow);
							  }); 
						 console.log("test icon menu");
						 console.log($scope.iconMenu);
						//console.log($scope.icons);
						//console.log($scope.icons[0]);
						//console.log($scope.icons[0].iconType);
						//console.log($scope.icons[0].iconPath);
					},function(response){
						alert("DID NOT VIEW ICONS");
						
					})
		  		
	});
	$scope.menuOptions = [
	                      
	                       // null,        // Dividier
	                        ['<img  class="svgtest" src="./svg/entry.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {
	                            $scope.defaultIcon='./svg/entry.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/exit.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                     
	                            $scope.defaultIcon='./svg/exit.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/chair.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                      
	                            $scope.defaultIcon='./svg/chair.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/armchair.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                      
	                            $scope.defaultIcon='./svg/armchair.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/table.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                        
	                            $scope.defaultIcon='./svg/table.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/stage.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/stage.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/food.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/food.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/restroomMan.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/restroomMan.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/restroomWoman.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/restroomWoman.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/restroomWheelchair.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/restroomWheelchair.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        null
	                    ];
	  
	 $scope.defaultIcon='./svg/entry.svg';
	  $scope.addDefaultIcon = function (type) {   //note: passed in type is not used
		  var dataObj = {
				  id: $scope.booking.id,
				  Areas:{
					  Area:$scope.areas
				  }
		  };						
		
			  var obj={bookingId:$scope.booking.id,type: $scope.defaultIcon};
			  $http.post('/area/addDefaultIcon', JSON.stringify(obj)).then(function(response){
				  $scope.areas=[];
				  console.log("empty");
				  console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
					  console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);

				  },function(response){
					  console.log("DID NOT view");
					  //console.log("response is "+angular.fromJson(response.data).error);
				  })
			  },function(response){//else is not saved successfully
				  console.log("DEFAULT ICON CANNOT BE ADDED");
				  alert("DEFAULT ICON CANNOT BE ADDED");
			  })
		 
	  } //END ADD DEFAULT ICON
	  
	  
	  $scope.addCustIcon = function(iconId){
		  //GET SELECTED CUSTOMISED ICON
		  var index=0;
		  var found=false;
		  var icon;
		  angular.forEach($scope.icons, function(item){             
			  if(found==false && $scope.icons[index].id==iconId){
				  icon=$scope.icons[index];
			  }else
				  index = index + 1;
		  }); 
		  console.log(iconId);
		  console.log(icon);
	
		  
		  var dataObj = {
				  id: $scope.booking.id,
				  Areas:{
					  Area:$scope.areas
				  }
		  };      
		 
			  var saveIconObj={bookingId:$scope.booking.id,iconId:icon.id};
			  $http.post('/area/addCustIcon', JSON.stringify(saveIconObj)).then(function(response){
				  $scope.areas=[];
				  console.log("empty");
				  console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
					  console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);

				  },function(response){
					  console.log("DID NOT view");
					  //console.log("response is "+angular.fromJson(response.data).error);
				  })
			  },function(response){//else is not saved successfully
				  console.log("CUST ICON CANNOT BE ADDED");
				  alert("CUST ICON CANNOT BE ADDED");
			 
		  } )

	  }//END ADD CUSTOMISED ICON
	  
	  
	$scope.passEvent=function(){
		shareData.addData($scope.event);
	}

	$scope.passBooking=function(booking){
		console.log(booking);
		var obj={
				event:$scope.event,
				booking:booking
				};
		shareData.addData(obj);
	}

	$scope.viewArea=function(){
		


	}
	$scope.showDefaultUnitPlan= function(){
		
		var unitIdObj={unitId:unit.id,bookingId:$scope.booking.id};
		 $http.post('//localhost:8443/area/saveDefaultAreas', JSON.stringify(unitIdObj)).then(function(response){
			  //console.log(angular.fromJson(response.data));
			 // $scope.areas=angular.fromJson(response.data);
			  
			  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){			
				  console.log(angular.fromJson(response.data));
				  $scope.areas=angular.fromJson(response.data);

			  },function(response){
				  console.log("DID NOT view");
				  //console.log("response is "+angular.fromJson(response.data));
			  })
			  
		  },function(response){
			  console.log("DID NOT SAVE DEFAULT AREAS");
			  //console.log("response is "+angular.fromJson(response.data).error);
		  })
		
	}

	  $scope.addArea = function () {  
		  var dataObj = {
				  id: $scope.booking.id,
				  Areas:{
					  Area:$scope.areas
				  }
		  };						
		 	
			  var dataObj={bookingId:$scope.booking.id};
			  $http.post('/area/addArea', JSON.stringify(dataObj)).then(function(response){
				  $scope.areas=[];
				  console.log("empty");
				  console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){			
					  console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);
	
				  },function(response){
					  console.log("DID NOT view");
					  //console.log("response is "+angular.fromJson(response.data));
				  })
			  },function(response){//else is not saved successfully
				  console.log("AREA CANNOT BE ADDED");
				  alert("AREA CANNOT BE ADDED");
			 
		  } )
	  }//END ADD AREA
	  
		$scope.updateArea=function(area){
			 console.log("Update the area");
			
			// console.log("scope test3");
		//	 console.log(   angular.element(document.getElementById('1')).scope());
			  var dataObj = {
				        id: $scope.booking.id,
				        Areas:{
				          Areas:$scope.areas
				        }
				    };
			
			  			var dataObj={area:area,bookingId:$scope.booking.id};
			  			$http.post('/area/updateArea', JSON.stringify(dataObj)).then(function(response){
			  				$scope.areas=[];
			  				console.log("empty");
			  				//console.log($scope.units);
			  				 $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
			  				        console.log(response.data);
			  				
			  				        console.log(angular.fromJson(response.data));
			  				      $scope.areas=angular.fromJson(response.data);
			  				  //  console.log("scope test4");
			  					// console.log(   angular.element(document.getElementById('1')).scope());
			  				       // $state.reload();//Can use
			  				     
			  				      },function(response){
			  				        console.log("DID NOT view");
			  				        //console.log("response is "+angular.fromJson(response.data).error);
			  				      })
			  		},function(response){//else is not saved successfully
			  				console.log("AREA CANNOT BE EDITED");
			  			alert("AREA CANNOT BE EDITED");
			  		})
			  		
			  		
			     
		 };//END UPDATE AREA
		 $scope.remove = function(area) { 
				
			  var dataObj = {
				        id: $scope.booking.id,
				        Areas:{
				          Areas:$scope.area
				        }
				    };
			
			    	  if (confirm('CONFIRM TO DELETE THIS Area'+area.areaName+'?')) {
			  			
			  			var dataObj={id:area.id,bookingId:$scope.booking.id};
			  			$http.post('/area/deleteArea', JSON.stringify(dataObj)).then(function(response){
			  				$scope.areas=[];
			  				 $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
			  				        console.log("pure response is "+response.data);
			  				
			  				        console.log("test anglar.fromJon"+angular.fromJson(response.data));
			  				        $scope.areas=angular.fromJson(response.data);
			  				
			  				      },function(response){
			  				        console.log("DID NOT view");
			  				        //console.log("response is "+angular.fromJson(response.data).error);
			  				      })
			  		},function(response){//else is not saved successfully
			  			console.log("AREA CANNOT BE DELETED");
			  		
			  		})
			  		
			  		}
			    
			
			   
		}//END REMOVE
	$scope.saveAreas = function () {   

		console.log("Test: start saving areas");
		var saveAreas=$scope.areas;
		var areasString=angular.toJson(saveAreas);
		console.log(areasString);

		var dataObj = {
				id: $scope.booking.id,
				Areas:{
					Area:saveAreas
				}
		};

		console.log(dataObj);

		$http.post('/area/saveAreas', JSON.stringify(dataObj)).then(function(response){
			console.log("pure response is "+JSON.stringify(response.data));
	

		},function(response){//else is not saved successfully
			console.log("DID NOT SAVE");
			console.log("response is "+JSON.stringify(response.data));
		})


	} //END SAVE AREAS

	

	$scope.showDetails= function (thisArea) {   
		//console.log(thisArea.id); 

		$scope.showDetail="id: "+ thisArea.id+", areaName: " + thisArea.areaName+", description: " + thisArea.description+"left: " + thisArea.square.left + ", top: " +  thisArea.square.top+ ", height: " + thisArea.square.height + ", width: " + thisArea.square.width;    

	} 
	
	$scope.downloadPlan = function () {
		  console.log("her0");
		  console.log(html2canvas);
		
		
		  var canvasdiv = document.getElementById("glassboxGrid");
		    html2canvas(canvasdiv,{
		    	 allowTaint: true,
	             logging: true,
	             taintTest: true,
		        onrendered: function (canvas) {
		            var a = document.createElement("a");
		            a.href = canvas.toDataURL("plan/png");
		            a.download ="plan.png";
		            a.click();
		        },
		       
		
		    });
		}
	 
	$scope.resize = function(area,evt,ui) {

		console.log("resize");

		area.square.width = evt.size.width;//working restrict A
		area.square.height = evt.size.height;
		area.square.left = parseInt(evt.position.left);
		area.square.top = parseInt(evt.position.top);
	}
	$scope.drag = function(area,evt,ui) {

		console.log(evt);
		console.log("DRAGGING");
		area.square.left = parseInt(evt.position.left);
		area.square.top = parseInt(evt.position.top);
		area.square.width = evt.helper.context.clientWidth;
		area.square.height = evt.helper.context.clientHeight;
	}



	/*
	    var areaIds="";
	    $scope.addToAreaIds=function(areaId){
	        areaIds+=(areaId+" ");
	        console.log(areaIds);
	    }

	    $scope.passAreaIds=function(){
	    	var stringToPassArea=areaIds.substring(0,areaIds.length-1);
	    	console.log(stringToPassArea);
	    	var objToPassArea={'areas':stringToPassArea};
	    	shareData.addData(JSON.stringify(objToPassArea));
	    	console.log(JSON.stringify(objToPassArea));
	    }
	 */
	
	 console.log("gridster test ");
	 console.log(meter);
	 $scope.gridsterOpts = {
			 
			 	
			    columns: unit.sizeX*(meter/scale), // the width of the grid, in columns
			    pushing: false, // whether to push other items out of the way on move or resize
			    floating: false, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
			    swapping: false, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
			    width:$scope.unitLengthGrid, // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
			    colWidth: scale, // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
			    rowHeight: scale, // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
			    margins: [1, 1], // the pixel distance between each widget
			    outerMargin: false, // whether margins apply to outer edges of the grid
			    sparse: false, // "true" can increase performance of dragging and resizing for big grid (e.g. 20x50)
			    isMobile: false, // stacks the grid items if true
			    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
			    mobileModeEnabled: false, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
			    minColumns: unit.sizeX*(meter/scale), // the minimum columns the grid must have
			    minRows: unit.sizeY*(meter/scale), // the minimum height of the grid, in rows
			    maxRows: unit.sizeY*(meter/scale),
			    defaultSizeX: 2, // the default width of a gridster item, if not specifed
			    defaultSizeY: 1, // the default height of a gridster item, if not specified
			    minSizeX: 1, // minimum column width of an item
			    maxSizeX: null, // maximum column width of an item
			    minSizeY: 1, // minumum row height of an item
			    maxSizeY: null, // maximum row height of an item
			    resizable: {
			       enabled: true,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, area) {
			    	   
			    	 
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished resizing
			    },
			    draggable: {
			       enabled: true, // whether dragging items is supported
			       //handle: '.my-class', // optional selector for drag handle
			       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
			       drag: function(event, $element, widget) {
			    	 
			       }, // optional callback fired when item is moved,
			       stop: function(event, $element, area) {
			    	  
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished dragging
			    }
			};

	 console.log("test opts");
	 console.log($scope.gridsterOpts.colWidth);
	 console.log($scope.gridsterOpts.maxRows);
	 console.log($scope.gridsterOpts.columns);
	 
	 
	 //MODAL FOR ONE AREA
	  $scope.complexResult = null;
		 $scope.showModal = function(area,$parent) {
			 console.log(area);
			    // Just provide a template url, a controller and call 'showModal'.
			    ModalService.showModal({
			    	
			    	      templateUrl: "views/updateAreaTemplate.html",
			    	      controller: "updateAreaController",
			    	      inputs: {
			    	        title: "Update Area",
			    	        area:area
			    	      }
			    	    }).then(function(modal) {
			    	      modal.element.modal();
			    	      modal.close.then(function(result) {
			    	      var area  = result.area;
			    	      // console.log("in then");
			    	      // console.log("scope test1");
			    	  	// console.log(   angular.element(document.getElementById('1')).scope());
			    	       $parent.updateArea(area);
			    	      // console.log("scope test2");
			    	  	// console.log(   angular.element(document.getElementById('1')).scope());
			    	       //$state.reload();
			    	      });
			    	    });

			  };//END SHOWMODAL
			  
			  $scope.dismissModal = function(result) {
				    close(result, 200); // close, but give 200ms for bootstrap to animate
				    $parent.updateArea();
				    console.log("in dissmiss");
				 };
})


//UPDATE AREA MODAL
app.controller('updateAreaController', ['$scope', '$element', 'title', 'close', 'area',
                                                function($scope, $element, title, close,area) {
	
		//UPDATE MODAL

		  $scope.title = title;
		  $scope.area=area;
		  //console.log(title);
		  //console.log(close);
		  //console.log($element);
		  //  This close function doesn't need to use jQuery or bootstrap, because
		  //  the button has the 'data-dismiss' attribute.
		  $scope.close = function() {
		 	  close({
		      area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  //  This cancel function must use the bootstrap, 'modal' function because
		  //  the doesn't have the 'data-dismiss' attribute.
		  $scope.cancel = function() {

		    //  Manually hide the modal.
		    $element.modal('hide');
		    
		    //  Now call close, returning control to the caller.
		    close({
		    	area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		

	
}])


//Unit Plan of Event used by event organiser
app.controller('viewAreaPlanController', function ($scope, $http,shareData,ModalService) {
	  var widthForAreaPlan;
	  var meter;
	  
	  var scale;
	  var bookingIdObj;
	
	  $scope.areas=[];
	  $scope.legends=[];
	angular.element(document).ready(function () {
		var obj=shareData.getData();
		$scope.booking=obj.booking;
		$scope.event=obj.event;
		console.log($scope.booking);
		console.log($scope.event);
		$scope.unit=obj.booking.unit;
		bookingIdObj={id:$scope.booking.id};
	 //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;    
	      console.log(widthForFloorPlan);
	      meter=parseInt((widthForFloorPlan-40)/($scope.unit.sizeX));
	      $scope.unitLengthGrid=meter*($scope.unit.sizeX);
		  $scope.unitWidthGrid=meter*($scope.unit.sizeY);	
		  scale=meter/2;//one grid represent 0.5m
		  console.log( $scope.unitLengthGrid+" "+$scope.unitWidthGrid);
		//get event id from previous page
		  			var getAreas= $http({
		  				method  : 'POST',
		  				url     : 'https://localhost:8443/area/viewAreas/',
		  				data    : bookingIdObj,
		  		});
		  			console.log("REACHED HERE FOR VIEWING AREAS " + JSON.stringify(bookingIdObj));
		  			getAreas.success(function(response){
		  				console.log(response);
		  				$scope.areas = response;

		  				angular.forEach($scope.areas, function(area){   
		  					$scope.addToLegend(area);
		  					
		  				})
		  				
		  			});
		  			getAreas.error(function(){
		  				//$scope.areas =[];
		  			});	
		  			
		  			
		  			$scope.addToLegend= function(area){
		  			console.log("test legend");
		  			console.log($scope.legends);
	  				var isDuplicate=false;
		  			angular.forEach($scope.legends, function(legend){    
			  				if((isDuplicate==false)&&(area.description == legend.description)){	
			  				
			  					isDuplicate=true;
			  					console.log(isDuplicate);
			  				}else{
			  				
			  				console.log(isDuplicate);
			  				}		  				
		  			}); 
		  			
		  			if(isDuplicate){	
	  					
	  				}else{
	  					$scope.legends.push(area);
	  				}
		  			console.log($scope.legends);
		  			
		  			}
		  			
	});

	
	$scope.showDefaultUnitPlan= function(){
		var unitIdObj={id:$scope.unit.id};
		 $http.post('//localhost:8443/area/viewAreasDefault', JSON.stringify(unitIdObj)).then(function(response){
			  console.log(angular.fromJson(response.data));
			  $scope.areas=angular.fromJson(response.data);
			  angular.forEach($scope.areas, function(area){   
					$scope.addToLegend(area);
					
				})

		  },function(response){
			  console.log("DID NOT view");
			  //console.log("response is "+angular.fromJson(response.data).error);
		  })
		
	}
	$scope.passEvent=function(){
		shareData.addData($scope.event);
	}

	$scope.passBooking=function(booking){
		console.log(booking);
		var obj={
				event:$scope.event,
				booking:booking
				};
		shareData.addData(obj);
	}

	$scope.viewArea=function(){
		


	}
	


	
	$scope.downloadPlan = function () {
		  console.log("her0");
		  console.log(html2canvas);
		
		
		  var canvasdiv = document.getElementById("screenshot");
		    html2canvas(canvasdiv,{
		    	 allowTaint: true,
	             logging: true,
	             taintTest: true,
		        onrendered: function (canvas) {
		            var a = document.createElement("a");
		            a.href = canvas.toDataURL("plan/png");
		            a.download ="plan.png";
		            a.click();
		        },
		       
		
		    });
		}
	

	
	 console.log("gridster test ");
	 console.log(meter);
	 $scope.gridsterOpts = {
			 
			 	
			    columns: $scope.unit.sizeX*(meter/scale), // the width of the grid, in columns
			    pushing: false, // whether to push other items out of the way on move or resize
			    floating: false, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
			    swapping: false, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
			    width:$scope.unitLengthGrid, // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
			    colWidth: scale, // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
			    rowHeight: scale, // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
			    margins: [1, 1], // the pixel distance between each widget
			    outerMargin: false, // whether margins apply to outer edges of the grid
			    sparse: false, // "true" can increase performance of dragging and resizing for big grid (e.g. 20x50)
			    isMobile: false, // stacks the grid items if true
			    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
			    mobileModeEnabled: false, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
			    minColumns: $scope.unit.sizeX*(meter/scale), // the minimum columns the grid must have
			    minRows: $scope.unit.sizeY*(meter/scale), // the minimum height of the grid, in rows
			    maxRows: $scope.unit.sizeY*(meter/scale),
			    defaultSizeX: 2, // the default width of a gridster item, if not specifed
			    defaultSizeY: 1, // the default height of a gridster item, if not specified
			    minSizeX: 1, // minimum column width of an item
			    maxSizeX: null, // maximum column width of an item
			    minSizeY: 1, // minumum row height of an item
			    maxSizeY: null, // maximum row height of an item
			    resizable: {
			       enabled: false,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, area) {
			    	   
			    	 
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished resizing
			    },
			    draggable: {
			       enabled: false, // whether dragging items is supported
			       //handle: '.my-class', // optional selector for drag handle
			       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
			       drag: function(event, $element, widget) {
			    	 
			       }, // optional callback fired when item is moved,
			       stop: function(event, $element, area) {
			    	  
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished dragging
			    }
			};

	 console.log("test opts");
	 console.log($scope.gridsterOpts.colWidth);
	 console.log($scope.gridsterOpts.maxRows);
	 console.log($scope.gridsterOpts.columns);
	 
	 
	 //MODAL FOR ONE AREA
	  $scope.complexResult = null;
		 $scope.showModal = function(area,$parent) {
			 console.log(area);
			    // Just provide a template url, a controller and call 'showModal'.
			    ModalService.showModal({
			    	
			    	      templateUrl: "views/viewAreaTemplate.html",
			    	      controller: "viewAreaController",
			    	      inputs: {
			    	        title: "Update Area",
			    	        area:area
			    	      }
			    	    }).then(function(modal) {
			    	      modal.element.modal();
			    	      modal.close.then(function(result) {
			    	      var area  = result.area;
			    	      // console.log("in then");
			    	      // console.log("scope test1");
			    	  	// console.log(   angular.element(document.getElementById('1')).scope());
			    	     //  $parent.updateArea(area);
			    	      // console.log("scope test2");
			    	  	// console.log(   angular.element(document.getElementById('1')).scope());
			    	       //$state.reload();
			    	      });
			    	    });

			  };//END SHOWMODAL
			  
			  $scope.dismissModal = function(result) {
				    close(result, 200); // close, but give 200ms for bootstrap to animate
				    console.log("in dissmiss");
				 };
})


//VIEW AREA MODAL
app.controller('viewAreaController', ['$scope', '$element', 'title', 'close', 'area',
                                                function($scope, $element, title, close,area) {
	
		//UPDATE MODAL

		  $scope.title = title;
		  $scope.area=area;
		 
		  $scope.close = function() {
		 	  close({
		      area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  //  This cancel function must use the bootstrap, 'modal' function because
		  //  the doesn't have the 'data-dismiss' attribute.
		  $scope.cancel = function() {

		    //  Manually hide the modal.
		    $element.modal('hide');
		    
		    //  Now call close, returning control to the caller.
		    close({
		    	area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		

	
}])


app.controller('ticketSaleExController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	$scope.payment={};
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.eventId = shareData.getData();
		$scope.url = "https://localhost:8443/event/getEvent1/"+$scope.eventId;
		
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
			$state.go("dashboard.viewTicketSales");
			console.log('GET PAYMENT FAILED! ');
		});
		
		
		$scope.order_item = "cat";
		$scope.order_reverse = false;
		$scope.url1 = "https://localhost:8443/event/getTicketSales/"+$scope.eventId;
		
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
			$state.go("dashboard.viewAllEventsEx");
			console.log('GET EVENTS FAILED! ');
		});
		
		
	});
	
}]);



