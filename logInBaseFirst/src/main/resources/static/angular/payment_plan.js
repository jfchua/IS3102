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

app.controller('policyController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/policy/viewPaymentPolicy").then(function(response){
			$scope.policy = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
		},function(response){
			alert("did not view plans");
		}	
		)	
	});
	$scope.passPaymentPolicy = function(policy){
		shareData.addData(policy);
	}

	$scope.addPaymentPolicy = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {			
				deposit_rate: $scope.policy.deposit_rate,
				subsequent_number: $scope.policy.subsequent_number,
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
		var dataObj = {						
				deposit_rate: $scope.policy1.deposit_rate,
				subsequent_number: $scope.policy1.subsequent_number,
		};	
		$scope.policy = angular.copy($scope.policy1)	
	});

	$scope.updatePaymentPolicy = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {				
		        id:$scope.policy.id,
				deposit_rate: $scope.policy.deposit_rate,
				subsequent_number: $scope.policy.subsequent_number,
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
