app.controller('configureTicketsController', function ($scope, $rootScope, $http,$state, $routeParams, shareData) {

	angular.element(document).ready(function () {
		$scope.data = {};
		var tempObj= {eventId: $rootScope.event.id};
		console.log(tempObj)
		$http.post("//localhost:8443/viewTicketCategories",JSON.stringify(tempObj)).then(function(response){
			$scope.tickets = response.data;
			console.log("DISPLAY ALL Categories");
			console.log($scope.tickets);

		},function(response){
			alert("did not view all ticket categories");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	});

	$scope.passCategory = function(category){
		shareData.addData(category);
		console.log(JSON.stringify(category));
	}
	
	$scope.deleteCat = function(category){
		var dataObj = {			
				eventId: category.id						
		};
		$http.post("//localhost:8443/deleteCategory", JSON.stringify(dataObj)).then(function(response){
			$scope.requestedTicket = true;
			alert('Deleted category successfully!');
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
			//$state.go($state.current, {}, {reload: true}); 
		},function(response){
			alert("Did not delete category from server " + JSON.stringify(response) );
			//console.log("response is : ")+JSON.stringify(response);
		});
	}

	$scope.submitAddCategory = function(){
		//categoryName $scope.numTickets);
		//categoryPrice

		var dataObj = {			
				eventId: $rootScope.event.id,
				name: $scope.categoryName,
				price: $scope.categoryPrice,
				numTickets: $scope.numTickets,						
		};
		$http.post("//localhost:8443/addCategory", JSON.stringify(dataObj)).then(function(response){
			$scope.requestedTicket = true;
			alert('Added category successfully!');
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
			$state.go("dashboard.configureTicketsEx");
		},function(response){
			alert("Did not add the category into server " + JSON.stringify(response) );
			//console.log("response is : ")+JSON.stringify(response);
		});
		

	}


});