app.controller('clientOrgController', ['$scope', '$http','$state','$location','ModalService', function ($scope, $http, $state, $location, ModalService) {
	//$scope.genders=['COMMONINFRA','PROPERTY','EVENT','FINANCE', 'TICKETING', 'BI'];
	$scope.genders=['FINANCE', 'TICKETING', 'BI'];
	$scope.selection=[];
    $scope.systems = ['COMMONINFRA','PROPERTY','EVENT'];
    console.log($scope.systems);
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
		console.log("** Passing data object of");
		//console.log($scope.clientOrg);
		
		angular.forEach($scope.systems, function(item){             
			$scope.selection.push(item);
		});      
		
		if (!$scope.clientOrg|| !$scope.clientOrg.name || !$scope.clientOrg.address || !$scope.clientOrg.postal || !$scope.clientOrg.phone || !$scope.clientOrg.fee
				||!$scope.clientOrg.start || !$scope.clientOrg.end || !$scope.clientOrg.nameAdmin || !$scope.clientOrg.email){
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
		
		if (!(/^[0-9]{6}$/.test($scope.clientOrg.postal))){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered a valid postal code",
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
		
		if ( $scope.clientOrg.phone.length < 3 ||  $scope.clientOrg.phone.length > 11 ||!(/^[0-9]+$/.test( $scope.clientOrg.phone))  ){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered a valid contact number",
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

		/*
		if (!(/^.+@.+\\..+$/.test($scope.clientOrg.email))){
			ModalService.showModal({

				templateUrl: "views/errorMessageTemplate.html",
				controller: "errorMessageModalController",
				inputs: {
					message: "Ensure that you have entered a valid email address",
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
		}*/
			
		
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
					$state.go("dashboard.viewClientOrgs");
				});
			});
			$scope.dismissModal = function(result) {
				close(result, 200); // close, but give 200ms for bootstrap to animate

				console.log("in dissmiss");
			};
			//$state.go("dashboard.worksapce");
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

app.controller('viewClientOrgs', ['$scope','$http', '$location','$state','ModalService',
                                  function($scope, $http, $location,$state,ModalService) {

	$scope.genders=['COMMONINFRA','PROPERTY','EVENT', 'FINANCE', 'TICKETING', 'BI'];
	$scope.selection=[];
	$scope.systems = ['COMMONINFRA','PROPERTY','EVENT'];
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
	/*$scope.name = "";


	$scope.updateValue = function(name){
		$scope.name = name;

	};*/
	//console.log("SENDING THE NAME: " + $scope.name);
	$scope.check = function(gender){
		var index = ($scope.genders).indexOf(gender);
		return (index<3);
	}
	$scope.save = function(index){
		$scope.Profiles[index].editable = true;
		//$scope.entity = $scope.Profiles[index];
		$scope.entity = $scope.Profiles[index];
		//$scope.entity.index = index;
		$scope.entity.editable = true;

		//$scope.name="";
		//$scope.updateValue = function(name){
			//alert("addgin " + name + " to " + $scope.name);
			//$scope.name = name;
			
		//};
		angular.forEach($scope.systems, function(item){             
			$scope.selection.push(item);
		});     
		var Edit = {
				//newname: $scope.name,
				name: $scope.entity.organisationName,
				subsys: $scope.selection		
		}
		console.log("ZMS");
        console.log(Edit);
		console.log(JSON.stringify(Edit));
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
	
	$scope.showModal = function(profile,$parent) {
		//console.log("hahahaha");
		//console.log(profile);
		profile.start_date = new Date(profile.start_date);
		profile.end_date = new Date(profile.end_date);
		
	    // Just provide a template url, a controller and call 'showModal'.
		
	    ModalService.showModal({
	    	
	    	      templateUrl: "views/updateOrgTemplate.html",
	    	      controller: "updateOrgController",
	    	      inputs: {
	    	        title: "View and Update Client Organisation Details",
	    	        profile: profile
	    	      }
	    	    }).then(function(modal) {
	    	      modal.element.modal();
	    	      modal.close.then(function(result) {
	    	      var profile = result.profile;
	    	      if(profile == null)
	    	    	  $state.reload();
	    	      else
	    	    	  $parent.updateClient(profile);
	    	     
	    	      // console.log("in then");
	    	      // console.log("scope test1");	    	       
	    	      // console.log("scope test2");
	    	       
	    	      });
	    	    });

	  };//END SHOWMODAL
	  
	  $scope.dismissModal = function(result) {
		    close(result, 200); // close, but give 200ms for bootstrap to animate
		    $parent.updateClient();
		    console.log("in dissmiss");
		 };
	
		 $scope.updateClient=function(profile){
			 console.log("Update the client other details");
			
			 if (!profile.address || !profile.organisationName || !profile.postal || !profile.phone || !profile.fee
						||!profile.start_date || !profile.end_date){
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
							$state.reload();
						});
					});

					$scope.dismissModal = function(result) {
						close(result, 200); // close, but give 200ms for bootstrap to animate

						console.log("in dissmiss");
					};
					return;
				}
				
				if (!(/^[0-9]{6}$/.test(profile.postal))){
					ModalService.showModal({

						templateUrl: "views/errorMessageTemplate.html",
						controller: "errorMessageModalController",
						inputs: {
							message: "Ensure that you have entered a valid postal code",
						}
					}).then(function(modal) {
						modal.element.modal();
						modal.close.then(function(result) {
							console.log("OK");
                            $state.reload();
						});
					});

					$scope.dismissModal = function(result) {
						close(result, 200); // close, but give 200ms for bootstrap to animate

						console.log("in dissmiss");
					};
					return;
				}
				
				if ( profile.phone.length < 3 ||  profile.phone.length > 11 ||!(/^[0-9]+$/.test(profile.phone))  ){
					ModalService.showModal({

						templateUrl: "views/errorMessageTemplate.html",
						controller: "errorMessageModalController",
						inputs: {
							message: "Ensure that you have entered a valid contact number",
						}
					}).then(function(modal) {
						modal.element.modal();
						modal.close.then(function(result) {
							console.log("OK");
							$state.reload();
						});
					});

					$scope.dismissModal = function(result) {
						close(result, 200); // close, but give 200ms for bootstrap to animate

						console.log("in dissmiss");
					};
					return;
				}
				
			// console.log("scope test3");
		//	 console.log(   angular.element(document.getElementById('1')).scope());
			  var dataObj = {
				        id: profile.id,
				        address: profile.address,
				        name: profile.organisationName,
				        postal: profile.postal,
				        phone: profile.phone,
				        start_date: (profile.start_date).toString(),
				        end_date: (profile.end_date).toString(),
				        fee: (profile.fee).toString()
				    };
			  $http.post('/user/updateOrgDetails', JSON.stringify(dataObj)).then(function(response){
					console.log("finish updating");
					ModalService.showModal({
						templateUrl: "views/popupMessageTemplate.html",
						controller: "errorMessageModalController",
						inputs: {
							message: "Client organisation saved successfully",
						}
					}).then(function(modal) {
						modal.element.modal();
						modal.close.then(function(result) {
							console.log("OK");
							$state.go("dashboard.viewClientOrgs");
						});
					});

					$scope.dismissModal = function(result) {
						close(result, 200); // close, but give 200ms for bootstrap to animate

						console.log("in dissmiss");
					};

			  },function(response){
				  alert("did not save changes to client organisation's other details");  
			      } )
		 };//END UPDATE UNIT	 
		 
	$scope.checkRole =function(role,profile){
		var roles=profile.systemSubscriptions;
		//console.log(roles);
		var hasRole=false;
		var index = 0;
		angular.forEach(roles, function(item){             
			if(hasRole==false&&role == roles[index]){	
				hasRole=true;
				//console.log(hasRole);
			}else{
				index = index + 1;
			}
		});      
		return hasRole;

	} ;


	$scope.delete = function(index){
		$scope.entity = $scope.Profiles[index];
		ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: 'Do you wish to delete client organisation '+$scope.entity.organisation_name+' ?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					$scope.data = {};
					console.log("Start deleting client org");
					var toDel = {
							id: $scope.entity.id,
					}
					
					$http.post("//localhost:8443/user/deleteClientOrg", JSON.stringify(toDel)).then(function(response){
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Client organisation deleted successfully',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								console.log("OK");
								//$state.go("dashboard.viewAllVendors");
								$scope.Profiles.splice(index, 1);
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

app.controller('updateOrgController', ['$scope', '$element', 'title', 'close', 'profile',
                                        function($scope, $element, title, close, profile) {

//UPDATE MODAL

  $scope.title = title;
  $scope.profile=profile;
  console.log(title);
  //console.log(profile);
  console.log($element);
  //  This close function doesn't need to use jQuery or bootstrap, because
  //  the button has the 'data-dismiss' attribute.
  $scope.close = function() {
 	  close({
      profile:$scope.profile
    }, 500); // close, but give 500ms for bootstrap to animate
  };

  //  This cancel function must use the bootstrap, 'modal' function because
  //  the doesn't have the 'data-dismiss' attribute.
  $scope.cancel = function() {

    //  Manually hide the modal.
    $element.modal('hide');
    
    //  Now call close, returning control to the caller.
    close({
    
    }, 500); // close, but give 500ms for bootstrap to animate
  };




}])