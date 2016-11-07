//VIEW VENDOR, ADD VENDOR
app.controller('paymentController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/payment/viewAllPaymentPlans").then(function(response){
			$scope.plans = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
			console.log($scope.plans);
		},function(response){
			alert("did not view plans");
		}	
		)
		
		$scope.checkFinish = function(plan){
			//console.log("MSMSMS");
			//console.log(plan.payable > 0);
			return (plan.payable > 0);
		}
	});
	$scope.generateInvoice = function(id){
		var dataObj = {
				id: id,
		};

		console.log("REACHED HERE FOR AUDIT LOG " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/downloadSubseInvoice',
			data    : dataObj, 
			responseType: 'arraybuffer'
		});

		console.log("DOWNLOADING");
		send.success(function(data){
			console.log(JSON.stringify(data));
			var file = new Blob([data], {type: 'application/pdf'});
			var fileURL = URL.createObjectURL(file);
			//window.open(fileURL);
			$state.reload();
			alert('DOWNLOADED!');
		});
		send.error(function(data){
			alert('DOWNLOAD GOT ERROR!');
		}); 
	}
	
	$scope.runTimer = function(){
		$http.get("//localhost:8443/payment/runTimer").then(function(response){
			alert("timer is running");
		},function(response){
			alert("fail to activate timer");
		}	
		)	
	}
	
	$scope.paymentPlanNull = function(paymentPlan){
		  return !(paymentPlan === null)
	}
	$scope.passPaymentPlan = function(plan){
		shareData.addData(plan);
	}
	$scope.generateReport = function(){
		$scope.data = {};

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/downloadReport',
			responseType: 'arraybuffer'
		});

		console.log("DOWNLOADING");
		send.success(function(data){
			console.log(JSON.stringify(data));
			var file = new Blob([data], {type: 'application/pdf'});
			var fileURL = URL.createObjectURL(file);
			window.open(fileURL);
			alert('DOWNLOADED!');
		});
		send.error(function(data){
			alert('DOWNLOAD GOT ERROR!');
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


app.controller('addPaymentController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/payment/viewApprovedEvents").then(function(response){
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
				total: ($scope.plan.total).toString(),
				deposit:($scope.plan.deposit).toString(),
				subsequent_number: $scope.plan.subsequentNumber,
		};

		var dataObj1 = {
				id: $scope.plan.id,
		};
		
		console.log("SUBMIT PAYMENT PLAN");
		console.log(JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/addPaymentPlan',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE PAYMENT");
		send.success(function(){
			
			alert('PAYMENT IS SAVED! DOWNLOAD THE INVOICE!! GOING BACK TO VIEW PAYMENT PLANS');
			
			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/payment/downloadInvoice',
				data    : dataObj1, 
				responseType: 'arraybuffer'
			});
            console.log("BEFORE");
            console.log(dataObj1);
			console.log("DOWNLOADING");
			send.success(function(data){
				console.log(JSON.stringify(data));
				var file = new Blob([data], {type: 'application/pdf'});
				var fileURL = URL.createObjectURL(file);
				//window.open(fileURL);
				alert('DOWNLOADED!');
			});
			send.error(function(data){
				alert('DOWNLOAD GOT ERROR!');
			});	
			$state.go("dashboard.viewAllPaymentPlans");
		});
		send.error(function(){
			alert('SAVING PAYMENT GOT ERROR!');
		});
	};

}]);



