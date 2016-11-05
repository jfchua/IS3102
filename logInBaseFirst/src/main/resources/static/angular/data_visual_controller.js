app.controller('ChartCtrl', ['$scope', '$timeout','$http', function ($scope, $timeout,$http) {
	$scope.test={hei:'hei', ha:'ha',ho:10, haha:11};
	//$scope.eventTypeCount=[];
	$scope.testnumbers="50,100,200,300,350,450,";
	angular.element(document).ready(function () {


		//RETRIEVE ICON WHEN LOADED
		$http.get("//localhost:8443/dataVisual/eventCountAgainstEventType").then(function(response){

			console.log(response.data);
			$scope.eventTypeCount=response.data;
			console.log($scope.eventTypeCount);
		},function(response){
			alert("DID NOT VIEW EVENT COUNT BY TYPES");
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
     var chart = c3.generate({
    	    bindto: '#chart',
    	    data: {
    	        x: 'x',
    	        xFormat: '%Y',
    	        columns: [

    	            ['x', '2010', '2011', '2012', '2013', '2014', '2015'],
    	            ['data1', 30, 200, 100, 400, 150, 250],
    	            ['data2', 130, 340, 200, 500, 250, 350]
    	        ],
    	        type: 'bar'
    	    },
    	  
    	    axis: {
    	        x: {
    	            type: 'timeseries',
    	            // if true, treat x value as localtime (Default)
    	            // if false, convert to UTC internally
    	            localtime: true,
    	            tick: {
    	                format: '%Y-%m'
    	            }
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


