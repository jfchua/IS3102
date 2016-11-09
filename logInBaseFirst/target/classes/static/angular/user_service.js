'use strict';
var REST_SERVICE_URI = '//localhost:8443/user';
app.factory('UserService', ['$http', '$q', function($http, $q){
 
    return {
         
    fetchAllUsers: function() {
            return $http.get('//localhost:8443/SpringMVC4RestAPI/user/')
            .then(
                    function(response){
                        return response.data;
                    }, 
                    function(errResponse){
                   	 console.log(errResponse);

                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
            );
        },
     verifyUser: function(userEmail){
    	 console.log("verifying user");
    	return $http({
    		    url: REST_SERVICE_URI + '/verifyEmail', 
    		    method: "GET",
    		    params: {userEmail: userEmail}
    		 }) .then(
                     function(response){
                    	 console.log(response);
                    	 console.log(response.data.result);
                      	return response.data.result;
                    
                      }, 
                      function(errResponse){
                      	console.log("Error, " + errResponse);
                          return false;
                      }
              );
        	 
    	
    	 
     },
     getSecurityQuestion: function(userEmail){
    	 return $http({
 		    url: REST_SERVICE_URI + '/getSecurityQuestion', 
		    method: "GET",
		    params: {userEmail: userEmail}
		 }) .then(
                 function(response){
                	 //console.log(response.data.question);
                  	return response.data.question;
                
                  }, 
                  function(errResponse){
                  	console.log("Error, " + errResponse);
                      return $q.reject(errResponse);
                  }
          );
    	 
    	 
     },
    resetPassword: function(userEmail, userSecurity){
    	console.log(userSecurity);
    	console.log("POSTING TO "  + REST_SERVICE_URI + "/reset  WITH EMAIL OF " + userEmail);
    	var data = JSON.stringify({email: userEmail, security: userSecurity});
		return $http.post(REST_SERVICE_URI + "/reset", data)
            .then(
                    function(response){
                    	return response;
                  
                    }, 
                    function(errResponse){
                    	console.log("Error, " + errResponse);
                        return $q.reject(errResponse);
                    }
            );
        },
        
        changePassword: function(changePasswordInfo){
  /*      	var req = {
        			 method: 'POST',
        			 url: '//localhost:8443/user/resetChangePassword',
        			 headers: {
        			   'Content-Type': 'application/json'
        			 },
        			 data: JSON.stringify(changePasswordInfo)
        			}*/
        	return $http.post('//localhost:8443/user/resetChangePassword',JSON.stringify(changePasswordInfo),
        			  {headers: { 'Content-Type': 'application/json; charset=utf-8' } })
    	//	return $http.post(REST_SERVICE_URI + "/resetChangePassword", JSON.stringify(changePasswordInfo))
        //	return $http.post(req)
                .then(
                        function(response){
                        console.log("Password reset successfully");
                        	return response;
                        	
                        }, 
                        function(errResponse){
                        	console.log("Error, " + errResponse);
                            return $q.reject(errResponse);
                        }
                );
            },
     
    updateUser: function(user, id){
            return $http.put('//localhost:8443/SpringMVC4RestAPI/user/'+id, user)
            .then(
                    function(response){
                        return response.data;
                    }, 
                    function(errResponse){
                        console.error('Error while updating user');
                        return $q.reject(errResponse);
                    }
            );
        },
     
   deleteUser: function(id){
            return $http.delete('//localhost:8443/SpringMVC4RestAPI/user/'+id)
            .then(
                    function(response){
                        return response.data;
                    }, 
                    function(errResponse){
                        console.error('Error while deleting user');
                        return $q.reject(errResponse);
                    }
            );
        }
         
    };
 
}]);