app.controller('updatePaymentController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.plan1 = shareData.getData();	
		console.log("$scope.policy1");
		console.log($scope.plan1);
		console.log("$scope.policy2");
		
		var dataObj = {		
				id : $scope.plan1.id,
				total : $scope.plan1.total,
				deposit: $scope.plan1.deposit,
				depositRate: $scope.plan1.depositRate,
				subsequentNumber: $scope.plan1.subsequentNumber,
				//nextInvoice: $scope.plan1.nextInvoice,
		};	
		$scope.plan = angular.copy($scope.plan1);
		console.log("$scope.policy3");
		console.log($scope.plan);
		console.log("$scope.policy4");
		$scope.checkStatus = function(){
			//console.log($scope.plan1.deposit == $scope.plan1.nextPayment);
			return ($scope.plan1.deposit == $scope.plan1.nextPayment);
		}
	});
	
	
	
	$scope.updatePaymentPlan = function(){
		//alert("SUCCESS");
		$scope.data = {};
		var dataObj = {			
				id: $scope.plan.id,
				depositRate: ($scope.plan.depositRate).toString(),
				subsequent_number: $scope.plan.subsequentNumber,
				//nextInvoice: $scope.plan.nextInvoice,
		};
		var dataObj1 = {
				id: $scope.plan.id,
		};

		console.log("SUBMIT PAYMENT PLAN");
		console.log(JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/updatePaymentPlan',
			data    : dataObj //forms user object
		});

		console.log("UPDATING THE PAYMENT");
		send.success(function(){		
alert('PAYMENT IS UPDATED! DOWNLOAD THE INVOICE!! GOING BACK TO VIEW PAYMENT PLANS');
			
			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/payment/downloadInvoiceForUpdate',
				data    : dataObj1, 
				responseType: 'arraybuffer'
			});

			console.log("DOWNLOADING");
			send.success(function(data){
				console.log(JSON.stringify(data));
				var file = new Blob([data], {type: 'application/pdf'});
				var fileURL = URL.createObjectURL(file);
				//window.open(fileURL);
				alert('DOWNLOADED!');
			});
			send.error(function(data){
				alert('DOWNLOAD GOT ERROR!');
			});	

			$state.go("dashboard.viewAllPaymentPlans");
		});
		send.error(function(){
			alert('SAVING PAYMENT GOT ERROR!');
		});
	};

}]);

app.controller('balanceController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/payment/viewAllOutstandingBalance").then(function(response){
			$scope.orgs = response.data;
			console.log("DISPLAY ALL ORGS OUTSTANDING BALANCE");
			console.log($scope.orgs);
		},function(response){
			alert("did not view balance");
		}	
		)	
	});
	$scope.passOrganiser = function(id){
		shareData.addData(id);
	}
}]);


app.controller('eventListController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.org = shareData.getData();
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$scope.url = "https://localhost:8443/payment/viewListOfEvents/"+$scope.org;
		//$scope.dataToShare = [];
		console.log("GETTING THE EVENTS");
		var getEvents = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/payment/viewListOfEvents/' + $scope.org,

		});
		console.log("Getting the events using the url: " + $scope.url);
		getEvents.success(function(response){
			console.log('GET EVENTS SUCCESS! ');
			console.log(JSON.stringify(response));
			console.log(response);
			$scope.events = response;
		});
		getEvents.error(function(response){
			$state.go("dashboard.viewAllOutstandingBalance");
			console.log('GET EVENTS FAILED! ');
		});
	});
}]);

