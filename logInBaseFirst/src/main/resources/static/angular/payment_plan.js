//VIEW VENDOR, ADD VENDOR
app.controller('paymentController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/payment/viewAllPaymentPlans").then(function(response){
			$scope.plans = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
		},function(response){
			alert("did not view plans");
		}	
		)	
	});
	$scope.passPaymentPlan = function(plan){
		shareData.addData(plan);
	}
/*
	$scope.addPaymentPlan = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {			
				eventId: $scope.plan.eventId,
				total: $scope.plan.total,
				deposit: $scope.plan.deposit,
				subsequent_number: $scope.plan.subsequent_number,
				subsequent: $scope.plan.subsequent,
		};

		console.log("REACHED HERE FOR SUBMIT PAYMENT PLAN " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/addPaymentPlan',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE PAYMENT");
		send.success(function(){		
			alert('PAYMENT IS SAVED! GOING BACK TO VIEW BUILDINGS');
			$state.go("dashboard.viewAllPaymentPlans");
		});
		send.error(function(){
			alert('SAVING PAYMENT GOT ERROR!');
		});
	};*/

}]);


app.controller('addPaymentController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/eventManager/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
		},function(response){
			alert("did not view plans");
		}	
		)	
	});
	$scope.plan = {};	
	$scope.currentlySelectedEvent;
	$scope.selectEvent = function(){
		$scope.selectedEvent=$scope.currentlySelectedEvent;
	}
	$scope.checkRent = function(eventId){
		console.log("start checking rent");
		console.log(eventId);
		$scope.data = {};
		var dataObj = {event: eventId};
		console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/checkRent',
			data    : dataObj //forms user object
		});
		send.success(function(response){
			$scope.plan = response;
			$scope.totalRent = $scope.plan.total/1.07;
			$scope.totalRentAfter = $scope.plan.total;
			//$scope.plan.total= response*1.07;
			console.log($scope.totalRent);
		});
		send.error(function(response){
			$scope.totalRent = response;
			console.log($scope.totalRent);
		});	
	}
	$scope.addPaymentPlan = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {			
				eventId: $scope.plan.id,
				total: $scope.plan.total,
				deposit: $scope.plan.deposit,
				subsequent_number: $scope.plan.subsequent_number,
		};

		console.log("REACHED HERE FOR SUBMIT PAYMENT PLAN " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/addPaymentPlan',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE PAYMENT");
		send.success(function(){		
			alert('PAYMENT IS SAVED! GOING BACK TO VIEW PAYMENT PLANS');
			$state.go("dashboard.viewAllPaymentPlans");
		});
		send.error(function(){
			alert('SAVING PAYMENT GOT ERROR!');
		});
	};

}]);





app.controller('policyController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/policy/viewPaymentPolicy").then(function(response){
			$scope.policy = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
			console.log($scope.policy);
		},function(response){
			alert("did not view plans");
		}	
		)	
	});
	$scope.passPaymentPolicy = function(policy){
		shareData.addData(policy);
	}
    $scope.deletePolicy = function(){
    	console.log("Start deleting");
    	if(confirm('CONFIRM TO DELETE POLICY ?')){
			var tempObj ={id:$scope.policy.id};
			console.log("fetch id "+ tempObj);
			//var buildings ={name: $scope.name, address: $scope.address};
			$http.post("//localhost:8443/policy/deletePaymentPolicy", JSON.stringify(tempObj)).then(function(response){
				//$scope.buildings = response.data;
				console.log("DELETE");
				alert('PAYMENT POLICY IS DELETED! GOING BACK TO VIEW PAYMENT POLICY...');
				//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))
				$state.go("dashboard.viewPaymentPolicy");
			},function(response){
				alert("DID NOT DELETE PAYMENT POLICY");
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)
		}
    }
 
	$scope.addPaymentPolicy = function(){
		
		$scope.data = {};
		var dataObj = {			
				depositRate: $scope.policy.depositRate,
				subsequentNumber: $scope.policy.subsequentNumber,
				dueDays: $scope.policy.numOfDueDays,
		};
		console.log("REACHED HERE FOR SUBMIT PAYMENT POLICY " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/policy/addPaymentPolicy',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE PAYMENT POLICY");
		send.success(function(){		
			alert('PAYMENT POLICY IS SAVED! GOING BACK TO VIEW PAYMENTPOLICY');
			$state.go("dashboard.viewPaymentPolicy");
		});
		send.error(function(){
			alert('SAVING PAYMENT POLICY GOT ERROR!');
		});
	};

}]);	

app.controller('updatePolicyController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.policy1 = shareData.getData();	
		//console.log("$scope.policy1");
		console.log($scope.policy1);
		//console.log("$scope.policy2");
		var dataObj = {						
				depositRate: $scope.policy1.depositRate,
				subsequentNumber: $scope.policy1.subsequentNumber,
				dueDays: $scope.policy1.numOfDueDays,
		};	
		$scope.policy = angular.copy($scope.policy1);
		//console.log("$scope.policy3");
		console.log($scope.policy);
		//console.log("$scope.policy4");
	});

	$scope.updatePaymentPolicy = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {				
		        id:$scope.policy.id,
				depositRate: $scope.policy.depositRate,
				subsequentNumber: $scope.policy.subsequentNumber,
				dueDays: $scope.policy.numOfDueDays,
		};
		console.log("REACHED HERE FOR SUBMIT PAYMENT POLICY " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/policy/updatePaymentPolicy',
			data    : dataObj //forms user object
		});
		console.log("SAVING THE PAYMENT POLICY");
		send.success(function(){		
			alert('PAYMENT POLICY IS SAVED! GOING BACK TO VIEW PAYMENTPOLICY');
			$state.go("dashboard.viewPaymentPolicy");
		});
		send.error(function(){
			alert('SAVING PAYMENT POLICY GOT ERROR!');
		});
	};

}]);

/*app.controller('deletePolicyController', ['$scope',  '$timeout','$http','shareData','$state', function ($scope,  $timeout,$http ,shareData,$state) {
	angular.element(document).ready(function () {
		$scope.policy = shareData.getData();
	});
	$scope.deletePolicy = function(){}
}])*/

