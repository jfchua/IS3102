app.controller('workspaceController', function ($scope, $http,shareData) {
 angular.element(document).ready(function () {
		 
	 });

	

})

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