app.controller('receivedPController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.org = shareData.getData();
		$scope.url = "https://localhost:8443/payment/viewListOfPaymentPlans/"+$scope.org;
		//$scope.dataToShare = [];
		console.log("GETTING THE Payment Plans");
		var getEvents = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/payment/viewListOfPaymentPlans/' + $scope.org,

		});
		console.log("Getting the payment plans using the url: " + $scope.url);
		getEvents.success(function(response){
			console.log('GET EVENTS SUCCESS! ');
			console.log(response);
			$scope.plans = response;
			$scope.selectedPlan;
		});
		getEvents.error(function(response){
			$state.go("dashboard.viewAllOutstandingBalance");
			console.log('GET PLANS FAILED! ');
		});
	});
	$scope.currentlySelectedPlan;
	$scope.selectPlan = function(){
		$scope.selectedPlan=$scope.currentlySelectedPlan;
	}
	console.log("finish selecting payment plan");
	
	$scope.getPlan = function(id){
		$scope.dataToShare = [];
		$scope.url = "https://localhost:8443/payment/getPaymentPlan/"+id;
		console.log("GETTING THE PLAN INFO")
		var getLevels = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/payment/getPaymentPlan/'+id,
	});
		console.log("Getting the payment plan using the url: " + $scope.url);
		getLevels.success(function(response){
			$scope.paymentPlan = response;
			console.log("RESPONSE IS" + JSON.stringify(response));
		});
		getLevels.error(function(){
			alert('Get Payment Plan Details error!!!!!!!!!!');
		});			
	}
	
	$scope.updateReceivedPayment = function(){
		$scope.data = {};
		var dataObj = {			
				id: $scope.paymentPlan.id,
				amountPaid: ($scope.amountPaid).toString(),
				cheque: $scope.chequeNum,
				nextInvoice: $scope.paymentPlan.nextInvoice,
		};
		console.log("REACHED HERE FOR SUBMIT PAYMENT Plan " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/updateReceivedPayment',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE PAYMENT PLan");
		send.success(function(){		
			alert('PAYMENT IS SAVED! GOING BACK TO VIEW ALL OUTSTANDING BALANCES');
			$state.go("dashboard.viewAllOutstandingBalance");
		});
		send.error(function(){
			alert('SAVING PAYMENT GOT ERROR!');
		});
	}
}]);

app.controller('eventWithTicketController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/payment/viewAllEventsWithTicket").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENTS");
		},function(response){
			alert("did not view events");
		}	
		)	
	});
	$scope.passEvent = function(id){
		shareData.addData(id);
	}
}]);


app.controller('ticketRController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	$scope.payment={};
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.org = shareData.getData();
		$scope.url = "https://localhost:8443/payment/getPaymentViaEvent/"+$scope.org;
		//$scope.dataToShare = [];
		console.log("GETTING THE Payment Plan");
		var getEvents = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/payment/getPaymentViaEvent/' + $scope.org,

		});
		console.log("Getting the payment plan using the url: " + $scope.url);
		getEvents.success(function(response){
			console.log('GET PAYMENT PLAN SUCCESS! ');
			console.log(response);
			$scope.payment = response;
		});
		getEvents.error(function(response){
			$state.go("dashboard.viewEventsWithTicketSales");
			console.log('GET PAYMENT FAILED! ');
		});
	});
	
	$scope.updateTicketRevenue = function(){
		$scope.data = {};
		var dataObj = {			
				id: $scope.payment.id,
				ticket: ($scope.ticket).toString(),
				//cheque: $scope.cheque,
		};
		console.log("REACHED HERE FOR SUBMIT PAYMENT Plan " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/updateTicketRevenue',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE TICKET REVENUE");
		send.success(function(){		
			alert('TICKET REVENUE IS UPDATED! GOING BACK TO VIEW ALL EVENTS WITH TICKET SALES');
			$state.go("dashboard.viewEventsWithTicketSales");
		});
		send.error(function(){
			alert('UPDATING TICKET REVENUE GOT ERROR!');
		});
	}
	
}]);

app.controller('outgoingController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	$scope.payment={};
	$scope.amountToBePaid={};
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.org = shareData.getData();
		$scope.url = "https://localhost:8443/payment/getPaymentViaEvent/"+$scope.org;
		//$scope.dataToShare = [];
		console.log("GETTING THE Payment Plan");
		var getEvents = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/payment/getPaymentViaEvent/' + $scope.org,

		});
		console.log("Getting the payment plan using the url: " + $scope.url);
		getEvents.success(function(response){
			console.log('GET PAYMENT PLAN SUCCESS! ');
			console.log(response);
			$scope.payment = response;
			console.log($scope.payment.payable);
			console.log($scope.payment.ticketRevenue);
			$scope.amountToBePaid = (0-$scope.payment.payable).toString();
		});
		getEvents.error(function(response){
			$state.go("dashboard.viewEventsWithTicketSales");
			console.log('GET PAYMENT FAILED! ');
		});			
	});
	
	$scope.updateOutgoingPayment = function(){
		$scope.data = {};
		var dataObj = {			
				id: $scope.payment.id,
				toBePaid: $scope.amountToBePaid,
				cheque: $scope.cheque,
		};
		console.log("REACHED HERE FOR SUBMIT PAYMENT Plan " + JSON.stringify(dataObj));
		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/updateOutgoingPayment',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE OUTGOING PAYMENT");
		send.success(function(){		
			alert('OUTGOING PAYMENT IS UPDATED! GOING BACK TO VIEW ALL EVENTS WITH TICKET SALES');
			$state.go("dashboard.viewEventsWithTicketSales");
		});
		send.error(function(){
			alert('SAVING OUTGOING PAYMENT GOT ERROR!');
		});
	}
	
}]);


