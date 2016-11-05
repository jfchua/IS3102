app.controller('LineChartCtrl', ['$scope', '$timeout','$http', function ($scope, $timeout,$http) {
	$scope.test={hei:'hei', ha:'ha',ho:10, haha:11};
	//$scope.eventTypeCount=[];
	$scope.testnumbers="50,100,200,300,350,450,";
	$scope.selectedBuilding = {};
	$scope.buildings = {};
	angular.element(document).ready(function () {
		$http.get("https://localhost:8443/building/viewBuildings").then(function(response){
			$scope.buildings = response.data;
			console.log("hahahaha");
			console.log(response.data);
			//alert("GET BUILDING SUCCESS");
		},function(response){
			alert("GET BUILDING ERROR");
		})
		/*var chart = c3.generate({
			data: {
				x: 'x',
				columns: [
				          ['x', 30, 50, 100, 230, 300, 310],
				          ['data1', 30, 200, 100, 400, 150, 250],
				          ['data2', 130, 300, 200, 300, 250, 450]
				          ]
			}
		});*/
		$scope.currentlySelectedBuilding;
		$scope.selectBuild = function(){
			$scope.selectedBuilding=$scope.currentlySelectedBuilding;
		}
		console.log("finish selecting building");

		$scope.getLevel = function(id){
			$scope.dataToShare = [];
			//var id = $scope.currentlySelected;
			$scope.url = "https://localhost:8443/level/viewLevels/"+id;
			//$scope.dataToShare = [];
			console.log("GETTING THE ALL LEVELS INFO")
			var getLevels = $http({
				method  : 'GET',
				url     : 'https://localhost:8443/level/viewLevels/'+id,
			});
			console.log("Getting the levels using the url: " + $scope.url);
			getLevels.success(function(response){
				$scope.levels = response;
				$scope.selectedLevel;
				console.log('hohohoho');
				console.log(response);
			});
			getLevels.error(function(){
				alert('Get levels error!!!!!!!!!!');
			});	
			$scope.currentlySelectedLevel;
			$scope.selectLevel = function(){
				$scope.selectedLevel=$scope.currentlySelectedLevel;
			}
			console.log("finish selecting level");	
		}

		$scope.selectedUnits=[];
		$scope.getUnit = function(levelId){

			$scope.levelID = levelId; 
			var dataObj = {id: $scope.levelID};
			console.log("GETTING THE ALL UNITS INFO")
			var getUnits = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/property/viewUnitsWithBookings/',
				data    : dataObj,
			});
			console.log("REACHED HERE FOR SUBMIT LEVEL " + JSON.stringify(dataObj));
			getUnits.success(function(response){
				$scope.units = response;
				console.log("lalalala");
				console.log($scope.units);
			});
			getUnits.error(function(){
				alert('Get units error!!!!!!!!!!');
			});		

			$scope.currentlySelectedUnit;
			$scope.selectUnit = function(){
				console.log("currently selected unit bookings");
				console.log($scope.currentlySelectedUnit.bookings);
				var duplicate = false;
				var index = 0;
				angular.forEach($scope.selectedUnits, function() {
					if(duplicate==false&&$scope.currentlySelectedUnit.id == $scope.selectedUnits[index].id)
						duplicate = true;
					else
						index = index + 1;
				});
				console.log(duplicate);
				if(!duplicate){
					$scope.selectedUnits.push($scope.currentlySelectedUnit);
				}
				console.log("Hailing test:");
				console.log($scope.currentlySelectedUnit);
				console.log($scope.selectedUnits);
			}

			$scope.deleteUnit = function(unit){
				var index = $scope.selectedUnits.indexOf(unit);
				$scope.selectedUnits.splice(index, 1);  
			}
			console.log("finish selecting units");	
		}	
		$scope.components = {};
		$scope.generateChart = function(){
			var dataObj = {
					units: $scope.selectedUnits,
					start: ($scope.event.event_start_date).toString(),
					end: ($scope.event.event_end_date).toString(),
			};
			console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/dataVisual/occupancyAgainstUnit',
				data    : dataObj //forms user object
			});
			send.success(function(response){
				$scope.occupancy = response;
				console.log($scope.occupancy);
				console.log("get component success");
				$scope.changeFormat($scope.occupancy);
			});
			send.error(function(response){
				alert("get component failure");
			});

			$scope.xData=['x'];
			$scope.eventCountTimeData=['Occupancy Rate'];
			$scope.changeFormat = function(occupancy){
				angular.forEach(occupancy, function(oneData) {
					$scope.xData.push(oneData.unit);
					$scope.eventCountTimeData.push(oneData.percent);
				});

				var chart = c3.generate({
					bindto: '#chart',
					data: {
						x: 'x',
						columns: [
						          $scope.xData,
						          $scope.eventCountTimeData
						          ],
						          type: 'line'
					},		     	  
					axis: {
						y: {
							tick: {
								format: d3.format(",%")
							}
						}
					}
				});
			}


		}		
	});



	$scope.downloadPlan = function () {
		console.log("her0");
		console.log(html2canvas);
		var canvasdiv = document.getElementById("screenshotChart");
		html2canvas(canvasdiv,{
			allowTaint: true,
			logging: true,
			taintTest: true,
			onrendered: function (canvas) {
				var a = document.createElement("a");
				a.href = canvas.toDataURL("plan/png");
				a.download ="chart_report.png";
				a.click();
			},     	
		});
	}


}]);


