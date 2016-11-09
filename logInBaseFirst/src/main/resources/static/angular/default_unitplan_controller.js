//DEFAULT AREA PLAN OF A UNIT DESIGNED BY PROPERTY MANAGER
app.controller('defaultUnitPlanController', function ($scope, $http,shareData,ModalService,$state) {
	  var widthForAreaPlan;
	  var meter;
	  var unit;
	  var scale;
	  var unitIdObj;
	  angular.element(document).ready(function () {
		var obj=shareData.getData();
		//console.log(obj);
		$scope.building=obj.building;
		$scope.level=obj.level;
		$scope.unit=obj.unit;
		//console.log($scope.building);
		//console.log($scope.level);
		
		unitIdObj={id:$scope.unit.id};
	 //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;    
	   //   console.log(widthForFloorPlan);
	      //meter=parseInt((widthForFloorPlan-20)/($scope.unit.sizeX));
	      meter=(widthForFloorPlan-20)/($scope.unit.sizeX);
	      $scope.unitLengthGrid=meter*($scope.unit.sizeX);
		  $scope.unitWidthGrid=meter*($scope.unit.sizeY);	
		  scale=meter/2;//one grid represent 0.5m
		//  console.log( $scope.unitLengthGrid+" "+$scope.unitWidthGrid);
		//get event id from previous page
		  			var getAreas= $http({
		  				method  : 'POST',
		  				url     : 'https://localhost:8443/area/viewAreasDefault/',
		  				data    : unitIdObj,
		  		});
		  		//	console.log("REACHED HERE FOR VIEWING AREAS " + JSON.stringify(unitIdObj));
		  			getAreas.success(function(response){
		  				//console.log(response);
		  				$scope.areas = response;
		  				
		  			});
		  			getAreas.error(function(){
		  			 console.log("Error in viewing areas of default unit plan");	
		  			});		
		  			
		  			
		  		  //RETRIEVE ICON WHEN LOADED
					$http.get("//localhost:8443/property/viewIcons").then(function(response){			
						//console.log(response.data);
						$scope.icons = response.data;
						
						$scope.icon=$scope.icons[0];
					},function(response){
						console.log("DID NOT VIEW ICONS");
						
					})
					
					
					
					
					//GET ICON MENU
					$http.get("//localhost:8443/property/getIconsMenu").then(function(response){			
						//console.log(response);
						var data = angular.fromJson(response.data);
						//console.log(data);
						//$scope.iconMenu=[];
						var index=0;
						 angular.forEach(data, function(item){             
							   	var iconMenuRow=[];
							   	iconMenuRow.push(data[index].name);
							   
							   	eval( 'var func = ' +data[index].funct ); //working
							  // 	console.log(data[index].funct);
							   	iconMenuRow.push(func);//working
							   	$scope.menuOptions.push(iconMenuRow);//working
							    // $scope.iconMenu.push([data[index]]);
							     index++;
							    // console.log(iconMenuRow);
							  }); 
						
					},function(response){
						console.log("DID NOT VIEW ICONS");
						
					})
		  		
	});
	$scope.menuOptions = [
	                      
	                       // null,        // Dividier
	                        ['<img  class="svgtest" src="./svg/entry.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {
	                            $scope.defaultIcon='./svg/entry.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/exit.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                     
	                            $scope.defaultIcon='./svg/exit.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/chair.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                      
	                            $scope.defaultIcon='./svg/chair.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/armchair.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                      
	                            $scope.defaultIcon='./svg/armchair.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/table.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                        
	                            $scope.defaultIcon='./svg/table.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/stage.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/stage.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/food.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/food.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/restroomMan.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/restroomMan.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/restroomWoman.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/restroomWoman.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        ['<img  class="svgtest" src="./svg/restroomWheelchair.svg" alt="" width="50px" height="50px">', function ($itemScope, $event, modelValue, text, $li) {                  
	                            $scope.defaultIcon='./svg/restroomWheelchair.svg';
	                            $scope.addDefaultIcon();
	                        }],
	                        null
	                    ];
	  
	 $scope.defaultIcon='./svg/entry.svg';
	  $scope.addDefaultIcon = function (type) {   //note: passed in type is not used

			  var obj={unitId:$scope.unit.id,type: $scope.defaultIcon};
			  $http.post('/area/addDefaultIconDefault', JSON.stringify(obj)).then(function(response){
				 // $scope.areas=[];
				 // console.log("empty");
				 // console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreasDefault', JSON.stringify(unitIdObj)).then(function(response){
					  //console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);

				  },function(response){
					  console.log("DID NOT VIEW AREAS OF DEFAULT UNIT PLAN");
					  //console.log("response is "+angular.fromJson(response.data).error);
				  })
			  },function(response){//else is not saved successfully
				  console.log("DEFAULT ICON CANNOT BE ADDED");
				 // alert("DEFAULT ICON CANNOT BE ADDED");
			  })
		 
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
	
		 
			  var saveIconObj={unitId:$scope.unit.id,iconId:icon.id};
			  $http.post('/area/addCustIconDefault', JSON.stringify(saveIconObj)).then(function(response){
				  //$scope.areas=[];
				  //console.log("empty");
				  //console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreasDefault', JSON.stringify(unitIdObj)).then(function(response){
					 // console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);

				  },function(response){
					  console.log("DID NOT VIEW AREAS OF DEFAULT UNIT PLAN");
					  //console.log("response is "+angular.fromJson(response.data).error);
				  })
			  },function(response){//else is not saved successfully
				  console.log("CUST ICON CANNOT BE ADDED");
				  //alert("CUST ICON CANNOT BE ADDED");
			 
		  } )

	  }//END ADD CUSTOMISED ICON
	  
	  //PASS BUILDING TO SHAREDATA FOR GOING BACK TO VIEW LEVELS
	  $scope.passBuilding = function(){
		  shareData.addData($scope.building);
		  $state.go("IFMS.viewLevels");
	  }
	  
	  
	  //FOR GO BACK TO VIEW FLOOR PLAN
	  $scope.viewFloorPlan= function(){
	    //shareData.addData(level);
		  var obj={building:$scope.building,
					level:$scope.level
					}
			shareData.addData(obj);
		  $state.go("IFMS.viewFloorPlan");
	  }
	  
	 
	$scope.viewUnitPlan=function(){
		
		var obj={
				
				building:$scope.building,
				level:$scope.level,
				unit:$scope.unit
				};
		shareData.addData(obj);
		 $state.go("IFMS.viewUnitPlanDefault");
	}

	$scope.passEvent=function(){
		shareData.addData($scope.event);
	}

	$scope.passBooking=function(booking){
		console.log(booking);
		var obj={
				event:$scope.event,
				booking:booking
				};
		shareData.addData(obj);
	}

	  $scope.addArea = function () {  
			
		 	
			  var dataObj={unitId:$scope.unit.id};
			  $http.post('/area/addAreaDefault', JSON.stringify(dataObj)).then(function(response){
				 // $scope.areas=[];
				 // console.log("empty");
				 // console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreasDefault', JSON.stringify(unitIdObj)).then(function(response){			
					 // console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);
	
				  },function(response){
					  console.log("DID NOT view");
					  //console.log("response is "+angular.fromJson(response.data));
				  })
			  },function(response){//else is not saved successfully
				  console.log("AREA CANNOT BE ADDED");
				 // alert("AREA CANNOT BE ADDED");
			 
		  } )
	  }//END ADD AREA
	  
		$scope.updateArea=function(area){
			 console.log("Update the area");
			
		
			
			  			var dataObj={area:area,unitId:$scope.unit.id};
			  			$http.post('/area/updateAreaDefault', JSON.stringify(dataObj)).then(function(response){
			  				//$scope.areas=[];
			  				//console.log("empty");
			  				//console.log($scope.units);
			  				 $http.post('//localhost:8443/area/viewAreasDefault', JSON.stringify(unitIdObj)).then(function(response){
			  				       // console.log(response.data);
			  				
			  				       // console.log(angular.fromJson(response.data));
			  				      $scope.areas=angular.fromJson(response.data);
			  				  //  console.log("scope test4");
			  					// console.log(   angular.element(document.getElementById('1')).scope());
			  				       // $state.reload();//Can use
			  				     
			  				      },function(response){
			  				        console.log("DID NOT view");
			  				        //console.log("response is "+angular.fromJson(response.data).error);
			  				      })
			  				      
			  				      
			  		},function(response){//else is not saved successfully
			  				console.log("AREA CANNOT BE EDITED");
			  			//alert("AREA CANNOT BE EDITED");
			  		})
			  		
			  		
			     
		 };//END UPDATE AREA
		 $scope.remove = function(area) { 
				
			  ModalService.showModal({
					templateUrl: "views/yesno.html",
					controller: "YesNoController",
					inputs: {
						message: "Do you wish to delete " +area.areaName+'?',
					}
				}).then(function(modal) {
					modal.element.modal();
					modal.close.then(function(result) {
						if(result){
							var dataObj={id:area.id,unitId:$scope.unit.id};
				  			$http.post('/area/deleteAreaDefault', JSON.stringify(dataObj)).then(function(response){
								console.log("Delete the area");
								 $http.post('//localhost:8443/area/viewAreasDefault', JSON.stringify(unitIdObj)).then(function(response){
				  				        //console.log("pure response is "+response.data);
				  				
				  				       // console.log("test anglar.fromJon"+angular.fromJson(response.data));
				  				        $scope.areas=angular.fromJson(response.data);
				  				
				  				      },function(response){
				  				        console.log("DID NOT view");
				  				        //console.log("response is "+angular.fromJson(response.data).error);
				  				      })


							},function(response){
									console.log("DID NOT DELETE AREA");
							}	
							)
						}
					});
				});

				$scope.dismissModal = function(result) {
					close(result, 200); // close, but give 200ms for bootstrap to animate

					console.log("in dissmiss");
				};

				//END SHOWMODAL
			    
			
			   
		}//END REMOVE
	$scope.saveAreas = function () {   

		console.log("Test: start saving areas");
		var saveAreas=$scope.areas;
		var areasString=angular.toJson(saveAreas);
		console.log(areasString);

		var dataObj = {
				id: $scope.unit.id,
				Areas:{
					Area:saveAreas
				}
		};

		console.log(dataObj);

		$http.post('/area/saveAreasDefault', JSON.stringify(dataObj)).then(function(response){
			console.log("pure response is "+JSON.stringify(response.data));
	

		},function(response){//else is not saved successfully
			console.log("DID NOT SAVE");
			console.log("response is "+JSON.stringify(response.data));
		})


	} //END SAVE AREAS

	


	
	$scope.downloadPlan = function () {
		 // console.log("her0");
		  //console.log(html2canvas);
		
		
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
			       enabled: true,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, area) {
			    	   
			    	 
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished resizing
			    },
			    draggable: {
			       enabled: true, // whether dragging items is supported
			       //handle: '.my-class', // optional selector for drag handle
			       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
			       drag: function(event, $element, widget) {
			    	 
			       }, // optional callback fired when item is moved,
			       stop: function(event, $element, area) {
			    	  
			    	   $scope.updateArea(area);
			       } // optional callback fired when item is finished dragging
			    }
			};

	
	 
	 
	 //MODAL FOR ONE AREA
	  $scope.complexResult = null;
		 $scope.showModal = function(area,$parent) {
			 //console.log(area);
			    // Just provide a template url, a controller and call 'showModal'.
			    ModalService.showModal({
			    	
			    	      templateUrl: "views/updateAreaTemplate.html",
			    	      controller: "updateAreaController",
			    	      inputs: {
			    	        title: "Update Area",
			    	        area:area
			    	      }
			    	    }).then(function(modal) {
			    	      modal.element.modal();
			    	      modal.close.then(function(result) {
			    	    	  if(result){  
			    	      var area  = result.area;
			    	       $parent.updateArea(area);
			    	    	  }
			    	      });
			    	    });

			  };//END SHOWMODAL
			  
			  $scope.dismissModal = function(result) {
				    close(result, 200); // close, but give 200ms for bootstrap to animate
				   // $parent.updateArea();
				 //   console.log("in dissmiss");
				 };
})