app.controller('paymentHistoryController', ['$scope', '$http','$state','$routeParams','shareData', function ($scope, $http,$state, $routeParams, shareData) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.org = shareData.getData();
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$scope.url = "https://localhost:8443/payment/getPaymentHistory/"+$scope.org;
		console.log("GETTING THE EVENTS");
		var getPayments = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/payment/getPaymentHistory/' + $scope.org,

		});
		console.log("Getting the payments using the url: " + $scope.url);
		getPayments.success(function(response){
			console.log('GET PAYMENTS SUCCESS! ');
			console.log(JSON.stringify(response));
			console.log(response);
			$scope.payments = response;
		});
		getPayments.error(function(response){
			$state.go("dashboard.viewAllOutstandingBalance");
			console.log('GET PAYMENTS FAILED! ');
		});
	});
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
		
		$http.get("//localhost:8443/policy/viewTurnover").then(function(response){
			$scope.turnover = response.data;
			console.log("DISPLAY TURNOVER RATIO");
			console.log($scope.turnover);
		},function(response){
			alert("did not view turnover ratio");
		}	
		)
		
		$http.get("//localhost:8443/policy/viewDays").then(function(response){
			$scope.days = response.data;
			console.log("DISPLAY DSO");
			console.log($scope.days);
		},function(response){
			alert("did not view DSO");
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
				interimPeriod: $scope.policy.interimPeriod,
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
				interimPeriod: $scope.policy1.interimPeriod,
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
				interimPeriod: $scope.policy.interimPeriod,
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


app.controller('invoiceController', ['$scope', '$http', function ($scope, $http) {
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
	$scope.currentlySelectedPlan;
	$scope.selectPlan = function(){
		$scope.selectedPlan=$scope.currentlySelectedPlan;
	}
	$scope.getInfo = function(id){
		console.log("start getting info for the selected payment plan");
		console.log(id);
		$scope.data = {};
		
		$scope.url = "https://localhost:8443/payment/getPaymentPlan/"+id;
		console.log("GETTING THE PLAN INFO")
		var getPlan = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/payment/getPaymentPlan/'+id,
	});
		console.log("Getting the payment plan using the url: " + $scope.url);
		getPlan.success(function(response){
			$scope.paymentPlan = response;
			console.log("RESPONSE IS");
			console.log(JSON.stringify(response));
		});
		getPlan.error(function(){
			alert('Get Payment Plan Details error!!!!!!!!!!');
		});			
		
	}
	
	$scope.generateInvoice = function(){
		$scope.data = {};
		//$scope.audit.endDate.setDate($scope.audit.endDate.getDate() + 1);
		var dataObj = {
				id: $scope.paymentPlan.id,
		};

		console.log("REACHED HERE FOR AUDIT LOG " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/downloadSubseInvoice',
			data    : dataObj, 
			responseType: 'arraybuffer'
		});

		console.log("DOWNLOADING");
		send.success(function(data){
			console.log(JSON.stringify(data));
			var file = new Blob([data], {type: 'application/pdf'});
			var fileURL = URL.createObjectURL(file);
			//window.open(fileURL);
			alert('DOWNLOADED!');
		});
		send.error(function(data){
			alert('Invoice has already been generated. Please select a new payment plan.');
		});
	};
}]);


