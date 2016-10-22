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
    $scope.deletePolicy = function(){
    	if(confirm('CONFIRM TO DELETE POLICY ?')){
			var tempObj ={eventId:$scope.policy.id};
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

/*app.controller('deletePolicyController', ['$scope',  '$timeout','$http','shareData','$state', function ($scope,  $timeout,$http ,shareData,$state) {
	angular.element(document).ready(function () {
		$scope.policy = shareData.getData();
	});
	$scope.deletePolicy = function(){}
}])*/

