app.controller('viewFloorPlanController', function ($scope, $http,shareData,$state,ModalService) {

 
  var levelIdObj;
  var levelId;
  var level;
  var building;
  var widthForFloorPlan;
  var obj;
  
  angular.element(document).ready(function () {
	  		//GET LEVEL	 
	  		$scope.units=[];
		   // level = shareData.getData();
	  		obj=shareData.getData();
	  		level=obj.level
	  		building=obj.building;
	  		console.log("GET Level");
	  		console.log(level);
	  		console.log("GET BUILDING");
	  		console.log(building);
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
		   
		      
		      meter=parseInt((widthForFloorPlan-12)/(level.length));
		      $scope.levelLengthGrid=meter*(level.length);
			    $scope.levelWidthGrid=meter*(level.width);	
			    console.log( $scope.levelLengthGrid+" "+$scope.levelWidthGrid);
			    
		    //GET UNITS FROM levelIdObj
		    levelId=level.id;
		    levelIdObj={
		        id:levelId
		    }
		    $http.post('/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
		      $scope.units=angular.fromJson(response.data);		
		    },function(response){
		      console.log("DID NOT view");
		      console.log("response is "+angular.fromJson(response.data).error);
		    })
		    /*
		    //RETRIEVE ICON WHEN LOADED
			$http.get("//localhost:8443/property/viewIcons").then(function(response){			
				console.log(angular.fromJson(response.data));
				$scope.icons = response.data;
				console.log($scope.icons);
				console.log($scope.icons[0]);
				console.log($scope.icons[0].iconType);
				console.log($scope.icons[0].iconPath);
				$scope.icon=$scope.icons[0];
			},function(response){
				alert("DID NOT VIEW ICONS");
				
			})
			*/
		    //GET BUILDING FROM levelIdObj
		    /*
		    var levelIdObj={id:level.id}
		    $http.post('/level/getBuilding', JSON.stringify(levelIdObj)).then(function(response){
		      console.log('GET BUILDING SUCCESS! ' + JSON.stringify(response));
		     building=response.data;
		      console.log(building);
		      console.log("Building ID IS " + building.id);
		    },function(response){
		      console.log('GET BUILDING ID FAILED! ' + JSON.stringify(response));
		    });	
		    */
		    console.log(building);
  });
  
  //PASS BUILDING TO SHAREDATA FOR GOING BACK TO VIEW LEVELS
  $scope.passBuilding = function(){
	  shareData.addData(building);
	  $state.go("dashboard.viewLevels");
  }
  
  $scope.editFloorPlan= function(){
    //shareData.addData(level);
	  var obj={building:building,
				level:level
				}
		shareData.addData(obj);
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
					    margins: [0, 0], // the pixel distance between each widget
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

//VIEW UNIT MODAL
app.controller('viewUnitController', ['$scope', '$element', 'title', 'close', 'unit',
                                                function($scope, $element, title, close,unit) {
	

		  $scope.title = title;
		  $scope.unit=unit;
		  console.log(title);
		  console.log(close);
		  console.log($element);
		  //  This close function doesn't need to use jQuery or bootstrap, because
		  //  the button has the 'data-dismiss' attribute.
		  $scope.close = function() {
		 	  close({
		     
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  //  This cancel function must use the bootstrap, 'modal' function because
		  //  the doesn't have the 'data-dismiss' attribute.
		  $scope.cancel = function() {

		    //  Manually hide the modal.
		    $element.modal('hide');
		    
		    //  Now call close, returning control to the caller.
		    close({
		    	
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  

	
}])


//FLOORPLAN
app.directive('draggable', function() {
  console.log("test draggable directive");
  jQuery = window.jQuery;
  console.log(jQuery);
  console.log(jQuery.ui);
  return {
    // A = attribute, E = Element, C = Class and M = HTML Comment
    restrict:'A',
    controller: 'floorPlanController',

    /*
          scope: {

                callback: '&onDrag'
            },
     */
    //The link function is responsible for registering DOM listeners as well as updating the DOM.
    //link: function postLink(scope, element, attrs) {
    link: function (scope, element, attrs) {
      element.draggable({
        //revert:'invalid'
        containment: '#glassbox',
        
         obstacle: "#1",
            preventCollision: true
      
        

      });
      element.on('drag', function (unit,evt, ui) {
        //console.log(evt);
        //  console.log(ui);
        scope.$apply(function() {
          if (scope.callback) { 
            scope.callback({$evt: evt, $ui: ui }); 
          }                
        })
      });
    }
  };
})

app.directive('resizable', function () {
  console.log("test resiable directive");
  // jQuery = window.jQuery;
  //console.log(jQuery);
  console.log("test jquery ui");
  console.log(jQuery.ui);
  return {

    restrict: 'A',
    controller: 'floorPlanController',
    scope: {

      callback: '&onResize'

    },
    link: function postLink(scope, element, attrs) {
      element.resizable({
        containment: '#glassbox'
      });
      console.log("test jquery ui");
      element.on('resize', function (unit,evt, ui) {
        //console.log(evt);
        //  console.log(ui);
        scope.$apply(function() {
          if (scope.callback) { 
            console.log("test jquery ui");
            scope.callback({$evt: evt, $ui: ui }); 
          }                
        })
      });
    }
  };
})




//UPDATE FLOOR PLAN
app.controller('floorPlanController', function ($scope, $http,shareData,$state,ModalService) {
  //jQuery = window.jQuery;
  //console.log(jQuery);
  //console.log(jQuery.ui);
  var levelIdObj;
  var levelId;
  var level;
  var building;
  $scope.units=[];
  var widthForFloorPlan;
  var obj;
  var meter;
  angular.element(document).ready(function () {
	  
	    	//GET LEVEL
	      //level = shareData.getData();
	  		obj=shareData.getData();
	  		level=obj.level
	  		building=obj.building;
	      $scope.levelLength;
	      $scope.levelWidth;     
	      console.log("GET LEVEL: ");
	      console.log(level);
	      if(!level.id){
	    	  console.log("FAIL TO GET LEVEL: ");
	        level=level[0];
	        console.log("GET LEVEL:");
	        console.log(level);
	      }
	      
	      //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;
	      $scope.levelLength=800;
	      $scope.levelWidth=parseInt((level.width)*800/(level.length));
	      
	      meter=parseInt((widthForFloorPlan-12)/(level.length));
	      $scope.levelLengthGrid=meter*(level.length);
		    $scope.levelWidthGrid=meter*(level.width);	
		    console.log( $scope.levelLengthGrid+" "+$scope.levelWidthGrid);
	      //GET UNITS
	      levelId=level.id;
	      levelIdObj={
	          id:levelId
	      }
	      $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
	        console.log("pure response is "+response.data);
	
	        console.log(angular.fromJson(response.data));
	        $scope.units=angular.fromJson(response.data);
	        
	      },function(response){
	        console.log("DID NOT view");
	        console.log("response is "+angular.fromJson(response.data).error);
	      })
	      
	     // $scope.icons=[];
	       //RETRIEVE ICON WHEN LOADED
			$http.get("//localhost:8443/property/viewIcons").then(function(response){			
				//console.log(response.data);
				$scope.icons = response.data;
				//console.log($scope.icons);
				//console.log($scope.icons[0]);
				//console.log($scope.icons[0].iconType);
				//console.log($scope.icons[0].iconPath);
				$scope.icon=$scope.icons[0];
			},function(response){
				alert("DID NOT VIEW ICONS");
				
			})
			
			
			
			
			//GET ICON MENU
			$http.get("//localhost:8443/property/getIconsMenu").then(function(response){			
				console.log(response);
				var data = angular.fromJson(response.data);
				console.log(data);
				$scope.iconMenu=[];
				var index=0;
				 angular.forEach(data, function(item){             
					   	var iconMenuRow=[];
					   	iconMenuRow.push(data[index].name);
					   
					   	eval( 'var func = ' +data[index].funct ); //working
					   	console.log(data[index].funct);
					   	iconMenuRow.push(func);//working
					   	$scope.iconMenu.push(iconMenuRow);//working
					    // $scope.iconMenu.push([data[index]]);
					     index++;
					     console.log(iconMenuRow);
					  }); 
				 console.log("test icon menu");
				 console.log($scope.iconMenu);
				//console.log($scope.icons);
				//console.log($scope.icons[0]);
				//console.log($scope.icons[0].iconType);
				//console.log($scope.icons[0].iconPath);
			},function(response){
				alert("DID NOT VIEW ICONS");
				
			})
			/*
			$scope.defaultTypes=[
			                  {'name':'Entry','type':'./svg/entry.svg'},
			                  {'name':'Escalator','type':'./svg/escalator.svg'},
			                  {'name':'Exit','type':'./svg/exit.svg'},
			                  {'name':'Lift','type':'./svg/lift.svg'},
			                  {'name':'Service Lift','type':'./svg/servicelift.svg'},
			                  {'name':'Staircase','type':'./svg/staircase.svg'},
			                  {'name':'Toilet','type':'./svg/toilet.svg'},
			                  {'name':'Unit','type':'./svg/rect.svg'}];
			$scope.selectedDefaultType=$scope.defaultTypes[0];
			*/
			/*  $scope.iconMenu = [
			                        
			                        // null,        // Dividier
			                         ['<img  class="svgtest" src="lights.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {
			                             var icon=$scope.icon[0];
			                        	 $scope.addSpecialUnitByIcon(icon);
			                         }],
			                  
			                        
			                     ];*/
    });
  
  
  $scope.menuOptions = [
                     
                       // null,        // Dividier
                        ['<img  class="svgtest" src="./svg/entry.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {
                            $scope.specialType='./svg/entry.svg';
                            $scope.addSpecialUnit();
                        }],
                 
                        ['<img  class="svgtest" src="./svg/escalator.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {
                            $scope.specialType='./svg/escalator.svg';
                            $scope.addSpecialUnit();
                        }],
                        ['<img  class="svgtest" src="./svg/exit.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                     
                            $scope.specialType='./svg/exit.svg';
                            $scope.addSpecialUnit();
                        }],
                        ['<img  class="svgtest" src="./svg/lift.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                      
                            $scope.specialType='./svg/lift.svg';
                            $scope.addSpecialUnit();
                        }],
                        ['<img  class="svgtest" src="./svg/servicelift.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                      
                            $scope.specialType='./svg/servicelift.svg';
                            $scope.addSpecialUnit();
                        }],
                        ['<img  class="svgtest" src="./svg/staircase.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                        
                            $scope.specialType='./svg/staircase.svg';
                            $scope.addSpecialUnit();
                        }],
                        ['<img  class="svgtest" src="./svg/toilet.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
                            $scope.specialType='./svg/toilet.svg';
                            $scope.addSpecialUnit();
                        }],
                    ];
  

  

			  $scope.addUnit = function () {  
				  var dataObj = {
						  id: levelId,
						  Units:{
							  Unit:$scope.units
						  }
				  };						
				  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){			
				  },function(response){			
				  } ).then(function(){			
					  var dataObj={levelId:levelId};
					  $http.post('/property/addUnit', JSON.stringify(dataObj)).then(function(response){
						  $scope.units=[];
						  console.log("empty");
						  console.log($scope.units);
						  $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){			
							  console.log(angular.fromJson(response.data));
							  $scope.units=angular.fromJson(response.data);
			
						  },function(response){
							  console.log("DID NOT view");
							  console.log("response is "+angular.fromJson(response.data).error);
						  })
					  },function(response){//else is not saved successfully
						  console.log("UNIT CANNOT BE ADDED");
						  alert("UNIT CANNOT BE ADDED");
					  })
				  } )
			  }//END ADD UNIT

			  $scope.specialType='./svg/entry.svg';
			  $scope.addSpecialUnit = function (type) {   //note: passed in type is not used
				  var dataObj = {
						  id: levelId,
						  Units:{
							  Unit:$scope.units
						  }
				  };      
				  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
				  },function(response){
				  } ).then(function(){
					  var dataObj={levelId:levelId,type: $scope.specialType};
					  $http.post('/property/addDefaultIcon', JSON.stringify(dataObj)).then(function(response){
						  $scope.units=[];
						  console.log("empty");
						  console.log($scope.units);
						  $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
							  console.log(angular.fromJson(response.data));
							  $scope.units=angular.fromJson(response.data);

						  },function(response){
							  console.log("DID NOT view");
							  console.log("response is "+angular.fromJson(response.data).error);
						  })
					  },function(response){//else is not saved successfully
						  console.log("UNIT CANNOT BE ADDED");
						  alert("UNIT CANNOT BE ADDED");
					  })
				  } )
			  } //END ADD DEFAULT ICON
			  
			  $scope.addSpecialUnitByIcon = function(iconId){
				  //GET SELECTED CUSTOMISED ICON
				  var index=0;
				  var found=false;
				  var icon;
				  angular.forEach($scope.icons, function(item){             
					  if(found==false && $scope.icons[index].id==iconId){
						  icon=$scope.icons[index];
					  }else
						  index = index + 1;
				  }); 
				  console.log(iconId);
				  console.log(icon);
				 
				  /*$scope.units.push({"levelId":levelId,"id": 0,"unitNumber": "","length": 100,"width": 100,"description": "#","square": {"left": 0,"top": 0,"height": 50,"width": 50, "color": "transparent","type": "","icon": {"id":icon.id,"iconType":icon.iconType,"iconPath":icon.iconPath}}});
				  var dataObj = {
						  id: levelId,
						  Units:{
							  Unit:$scope.units
						  }
				  };
				  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
					  $scope.units=[];
					  $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
						  console.log("pure response is "+response.data);

						  console.log("test anglar.fromJon"+angular.fromJson(response.data));
						  $scope.units=angular.fromJson(response.data);
					  },function(response){
						  console.log("DID NOT CREATE");
					  })

				  } )*/
				  
				  var dataObj = {
						  id: levelId,
						  Units:{
							  Unit:$scope.units
						  }
				  };      
				  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
				  },function(response){
				  } ).then(function(){
					  var dataObj={levelId:levelId,iconId:icon.id};
					  $http.post('/property/addCustIcon', JSON.stringify(dataObj)).then(function(response){
						  $scope.units=[];
						  console.log("empty");
						  console.log($scope.units);
						  $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
							  console.log(angular.fromJson(response.data));
							  $scope.units=angular.fromJson(response.data);

						  },function(response){
							  console.log("DID NOT view");
							  console.log("response is "+angular.fromJson(response.data).error);
						  })
					  },function(response){//else is not saved successfully
						  console.log("UNIT CANNOT BE ADDED");
						  alert("UNIT CANNOT BE ADDED");
					  })
				  } )

			  }//END ADD CUSTOMISED ICON
  $scope.saveUnits = function () {   
    console.log("Test: start saving units");
    var saveUnits=$scope.units;
    var unitsString=angular.toJson(saveUnits);
    console.log(unitsString);
    var dataObj = {
        id: levelId,
        Units:{
          Unit:saveUnits
        }
    };

    console.log(dataObj);

    $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
      console.log("pure response is "+JSON.stringify(response.data));
      alert("FLOOR PLAN IS SAVED. GOING BACK TO VIEW FLOOR PLAN...");
      shareData.addData(obj);
      $state.go("dashboard.viewFloorPlan");
    },function(response){//else is not saved successfully
      console.log("DID NOT SAVE");
      console.log("response is "+JSON.stringify(response.data));
    })


  } 
  //FOR GO BACK TO VIEW FLOOR PLAN
  $scope.viewFloorPlan= function(){
    //shareData.addData(level);
	  var obj={building:building,
				level:level
				}
		shareData.addData(obj);
  }
  
  //PASS BUILDING TO SHAREDATA FOR GOING BACK TO VIEW LEVELS
  $scope.passBuilding = function(){
	  shareData.addData(building);
	 // $state.go("dashboard.viewLevels");
  }
  
  $scope.viewLevelsByBuildingId = function(){
    var buildingId;
    $scope.dataToShare = [];
    //get building id from levelId
    $http.post('/level/getBuildingId', JSON.stringify(levelIdObj)).then(function(response){
      console.log('GET BUILDING SUCCESS! ' + JSON.stringify(response));
      var buildingJson=response.data;
      console.log(buildingJson);
      //var temp=JSON.stringify(buildingJson)
      buildingId=buildingJson.buildingId;

      console.log("Building ID IS " + buildingId);

    },function(response){
      console.log('GET BUILDING ID FAILED! ' + JSON.stringify(response));
    }).then(function() {

      //get levels of building id and then save to share data
      $scope.url = "https://localhost:8443/level/viewLevels/"+buildingId;
      //$scope.dataToShare = [];
      console.log("GETTING THE LEVELS")
      var getLevels = $http({
        method  : 'GET',
        url     : 'https://localhost:8443/level/viewLevels/' + buildingId

      });

      console.log("Getting the levels using the url: " + $scope.url);
      getLevels.success(function(response){
        //$scope.dataToShare.push(id);
        //$location.path("/viewLevels/"+id);
        console.log('GET LEVELS SUCCESS! ' + JSON.stringify(response));
        console.log("ID IS " + buildingId);
        shareData.addData(JSON.stringify(response));
        //shareData.addDataId(JSON.stringify(id));
        //$location.path("/viewLevels");
      });
      getLevels.error(function(response){
        $state.go("dashboard.viewBuilding");//not sure
        console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
      });

    })
  }
  //$scope.showDetail="test";
   $scope.showDetails= function (thisUnit) {   
     console.log(thisUnit);
    // console.log(event);
    // console.log(event.target.classList);
     $scope.showDetail="unitNumber: "+thisUnit.unitNumber  +  " Unit Width: " + parseInt((thisUnit.square.height)*(level.length)/800) + "meter, Unit Length: " + parseInt((thisUnit.square.width)*(level.length)/800) +"meter";
     console.log($scope.showDetail);
      } 

  $scope.resize = function(unit,evt,ui) {

    console.log("resize");

    unit.square.width = parseInt(evt.size.width);//working restrict A
    unit.square.height = parseInt(evt.size.height);
    unit.square.left = parseInt(evt.position.left);
    unit.square.top = parseInt(evt.position.top);
  
  }
  $scope.drag = function(unit,evt,ui) {

    console.log(evt);
    console.log("DRAGGING");
    // $scope.units[x.id].square.width = ui.size.width;
    //$scope.units[x.id].square.height = ui.size.height;
    //$scope.units[unit.id-1].square.width = evt.size.width;
    //$scope.units[unit.id-1].square.height = evt.size.height;
    // console.log( evt.target);
    // console.log(  evt.helper.context.clientHeight); //working
    // console.log(  evt.helper[0].clientHeight); //working restrict E
    unit.square.left = parseInt(evt.position.left);
    unit.square.top = parseInt(evt.position.top);
    unit.square.width = evt.helper.context.clientWidth;
    unit.square.height = evt.helper.context.clientHeight;
  }
