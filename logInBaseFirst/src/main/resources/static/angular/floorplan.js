app.controller('viewFloorPlanController', function ($scope, $http,shareData,$state,ModalService) {

 
  var levelIdObj;
  var levelId;
  var level;
  var building;
  var widthForFloorPlan;
  var obj;
  $scope.legends=[];
  angular.element(document).ready(function () {
	  		//GET LEVEL	 
	  		$scope.units=[];
		   // level = shareData.getData();
	  		obj=shareData.getData();
	  		level=obj.level
	  		building=obj.building;
	  		
		    
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
			  
			    
		    //GET UNITS FROM levelIdObj
		    levelId=level.id;
		    levelIdObj={
		        id:levelId
		    }
		    $http.post('/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
		      $scope.units=angular.fromJson(response.data);	
		      angular.forEach($scope.units, function(unit){   
					$scope.addToLegend(unit);
					
				})
		      
		    },function(response){
		      console.log("DID NOT VIEW UNITS");
		     
		    })
		    
		    
		    //ADD UNIQUE UNIT TO LEGENDS
		    $scope.addToLegend= function(unit){
	  			
  				var isDuplicate=false;
	  			angular.forEach($scope.legends, function(legend){    
		  				if((isDuplicate==false)&&(unit.description == legend.description)){	
		  				
		  					isDuplicate=true;
		  					//console.log(isDuplicate);
		  				}else{
		  				
		  				//console.log(isDuplicate);
		  				}
		  			
	  				
	  			}); 
	  			
	  			if(isDuplicate){	
  					
  				}else{
  					$scope.legends.push(unit);
  				}
	  			
	  			
	  			}
		  
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
  $scope.passBuilding = function(){
	  shareData.addData(building);
	  $state.go("IFMS.viewLevels");
  }
  
  $scope.editFloorPlan= function(){
    //shareData.addData(level);
	  var obj={building:building,
				level:level
				}
		shareData.addData(obj);
  }
  
  $scope.viewUnitPlanDefault=function (unit){
	  console.log(unit);
	  if(unit.square.type=='./svg/rect.svg'){
		  
	  var obj={building:building,
				level:level,
				unit:unit,
				}
	  console.log(unit);
		shareData.addData(obj);
	  $state.go("IFMS.viewUnitPlanDefault");
	  }
  }
   $scope.showDetails= function (thisUnit) {   
     console.log(thisUnit);
    // console.log(event);
    // console.log(event.target.classList);
     $scope.showDetail="unitNumber: "+thisUnit.unitNumber  +  " Unit Width: " + parseInt((thisUnit.square.height)*(level.length)/800) + "meter, Unit Length: " + parseInt((thisUnit.square.width)*(level.length)/800) +"meter";
     console.log($scope.showDetail);
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
		 //console.log(unit);
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

//VIEW UNIT MODAL
app.controller('viewUnitController', ['$scope', '$element', 'title', 'close', 'unit',
                                                function($scope, $element, title, close,unit) {
	

		  $scope.title = title;
		  $scope.unit=unit;

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
	      $scope.background=level.filePath;
	      
//	      if(!level.id){
//	    	  console.log("FAIL TO GET LEVEL: ");
//	        level=level[0];
//	        console.log("GET LEVEL:");
//	        console.log(level);
//	      }
	      
	      //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;
	      $scope.levelLength=800;
	      $scope.levelWidth=parseInt((level.width)*800/(level.length));
	      
	      //meter=parseInt((widthForFloorPlan-40)/(level.length));
	      meter=(widthForFloorPlan-40)/(level.length);
	      $scope.levelLengthGrid=meter*(level.length);
		    $scope.levelWidthGrid=meter*(level.width);	
		   // console.log( $scope.levelLengthGrid+" "+$scope.levelWidthGrid);
	      //GET UNITS
	      levelId=level.id;
	      levelIdObj={
	          id:levelId
	      }
	      $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
	        //console.log("pure response is "+response.data);
	
	       // console.log(angular.fromJson(response.data));
	        $scope.units=angular.fromJson(response.data);
	        
	      },function(response){
	        console.log("DID NOT VIEW UNITS");
	        //console.log("response is "+angular.fromJson(response.data).error);
	      })
	      
	     // $scope.icons=[];
	       //RETRIEVE ICON WHEN LOADED
			$http.get("//localhost:8443/property/viewIcons").then(function(response){			
				
				$scope.icons = response.data;
				
				$scope.icon=$scope.icons[0];
			},function(response){
				console.log("DID NOT VIEW ICONS");
				
			})
			
			
			
			
			//GET ICON MENU
			$http.get("//localhost:8443/property/getIconsMenu").then(function(response){			
			//	console.log(response);
				var data = angular.fromJson(response.data);
			//	console.log(data);
				$scope.iconMenu=[];
				var index=0;
				 angular.forEach(data, function(item){             
					   	var iconMenuRow=[];
					   	iconMenuRow.push(data[index].name);
					   
					   	eval( 'var func = ' +data[index].funct ); //working
				//	   	console.log(data[index].funct);
					   	iconMenuRow.push(func);//working
					   	$scope.iconMenu.push(iconMenuRow);//working
					    // $scope.iconMenu.push([data[index]]);
					     index++;
				//	     console.log(iconMenuRow);
					  }); 
				// console.log("test icon menu");
				// console.log($scope.iconMenu);
				
			},function(response){
				console.log("DID NOT VIEW ICON MENU");
				
			})
		
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
				//  var dataObj = {
				//		  id: levelId,
				//		  Units:{
				//			  Unit:$scope.units
				//		  }
				 // };						
				//  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){			
				//  },function(response){			
				//  } ).then(function(){			
					  var dataObj={levelId:levelId};
					  $http.post('/property/addUnit', JSON.stringify(dataObj)).then(function(response){
						 // $scope.units=[];
						  console.log("empty");
						 // console.log($scope.units);
						  $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){			
							//  console.log(angular.fromJson(response.data));
							  $scope.units=angular.fromJson(response.data);
			
						  },function(response){
							  console.log("DID NOT view");
							 // console.log("response is "+angular.fromJson(response.data).error);
						  })
					  },function(response){//else is not saved successfully
						  console.log("UNIT CANNOT BE ADDED");
						  //alert("UNIT CANNOT BE ADDED");
					  })
				 // } )
			  }//END ADD UNIT

			  $scope.specialType='./svg/entry.svg';
			  $scope.addSpecialUnit = function (type) {   //note: passed in type is not used
//				  var dataObj = {
//						  id: levelId,
//						  Units:{
//							  Unit:$scope.units
//						  }
//				  };      
//				  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
//				  },function(response){
//				  } ).then(function(){
					  var dataObj={levelId:levelId,type: $scope.specialType};
					  $http.post('/property/addDefaultIcon', JSON.stringify(dataObj)).then(function(response){
						//  $scope.units=[];
						//  console.log("empty");
						//  console.log($scope.units);
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
				 // } )
			  } //END ADD DEFAULT ICON
			  
			  $scope.addCustIcon = function(iconId){
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
//				  console.log(iconId);
//				  console.log(icon);
				 
				  
//				  var dataObj = {
//						  id: levelId,
//						  Units:{
//							  Unit:$scope.units
//						  }
//				  };      
//				  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
//				  },function(response){
//				  } ).then(function(){
					  var dataObj={levelId:levelId,iconId:icon.id};
					  $http.post('/property/addCustIcon', JSON.stringify(dataObj)).then(function(response){
//						  $scope.units=[];
//						  console.log("empty");
//						  console.log($scope.units);
						  $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
							  console.log(angular.fromJson(response.data));
							  $scope.units=angular.fromJson(response.data);

						  },function(response){
							  console.log("DID NOT view");
							  console.log("response is "+angular.fromJson(response.data).error);
						  })
					  },function(response){//else is not saved successfully
						  console.log("UNIT CANNOT BE ADDED");
						  //alert("UNIT CANNOT BE ADDED");
					  })
				 // } )

			  }//END ADD CUSTOMISED ICON
  $scope.saveUnits = function () {   
    //console.log("Test: start saving units");
    var saveUnits=$scope.units;
    var unitsString=angular.toJson(saveUnits);
    //console.log(unitsString);
    var dataObj = {
        id: levelId,
        Units:{
          Unit:saveUnits
        }
    };

    //console.log(dataObj);

    $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
      console.log("pure response is "+JSON.stringify(response.data));
      console.log("FLOOR PLAN IS SAVED. GOING BACK TO VIEW FLOOR PLAN...");
      shareData.addData(obj);
      $state.go("IFMS.viewFloorPlan");
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
	 // $state.go("IFMS.viewLevels");
  }
  

  
	$scope.remove = function(unit) { 
		
        ModalService.showModal({
			templateUrl: "views/yesno.html",
			controller: "YesNoController",
			inputs: {
				message: "Do you wish to delete " +unit.unitNumber+'?',
			}
		}).then(function(modal) {
			modal.element.modal();
			modal.close.then(function(result) {
				if(result){
					var dataObj={id:unit.id,levelId:levelId};

					$http.post('/property/deleteUnit', JSON.stringify(dataObj)).then(function(response){
						console.log("Deleted the unit");
						ModalService.showModal({

							templateUrl: "views/popupMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: 'Unit successfully deleted',
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
							});
						});
						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate
						};
						//END SHOWMODAL
						//$scope.units=[];
		  				 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
		  				       // console.log("pure response is "+response.data);
		  				
		  				       // console.log("test anglar.fromJon"+angular.fromJson(response.data));
		  				        $scope.units=angular.fromJson(response.data);
		  				
		  				      },function(response){
		  				        console.log("DID NOT view");
		  				       // console.log("response is "+angular.fromJson(response.data).error);
		  				      })


					},function(response){
						ModalService.showModal({

							templateUrl: "views/errorMessageTemplate.html",
							controller: "errorMessageModalController",
							inputs: {
								message: response.data,
							}
						}).then(function(modal) {
							modal.element.modal();
							modal.close.then(function(result) {
								
							});
						});
						$scope.dismissModal = function(result) {
							close(result, 200); // close, but give 200ms for bootstrap to animate

						
						};
					}	
					)
				}
			});
		});

		$scope.dismissModal = function(result) {
		};

		//END SHOWMODAL
		      
		
		   
	}//END REMOVE
	//console.log("scope test 0");
	// console.log(   angular.element(document.getElementById('1')).scope());
	$scope.updateUnit=function(unit){
		 console.log("Update the unit");
		
		// console.log("scope test3");
	//	 console.log(   angular.element(document.getElementById('1')).scope());
		//  var dataObj = {
		//	        id: levelId,
		//	        Units:{
		//	          Unit:$scope.units
		//	        }
		//	    };
		//  $http.post('/property/saveUnits', JSON.stringify(dataObj)).then(function(response){
		    	
		 // },function(response){
		        
		    //  } ).then(function(){
		  			
		  			var dataObj={unit:unit,levelId:levelId};
		  			$http.post('/property/updateUnit', JSON.stringify(dataObj)).then(function(response){
		  				//$scope.units=[];//MIGHT NEED TO MOVE BACK, ATTENTION
		  				//console.log("empty");
		  				//console.log($scope.units);
		  				 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
		  				        //console.log(response.data);
		  				
		  				       // console.log(angular.fromJson(response.data));
		  				      $scope.units=angular.fromJson(response.data);
		  				       // $state.reload();//Can use
		  				     
		  				      },function(response){
		  				        console.log("DID NOT view");
		  				       // console.log("response is "+angular.fromJson(response.data).error);
		  				      })
		  		},function(response){//else is not saved successfully
		  				console.log("UNIT CANNOT BE EDITED");
		  			
		  		})
		  		
		  		
		    //  } )
	 };//END UPDATE UNIT
