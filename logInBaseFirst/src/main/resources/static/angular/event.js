app.service('shareData', function($window) {
	var KEY = 'App.SelectedValue';
	var data = {};
	var addData = function(newObj) {
		var mydata = $window.sessionStorage.getItem(KEY);
		data = newObj;
		if (mydata) {
			mydata = JSON.parse(mydata);
			//console.log(mydata);
		} else {
			mydata = [];
		}
		mydata.push(newObj);
		//console.log("hailing test");
		//console.log(mydata);
		$window.sessionStorage.setItem(KEY, JSON.stringify(mydata));
	};

	var getData = function(){
		var mydata = $window.sessionStorage.getItem(KEY);
		return data;
		if (mydata) {
			mydata = JSON.parse(mydata);
		}
		return mydata || [];
	};

	return {
		addData: addData,
		getData: getData
	};
});



app.controller('eventExternalController', ['$scope', '$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {


	$scope.addEvent = function(){
		var unitIdsString="";
		var unitIdsObj = JSON.parse(shareData.getData());
		unitIdsString+=unitIdsObj.units;
		console.log("test hailing");
		console.log(unitIdsString);

		console.log("start adding");
		$scope.data = {};

		var dataObj = {
				units:unitIdsString,
				event_title: $scope.event.event_title,
				event_content: $scope.event.event_content,
				event_description: $scope.event.event_description,
				event_approval_status: "processing",
				//x.setHours(x.getHours() - x.getTimezoneOffset() / 60);			
				//event_start_date: $scope.event.event_start_date.setHours($scope.event.event_start_date.getHours()-$scope.event.event_start_date.getTimezoneOffset() / 60),
				event_start_date: ($scope.event.event_start_date).toString(),
				event_end_date: ($scope.event.event_end_date).toString(),
				//event_period: $scope.event.event_period,
				filePath: $scope.event.filePath,
		};
		//console.log(JSON.parse($scope.event.event_start_date));
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
		//Date.prototype.toJSON = function(){ return moment(this).format(); }
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/event/addEvent',
			data    : dataObj //forms user object
		});

		//shareData.addData(JSON.stringify(response));	 

		console.log("SAVING THE Event");
		send.success(function(){
			alert('Event IS SAVED!');
		});
		send.error(function(){
			alert('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
		});
	};

	$scope.viewEvents = function(){
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		$http.get("//localhost:8443/event/viewAllEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENT");
			console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.events);

		},function(response){
			alert("did not view all events");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	}

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

	$scope.getEvent = function(id){		
		$scope.dataToShare = [];	  
		$scope.shareMyData = function (myValue) {
			//$scope.dataToShare = myValue;
			//shareData.addData($scope.dataToShare);
		}
		$scope.url = "https://localhost:8443/event/getEvent1/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENT INFO")
		var getEvent = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/event/getEvent1/' + id        
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
			$location.path("/viewAllEventsEx");
			console.log('GET Event FAILED! ' + JSON.stringify(response));
		});

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

	$scope.updateEvent = function(){
		console.log("Start updating");
		$scope.data = {};
		//$scope.building = JSON.parse(shareData.getData());
		//console.log($scope.building.id);
		//$scope.level = JSON.parse(shareData.getDataId());
		console.log($scope.event.id);
		var dataObj = {	
				id: $scope.event.id,
				units:$scope.event.units,
				event_title: $scope.event.event_title,
				event_content: $scope.event.event_content,
				event_description: $scope.event.event_description,
				event_approval_status: "processing",
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
			alert('EVENT IS SAVED!');
		});
		send.error(function(){
			alert('SAVING Event GOT ERROR BECAUSE UNIT IS NOT AVAILABLE!');
		});
	};	


	$scope.deleteEvent = function(){
		$scope.data = {};
		console.log("Start deleting event");
		$scope.event = JSON.parse(shareData.getData());
		console.log($scope.event.id);
		var tempObj ={eventId:$scope.event.id};
		console.log("fetch id "+ tempObj);
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.post("//localhost:8443/event/deleteEvent", JSON.stringify(tempObj)).then(function(response){
			//$scope.buildings = response.data;
			console.log("Cancel the EVENT");
		},function(response){
			alert("DID NOT Cancel EVENT");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

	}
	$scope.getEventByIdForDelete= function(){

		//var buildings ={name: $scope.name, address: $scope.address};
		//$http.post("//localhost:8443/building/getBuilding", JSON.stringify(tempObj))
		$scope.event = JSON.parse(shareData.getData());

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
			$location.path("/viewAllEventsEx");
			console.log('GET Booking FAILED! ' + JSON.stringify(response));
		});

	}
	
	
	
}]);


app.controller('bookingController', ['$scope','$http','$location','$routeParams','shareData', function ($scope, $http,$location, $routeParams, shareData) {
	$scope.viewBookings = function(){
		$scope.data = {};
		//var tempObj= {id:1};
		//console.log(tempObj)
		console.log("DISPLAY ALL BOOKINGS");
		$scope.bookings = JSON.parse(shareData.getData());
		var url = "https://localhost:8443/booking/viewAllBookings";	
	    console.log("BOOKING DATA ARE OF THE FOLLOWING: " + $scope.bookings);	
	};

	$scope.deleteBooking = function(id){
		$scope.data = {};
		console.log("Start deleting event");
		$scope.url = "https://localhost:8443/booking/deleteBooking/"+id;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENT INFO")
		var deleteBooking = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/booking/deleteBooking/' + id        
		});
		console.log("Deleting the event using the url: " + $scope.url);
		deleteBooking.success(function(response){
			console.log('DELETE BOOKING SUCCESS! ' + JSON.stringify(response));
			console.log("ID IS " + id);
			//shareData.addData(JSON.stringify(response));
		});
		deleteBooking.error(function(response){
			$location.path("/viewAllEventsEx");
			console.log('DELETE BOOKING FAILED! ' + JSON.stringify(response));
		});
				
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
	
}]);