/*
  $scope.remove = function(unit) { 
	  if(confirm("CONFIRM TO DELETE UNIT "+unit.unitNumber+"?")){
		 
		    var index = $scope.units.indexOf(unit);
		    $scope.units.splice(index, 1); 
		   
	  }
    
  }
  $scope.removeCopy = function(unit) { 
	  if(confirm("CONFIRM TO DELETE UNIT "+unit.unitNumber+"?")){
		 
		    var index = $scope.units.indexOf(unit);
		    $scope.units.splice(index, 1); 
		    $scope.update=[];
		    angular.copy($scope.units, $scope.update);
		    $scope.units=[];
		    angular.copy($scope.update, $scope.units);
	  }
    
  }
  */
	$scope.remove = function(unit) { 
		
		  var dataObj = {
			        id: levelId,
			        Units:{
			          Unit:$scope.units
			        }
			    };
		  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
		    	
		  },function(response){
		        
		      } ).then(function(){
		    	  if (confirm('CONFIRM TO DELETE THIS UNIT'+unit.unitNumber+'?')) {
		  			
		  			var dataObj={id:unit.id,levelId:levelId};
		  			$http.post('/property/deleteUnit', JSON.stringify(dataObj)).then(function(response){
		  				$scope.units=[];
		  				 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
		  				        console.log("pure response is "+response.data);
		  				
		  				        console.log("test anglar.fromJon"+angular.fromJson(response.data));
		  				        $scope.units=angular.fromJson(response.data);
		  				
		  				      },function(response){
		  				        console.log("DID NOT view");
		  				        console.log("response is "+angular.fromJson(response.data).error);
		  				      })
		  		},function(response){//else is not saved successfully
		  			console.log("UNIT CANNOT BE DELETED");
		  			alert("THERE ARE ADVANCE BOOKINGS ON THIS UNIT. UNIT CANNOT BE DELETED");
		  		})
		  		
		  		}
		      } )
		
		   
	}//END REMOVE
	//console.log("scope test 0");
	// console.log(   angular.element(document.getElementById('1')).scope());
	$scope.updateUnit=function(unit){
		 console.log("Update the unit");
		
		// console.log("scope test3");
	//	 console.log(   angular.element(document.getElementById('1')).scope());
		  var dataObj = {
			        id: levelId,
			        Units:{
			          Unit:$scope.units
			        }
			    };
		  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
		    	
		  },function(response){
		        
		      } ).then(function(){
		  			
		  			var dataObj={unit:unit,levelId:levelId};
		  			$http.post('/property/updateUnit', JSON.stringify(dataObj)).then(function(response){
		  				$scope.units=[];
		  				console.log("empty");
		  				console.log($scope.units);
		  				 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
		  				        console.log(response.data);
		  				
		  				        console.log(angular.fromJson(response.data));
		  				      $scope.units=angular.fromJson(response.data);
		  				  //  console.log("scope test4");
		  					// console.log(   angular.element(document.getElementById('1')).scope());
		  				       // $state.reload();//Can use
		  				     
		  				      },function(response){
		  				        console.log("DID NOT view");
		  				        console.log("response is "+angular.fromJson(response.data).error);
		  				      })
		  		},function(response){//else is not saved successfully
		  				console.log("UNIT CANNOT BE EDITED");
		  			alert("UNIT CANNOT BE EDITED");
		  		})
		  		
		  		
		      } )
	 };//END UPDATE UNIT
