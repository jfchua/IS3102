app.controller('viewFloorPlanController', function ($scope, $http,shareData) {
  //jQuery = window.jQuery;
  //console.log(jQuery);
  //console.log(jQuery.ui);
  var levelIdObj;
  var levelId;
  var level;
  var building;
  //$scope.units;
  
  angular.element(document).ready(function () {
	  		//GET LEVEL
	  $scope.units=[];
		    level = shareData.getData();
		    	console.log("GET LEVEL: ");
		      console.log(level);
		      $scope.levelLength;
		      $scope.levelWidth;
		    	   
		    if(!level.id){
		    	console.log("LEVEL IS NOT GET");
		      level=level[0];
		      
		    }
		    console.log("GET LEVEL: ");
		    console.log(level);
		    //SET GLASSBOX SIZE ACCORDINGTO LEVEL ATTRIBUTES LENGTH AND WIDTH
		    $scope.levelLength=900;
		    $scope.levelWidth=parseInt((level.width)*900/(level.length));
		    
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
		    var levelIdObj={id:level.id}
		    $http.post('/level/getBuilding', JSON.stringify(levelIdObj)).then(function(response){
		      console.log('GET BUILDING SUCCESS! ' + JSON.stringify(response));
		     building=response.data;
		      console.log(building);
		      console.log("Building ID IS " + building.id);
		    },function(response){
		      console.log('GET BUILDING ID FAILED! ' + JSON.stringify(response));
		    });	
		    console.log(building);
  });
  
  //PASS BUILDING TO SHAREDATA FOR GOING BACK TO VIEW LEVELS
  $scope.passBuilding = function(){
	  shareData.addData(building);
  }
  
  $scope.editFloorPlan= function(){
    shareData.addData(level);
  }
   $scope.showDetails= function (thisUnit) {   
     console.log(thisUnit);
    // console.log(event);
    // console.log(event.target.classList);
     $scope.showDetail="unitNumber: "+thisUnit.unitNumber  +  " Unit Width: " + parseInt((thisUnit.square.height)*(level.length)/900) + "meter, Unit Length: " + parseInt((thisUnit.square.width)*(level.length)/900) +"meter";
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

  

  /*
   var downloadPlan=function() {
      
        // console.log(document.getElementById("1"));//can be used in read dragging
     console.log(document.getElementById("glassbox"));  
     console.log(html2canvas);
     var glassboxEle=document.getElementById("glassbox");
     html2canvas(glassboxEle, {
                  onrendered: function(canvas) {
                      theCanvas = canvas;
                      document.body.appendChild(canvas);

                      // Convert and download as image 
                      Canvas2Image.saveAsPNG(canvas); 
                      $("#respond").append(canvas);
                      // Clean up 
                      //document.body.removeChild(canvas);
                  }
              });
      

    }*/

  $scope.downloadPlan = html2canvas;
  /*$scope.downloadPlan=function(){
  
  console.log(html2canvas);
  html2canvas(window, document);

  // expose functionality
  module.exports = {
      html2canvas: function (elements, opts) {
          return window.html2canvas(elements, opts);
      },
      src: html2canvas
  };
  }
  */
  /*
   
   $scope.downloadPlan = function(){
       html2canvas($("#glassbox"), {
              onrendered: function(canvas) {
                  theCanvas = canvas;
                  document.body.appendChild(canvas);

                  // Convert and download as image 
                  Canvas2Image.saveAsPNG(canvas); 
                  $("#respond").append(canvas);
                  // Clean up 
                  //document.body.removeChild(canvas);
              }
          });
  }
  */
  

})


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
app.controller('floorPlanController', function ($scope, $http,shareData,$location) {
  //jQuery = window.jQuery;
  //console.log(jQuery);
  //console.log(jQuery.ui);
  var levelIdObj;
  var levelId;
  var level;
  $scope.units=[];
  angular.element(document).ready(function () {
	  
	    	//GET LEVEL
	      level = shareData.getData();
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
	      $scope.levelLength=900;
	      $scope.levelWidth=parseInt((level.width)*900/(level.length));
	     
	      //GET UNITS
	      levelId=level.id;
	      levelIdObj={
	          id:levelId
	      }
	      $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
	        console.log("pure response is "+response.data);
	
	        console.log("test anglar.fromJon"+angular.fromJson(response.data));
	        $scope.units=angular.fromJson(response.data);
	
	      },function(response){
	        console.log("DID NOT view");
	        console.log("response is "+angular.fromJson(response.data).error);
	      })
	      $scope.icons=[];
	       //RETRIEVE ICON WHEN LOADED
			$http.get("//localhost:8443/property/viewIcons").then(function(response){			
				console.log(angular.fromJson(response.data));
				$scope.icons = response.data;
				console.log($scope.icons);
				//console.log($scope.icons[0]);
				//console.log($scope.icons[0].iconType);
				//console.log($scope.icons[0].iconPath);
				$scope.icon=$scope.icons[0];
			},function(response){
				alert("DID NOT VIEW ICONS");
				
			})
    });
/*
  $scope.addUnit = function () {  
	    var dataObj={"levelId":levelId,"id": 0,"unitNumber": "#unit","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "coral","type": "./svg/rect.svg","icon": ""}};
	    
	    $http.post('/property/addUnit', JSON.stringify(dataObj)).then(function(response){
	    	$scope.units=[];
			 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
			        console.log("pure response is "+response.data);
			
			        console.log("test anglar.fromJon"+angular.fromJson(response.data));
			        $scope.units=angular.fromJson(response.data);
	      },function(response){
	        console.log("DID NOT CREATE");
	      })
	 
	  } )
  }
  
	  $scope.specialType='./svg/entry.svg';
	  $scope.addSpecialUnit = function (type) {   
		  var dataObj={"levelId":levelId,"id": 0,"unitNumber": "","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "transparent","type": $scope.specialType,"icon": ""}};
	    $http.post('/property/addUnit', JSON.stringify(dataObj)).then(function(response){
	    	$scope.units=[];
			 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
			        console.log("pure response is "+response.data);
			
			        console.log("test anglar.fromJon"+angular.fromJson(response.data));
			        $scope.units=angular.fromJson(response.data);
	      },function(response){
	        console.log("DID NOT CREATE");
	      })
	 
	  } )
	 
	  } 
	  $scope.addSpecialUnitByIcon = function(icon){
		  var dataObj={"levelId":levelId,"id": 0,"unitNumber": "","length": 100,"width": 100,"description": "#","square": {"left": 0,"top": 0,"height": 50,"width": 50, "color": "transparent","type": "","icon": {"id":$scope.icon.id,"iconType":$scope.icon.iconType,"iconPath":$scope.icon.iconPath}}};
		  $http.post('/property/addUnit', JSON.stringify(dataObj)).then(function(response){
		    	$scope.units=[];
				 $http.post('//localhost:8443/property/viewUnits', JSON.stringify(levelIdObj)).then(function(response){
				        console.log("pure response is "+response.data);
				
				        console.log("test anglar.fromJon"+angular.fromJson(response.data));
				        $scope.units=angular.fromJson(response.data);
		      },function(response){
		        console.log("DID NOT CREATE");
		      })
		 
		  } )
		  
	  }
	  
	  */
  $scope.addUnit = function () {  
	    $scope.units.push({"id": 0,"unitNumber": "#unit","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "coral","type": "./svg/rect.svg","icon": ""}});
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
	 
	  } )
}

	  $scope.specialType='./svg/entry.svg';
	  $scope.addSpecialUnit = function (type) {   
		  $scope.units.push({"levelId":levelId,"id": 0,"unitNumber": "","length": 100,"width": 100,"description": "#","square": {"left": 100,"top": 100,"height": 100,"width": 100, "color": "transparent","type": $scope.specialType,"icon": ""}});
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
	 
	  } )
	 
	  } 
	  $scope.addSpecialUnitByIcon = function(icon){
		  $scope.units.push({"levelId":levelId,"id": 0,"unitNumber": "","length": 100,"width": 100,"description": "#","square": {"left": 0,"top": 0,"height": 50,"width": 50, "color": "transparent","type": "","icon": {"id":$scope.icon.id,"iconType":$scope.icon.iconType,"iconPath":$scope.icon.iconPath}}});
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
		 
		  } )
		  
	  }
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
      shareData.addData(level);
      $location.path("/viewFloorPlan");
    },function(response){//else is not saved successfully
      console.log("DID NOT SAVE");
      console.log("response is "+JSON.stringify(response.data));
    })


  } 
  $scope.viewFloorPlan= function(){
    shareData.addData(level);
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
        $location.path("/viewBuilding");//not sure
        console.log('GET LEVELS FAILED! ' + JSON.stringify(response));
      });

    })
  }
  //$scope.showDetail="test";
   $scope.showDetails= function (thisUnit) {   
     console.log(thisUnit);
    // console.log(event);
    // console.log(event.target.classList);
     $scope.showDetail="unitNumber: "+thisUnit.unitNumber  +  " Unit Width: " + parseInt((thisUnit.square.height)*(level.length)/900) + "meter, Unit Length: " + parseInt((thisUnit.square.width)*(level.length)/900) +"meter";
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
	$scope.remove = function(unit,index) { 
		
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
		
		   
	}
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
})
