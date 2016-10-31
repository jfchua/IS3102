//Unit Plan of Event used by event organiser
app.controller('areaPlanController', function ($scope, $http,shareData,ModalService) {
	  var widthForAreaPlan;
	  var meter;
	  var unit;
	  var scale;
	  var bookingIdObj;
	angular.element(document).ready(function () {
		var obj=shareData.getData();
		$scope.booking=obj.booking;
		$scope.event=obj.event;
		console.log($scope.booking);
		console.log($scope.event);
		unit=obj.booking.unit;
		bookingIdObj={id:$scope.booking.id};
	 //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;    
	      console.log(widthForFloorPlan);
	      meter=parseInt((widthForFloorPlan-40)/(unit.sizeX));
	      $scope.unitLengthGrid=meter*(unit.sizeX);
		  $scope.unitWidthGrid=meter*(unit.sizeY);	
		  scale=meter/2;//one grid represent 0.5m
		  console.log( $scope.unitLengthGrid+" "+$scope.unitWidthGrid);
		//get event id from previous page
		  			var getAreas= $http({
		  				method  : 'POST',
		  				url     : 'https://localhost:8443/area/viewAreas/',
		  				data    : bookingIdObj,
		  		});
		  			console.log("REACHED HERE FOR VIEWING AREAS " + JSON.stringify(bookingIdObj));
		  			getAreas.success(function(response){
		  				console.log(response);
		  				$scope.areas = response;
		  				
		  			});
		  			getAreas.error(function(){
		  				
		  			});		
		  			
		  			
		  		  //RETRIEVE ICON WHEN LOADED
					$http.get("//localhost:8443/property/viewIcons").then(function(response){			
						//console.log(response.data);
						$scope.icons = response.data;
						console.log($scope.icons);
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
						//$scope.iconMenu=[];
						var index=0;
						 angular.forEach(data, function(item){             
							   	var iconMenuRow=[];
							   	iconMenuRow.push(data[index].name);
							   
							   	eval( 'var func = ' +data[index].funct ); //working
							   	console.log(data[index].funct);
							   	iconMenuRow.push(func);//working
							   	$scope.menuOptions.push(iconMenuRow);//working
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
		  var dataObj = {
				  id: $scope.booking.id,
				  Areas:{
					  Area:$scope.areas
				  }
		  };						
		
			  var obj={bookingId:$scope.booking.id,type: $scope.defaultIcon};
			  $http.post('/area/addDefaultIcon', JSON.stringify(obj)).then(function(response){
				  $scope.areas=[];
				  console.log("empty");
				  console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
					  console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);

				  },function(response){
					  console.log("DID NOT view");
					  //console.log("response is "+angular.fromJson(response.data).error);
				  })
			  },function(response){//else is not saved successfully
				  console.log("DEFAULT ICON CANNOT BE ADDED");
				  alert("DEFAULT ICON CANNOT BE ADDED");
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
		  console.log(iconId);
		  console.log(icon);
	
		  
		  var dataObj = {
				  id: $scope.booking.id,
				  Areas:{
					  Area:$scope.areas
				  }
		  };      
		 
			  var saveIconObj={bookingId:$scope.booking.id,iconId:icon.id};
			  $http.post('/area/addCustIcon', JSON.stringify(saveIconObj)).then(function(response){
				  $scope.areas=[];
				  console.log("empty");
				  console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
					  console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);

				  },function(response){
					  console.log("DID NOT view");
					  //console.log("response is "+angular.fromJson(response.data).error);
				  })
			  },function(response){//else is not saved successfully
				  console.log("CUST ICON CANNOT BE ADDED");
				  alert("CUST ICON CANNOT BE ADDED");
			 
		  } )

	  }//END ADD CUSTOMISED ICON
	  
	  
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

	$scope.viewArea=function(){
		


	}
	$scope.showDefaultUnitPlan= function(){
		
		var unitIdObj={unitId:unit.id,bookingId:$scope.booking.id};
		 $http.post('//localhost:8443/area/saveDefaultAreas', JSON.stringify(unitIdObj)).then(function(response){
			  //console.log(angular.fromJson(response.data));
			 // $scope.areas=angular.fromJson(response.data);
			  
			  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){			
				  console.log(angular.fromJson(response.data));
				  $scope.areas=angular.fromJson(response.data);

			  },function(response){
				  console.log("DID NOT view");
				  //console.log("response is "+angular.fromJson(response.data));
			  })
			  
		  },function(response){
			  console.log("DID NOT SAVE DEFAULT AREAS");
			  //console.log("response is "+angular.fromJson(response.data).error);
		  })
		
	}

	  $scope.addArea = function () {  
		  var dataObj = {
				  id: $scope.booking.id,
				  Areas:{
					  Area:$scope.areas
				  }
		  };						
		 	
			  var dataObj={bookingId:$scope.booking.id};
			  $http.post('/area/addArea', JSON.stringify(dataObj)).then(function(response){
				  $scope.areas=[];
				  console.log("empty");
				  console.log($scope.areas);
				  $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){			
					  console.log(angular.fromJson(response.data));
					  $scope.areas=angular.fromJson(response.data);
	
				  },function(response){
					  console.log("DID NOT view");
					  //console.log("response is "+angular.fromJson(response.data));
				  })
			  },function(response){//else is not saved successfully
				  console.log("AREA CANNOT BE ADDED");
				  alert("AREA CANNOT BE ADDED");
			 
		  } )
	  }//END ADD AREA
	  
		$scope.updateArea=function(area){
			 console.log("Update the area");
			
			// console.log("scope test3");
		//	 console.log(   angular.element(document.getElementById('1')).scope());
			  var dataObj = {
				        id: $scope.booking.id,
				        Areas:{
				          Areas:$scope.areas
				        }
				    };
			
			  			var dataObj={area:area,bookingId:$scope.booking.id};
			  			$http.post('/area/updateArea', JSON.stringify(dataObj)).then(function(response){
			  				$scope.areas=[];
			  				console.log("empty");
			  				//console.log($scope.units);
			  				 $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
			  				        console.log(response.data);
			  				
			  				        console.log(angular.fromJson(response.data));
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
			  			alert("AREA CANNOT BE EDITED");
			  		})
			  		
			  		
			     
		 };//END UPDATE AREA
		 $scope.remove = function(area) { 
				
			  var dataObj = {
				        id: $scope.booking.id,
				        Areas:{
				          Areas:$scope.area
				        }
				    };
			
			    	  if (confirm('CONFIRM TO DELETE THIS Area'+area.areaName+'?')) {
			  			
			  			var dataObj={id:area.id,bookingId:$scope.booking.id};
			  			$http.post('/area/deleteArea', JSON.stringify(dataObj)).then(function(response){
			  				$scope.areas=[];
			  				 $http.post('//localhost:8443/area/viewAreas', JSON.stringify(bookingIdObj)).then(function(response){
			  				        console.log("pure response is "+response.data);
			  				
			  				        console.log("test anglar.fromJon"+angular.fromJson(response.data));
			  				        $scope.areas=angular.fromJson(response.data);
			  				
			  				      },function(response){
			  				        console.log("DID NOT view");
			  				        //console.log("response is "+angular.fromJson(response.data).error);
			  				      })
			  		},function(response){//else is not saved successfully
			  			console.log("AREA CANNOT BE DELETED");
			  		
			  		})
			  		
			  		}
			    
			
			   
		}//END REMOVE
	$scope.saveAreas = function () {   

		console.log("Test: start saving areas");
		var saveAreas=$scope.areas;
		var areasString=angular.toJson(saveAreas);
		console.log(areasString);

		var dataObj = {
				id: $scope.booking.id,
				Areas:{
					Area:saveAreas
				}
		};

		console.log(dataObj);

		$http.post('/area/saveAreas', JSON.stringify(dataObj)).then(function(response){
			console.log("pure response is "+JSON.stringify(response.data));
	

		},function(response){//else is not saved successfully
			console.log("DID NOT SAVE");
			console.log("response is "+JSON.stringify(response.data));
		})


	} //END SAVE AREAS

	

	$scope.showDetails= function (thisArea) {   
		//console.log(thisArea.id); 

		$scope.showDetail="id: "+ thisArea.id+", areaName: " + thisArea.areaName+", description: " + thisArea.description+"left: " + thisArea.square.left + ", top: " +  thisArea.square.top+ ", height: " + thisArea.square.height + ", width: " + thisArea.square.width;    

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
	 
	$scope.resize = function(area,evt,ui) {

		console.log("resize");

		area.square.width = evt.size.width;//working restrict A
		area.square.height = evt.size.height;
		area.square.left = parseInt(evt.position.left);
		area.square.top = parseInt(evt.position.top);
	}
	$scope.drag = function(area,evt,ui) {

		console.log(evt);
		console.log("DRAGGING");
		area.square.left = parseInt(evt.position.left);
		area.square.top = parseInt(evt.position.top);
		area.square.width = evt.helper.context.clientWidth;
		area.square.height = evt.helper.context.clientHeight;
	}



	/*
	    var areaIds="";
	    $scope.addToAreaIds=function(areaId){
	        areaIds+=(areaId+" ");
	        console.log(areaIds);
	    }

	    $scope.passAreaIds=function(){
	    	var stringToPassArea=areaIds.substring(0,areaIds.length-1);
	    	console.log(stringToPassArea);
	    	var objToPassArea={'areas':stringToPassArea};
	    	shareData.addData(JSON.stringify(objToPassArea));
	    	console.log(JSON.stringify(objToPassArea));
	    }
	 */
	
	 console.log("gridster test ");
	 console.log(meter);
	 $scope.gridsterOpts = {
			 
			 	
			    columns: unit.sizeX*(meter/scale), // the width of the grid, in columns
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
			    minColumns: unit.sizeX*(meter/scale), // the minimum columns the grid must have
			    minRows: unit.sizeY*(meter/scale), // the minimum height of the grid, in rows
			    maxRows: unit.sizeY*(meter/scale),
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
			    	
			    	      templateUrl: "views/updateAreaTemplate.html",
			    	      controller: "updateAreaController",
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
			    	       $parent.updateArea(area);
			    	      // console.log("scope test2");
			    	  	// console.log(   angular.element(document.getElementById('1')).scope());
			    	       //$state.reload();
			    	      });
			    	    });

			  };//END SHOWMODAL
			  
			  $scope.dismissModal = function(result) {
				    close(result, 200); // close, but give 200ms for bootstrap to animate
				    $parent.updateArea();
				    console.log("in dissmiss");
				 };
})