//  for external event organisers
  var unitIds="";
  $scope.addToUnitIds=function(unitId){
    unitIds+=(unitId+" ");
    console.log(unitIds);
  }
//  pass selected units ids to share data
  $scope.passUnitIds=function(){
    var stringToPassUnit=unitIds.substring(0,unitIds.length-1);
    console.log(stringToPassUnit);
    var objToPassUnit={'units':stringToPassUnit};
    shareData.addData(JSON.stringify(objToPassUnit));
    console.log(JSON.stringify(objToPassUnit));
  }
  
  //MODAL FOR ONE UNIT
  $scope.complexResult = null;
	 $scope.showModal = function(unit,$parent) {
		 console.log(unit);
		    // Just provide a template url, a controller and call 'showModal'.
		    ModalService.showModal({
		    	
		    	      templateUrl: "views/updateUnitTemplate.html",
		    	      controller: "updateUnitController",
		    	      inputs: {
		    	        title: "Update Unit",
		    	        unit:unit
		    	      }
		    	    }).then(function(modal) {
		    	      modal.element.modal();
		    	      modal.close.then(function(result) {
		    	      var unit  = result.unit;
		    	      // console.log("in then");
		    	      // console.log("scope test1");
		    	  	// console.log(   angular.element(document.getElementById('1')).scope());
		    	       $parent.updateUnit(unit);
		    	      // console.log("scope test2");
		    	  	// console.log(   angular.element(document.getElementById('1')).scope());
		    	       //$state.reload();
		    	      });
		    	    });

		  };//END SHOWMODAL
		  
		  $scope.dismissModal = function(result) {
			    close(result, 200); // close, but give 200ms for bootstrap to animate
			    $parent.updateUnit();
			    console.log("in dissmiss");
			 };
	//GRIDSTER
			 /*$scope.standardItems = [
			                         { sizeX: 100, sizeY: 50, row: 0, col: 0 ,haha:"haha",test:"test"},
			                         { sizeX: 20, sizeY: 20, row: 50, col: 50 ,haha:"hoho",test:"hoho"}
			                        
			                       ];*/
			
			 console.log($scope.standardItems);
			 console.log("gridster test ");
			 console.log(meter);
			 $scope.gridsterOpts = {
					 
					 	
					    columns: level.length, // the width of the grid, in columns
					    pushing: false, // whether to push other items out of the way on move or resize
					    floating: false, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
					    swapping: false, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
					    width:$scope.levelLengthGrid, // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
					    colWidth: meter, // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
					    rowHeight: meter, // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
					    margins: [0, 0], // the pixel distance between each widget
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
					       enabled: true,
					       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
					       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
					       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
					       stop: function(event, $element, unit) {
					    	   
					    	   //console.log($element);
					    	 //  console.log(unit);
					    	   $scope.updateUnit(unit);
					       } // optional callback fired when item is finished resizing
					    },
					    draggable: {
					       enabled: true, // whether dragging items is supported
					       //handle: '.my-class', // optional selector for drag handle
					       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
					       drag: function(event, $element, widget) {
					    	 
					       }, // optional callback fired when item is moved,
					       stop: function(event, $element, unit) {
					    	   //console.log($element);
					    	   //console.log(unit);
					    	   $scope.updateUnit(unit);
					       } // optional callback fired when item is finished dragging
					    }
					};
		
			 console.log("test opts");
			 console.log($scope.gridsterOpts.colWidth);
			 console.log($scope.gridsterOpts.maxRows);
			 console.log($scope.gridsterOpts.columns);
})



//UPDATE EVENT STATUS MODAL
app.controller('updateUnitController', ['$scope', '$element', 'title', 'close', 'unit',
                                                function($scope, $element, title, close,unit) {
	
		//UPDATE MODAL

		  $scope.title = title;
		  $scope.unit=unit;
		  console.log(title);
		  console.log(close);
		  console.log($element);
		  //  This close function doesn't need to use jQuery or bootstrap, because
		  //  the button has the 'data-dismiss' attribute.
		  $scope.close = function() {
		 	  close({
		      unit:$scope.unit
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  //  This cancel function must use the bootstrap, 'modal' function because
		  //  the doesn't have the 'data-dismiss' attribute.
		  $scope.cancel = function() {

		    //  Manually hide the modal.
		    $element.modal('hide');
		    
		    //  Now call close, returning control to the caller.
		    close({
		    	unit:$scope.unit
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		

	
}])
