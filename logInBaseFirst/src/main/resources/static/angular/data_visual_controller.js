app.controller('ChartCtrl', ['$scope', '$timeout','$http', function ($scope, $timeout,$http) {


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
  
     $scope.labelsBar = ['2006', '2007', '2008', '2009', '2010', '2011', '2012'];
     $scope.seriesBar = ['Series A', 'Series B'];

     $scope.dataBar = [
       [65, 59, 80, 81, 56, 55, 40],
       [28, 48, 40, 19, 86, 27, 90]
     ];
     
     $scope.optionsBar = { legend: { display: true } };
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


app.controller("BarCtrl", function ($scope) {
	  $scope.labels = ['2006', '2007', '2008', '2009', '2010', '2011', '2012'];
	  $scope.series = ['Series A', 'Series B'];
	  $scope.options = { legend: { display: true } };
	  $scope.data = [
	    [65, 59, 80, 81, 56, 55, 40],
	    [28, 48, 40, 19, 86, 27, 90]
	  ];
	});