//Unit Plan of Event used by event organiser
app.controller('viewDefaultUnitPlanController', function ($scope, $http,shareData,ModalService,$state) {
	  var widthForAreaPlan;
	  var meter;	  
	  var scale;
	  var levelIdObj;
	  $scope.areas=[];
	  $scope.legends=[];
	  $scope.unit;
	angular.element(document).ready(function () {
		var obj=shareData.getData();
		$scope.building=obj.building;
		$scope.level=obj.level;
		$scope.unit=obj.unit;
		
		unitIdObj={id:$scope.unit.id};
	 //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;    
	     // console.log(widthForFloorPlan);
	     // meter=parseInt((widthForFloorPlan-20)/($scope.unit.sizeX));
	      meter=(widthForFloorPlan-20)/($scope.unit.sizeX);
	      $scope.unitLengthGrid=meter*($scope.unit.sizeX);
		  $scope.unitWidthGrid=meter*($scope.unit.sizeY);	
		  scale=meter/2;//one grid represent 0.5m
		//  console.log( $scope.unitLengthGrid+" "+$scope.unitWidthGrid);
		//get event id from previous page
		  			var getAreas= $http({
		  				method  : 'POST',
		  				url     : 'https://localhost:8443/area/viewAreasDefault/',
		  				data    : unitIdObj,
		  		});
		  		//	console.log("REACHED HERE FOR VIEWING AREAS " + JSON.stringify(unitIdObj));
		  			getAreas.success(function(response){
		  			//	console.log(response);
		  				$scope.areas = response;

		  				angular.forEach($scope.areas, function(area){   
		  					$scope.addToLegend(area);
		  					
		  				})
		  				
		  			});
		  			getAreas.error(function(){
		  				//$scope.areas =[];
		  				console.log("ERROR VIEWING AREAS OF DEFAULT UNIT PLAN");
		  			});	
		  			
		  			
		  			$scope.addToLegend= function(area){
		  			//PUSH UNIQUE AREA TO LEGENDS
		  		
	  				var isDuplicate=false;
		  			angular.forEach($scope.legends, function(legend){    
			  				if((isDuplicate==false)&&(area.description == legend.description)){	
			  				
			  					isDuplicate=true;
			  					//console.log(isDuplicate);
			  				}else{
			  				
			  				//console.log(isDuplicate);
			  				}
			  			
		  				
		  			}); 
		  			
		  			if(isDuplicate){	
	  					
	  				}else{
	  					$scope.legends.push(area);
	  				}
		  			//console.log($scope.legends);
		  			
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
		  $state.go("IFMS.viewLevels");
	  }
	  
	  
	  //FOR GO BACK TO VIEW FLOOR PLAN
	  $scope.viewFloorPlan= function(){
	    //shareData.addData(level);
		  var obj={building:$scope.building,
					level:$scope.level
					}
			shareData.addData(obj);
		  $state.go("IFMS.viewFloorPlan");
	  }
	  
	 
	$scope.editUnitPlan=function(){
		
		var obj={
				
				building:$scope.building,
				level:$scope.level,
				unit:$scope.unit
				};
		shareData.addData(obj);
		 $state.go("IFMS.createUnitPlanDefault");
	}


	
	$scope.downloadPlan = function () {
		 
		
		  var canvasdiv = document.getElementById("screenshot");
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

	 
	 
	 
	 //MODAL FOR ONE AREA
	  $scope.complexResult = null;
		 $scope.showModal = function(area,$parent) {
			 //console.log(area);
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
			    	     
			    	      });
			    	    });

			  };//END SHOWMODAL
			  
			  $scope.dismissModal = function(result) {
				    close(result, 200); // close, but give 200ms for bootstrap to animate
				    //console.log("in dissmiss");
				 };
})

