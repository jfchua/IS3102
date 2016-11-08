app.controller('ChartCtrl', ['$scope', '$timeout','$http','ModalService','$state', function ($scope, $timeout,$http,ModalService,$state) {
	$scope.test={hei:'hei', ha:'ha',ho:10, haha:11};
	//$scope.eventTypeCount=[];
	$scope.testnumbers="50,100,200,300,350,450,";
	angular.element(document).ready(function () {
		$scope.showAll=true;
		$scope.show1=false;
		$scope.show2=false;
		$scope.show3=false;
		$scope.show4=false;
		$scope.show5=false;
		$scope.printed=false;
		$scope.date=new Date();
		//RETRIEVE EVENTS COUNT AGAINST EVENT TYPE WHEN LOADED
		$http.get("//localhost:8443/dataVisual/eventCountAgainstEventType").then(function(response){

			console.log(response.data);
			$scope.eventTypeCount=response.data;
			console.log($scope.eventTypeCount);
		},function(response){
			console.log("DID NOT VIEW EVENT COUNT BY TYPES");
		})
		
		
		//RETRIEVE EVENTS COUNT AGAINST TIME WHEN LOADED
		$http.get("//localhost:8443/dataVisual/eventCountAgainstTime").then(function(response){
			console.log("event count against time");
			console.log(response.data);
			$scope.eventCountTime=response.data;
			console.log($scope.eventCountTime);
			$scope.changeFormatForEventTime($scope.eventCountTime);
		},function(response){
			console.log("DID NOT VIEW EVENT COUNT BY TIME");
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
     $scope.eventCountTimeData=['Total'];
     $scope.eventCountTimeConcert=['Concert'];
     $scope.eventCountTimeConference=['Conference'];
     $scope.eventCountTimeFair=['Fair'];
     $scope.eventCountTimeFamily=['Family'];
     $scope.eventCountTimeLifestyle=['Lifestyle'];
     $scope.eventCountTimeSeminar=['Seminar'];
     $scope.changeFormatForEventTime = function(eventCountTime){
    	 angular.forEach(eventCountTime, function(oneData) {
    		 $scope.xData.push(oneData.label);
    		 $scope.eventCountTimeData.push(oneData.count);
    		 $scope.eventCountTimeConcert.push(oneData.concert);
    	     $scope.eventCountTimeConference.push(oneData.conference);
    	     $scope.eventCountTimeFair.push(oneData.fair);
    	     $scope.eventCountTimeFamily.push(oneData.family);
    	     $scope.eventCountTimeLifestyle.push(oneData.lifestyle);
    	     $scope.eventCountTimeSeminar.push(oneData.seminar);
 		});
    	 
         var chartEventTime = c3.generate({
     	    bindto: '#chartEventTime',
     	    data: {
     	        x: 'x',
     	        xFormat: '%Y-%m',
     	        columns: [
     	                $scope.xData,
     	                $scope.eventCountTimeData,
     	               $scope.eventCountTimeConcert,
     	      	     $scope.eventCountTimeConference,
     	      	     $scope.eventCountTimeFair,
     	      	     $scope.eventCountTimeFamily,
     	      	     $scope.eventCountTimeLifestyle,
     	      	     $scope.eventCountTimeSeminar,
     	                ],
     	        type: 'bar',
     	       types: {
     	    	  Total: 'line',
  	              
  	          },
  	        labels: true
     	    },
     	  
     	    axis: {
     	        x: {
     	            type: 'category',
     	            // if true, treat x value as localtime (Default)
     	            // if false, convert to UTC internally
     	            localtime: true,
     	            tick: {
     	                format: '%Y-%m'
     	            },
     	         
     	        }
     	    }
     	});
     }
     $scope.haha=function(){
    	 $scope.showAll=true;
    	 $scope.show1=false;
    	 $scope.show2=false;
    	 $scope.show3=false;
    	 $scope.show4=false;
    	 $scope.show5=false;
    	 if($scope.printed)
    	 $state.reload();
     }
 	$scope.checkDateErr = function(startDate,endDate) {
 		$scope.errMessage = '';
 		var curDate = new Date();

 		if(new Date(startDate) > new Date(endDate)){
 			ModalService.showModal({

 				templateUrl: "views/errorMessageTemplate.html",
 				controller: "errorMessageModalController",
 				inputs: {
 					message: "End Date should be greater than start date",
 				}
 			}).then(function(modal) {
 				modal.element.modal();
 				modal.close.then(function(result) {
 					console.log("OK");
 					$scope.end_date = '';
 				});
 			});

 			$scope.dismissModal = function(result) {
 				close(result, 200); // close, but give 200ms for bootstrap to animate

 				console.log("in dissmiss");
 			};

 			return false;
 		}
 	};
 	
     $scope.generateEventTimeChartCust = function(){
			var dataObj = {
					
					start: ($scope.start_date).toString(),
					end: ($scope.end_date).toString(),
			};
			console.log(($scope.start_date).toString());
			console.log(($scope.end_date).toString());
			console.log("REACHED HERE FOR SUBMIT EVENT " + JSON.stringify(dataObj));
			var send = $http({
				method  : 'POST',
				url     : 'https://localhost:8443/dataVisual/eventCountAgainstTimeCust',
				data    : dataObj //forms user object
			});
			send.success(function(response){
				console.log(response);
				$scope.eventCountTimeCust=response;
				console.log($scope.eventCountTimeCust);
				$scope.changeFormatForEventTimeCust($scope.eventCountTimeCust);
			});
			send.error(function(response){
				console.log("ERROR: GET EVENT COUNT AGAINST SELECTED PERIOD");
			});

			
			}
    var xDataCust=['x'];
    var eventCountTimeDataCust=['Total'];
     var eventCountTimeConcertCust=['Concert'];
     var eventCountTimeConferenceCust=['Conference'];
     var eventCountTimeFairCust=['Fair'];
    var eventCountTimeFamilyCust=['Family'];
     var eventCountTimeLifestyleCust=['Lifestyle'];
     var eventCountTimeSeminarCust=['Seminar'];
     $scope.changeFormatForEventTimeCust = function(eventCountTime){
    	 angular.forEach(eventCountTime, function(oneData) {
    		 xDataCust.push(oneData.label);
    		 eventCountTimeDataCust.push(oneData.count);
    		 eventCountTimeConcertCust.push(oneData.concert);
    	     eventCountTimeConferenceCust.push(oneData.conference);
    	     eventCountTimeFairCust.push(oneData.fair);
    	     eventCountTimeFamilyCust.push(oneData.family);
    	     eventCountTimeLifestyleCust.push(oneData.lifestyle);
    	     eventCountTimeSeminarCust.push(oneData.seminar);
 		});
    	 
         var chartEventCountTimeCust = c3.generate({
     	    bindto: '#chartEventCountTimeCust',
     	    data: {
     	        x: 'x',
     	        xFormat: '%Y-%m',
     	        columns: [
     	                xDataCust,
     	                eventCountTimeDataCust,
     	               eventCountTimeConcertCust,
     	      	     eventCountTimeConferenceCust,
     	      	     eventCountTimeFairCust,
     	      	     eventCountTimeFamilyCust,
     	      	     eventCountTimeLifestyleCust,
     	      	     eventCountTimeSeminarCust,
     	                ],
     	        type: 'bar',
     	       types: {
     	    	  Total: 'line',
  	              
  	          },
  	        labels: true
     	    },
     	  
     	    axis: {
     	        x: {
     	            type: 'category',
     	            // if true, treat x value as localtime (Default)
     	            // if false, convert to UTC internally
     	            localtime: true,
     	            tick: {
     	                format: '%Y-%m'
     	            },
     	         
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