//UPDATE AREA MODAL
app.controller('updateAreaController', ['$scope', '$element', 'title', 'close', 'area',
                                                function($scope, $element, title, close,area) {
	
		//UPDATE MODAL

		  $scope.title = title;
		  $scope.area=area;
		  //console.log(title);
		  //console.log(close);
		  //console.log($element);
		  //  This close function doesn't need to use jQuery or bootstrap, because
		  //  the button has the 'data-dismiss' attribute.
		  $scope.close = function() {
		 	  close({
		      area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  //  This cancel function must use the bootstrap, 'modal' function because
		  //  the doesn't have the 'data-dismiss' attribute.
		  $scope.cancel = function() {

		    //  Manually hide the modal.
		    $element.modal('hide');
		    
		    //  Now call close, returning control to the caller.
		    close({
		    	area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		

	
}])


//Unit Plan of Event used by event organiser
app.controller('viewAreaPlanController', function ($scope, $http,shareData,ModalService) {
	  var widthForAreaPlan;
	  var meter;
	  
	  var scale;
	  var bookingIdObj;
	
	  $scope.areas=[];
	  $scope.legends=[];
	angular.element(document).ready(function () {
		var obj=shareData.getData();
		$scope.booking=obj.booking;
		$scope.event=obj.event;
		console.log($scope.booking);
		console.log($scope.event);
		$scope.unit=obj.booking.unit;
		bookingIdObj={id:$scope.booking.id};
	 //SET GLASSBOX SIZE ACCORDING TO LEVEL ATTRIBUTES LENGHTH AND WIDTH
	      widthForFloorPlan= document.getElementById('panelheadGrid').clientWidth;    
	      console.log(widthForFloorPlan);
	      meter=parseInt((widthForFloorPlan-40)/($scope.unit.sizeX));
	      $scope.unitLengthGrid=meter*($scope.unit.sizeX);
		  $scope.unitWidthGrid=meter*($scope.unit.sizeY);	
		  scale=meter/2;//one grid represent 0.5m
		  console.log( $scope.unitLengthGrid+" "+$scope.unitWidthGrid);
		//get event id from previous page
		  			var getAreas= $http({
		  				method  : 'POST',
		  				url     : 'https://localhost:8443/area/viewAreas/',
		  				data    : bookingIdObj,
		  		});
		  			console.log("REACHED HERE FOR VIEWING AREAS " + JSON.stringify(bookingIdObj));
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

	
	$scope.showDefaultUnitPlan= function(){
		var unitIdObj={id:$scope.unit.id};
		 $http.post('//localhost:8443/area/viewAreasDefault', JSON.stringify(unitIdObj)).then(function(response){
			  console.log(angular.fromJson(response.data));
			  $scope.areas=angular.fromJson(response.data);
			  angular.forEach($scope.areas, function(area){   
					$scope.addToLegend(area);
					
				})

		  },function(response){
			  console.log("DID NOT view");
			  //console.log("response is "+angular.fromJson(response.data).error);
		  })
		
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

	$scope.viewArea=function(){
		


	}
	


	
	$scope.downloadPlan = function () {
		  console.log("her0");
		  console.log(html2canvas);
		
		
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


//VIEW AREA MODAL
app.controller('viewAreaController', ['$scope', '$element', 'title', 'close', 'area',
                                                function($scope, $element, title, close,area) {
	
		//UPDATE MODAL

		  $scope.title = title;
		  $scope.area=area;
		 
		  $scope.close = function() {
		 	  close({
		      area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		  //  This cancel function must use the bootstrap, 'modal' function because
		  //  the doesn't have the 'data-dismiss' attribute.
		  $scope.cancel = function() {

		    //  Manually hide the modal.
		    $element.modal('hide');
		    
		    //  Now call close, returning control to the caller.
		    close({
		    	area:$scope.area
		    }, 500); // close, but give 500ms for bootstrap to animate
		  };

		

	
}])
