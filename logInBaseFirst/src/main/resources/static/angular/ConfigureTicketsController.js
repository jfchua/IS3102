app.controller('configureTicketsController', ['$scope','$rootScope','$http','$state','shareData','ModalService', function ($scope, $rootScope, $http,$state, shareData,ModalService) {

	angular.element(document).ready(function () {
		$scope.data = {};
		var tempObj= {eventId: $rootScope.event.id};
		console.log("TTTTTT" + JSON.stringify(tempObj));
		console.log(tempObj)
		$http.post("//localhost:8443/viewTicketCategories",JSON.stringify(tempObj)).then(function(response){
			$scope.tickets = response.data;
			console.log("DISPLAY ALL Categories");
			console.log($scope.tickets);

		},function(response){
			console.log("did not view all ticket categories");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)

		$http.post("//localhost:8443/getDiscounts",JSON.stringify(tempObj)).then(function(response){
			$scope.discounts = response.data;
			console.log("DISPLAY ALL discounts");
			console.log($scope.discounts);

		},function(response){
			//alert("did not view discounts");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	

		$http.post("//localhost:8443/getBeacons",JSON.stringify(tempObj)).then(function(response){
			$scope.beacons = response.data;
			console.log("DISPLAY ALL BEACONS");
			console.log($scope.beacons);

		},function(response){
			//alert("did not view discounts");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)	
	});

	$scope.passCategory = function(category){	
		console.log("PASSINGATEGORY1" + JSON.stringify(category));
		shareData.addData(category);

		//$rootScope.category = category;

		//$state.go("dashboard.configureTickets");
	}

	$scope.passDiscount = function(dis){	
		console.log("PASSINGATEGORY1" + JSON.stringify(dis));
		shareData.addData(dis);
	}
	
	$scope.passBeacon = function(beacon){	
		console.log("PASSINGATEGORY1" + JSON.stringify(beacon));
		shareData.addData(beacon);
	}

	$scope.submitAddDiscount = function(){

		if ( !$scope.retailerName || !$scope.discountMessage ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Please ensure you have entered all fields correctly',
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
				eventId: $rootScope.event.id,
				retailerName: $scope.retailerName,
				message: $scope.discountMessage			
		};

		$http.post("//localhost:8443/addDiscount", JSON.stringify(dataObj)).then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Discount added successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureDiscountsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Server error in adding discount',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureDiscountsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});




	}

	
	$scope.submitAddBeacon = function(){

		if ( !$scope.b.beaconUUID || !$scope.b.message ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Please ensure you have entered all fields correctly',
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
				eventId: $rootScope.event.id,
				beaconId: $scope.b.beaconUUID,
				message: $scope.b.message,			
		};
        console.log("***");
        console.log(dataObj);
        console.log(JSON.stringify(dataObj));
		$http.post("//localhost:8443/addBeacon", JSON.stringify(dataObj)).then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Beacon added successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureBeaconsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Server error in adding discount',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureBeaconsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	}


	$scope.deleteDiscount = function(dis){
		var dataObj = {			
				eventId: dis.id						
		};
		$http.post("//localhost:8443/deleteDiscount", JSON.stringify(dataObj)).then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Discount deleted successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go($state.current, {}, {reload: true}); 
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))

		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: response.data,
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
	
	$scope.deleteBeacon = function(bc){
		var dataObj = {			
				beaconId: bc.id						
		};
		$http.post("//localhost:8443/deleteBeacon", JSON.stringify(dataObj)).then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Beacon deleted successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go($state.current, {}, {reload: true}); 
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))

		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: response.data,
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

	$scope.deleteCat = function(category){
		var dataObj = {			
				eventId: category.id						
		};
		$http.post("//localhost:8443/deleteCategory", JSON.stringify(dataObj)).then(function(response){
			$scope.requestedTicket = true;
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Category deleted successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go($state.current, {}, {reload: true}); 
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
			//if (confirm('LEVEL IS SAVED! GO BACK TO VIEW BUILDINGS?'))

		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: response.data,
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

	$scope.submitAddCategory = function(){
		//categoryName $scope.numTickets);
		//categoryPrice

		if ( !$scope.categoryName || !$scope.categoryPrice || !$scope.numTickets ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Please ensure you have entered all fields correctly',
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
		else if ( $scope.categoryName.length > 30 ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Category name should not be longer than 30 characters',
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
		else if ($scope.categoryPrice<0.01){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Category price must be greater than $0.01',
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
		else if ($scope.numTickets < 1 ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'At least 1 ticket must be sold for this category!',
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
				eventId: $rootScope.event.id,
				name: $scope.categoryName,
				price: $scope.categoryPrice,
				numTickets: $scope.numTickets,						
		};
		$http.post("//localhost:8443/addCategory", JSON.stringify(dataObj)).then(function(response){
			$scope.requestedTicket = true;
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Category added successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureTicketsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Server error in adding category',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureTicketsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});


	}


}]);


app.controller('updateCategoryController', ['$scope', '$http','$state','$rootScope','shareData', 'ModalService',function ($scope, $http,$state, $rootScope, shareData,ModalService) {
	angular.element(document).ready(function () {
		$scope.category = angular.copy(shareData.getData());
		console.log(JSON.stringify($scope.category));
		$scope.categoryName = $scope.category.categoryName;
		$scope.categoryPrice = $scope.category.price;
		$scope.numTickets = $scope.category.numOfTickets;
		$scope.categoryId = $scope.category.id;

	}

	)
	$scope.submitUpdateCategory = function(){	

		if ( !$scope.categoryName || !$scope.categoryPrice || !$scope.numTickets ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Please ensure you have entered all fields correctly',
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
		else if ( $scope.categoryName.length > 30 ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Category name should not be longer than 30 characters',
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
		else if ($scope.categoryPrice<0.01){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Category price must be greater than $0.01',
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
		else if ($scope.numTickets < 1 ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'At least 1 ticket must be sold for this category!',
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
				categoryId: $scope.categoryId,
				name: $scope.categoryName,
				price: ($scope.categoryPrice).toString(),
				numTickets: $scope.numTickets,						
		};
		$http.post("//localhost:8443/updateCategory", JSON.stringify(dataObj)).then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Category updated successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureTicketsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL

		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: response.data,
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureTicketsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	}
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

app.controller('updateDiscountController', ['$scope','$rootScope','$http','$state','shareData','ModalService', function ($scope, $rootScope, $http,$state, shareData,ModalService) {
	angular.element(document).ready(function () {
		$scope.dis = angular.copy(shareData.getData());
		$scope.id = $scope.dis.id;
		$scope.retailerName = $scope.dis.retailerName;
		$scope.discountMessage = $scope.dis.discountMessage;

	})

	$scope.updateDiscount = function(){	

		if ( !$scope.retailerName || !$scope.discountMessage ){
			//alert($scope.retailerName + " " +$scope.discountMessage );
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Please ensure you have entered all fields correctly',
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
				discountId: $scope.id,
				retailerName: $scope.retailerName,
				message: $scope.discountMessage						
		};
		$http.post("//localhost:8443/updateDiscount", JSON.stringify(dataObj)).then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Discount updated successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureDiscountsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL

		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Server error in updating discount',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureDiscountsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	}


}])

app.controller('updateBeaconController', ['$scope','$rootScope','$http','$state','shareData','ModalService', function ($scope, $rootScope, $http,$state, shareData,ModalService) {
	angular.element(document).ready(function () {
		$scope.b = angular.copy(shareData.getData());
		//$scope.id = $scope.b.id;
		//$scope.retailerName = $scope.dis.retailerName;
		//$scope.discountMessage = $scope.dis.discountMessage;

	})

	$scope.updateBeacon = function(){	

		if (!$scope.b.message ){
			//alert($scope.retailerName + " " +$scope.discountMessage );
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Please ensure you have entered all fields correctly',
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
				beaconId: $scope.b.id,
				message: $scope.b.message,					
		};
		console.log("****");
        console.log(dataObj);
		$http.post("//localhost:8443/updateBeacon", JSON.stringify(dataObj)).then(function(response){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Beacon updated successfully',
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureBeaconsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL

		},function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: response.data,
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$state.go("IFMS.configureBeaconsEx");
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
		});
	}


}])


