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
	};

}]);	

