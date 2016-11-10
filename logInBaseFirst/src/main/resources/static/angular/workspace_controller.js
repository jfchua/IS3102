app.controller('workspaceController', function ($scope, $http,shareData,Auth) {
 angular.element(document).ready(function () {
	 $scope.$parent.eventSources.length=0;
	   	//console.log( $scope.eventSources);
	   	//console.log( $scope.$parent.eventSources);

		//VIEW EVENTS
		if(Auth.hasRoles('ROLE_PROPERTY')){
			console.log("GET CALENDAR EVENTS FOR PROPERTY ROLE");
			$scope.getEvents();
			$scope.getMaints();
			$scope.getTodos();
			console.log( $scope.eventSources);
		}else if(Auth.hasRoles('ROLE_FINANCE')||Auth.hasRoles('ROLE_TICKETING')||Auth.hasRoles('ROLE_EVENT')){
			$scope.getEvents();
			$scope.getTodos();
		}else if(Auth.hasRoles('ROLE_EXTEVE')){
			console.log("GET CALENDAR EVENTS FOR EXTERNAL EVENT ORGANISOR ROLE");
			$scope.getExEvents();
			$scope.getTodos();
			
		}else{
			console.log("NOT GETTING CALENDAR EVENTS");
			$scope.getTodos();
		}
		
			
			
	   
	 });//END OF READY

	

})//END OF WORKSPACE CONTROLLER


