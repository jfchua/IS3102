app.controller('workspaceController', function ($scope, $http,shareData,Auth) {
 angular.element(document).ready(function () {
	 //CONFIG CALENDAR
	   $scope.uiConfig = {
			      calendar:{
			        width: 600,
			        editable:false,
			        header:{
			          left: 'month agendaWeek agendaDay',
			          center: 'title',
			          right: 'today prev,next'
			        },
			        eventClick: $scope.alertEventOnClick,
			        eventDrop: $scope.alertOnDrop,
			        eventResize: $scope.alertOnResize
			      }
			    };
	   $scope.eventSources=[];
	   
	   //GET TODOS
	   $http({
			method: 'GET',
			url: 'https://localhost:8443/todo/getToDoList'
		}).success(function (result) {
			$scope.todos = result;
			//CLEAR EVENTSOURCES OF CALENDAR
			//$scope.eventSources.length=0;
			//$scope.eventSources=[$scope.eventsFormated];
			//console.log($scope.eventSources);
			//ADD TODOS TO EVENTSOURCES OF CALENDAR
			if($scope.todos.length!=0){
			var index=0;
		    angular.forEach($scope.todos, function() {

		         var todo=[{start: $scope.todos[index].date,
		        		 	title:$scope.todos[index].task,
		        		 	className: ['newtask'],
		        		 	editable:false,
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
		
		//VIEW EVENTS
		if(Auth.hasRoles('ROLE_PROPERTY')){
			console.log("property ");
		}else if(Auth.hasRoles('ROLE_EXTEVE')){
			console.log("external event");
		}else{
			console.log("error");
		}
		
	 });//END OF READY

	

})//END OF WORKSPACE CONTROLLER


//FOR SIDE BAR PROFILE AND TOP BAR PROFILE
app.controller('dashboardController', function ($scope, $http,shareData) {
	
	 angular.element(document).ready(function () {
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
		 
		 //GET USERINFO
			$scope.userInfo = [];	
			$http.get("//localhost:8443/user/viewCurrentUser").then(function(response){
				console.log("test Hailing");
				console.log(response);
				console.log(response.data);
				$scope.userInfo = angular.fromJson(response.data);
				console.log("DISPLAY current user");
				if(response.data.theme){
							$('<link>')
								  .appendTo('head')
					 			  .attr({type : 'text/css', rel : 'stylesheet'})
								  .attr('href', 'css/styles/app-'+response.data.theme+'.css');
								console.log("THEME IS "+response.data.theme);
							}
			},function(response){
				alert("did not view user info");
			}	
			)
	 });

})

