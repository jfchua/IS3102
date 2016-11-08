app.controller('viewFloorPlanExController', function ($scope, $http,shareData,$state,ModalService) {

	$scope.showUnitPlan=false;

var levelIdObj;
var levelId;
var level;
var building;
var widthForFloorPlan;
var obj;
$scope.legends=[];
angular.element(document).ready(function () {
	  		//GET LEVEL	 
	  		//$scope.units=[];
		   // level = shareData.getData();
	  		obj=shareData.getData();
	  		level=obj.level
	  		//building=obj.building;
	  		console.log("GET Level");
	  		console.log(level);
	  		console.log("GET BUILDING");
	  		//console.log(building);
		    	console.log("GET LEVEL: ");
		      console.log(level);
		      $scope.levelLength;
		      $scope.levelWidth;
		    //IN CASE OF GETTING AN ARRAY FROM SHAREDATA 
		    if(!level.id){
		    	console.log("LEVEL IS NOT GET");
		      level=level[0];
		      
		    }
		   // console.log("GET LEVEL: ");
		   // console.log(level);
		   
	
		
		    //$scope.levelLength=800;
		    //$scope.levelWidth=parseInt((level.width)*800/(level.length));
		    
		    //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
		      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;
		   
		      
		     // meter=parseInt((widthForFloorPlan-40)/(level.length));
		    meter=(widthForFloorPlan-40)/(level.length);
		      $scope.levelLengthGrid=meter*(level.length);
			    $scope.levelWidthGrid=meter*(level.width);	
			    $scope.background=level.filePath;
			    console.log( $scope.levelLengthGrid+" "+$scope.levelWidthGrid);
			    
		    //GET UNITS FROM levelIdObj
		  
		      $scope.units=level.units;	
		      
		      
		    //ADD UNIQUE UNIT TO LEGENDS
		      $scope.addToLegend= function(unit){
		      	var isDuplicate=false;
		      		angular.forEach($scope.legends, function(legend){    
		      				if((isDuplicate==false)&&(unit.description == legend.description)){	
		      				
		      					isDuplicate=true;
		      					console.log(isDuplicate);
		      				}else{
		      				
		      				console.log(isDuplicate);
		      				}
		      			
		      			
		      		}); 
		      		
		      		if(isDuplicate){	
		      		
		      	}else{
		      		$scope.legends.push(unit);
		      	}
		      		console.log($scope.legends);
		      		
		      		}
		      angular.forEach($scope.units, function(unit){   
					$scope.addToLegend(unit);
					
				})
				console.log("test units");
		      console.log($scope.units);
		   
		   
});


$scope.twoOptions=[
                 ['View Details', function ($itemScope, $event, modelValue, text, $li,$parent,unit) {
              	  // console.log($itemScope.unit);
              	   $scope.showModal($itemScope.unit,$parent);
                 }],
                 ['View Unit Plan', function ($itemScope, $event, modelValue, text, $li) {
              	
                     $scope.viewUnitPlanDefault($itemScope.unit);
                    
                 }],
                 ];
//PASS BUILDING TO SHAREDATA FOR GOING BACK TO VIEW LEVELS
//$scope.passBuilding = function(){
	//  shareData.addData(building);
	//  $state.go("dashboard.viewLevels");
//}


$scope.viewUnitPlanDefault=function (unit){
	  console.log(unit);
	  $scope.showUnitPlan=true;
	  if(unit.square.type=='./svg/rect.svg'){
		  
	  var obj={building:building,
				level:level,
				unit:unit,
				}
	  console.log(unit);
		shareData.addData(obj);
	  $state.go('dashboard.viewBuildingEx.viewFloorPlanEx.viewUnitPlanDefaultEx',{param2: unit.id});
	  }
}
 $scope.showDetails= function (thisUnit) {   
   console.log(thisUnit);
  // console.log(event);
  // console.log(event.target.classList);
   $scope.showDetail="unitNumber: "+thisUnit.unitNumber  +  " Unit Width: " + parseInt((thisUnit.square.height)*(level.length)/800) + "meter, Unit Length: " + parseInt((thisUnit.square.width)*(level.length)/800) +"meter";
   console.log($scope.showDetail);
    } 
$scope.viewLevelsByBuildingId = function(){
  var buildingId;
  $scope.dataToShare = [];
  //get building id from levelId
  $http.post('/level/getBuilding', JSON.stringify(levelIdObj)).then(function(response){
    console.log('GET BUILDING SUCCESS! ' + JSON.stringify(response));
    var building=response.data;
    console.log(building);
    //var temp=JSON.stringify(buildingJson)
    shareData.addData(building);

    console.log("Building ID IS " + building.id);

  },function(response){
    console.log('GET BUILDING ID FAILED! ' + JSON.stringify(response));
  });
  
 

  }




$scope.downloadPlan = function () {
	  console.log("her0");
	  console.log(html2canvas);
	
	
	  var canvasdiv = document.getElementById("glassboxGrid");
	    html2canvas(canvasdiv,{
	    	 allowTaint: true,
           logging: true,
           taintTest: true,
	        onrendered: function (canvas) {
	            var a = document.createElement("a");
	            a.href = canvas.toDataURL("plan/png");
	            a.download ="plan.png";
	            a.click();
	        },
	       
	
	    });
	}

//MODAL FOR VIEWING ONE UNIT
$scope.complexResult = null;
	 $scope.showModal = function(unit) {
		 console.log(unit);
		    // Just provide a template url, a controller and call 'showModal'.
		    ModalService.showModal({
		    	
		    	      templateUrl: "views/viewUnitTemplate.html",
		    	      controller: "viewUnitController",
		    	      inputs: {
		    	        title: "View Unit",
		    	        unit:unit
		    	      }
		    	    }).then(function(modal) {
		    	      modal.element.modal();
		    	      modal.close.then(function(result) {
		    	     console.log("FINISHED VIEWING UNIT");
		    	      });
		    	    });

		  };//END SHOWMODAL
		  
		  $scope.dismissModal = function(result) {
			    close(result, 200); // close, but give 200ms for bootstrap to animate
		
			    console.log("in dissmiss");
			 };
			 
			 
			 //GRIDSTER CONFIG
			 console.log("gridster test view");
			 console.log(meter);
			 $scope.gridsterOpts = {
					 
					 	
					    columns: level.length, // the width of the grid, in columns
					    pushing: false, // whether to push other items out of the way on move or resize
					    floating: false, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
					    swapping: false, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
					    width:$scope.levelLengthGrid, // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
					    colWidth: meter, // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
					    rowHeight: meter, // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
					    margins: [2, 2], // the pixel distance between each widget
					    outerMargin: false, // whether margins apply to outer edges of the grid
					    sparse: false, // "true" can increase performance of dragging and resizing for big grid (e.g. 20x50)
					    isMobile: false, // stacks the grid items if true
					    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
					    mobileModeEnabled: false, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
					    minColumns: level.length, // the minimum columns the grid must have
					    minRows: level.width, // the minimum height of the grid, in rows
					    maxRows: level.width,
					    defaultSizeX: 2, // the default width of a gridster item, if not specifed
					    defaultSizeY: 1, // the default height of a gridster item, if not specified
					    minSizeX: 1, // minimum column width of an item
					    maxSizeX: null, // maximum column width of an item
					    minSizeY: 1, // minumum row height of an item
					    maxSizeY: null, // maximum row height of an item
					    resizable: {
						       enabled: false,
						       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
						       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
						       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
						       stop: function(event, $element, unit) {} // optional callback fired when item is finished resizing
						    },
						    draggable: {
						       enabled: false, // whether dragging items is supported
						       //handle: '.my-class', // optional selector for drag handle
						       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
						       drag: function(event, $element, widget) {}, // optional callback fired when item is moved,
						       stop: function(event, $element, unit) {} // optional callback fired when item is finished dragging
						    }
					
					};
		
			 console.log("test opts view");
			 console.log($scope.gridsterOpts.colWidth);
			 console.log($scope.gridsterOpts.maxRows);
			 console.log($scope.gridsterOpts.columns);
})