//FOR SIDE BAR PROFILE AND TOP BAR PROFILE
app.controller('dashboardController', function ($scope, $http,shareData,$state,Auth) {
	
	 angular.element(document).ready(function () {
		 //angular.element(document.getElementById('body-container')).ready(function () {
		// console.log("in dashboard");
		 //console.log(Auth.getUser());
		 var userObj=Auth.getUser();

		 var client=userObj.principal.user.clientOrganisation;
		 //GET CLIENT ORG NAME
		 $scope.clientName=client.organisationName;
		 //GET LOGO
		 if(client.logoFilePath)
			 $scope.logo = client.logoFilePath;
		 else
			 $scope.logo="img/ifms.png";
		 //console.log($scope.logo);
		 //CHANGE THEME COLOUR
		 if(client.themeColour){
			 $('<link>')
			 .appendTo('head')
			 .attr({type : 'text/css', rel : 'stylesheet'})
			 .attr('href', 'css/styles/app-'+client.themeColour+'.css');
			// console.log("THEME IS "+client.themeColour);
		 }
		 //GET USER NAME
		 $scope.userName=userObj.principal.user.name;

		 //GET USER ROLES
		 var roles=userObj.principal.user.rolesAsStringArray;
		 var rolesObj=[];
		 angular.forEach(roles, function(role){   
			 if("ROLE_SUPERADMIN"===role){
				 rolesObj.push({roleName:"Algattas admin"});
			 }else{				
				 if("ROLE_ADMIN"===role){
					 rolesObj.push({roleName:"IT admin"});
				 }else if("ROLE_EVENT"===role){
					 rolesObj.push({roleName:"managing events"});
				 }else if("ROLE_PROPERTY"===role){
					 rolesObj.push({roleName:"managing property"});
				 }else if("ROLE_FINANCE"===role){
					 rolesObj.push({roleName:"managing finance"});
				 }else if("ROLE_TICKETING"===role){
					 rolesObj.push({roleName:"managing tickets"});
				 }else if("ROLE_EXTEVE"===role){
					 rolesObj.push({roleName:"organising events"});
				 }else if("ROLE_HIGHER"===role){
					 rolesObj.push({roleName:"management level"});
				 }
			 }
			})
			$scope.userRoles=rolesObj;
			 /*
		 //GET LOGO
		 $http({
	 			method: 'GET',
	 			url: 'https://localhost:8443/getCompanyLogo'
			}).success(function (result) {
	 			if(result)
	 			$scope.logo = result;
				else
	 			$scope.logo="img/ifms.png";
	 			console.log(result);
	 			console.log("LOGO: " + JSON.stringify(result));
	 		}).error(function(result){
	 			//do something
	 			
	 			console.log("LOGOERROR: " + JSON.stringify(result));
	 		});
		 */
		 //GET USERINFO
		 /*
			$scope.userInfo = [];	
			$http.get("//localhost:8443/user/viewCurrentUser").then(function(response){
				console.log("test Hailing");
				console.log(response);
				console.log(response.data);
				$scope.userInfo = angular.fromJson(response.data);
				//console.log($scope.userInfo);
				console.log("DISPLAY current user");

			},function(response){
				alert("did not view user info");
			}	
			)
			*/
		//CONFIG CALENDAR
	   $scope.uiConfig = {
			      calendar:{
			        
			        editable:false,
			        header:{
			          left: 'month agendaWeek agendaDay',
			          center: 'title',
			          right: 'today prev,next'
			        },
			        eventClick: $scope.alertEventOnClick,
			       
			        eventResize: $scope.alertOnResize,
       		 	 	eventDrop: function(event, delta, revertFunc) {
    		 		 
    		 		 console.log(event);
    		 		console.log(event.className[0]);
    		 		 console.log("start updating todo");
    		 		$http({
						method : "POST",
						url : "https://localhost:8443/todo/updateToDoTask",
						data: {id:event.className[0], date: event.start}
					}).then(function mySuccess(response) {
						console.log("UPDATE TO DO LIST");
//						$http({
//							method: 'GET',
//							url: 'https://localhost:8443/todo/getToDoList'
//						}).success(function (result) {
							$scope.eventSources.length=0;

							//VIEW EVENTS
							if(Auth.hasRoles('ROLE_PROPERTY')){
								console.log("GET CALENDAR EVENTS FOR PROPERTY ROLE");
								$scope.getEvents();
								$scope.getMaints();
								$scope.getTodos();
								console.log( $scope.eventSources);
							}else if(Auth.hasRoles('ROLE_FINANCE')||Auth.hasRoles('ROLE_TICKETING')||Auth.hasRoles('ROLE_EVENT')){
								$scope.getEvents();
								$scope.getTodos();
							}else if(Auth.hasRoles('ROLE_EXTEVE')){
								console.log("GET CALENDAR EVENTS FOR EXTERNAL EVENT ORGANISOR ROLE");
								$scope.getExEvents();
								$scope.getTodos();
								
							}else{
								console.log("NOT GETTING CALENDAR EVENTS");
								$scope.getTodos();
							}
							
							
//						}).error(function(result){
//							//do something
//							console.log("ERROR GETTING TODO LIST");
//						})
					
					}, function myError(response) {
						//alert(response);
					});
    		 		 

    		     }
			      }
			    };
	   $scope.eventSources=[];
	   
	   
	   
	   //GET EVENTS
	   $scope.getEvents = function(){
			//var buildings ={name: $scope.name, address: $scope.address};
			$http.get("//localhost:8443/eventManager/viewAllEvents").then(function(response){
				$scope.eventsCalendar = response.data;
				//console.log("DISPLAY ALL EVENT fir event manager");
				//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.buildings);
				//ADD EVENTS INTO EVENTSOURCES OF CALENDAR
				
				if($scope.eventsCalendar.length!=0){
					var index=0;
				    angular.forEach($scope.eventsCalendar, function() {
				    	if($scope.eventsCalendar[index].approvalStatus=="APPROVED" ){
				         var event=[{start: $scope.eventsCalendar[index].event_start_date,
				        	 		end: $scope.eventsCalendar[index].event_end_date,			         
				        		 	title:$scope.eventsCalendar[index].event_title+" (Approved)",
				        		 	allDay: false,
				        		 	color: 'SeaGreen'
				         			}];
				         
				        $scope.eventSources.push(event);
				        	
				    	}else if($scope.eventsCalendar[index].approvalStatus=="SUCCESSFUL"){
					         var event=[{start: $scope.eventsCalendar[index].event_start_date,
				        	 		end: $scope.eventsCalendar[index].event_end_date,			         
				        		 	title:$scope.eventsCalendar[index].event_title+" (Successful)",
				        		 	allDay: false,
				        		 	color: 'Gold'
				         			}];
				         
				        $scope.eventSources.push(event);
				        	
				    	}else if($scope.eventsCalendar[index].approvalStatus=="PROCESSING"){
					         var event=[{start: $scope.eventsCalendar[index].event_start_date,
				        	 		end: $scope.eventsCalendar[index].event_end_date,			         
				        		 	title:$scope.eventsCalendar[index].event_title+" Processing",
				        		 	allDay: false,
				        		 	color: 'LightSkyBlue'
				         			}];
				         
				        $scope.eventSources.push(event);
				        	
				    	}
				    	index = index + 1;
				    });
				   // $scope.eventSources.push([{start:today,end:next,title:"Book Sale 2017",allDay: false}]);//need to delete this line
				    //console.log( $scope.eventSources);
					}
			},function(response){
				//alert(response);
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)	
			
		}
	   
	   
	 //RETRIEVE MAINTENANCES
	   //$scope.eventsFormated=[];
	   $scope.getMaints = function(){
			//var buildings ={name: $scope.name, address: $scope.address};
			$http.get("//localhost:8443/maintenance/viewMaintenance").then(function(response){
				$scope.maints = response.data;
				if($scope.maints.length!=0){
					var index=0;
				    angular.forEach($scope.maints, function() {

				         var maint=[{start: $scope.maints[index].start,
				        	 		end: $scope.maints[index].end,			         
				        		 	title:$scope.maints[index].description,
				        		 	allDay: false,
				        		 	color: 'IndianRed'
				         			}];
				         
				        $scope.eventSources.push(maint);
				        	index = index + 1;
				    });
				   // $scope.eventSources.push([{start:today,end:next,title:"Book Sale 2017",allDay: false}]);//need to delete this line
				   // console.log( $scope.eventSources);
					}
			},function(response){
				//alert(response);
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)	
			
		}
	   
	   //RETRIEVE EVENTS OF EXTERNAL EVENT ORGANISER
	   //$scope.eventsFormated=[];
	   $scope.getExEvents = function(){
			//var buildings ={name: $scope.name, address: $scope.address};
			$http.get("//localhost:8443/event/viewAllEvents").then(function(response){
				$scope.eventsCalendar = response.data;
				//console.log("DISPLAY ALL EVENT fir event manager");
				//console.log("EVENT DATA ARE OF THE FOLLOWING: " + $scope.buildings);
				//ADD EVENTS INTO EVENTSOURCES OF CALENDAR
				
				if($scope.eventsCalendar.length!=0){
					var index=0;
				    angular.forEach($scope.eventsCalendar, function() {
				    	if($scope.eventsCalendar[index].approvalStatus=="APPROVED" ||$scope.eventsCalendar[index].approvalStatus=="SUCCESSFUL"){
				         var event=[{start: $scope.eventsCalendar[index].event_start_date,
				        	 		end: $scope.eventsCalendar[index].event_end_date,			         
				        		 	title:$scope.eventsCalendar[index].event_title,
				        		 	allDay: false,
				        		 	color: 'DarkSeaGreen'
				         			}];
				         
				        $scope.eventSources.push(event);
				        	
				    	}else if($scope.eventsCalendar[index].approvalStatus=="SUCCESSFUL"){
					         var event=[{start: $scope.eventsCalendar[index].event_start_date,
				        	 		end: $scope.eventsCalendar[index].event_end_date,			         
				        		 	title:$scope.eventsCalendar[index].event_title+" (Successful)",
				        		 	allDay: false,
				        		 	color: 'Gold'
				         			}];
				         
				        $scope.eventSources.push(event);
				        	
				    	}else if($scope.eventsCalendar[index].approvalStatus=="PROCESSING"){
					         var event=[{start: $scope.eventsCalendar[index].event_start_date,
				        	 		end: $scope.eventsCalendar[index].event_end_date,			         
				        		 	title:$scope.eventsCalendar[index].event_title+" (Processing)",
				        		 	allDay: false,
				        		 	color: 'LightSkyBlue'
				         			}];
				         
				        $scope.eventSources.push(event);
				        	
				    	}
				    	index = index + 1;
				    });
				   // $scope.eventSources.push([{start:today,end:next,title:"Book Sale 2017",allDay: false}]);//need to delete this line
				  //  console.log( $scope.eventSources);
					}
			},function(response){
				//alert(response);
				//console.log("response is : ")+JSON.stringify(response);
			}	
			)	
			
		}
	   
	   
	   //GET TODOS
		$scope.getTodos = function(){$http({
			method: 'GET',
			url: 'https://localhost:8443/todo/getToDoList'
		}).success(function (result) {
			$scope.saved = result;
			//CLEAR EVENTSOURCES OF CALENDAR
			//$scope.eventSources.length=0;
			//$scope.eventSources=[$scope.eventsFormated];
			//console.log($scope.eventSources);
			//ADD TODOS TO EVENTSOURCES OF CALENDAR
			if($scope.saved.length!=0){
			var index=0;
		    angular.forEach($scope.saved, function() {

		         var todo=[{start: $scope.saved[index].date,
		        		 	title:$scope.saved[index].task,
		        		 	className: [$scope.saved[index].id],
		        		 	editable:true,
		        		 	color: 'SteelBlue'
		         			}];
		        		 
		        $scope.eventSources.push(todo);
		         
		         
		        	index = index + 1;
		    });
		  //  $scope.eventSources.push([{start:today,end:next,title:"Book Sale 2017",allDay: false}]);//need to delete this line
		   // console.log( $scope.eventSources);
			}
			
		}).error(function(result){
			//do something
			console.log("ERROR GETTING TODO LIST");
		})
		}
			 //RETRIEVE EVENTS
			   //$scope.eventsFormated=[];
			  // angular.element(document).ready(function () {
				
				   //GET TODOS FOR TODOS IN WORKSPACE AND SIDEBAR
		/*
					 console.log("test for getting todo list");
					 
					 $http({
							method: 'GET',
							url: 'https://localhost:8443/todo/getToDoList'
						}).success(function (result) {
							$scope.saved = result;
							
						}).error(function(result){
							//do something
							console.log("ERROR GETTING TODO LIST");
						})*/
				 //});

			   	 $scope.today = new Date();
				
				 $scope.saved = localStorage.getItem('taskItems');
				 $scope.taskItem = (localStorage.getItem('taskItems')!==null) ? 
					JSON.parse($scope.saved) : [ {description: "Why not add a task?", date: $scope.today, complete: false}];
					localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));
					
					//GET TODOS
					//$scope.todosFormated=[];
					var getTdList = function(){$http({
						method: 'GET',
						url: 'https://localhost:8443/todo/getToDoList'
					}).success(function (result) {
						$scope.saved = result;
						//$state.reload();
						
			
					
					}).error(function(result){
						//do something
						console.log("ERROR GETTING TODO LIST");
					})
					}
					//$scope.save();
					//FOR ALL USERS: GET TODOS	
					//getTdList();

					//$state.reload();
					//FOR PROPERTY MANAGER: GET MAINTENANCES		
					/*if(Auth.hasRoles('ROLE_PROPERTY')){
						
					}*/
					
					$scope.newTask = null;
					$scope.newTaskDate = null;
					$scope.categories = [
					                     {name: 'Personal'},
					                     {name: 'Work'},
					                     {name: 'School'},
					                     {name: 'Cleaning'},
					                     {name: 'Other'}
					                     ];
					$scope.newTaskCategory = $scope.categories;
					$scope.addNew = function (newTask,newTaskDate) {
						//console.log(newTask);
						//console.log(newTaskDate);
						//console.log($scope.newTask);
						//console.log($scope.newTaskDate);
						//$scope.saved.push({task:newTask, date: newTaskDate});
						$http({
							method : "POST",
							url : "https://localhost:8443/todo/addToDoTask",
							data: {task:newTask, date: newTaskDate}
						}).then(function mySuccess(response) {
							console.log("ADDED NEW TO DO LIST");
//							$http({
//								method: 'GET',
//								url: 'https://localhost:8443/todo/getToDoList'
//							}).success(function (result) {
								//$scope.saved = result;
								//GET TODOS	
								//getTdList();//might need to get back
								$scope.eventSources.length=0;
								//console.log("after add");
								//console.log($scope.eventSources);
								//VIEW EVENTS
								if(Auth.hasRoles('ROLE_PROPERTY')){
									console.log("GET CALENDAR EVENTS FOR PROPERTY ROLE");
									$scope.getEvents();
									$scope.getMaints();
									$scope.getTodos();
									console.log( $scope.eventSources);
								}else if(Auth.hasRoles('ROLE_FINANCE')||Auth.hasRoles('ROLE_TICKETING')||Auth.hasRoles('ROLE_EVENT')){
									$scope.getEvents();
									$scope.getTodos();
								}else if(Auth.hasRoles('ROLE_EXTEVE')){
									console.log("GET CALENDAR EVENTS FOR EXTERNAL EVENT ORGANISOR ROLE");
									$scope.getExEvents();
									$scope.getTodos();
									
								}else{
									console.log("NOT GETTING CALENDAR EVENTS");
									$scope.getTodos();
								}
								
				
								//FOR PROPERTY MANAGER: GET MAINTENANCES		
								/*if(Auth.hasRoles('ROLE_PROPERTY')){
									
								}*/
								
//							}).error(function(result){
//								//do something
//								console.log("ERROR GETTING TODO LIST");
//							})
						
							//$route.reload();
							//console.log("refresh page")





						}, function myError(response) {
							//alert(response);
						});
						$scope.newTask = '';
						$scope.newTaskDate = '';
						$scope.newTaskCategory = $scope.categories;
						localStorage.setItem('taskItems', JSON.stringify($scope.taskItem));
						
					};
					$scope.deleteTask = function (id) {
						$http({
							method : "POST",
							url : "https://localhost:8443/todo/deleteToDoTask",
							data: id
						}).then(function mySuccess(response) {
							console.log("Successfully delete task with Id: " + id);
							//$scope.saved = [];
							$http({
								method: 'GET',
								url: 'https://localhost:8443/todo/getToDoList'
							}).success(function (result) {
								//$scope.saved = result;		
								
								
								//getEvents();
								//getTdList();
								//getMaints();
								//FOR ALL USERS: GET TODOS	
								getTdList();
								//FOR PROPERTY,EVENT,FINANCE,TICKETING: GET EVENTS AND MAINTENANCES
								
								
								//FOR PROPERTY MANAGER: GET MAINTENANCES		
								/*if(Auth.hasRoles('ROLE_PROPERTY')){
									
								}*/
								$scope.eventSources.length=0;
								//console.log("after add");
								//console.log($scope.eventSources);
								//VIEW EVENTS
								if(Auth.hasRoles('ROLE_PROPERTY')){
									console.log("GET CALENDAR EVENTS FOR PROPERTY ROLE");
									$scope.getEvents();
									$scope.getMaints();
									$scope.getTodos();
									console.log( $scope.eventSources);
								}else if(Auth.hasRoles('ROLE_FINANCE')||Auth.hasRoles('ROLE_TICKETING')||Auth.hasRoles('ROLE_EVENT')){
									$scope.getEvents();
									$scope.getTodos();
								}else if(Auth.hasRoles('ROLE_EXTEVE')){
									console.log("GET CALENDAR EVENTS FOR EXTERNAL EVENT ORGANISOR ROLE");
									$scope.getExEvents();
									$scope.getTodos();
									
								}else{
									console.log("NOT GETTING CALENDAR EVENTS");
									$scope.getTodos();
								}
								
								
								
							}).error(function(result){
								//do something
								console.log("ERROR GETTING TODO LIST");
							})
						}, function myError(response) {
							//alert(response);
						});		
						//getTdList();
					
						

					};
	 });

})

