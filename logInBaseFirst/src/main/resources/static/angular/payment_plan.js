//VIEW VENDOR, ADD VENDOR
app.controller('paymentController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/payment/viewAllPaymentPlans").then(function(response){
			$scope.plans = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
			console.log($scope.plans);
		},function(response){
			//alert("did not view plans");
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Invoice is sent to the event organiser successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewAllPaymentPlans");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		send.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to generate invoice",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});

			//END SHOWMODAL

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		}); 
	}
	
	$scope.runTimer = function(){
		$http.get("//localhost:8443/payment/runTimer").then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Timer is activated successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewAllPaymentPlans");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to activate timer",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});

			//END SHOWMODAL

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Report is successfully generated.',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewAllPaymentPlans");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		send.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to generate report",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});

			//END SHOWMODAL

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


app.controller('addPaymentController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/payment/viewApprovedEvents").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
		},function(response){
			//alert("did not view plans");
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
			//$scope.totalRent = response;
			console.log(response);
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

		var send1 = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/payment/addPaymentPlan',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE PAYMENT");
		send1.success(function(){
			
			//alert('PAYMENT IS SAVED! DOWNLOAD THE INVOICE!! GOING BACK TO VIEW PAYMENT PLANS');
			
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
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Invoice is sent to the event organiser successfully',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						//$state.go("IFMS.viewAllOutstandingBalance");
					});
				});

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
			});
			send.error(function(data){
				ModalService.showModal({

					templateUrl: "views/errorMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "Fail to generate invoice",
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});

				//END SHOWMODAL

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
			});	
			$state.go("IFMS.viewAllPaymentPlans");
		});
		send1.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to save payment plan",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});

			//END SHOWMODAL

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	};

}]);



app.controller('updatePaymentController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
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
//alert('PAYMENT IS UPDATED! DOWNLOAD THE INVOICE!! GOING BACK TO VIEW PAYMENT PLANS');
			
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
				ModalService.showModal({

					templateUrl: "views/popupMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: 'Invoice is sent to the event organiser successfully',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
						//$state.go("dashboard.viewAllOutstandingBalance");
					});
				});

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
			});
			send.error(function(data){
				ModalService.showModal({

					templateUrl: "views/errorMessageTemplate.html",
					controller: "errorMessageModalController",
					inputs: {
						message: "Fail to generate invoice",
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						console.log("OK");
					});
				});

				//END SHOWMODAL

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};
			});	

			$state.go("IFMS.viewAllPaymentPlans");
		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to save payment plan",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});

			//END SHOWMODAL

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	};

}]);

app.controller('balanceController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/payment/viewAllOutstandingBalance").then(function(response){
			$scope.orgs = response.data;
			console.log("DISPLAY ALL ORGS OUTSTANDING BALANCE");
			console.log($scope.orgs);
		},function(response){
			//alert("did not view balance");
		}	
		)	
	});
	$scope.passOrganiser = function(id){
		shareData.addData(id);
	}
}]);


app.controller('eventListController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
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
			//$state.go("dashboard.viewAllOutstandingBalance");
			console.log('GET EVENTS FAILED! ');
		});
	});
}]);

app.controller('receivedPController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
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
			//$state.go("dashboard.viewAllOutstandingBalance");
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
			//alert('Get Payment Plan Details error!!!!!!!!!!');
		});			
	}
	
	$scope.updateReceivedPayment = function(){
		$scope.data = {};
		
				if (!$scope.chequeNum || !$scope.amountPaid || $scope.amountPaid <= 0){
					ModalService.showModal({

						templateUrl: "views/errorMessageTemplate.html",
						controller: "errorMessageModalController",
						inputs: {
							message: "Ensure that you have entered all fields",
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
					return;
				}
		
				if ($scope.amountPaid != $scope.paymentPlan.nextPayment){
					ModalService.showModal({

						templateUrl: "views/errorMessageTemplate.html",
						controller: "errorMessageModalController",
						inputs: {
							message: "Ensure that you have entered the correct amount",
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
					return;
				}
		
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
					ModalService.showModal({

						templateUrl: "views/popupMessageTemplate.html",
						controller: "errorMessageModalController",
						inputs: {
							message: 'Received payment is updated.',
						}
					}).then(function(modal) {
						modal.element.modal();
						modal.close.then(function(result) {
							console.log("OK");
							$state.go("IFMS.viewAllOutstandingBalance");
						});
					});

					$scope.dismissModal = function(result) {
						close(result, 200); // close, but give 200ms for bootstrap to animate

						console.log("in dissmiss");
					};
			
		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to update received payment",
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
	}
}]);

app.controller('eventWithTicketController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$scope.order_item = "id";
		$scope.order_reverse = false;
		$http.get("//localhost:8443/payment/viewAllEventsWithTicket").then(function(response){
			$scope.events = response.data;
			console.log("DISPLAY ALL EVENTS");
		},function(response){
			//alert("did not view events");
		}	
		)	
	});
	$scope.passEvent = function(id){
		shareData.addData(id);
	}
}]);


