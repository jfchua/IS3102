app.controller('ChartCtrl', ['$scope', '$timeout','$http', function ($scope, $timeout,$http) {
	$scope.test={hei:'hei', ha:'ha',ho:10, haha:11};
	//$scope.eventTypeCount=[];
	$scope.testnumbers="50,100,200,300,350,450,";
	angular.element(document).ready(function () {


		//RETRIEVE EVENTS COUNT AGAINST EVENT TYPE WHEN LOADED
		$http.get("//localhost:8443/dataVisual/eventCountAgainstEventType").then(function(response){

			console.log(response.data);
			$scope.eventTypeCount=response.data;
			console.log($scope.eventTypeCount);
		},function(response){
			alert("DID NOT VIEW EVENT COUNT BY TYPES");
		})
		
		
		//RETRIEVE EVENTS COUNT AGAINST TIME WHEN LOADED
		$http.get("//localhost:8443/dataVisual/eventCountAgainstTime").then(function(response){
			console.log("event count against time");
			console.log(response.data);
			$scope.eventCountTime=response.data;
			console.log($scope.eventCountTime);
			$scope.changeFormatForEventTime($scope.eventCountTime);
		},function(response){
			alert("DID NOT VIEW EVENT COUNT BY TIME");
		})

	});
     $scope.pie = {
    		 labels : ["Cat 1", "Cat 2", "Unsold"],
    		data: [24, 36, 15],
    		
    	     // data : [$http.get('//localhost:8443/BI/dataVisual')],
    		colours: ['#3CA2E0','#F0AD4E','#7AB67B'],
    		options: {
    	        legend: {
    	            display: true,
    	            labels: {
    	                fontColor: 'rgb(255, 99, 132)'
    	            }
    	        }
    	    }
    };
     $scope.xData=['x'];
     $scope.eventCountTimeData=['Number of Events'];
     $scope.changeFormatForEventTime = function(eventCountTime){
    	 angular.forEach(eventCountTime, function(oneData) {
    		 $scope.xData.push(oneData.label);
    		 $scope.eventCountTimeData.push(oneData.count);
 		});
    	 
         var chart = c3.generate({
     	    bindto: '#chart',
     	    data: {
     	        x: 'x',
     	        xFormat: '%Y-%m',
     	        columns: [

     	                 $scope.xData,
     	                $scope.eventCountTimeData
     	                ],
     	        type: 'line'
     	    },
     	  
     	    axis: {
     	        x: {
     	            type: 'category',
     	            // if true, treat x value as localtime (Default)
     	            // if false, convert to UTC internally
     	            localtime: true,
     	            tick: {
     	                format: '%Y-%m'
     	            }
     	        }
     	    }
     	});
     }

  

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