//  for external event organisers
  var unitIds="";
  $scope.addToUnitIds=function(unitId){
    unitIds+=(unitId+" ");
    //console.log(unitIds);
  }
//  pass selected units ids to share data
  $scope.passUnitIds=function(){
    var stringToPassUnit=unitIds.substring(0,unitIds.length-1);
   // console.log(stringToPassUnit);
    var objToPassUnit={'units':stringToPassUnit};
    shareData.addData(JSON.stringify(objToPassUnit));
   // console.log(JSON.stringify(objToPassUnit));
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
		    	    	  if(result){  
		    	    		  if(result){
		    	    			  var unit  = result.unit;
			    	    		  $parent.updateUnit(unit);
		    	    		  }
		    	    		 
		    	    		  }
		    	      });
		    	    });

		  };//END SHOWMODAL
		  
		  $scope.dismissModal = function(result) {
			    close(result, 200); // close, but give 200ms for bootstrap to animate
			    //$parent.updateUnit();
			   // console.log("in dissmiss");
			 };
	//GRIDSTER
			 /*$scope.standardItems = [
			                         { sizeX: 100, sizeY: 50, row: 0, col: 0 ,haha:"haha",test:"test"},
			                         { sizeX: 20, sizeY: 20, row: 50, col: 50 ,haha:"hoho",test:"hoho"}
			                        
			                       ];*/
			
			
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
					    sparse: true, // "true" can increase performance of dragging and resizing for big grid (e.g. 20x50)
					    isMobile: false, // stacks the grid items if true
					    mobileBreakPoint: 0, // if the screen is not wider that this, remove the grid layout and stack the items
					    mobileModeEnabled: false, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
					    minColumns: level.length, // the minimum columns the grid must have
					    minRows: level.width, // the minimum height of the grid, in rows
					    maxRows: level.width,
					    defaultSizeX: 2, // the default width of a gridster item, if not specifed
					    defaultSizeY: 1, // the default height of a gridster item, if not specified
					    minSizeX: 1, // minimum column width of an item
					    maxSizeX: level.length, // maximum column width of an item
					    minSizeY: 1, // minumum row height of an item
					    maxSizeY: level.width, // maximum row height of an item
					    resizable: {
					       enabled: true,
					       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
					       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
					       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
					       stop: function(event, $element, unit) {
					    	   
					    	   //console.log($element);
					    	 //  console.log(unit);
					    	   console.log("X:"+unit.sizeX+"Y:"+unit.sizeY+"row:"+unit.row+"col:"+unit.col);
					    	   $scope.updateUnit(unit);
					       } // optional callback fired when item is finished resizing
					    },
					    draggable: {
					       enabled: true, // whether dragging items is supported
					       //handle: '.my-class', // optional selector for drag handle
					       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
					       drag: function(event, $element, widget) {}, // optional callback fired when item is moved,
					       stop: function(event, $element, unit) {
					    	   //console.log($element);
					    	   //console.log(unit);
					    	   console.log("X:"+unit.sizeX+"Y:"+unit.sizeY+"row:"+unit.row+"col:"+unit.col);
					    	   $scope.updateUnit(unit);
					    	   
					       } // optional callback fired when item is finished dragging
					    }
					};
		
			
})



//UPDATE UNIT MODAL
app.controller('updateUnitController', ['$scope', '$element', 'title', 'close', 'unit',
                                                function($scope, $element, title, close,unit) {
	
		//UPDATE MODAL

		  $scope.title = title;
		  $scope.unit=unit;
		  
		  //  This close function doesn't need to use jQuery or bootstrap, because
		  //  the button has the 'data-dismiss' attribute.
		  $scope.close = function() {
		 	  close({
		      unit:$scope.unit
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  //  This cancel function must use the bootstrap, 'modal' function because
		  //  the doesn't have the 'data-dismiss' attribute.
		  $scope.cancel = function(result) {
		    //  Manually hide the modal.
		    $element.modal('hide');		    
		    //  Now call close, returning control to the caller.
		    close(result, 500); // close, but give 500ms for bootstrap to animate
		  };

		

	
}])