app.controller('ticketRController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
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
			//$state.go("dashboard.viewEventsWithTicketSales");
			console.log('GET PAYMENT FAILED! ');
		});
	});
	
	$scope.updateTicketRevenue = function(){
		$scope.data = {};
		
		if (!$scope.ticket || $scope.ticket <= 0){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered valid ticket revenue amount",
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
			return;
		}
		
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
					ModalService.showModal({

						templateUrl: "views/popupMessageTemplate.html",
						controller: "errorMessageModalController",
						inputs: {
							message: 'Ticket revenue is updated.',
						}
					}).then(function(modal) {
						modal.element.modal();
						modal.close.then(function(result) {
							console.log("OK");
							$state.go("IFMS.viewEventsWithTicketSales");
						});
					});

					$scope.dismissModal = function(result) {
						close(result, 200); // close, but give 200ms for bootstrap to animate

						console.log("in dissmiss");
					};		
			
		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to update ticket revenue",
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
	}
	
}]);

app.controller('outgoingController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
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
			//$state.go("dashboard.viewEventsWithTicketSales");
			console.log('GET PAYMENT FAILED! ');
		});			
	});
	
	$scope.updateOutgoingPayment = function(){
		$scope.data = {};
		if (!$scope.cheque){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered cheque number before you proceed",
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
			return;
		}
						
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Outgoing payment is updated.',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewEventsWithTicketSales");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};					
		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to update outgoing payment",
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
	}
	
}]);


app.controller('paymentHistoryController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData, ModalService) {
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
			//$state.go("dashboard.viewAllOutstandingBalance");
			console.log('GET PAYMENTS FAILED! ');
		});
	});
}]);


app.controller('policyController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService) {
	console.log("HAHAHA");

	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("https://localhost:8443/policy/viewPaymentPolicy").then(function(response){
			console.log(response);
			console.log(response.data);
			console.log("HAHAHA");
			$scope.policy = response;
			console.log("DISPLAY ALL PAYMENT PLANS");
			console.log($scope.policy);
		},function(response){
			//alert("did not view plans");
		}	
		)
		
		$http.get("//localhost:8443/policy/viewTurnover").then(function(response){
			$scope.turnover = response.data;
			console.log("DISPLAY TURNOVER RATIO");
			console.log($scope.turnover);
		},function(response){
			//alert("did not view turnover ratio");
		}	
		)
		
		$http.get("//localhost:8443/policy/viewDays").then(function(response){
			$scope.days = response.data;
			console.log("DISPLAY DSO");
			console.log($scope.days);
		},function(response){
			//alert("did not view DSO");
		}	
		)
		
	});
	$scope.passPaymentPolicy = function(policy){
		shareData.addData(policy);
	}
    $scope.deletePolicy = function(){
    	ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: 'Do you wish to delete payment policy '+$scope.policy.id+' ?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					$scope.data = {};
					console.log("Start deleting policy");
					//$scope.vendor = shareData.getData();
					console.log($scope.policy.id);
					var tempObj ={id:$scope.policy.id};
					console.log("fetch id "+ tempObj);
					$http.post("//localhost:8443/policy/deletePaymentPolicy", JSON.stringify(tempObj)).then(function(response){
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Payment policy deleted successfully',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("IFMS.viewPaymentPolicy");
							});
						});

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//END SHOWMODAL

					},function(data){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: "Fail to delete payment policy",
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								$state.go("IFMS.viewPaymentPolicy");
							});
						});

						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

							console.log("in dissmiss");
						};
						//END SHOWMODAL
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
 
	$scope.addPaymentPolicy = function(){
		
		$scope.data = {};
		if (!$scope.policy.depositRate || !$scope.policy.subsequentNumber  || !$scope.policy.numOfDueDays 
				||!$scope.policy.interimPeriod){
			ModalService.showModal({
				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered all the fields",
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
			return;
		}
		if ($scope.policy.depositRate < 0 || $scope.policy.depositRate > 1){
			ModalService.showModal({
				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered valid deposit rate",
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
			return;
		}
		if ($scope.policy.subsequentNumber < 0 || $scope.policy.numOfDueDays < 0 ||$scope.policy.interimPeriod < 0 ){
			ModalService.showModal({
				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered valid values",
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
			return;
		}
		
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Payment policy is saved.',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewPaymentPolicy");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};								
		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to save payment policy",
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

app.controller('updatePolicyController', ['$scope', '$http','$state','$routeParams','shareData','ModalService', function ($scope, $http,$state, $routeParams, shareData,ModalService) {
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
		if (!$scope.policy.depositRate || !$scope.policy.subsequentNumber  || !$scope.policy.numOfDueDays 
				||!$scope.policy.interimPeriod){
			ModalService.showModal({
				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered all the fields",
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
			return;
		}
		if ($scope.policy.depositRate < 0 || $scope.policy.depositRate > 1){
			ModalService.showModal({
				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered valid deposit rate",
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
			return;
		}
		if ($scope.policy.subsequentNumber < 0 || $scope.policy.numOfDueDays < 0 ||$scope.policy.interimPeriod < 0 ){
			ModalService.showModal({
				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered valid values",
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
			return;
		}
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Payment policy is saved.',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewPaymentPolicy");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};					
		});
		send.error(function(){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to save payment policy",
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


app.controller('invoiceController', ['$scope', '$http','ModalService','$state', function ($scope, $http,ModalService,$state) {
	angular.element(document).ready(function () {
		$scope.data = {};	
		$http.get("//localhost:8443/payment/viewAllPaymentPlans").then(function(response){
			$scope.plans = response.data;
			console.log("DISPLAY ALL PAYMENT PLANS");
		},function(response){
			//alert("did not view plans");
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
			//alert('Get Payment Plan Details error!!!!!!!!!!');
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
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Invoice is sent to the event organiser successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.viewAllPaymentPlans");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
		send.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Fail to generate invoice",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
				});
			});

			//END SHOWMODAL

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	};
}]);


