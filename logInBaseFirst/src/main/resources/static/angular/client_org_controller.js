app.controller('clientOrgController', ['$scope', '$http','$location','ModalService', function ($scope, $http, $location,ModalService) {
	$scope.genders=['COMMONINFRA','PROPERTY','EVENT','FINANCE', 'TICKETING', 'BI'];
	$scope.selection=[];

	$scope.toggleSelection = function toggleSelection(gender) {
		var idx = $scope.selection.indexOf(gender);
		if (idx > -1) {
			// is currently selected
			$scope.selection.splice(idx, 1);
		}
		else {
			// is newly selected
			$scope.selection.push(gender);
		}
	};

	$scope.checkDateErr = function(startDate,endDate) {
		$scope.errMessage = '';
		var curDate = new Date();

		if(new Date(startDate) > new Date(endDate)){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "End Date should be after start date",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.event.event_end_date = '';
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};

			return false;
		}
		if(new Date(startDate) < curDate){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Start date should not be before today",
				}
			}).then(function(modal) {
				modal.element.modal();
				modal.close.then(function(result) {
					console.log("OK");
					$scope.event.event_start_date = '';
				});
			});

			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//END SHOWMODAL
			return false;
		}
	};

	
	$scope.submit = function(){
		//alert("SUCCESS");
		$scope.data = {};
		console.log("** Passing data object of " + dataObj);
		var dataObj = {
				name: $scope.clientOrg.name,
				email: $scope.clientOrg.email,
				subscription: $scope.selection,
				nameAdmin: $scope.clientOrg.nameAdmin,
				fee: ($scope.clientOrg.fee).toString(),
				start: ($scope.clientOrg.start).toString(),
				end: ($scope.clientOrg.end).toString(),
				address: $scope.clientOrg.address,
				postal: $scope.clientOrg.postal,
				phone: $scope.clientOrg.phone,
		};

		console.log("REACHED HERE FOR SUBMIT BUILDING " + JSON.stringify(dataObj));

		var send = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/addClientOrganisation',
			data    : dataObj //forms user object
		});

		console.log("SAVING THE CLIENT ORG");
		send.success(function(data){
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Client organisation saved successsfully!',
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
			$location.path("/dashboard");
		});
		send.error(function(data){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: data,
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

	/*$scope.view = function(){
			var dataObj = {
					name: $scope.building.name,
					numFloor: $scope.building.numFloor,
					address: $scope.building.address,
					city: $scope.building.city,
					postalCode: $scope.building.postalCode,
					filePath: $scope.building.filePath
			}
		};*/

}]);





//////////VIEW CLIENT ORGS//////////

app.controller('viewClientOrgs', ['$scope','$http', '$location','ModalService',
                                  function($scope, $http,$location,ModalService) {

	$scope.genders=['Property System','Event Management System','Finance System'];
	$scope.selection=[];

	$scope.toggleSelection = function toggleSelection(gender) {
		var idx = $scope.selection.indexOf(gender);
		if (idx > -1) {
			// is currently selected
			$scope.selection.splice(idx, 1);
		}
		else {
			// is newly selected
			$scope.selection.push(gender);
		}
	};
	// END OF SUB SYSTEM ASSIGNMENT

	$scope.Profiles = [];

	$scope.send = function(){
		console.log("REACHED HERE FOR FETCHING ALL ORGS");

		var fetch = $http({
			method  : 'GET',
			url     : 'https://localhost:8443/user/viewClientOrgs',
			//forms user object
		});

		console.log("fetching the orgs list.......");
		fetch.success(function(response){

			$scope.Profiles = response;
			//	alert('FETCHED ALL orgs SUCCESS!!! ' + JSON.stringify(response));
		});
		fetch.error(function(response){
			alert('FETCH ALL orgs FAILED!!!');
		});

		//alert('done with viewing users');
		//$location.path('/viewUserList');
	}
	$scope.send();


	$scope.entity = {};
	$scope.name = "";


	$scope.updateValue = function(name){
		$scope.name = name;

	};
	console.log("SENDING THE NAME: " + $scope.name);
	$scope.save = function(index){
		$scope.Profiles[index].editable = true;
		//$scope.entity = $scope.Profiles[index];
		$scope.entity = $scope.Profiles[index];
		//$scope.entity.index = index;
		$scope.entity.editable = true;


		$scope.updateValue = function(name){
			//alert("addgin " + name + " to " + $scope.name);
			$scope.name = name;
		};


		var Edit = {
				newname: $scope.name,
				name: $scope.entity.organisationName,
				subsys: $scope.selection		
		}


		var toEdit = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/updateClientOrg',
			data 	: Edit
			//forms user object
		});

		console.log("fetching the user list.......");
		toEdit.success(function(response){
			$scope.Profiles = response;
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Client organisation updated successsfully!',
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
			$scope.send();
		});
		toEdit.error(function(response){
			alert('Error, ');
		});

	}


	$scope.checkRole =function(role,profile){
		var roles=profile.systemSubscriptions;
		console.log(roles);
		var hasRole=false;
		var index = 0;
		angular.forEach(roles, function(item){             
			if(hasRole==false&&role == roles[index]){	
				hasRole=true;
				console.log(hasRole);
			}else{
				index = index + 1;
			}
		});      
		return hasRole;

	} ;


	$scope.delete = function(index){
		//$scope.Profiles.splice(index,1);
		//send to db to delete
		//var index = $scope.Profiles[index];
		//console.log("DEX" + index);
		$scope.entity = $scope.Profiles[index];


		console.log(JSON.stringify($scope.entity));
		var toDel = {
				id: $scope.entity.id,
		}

		//var toDel = $scope.Profiles[index];
		console.log("ITEM ID TO DELETE: " + JSON.stringify(toDel));

		var del = $http({
			method  : 'POST',
			url     : 'https://localhost:8443/user/deleteClientOrg',
			data 	: toDel
			//forms user object
		});

		console.log("fetching the user list.......");
		del.success(function(response){
			//$scope.Profiles = response;
			ModalService.showModal({

				templateUrl: "views/popupMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Client organisation deleted successsfully!',
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
		del.error(function(response){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: 'Did not managed to delete the client organisation',
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


		$scope.Profiles.splice(index, 1);
	}

	$scope.edit = function(index){
		$scope.Profiles[index].editable = true;
		//send to db to save

	}

	$scope.add = function(){
		$scope.Profiles.push({
			name : "",
			email : "",
			editable : true
		})
	}
}
]);