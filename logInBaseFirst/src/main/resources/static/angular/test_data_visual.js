app.controller('LineChartCtrl', ['$scope', '$timeout','$http', function ($scope, $timeout,$http) {
	$scope.test={hei:'hei', ha:'ha',ho:10, haha:11};
	//$scope.eventTypeCount=[];
	$scope.testnumbers="50,100,200,300,350,450,";
	angular.element(document).ready(function () {
		/*$http.get("https://localhost:8443/building/viewBuildings").then(function(response){
			$scope.buildings = response;
			$scope.selectedBuilding;
		},function(response){
			alert("GET BUILDING ERROR");
		})*/
		var chart = c3.generate({
    	    data: {
    	        x: 'x',
    	        columns: [
    	            ['x', 30, 50, 100, 230, 300, 310],
    	            ['data1', 30, 200, 100, 400, 150, 250],
    	            ['data2', 130, 300, 200, 300, 250, 450]
    	        ]
    	    }
    	});
		

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