//Unit Plan of Event used by event organiser
app.controller('viewDefaultUnitPlanExController', function ($scope, $http,shareData,ModalService,$state) {
	  var widthForAreaPlan;
	  var meter;	  
	  var scale;
	  var levelIdObj;
	  $scope.areas=[];
	  $scope.legends=[];
	  $scope.unit;
	  $scope.showUnitPlan=true;
	angular.element(document).ready(function () {
		var obj=shareData.getData();
		$scope.building=obj.building;
		$scope.level=obj.level;
		$scope.unit=obj.unit;
		console.log($scope.building);
		console.log($scope.level);
		console.log($scope.unit);
		unitIdObj={id:$scope.unit.id};
	 //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;    
	      console.log(widthForFloorPlan);
	     // meter=parseInt((widthForFloorPlan-20)/($scope.unit.sizeX));
	      meter=(widthForFloorPlan-20)/($scope.unit.sizeX);
	      $scope.unitLengthGrid=meter*($scope.unit.sizeX);
		  $scope.unitWidthGrid=meter*($scope.unit.sizeY);	
		  scale=meter/2;//one grid represent 0.5m
		  console.log( $scope.unitLengthGrid+" "+$scope.unitWidthGrid);
		//get event id from previous page
		  			var getAreas= $http({
		  				method  : 'POST',
		  				url     : 'https://localhost:8443/area/viewAreasDefault/',
		  				data    : unitIdObj,
		  		});
		  			console.log("REACHED HERE FOR VIEWING AREAS " + JSON.stringify(unitIdObj));
		  			getAreas.success(function(response){
		  				console.log(response);
		  				$scope.areas = response;

		  				angular.forEach($scope.areas, function(area){   
		  					$scope.addToLegend(area);
		  					
		  				})
		  				
		  			});
		  			getAreas.error(function(){
		  				//$scope.areas =[];
		  			});	
		  			
		  			
		  			$scope.addToLegend= function(area){
		  			//PUSH UNIQUE AREA TO LEGENDS
		  		
		  			//console.log(areas[0]);
		  			//$scope.legends.push(areas[index]);
		  			console.log("test legend");
		  			console.log($scope.legends);
	  				var isDuplicate=false;
		  			angular.forEach($scope.legends, function(legend){    
			  				if((isDuplicate==false)&&(area.description == legend.description)){	
			  				
			  					isDuplicate=true;
			  					console.log(isDuplicate);
			  				}else{
			  				
			  				console.log(isDuplicate);
			  				}
			  			
		  				
		  			}); 
		  			
		  			if(isDuplicate){	
	  					
	  				}else{
	  					$scope.legends.push(area);
	  				}
		  			console.log($scope.legends);
		  			
		  			}
		  		
	});
/*
	$scope.passLevel=function(){
		shareData.addData($scope.level);
		$state.go("dashboard.viewFloorPlan");
	}
	*/
	 //PASS BUILDING TO SHAREDATA FOR GOING BACK TO VIEW LEVELS
	  $scope.passBuilding = function(){
		  shareData.addData($scope.building);
		  $state.go("dashboard.viewLevelsEx");
	  }
	  
	  
	  //FOR GO BACK TO VIEW FLOOR PLAN
	  $scope.viewFloorPlan= function(){
	    //shareData.addData(level);
		  var obj={building:$scope.building,
					level:$scope.level
					}
			shareData.addData(obj);
		  $state.go("dashboard.viewFloorPlanEx");
	  }
	  
	 



	
	
	
	 console.log("gridster test ");
	 console.log(meter);
	 $scope.gridsterOpts = {
			 
			 	
			    columns: $scope.unit.sizeX*(meter/scale), // the width of the grid, in columns
			    pushing: false, // whether to push other items out of the way on move or resize
			    floating: false, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
			    swapping: false, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
			    width:$scope.unitLengthGrid, // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
			    colWidth: scale, // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
			    rowHeight: scale, // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
			    margins: [1, 1], // the pixel distance between each widget
			    outerMargin: false, // whether margins apply to outer edges of the grid
			    sparse: false, // "true" can increase performance of dragging and resizing for big grid (e.g. 20x50)
			    isMobile: false, // stacks the grid items if true
			    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
			    mobileModeEnabled: false, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
			    minColumns: $scope.unit.sizeX*(meter/scale), // the minimum columns the grid must have
			    minRows: $scope.unit.sizeY*(meter/scale), // the minimum height of the grid, in rows
			    maxRows: $scope.unit.sizeY*(meter/scale),
			    defaultSizeX: 2, // the default width of a gridster item, if not specifed
			    defaultSizeY: 1, // the default height of a gridster item, if not specified
			    minSizeX: 1, // minimum column width of an item
			    maxSizeX: null, // maximum column width of an item
			    minSizeY: 1, // minumum row height of an item
			    maxSizeY: null, // maximum row height of an item
			    resizable: {
			       enabled: false,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, area) {
			    	   
			    	 
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished resizing
			    },
			    draggable: {
			       enabled: false, // whether dragging items is supported
			       //handle: '.my-class', // optional selector for drag handle
			       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
			       drag: function(event, $element, widget) {
			    	 
			       }, // optional callback fired when item is moved,
			       stop: function(event, $element, area) {
			    	  
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished dragging
			    }
			};

	 console.log("test opts");
	 console.log($scope.gridsterOpts.colWidth);
	 console.log($scope.gridsterOpts.maxRows);
	 console.log($scope.gridsterOpts.columns);
	 
	 
	 //MODAL FOR ONE AREA
	  $scope.complexResult = null;
		 $scope.showModal = function(area,$parent) {
			 console.log(area);
			    // Just provide a template url, a controller and call 'showModal'.
			    ModalService.showModal({
			    	
			    	      templateUrl: "views/viewAreaTemplate.html",
			    	      controller: "viewAreaController",
			    	      inputs: {
			    	        title: "Update Area",
			    	        area:area
			    	      }
			    	    }).then(function(modal) {
			    	      modal.element.modal();
			    	      modal.close.then(function(result) {
			    	      var area  = result.area;
			    	      // console.log("in then");
			    	      // console.log("scope test1");
			    	  	// console.log(   angular.element(document.getElementById('1')).scope());
			    	     //  $parent.updateArea(area);
			    	      // console.log("scope test2");
			    	  	// console.log(   angular.element(document.getElementById('1')).scope());
			    	       //$state.reload();
			    	      });
			    	    });

			  };//END SHOWMODAL
			  
			  $scope.dismissModal = function(result) {
				    close(result, 200); // close, but give 200ms for bootstrap to animate
				    console.log("in dissmiss");
				 };
})



app.controller('viewBuildingExController', ['$scope', '$http','$state','$routeParams','shareData','ModalService','Upload', '$timeout', function ($scope, $http,$state, $routeParams, shareData,ModalService,Upload,$timeout) {
	
	$scope.showFloorPlan=false;
	$scope.showUnitPlan=false;
	angular.element(document).ready(function () {
		//retrieve buildings when page loaded
		$scope.data = {};
		//var buildings ={name: $scope.name, address: $scope.address};
		$http.get("//localhost:8443/building/viewBuildingsWithUnits").then(function(response){
			$scope.buildings = response.data;
			console.log("DISPLAY ALL BUILDINGS");
			console.log("test view buildings");
			console.log( $scope.buildings);
			
			//GET PAGINATION FOR VIEWING GBUILDINGS
			// $scope.totalItems = $scope.buildings;
			  $scope.currentPage = 1;
			  $scope.currentPageLevel = 1;
			  $scope.setPage = function (pageNo) {
			    $scope.currentPage = pageNo;
			  };

			  $scope.maxSize = 1;
			//  $scope.bigTotalItems = 175;
			  //$scope.bigCurrentPage = 1;
		},function(response){
			console.log("did not view buildings, server error");
			//console.log("response is : ")+JSON.stringify(response);
		}	
		)
	});
	
	$scope.passBuilding = function(building){
		shareData.addData(building);	
	}

	$scope.viewBuildingImage=function(index){
		angular.forEach($scope.buildings, function(building){    
				if($scope.buildings.indexOf(building)==index)
					shareData.addData(building);
		});
		$state.go('dashboard.viewBuildingEx.viewBuildingImageEx',{param3: building.name});
	}
	$scope.viewFloorPlan=function(level){
		var obj={level:level
				
		}
		
		//console.log($scope.units);
		shareData.addData(obj);
		
		$scope.showFloorPlan=true;
		$state.go('dashboard.viewBuildingEx.viewFloorPlanEx',{param1: level.levelNum});
		//$scope.showUnitPlan=false;
	}//END OF VIEW FLOOR PLAN
	
}]);

