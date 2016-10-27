/*'use strict';

angular.module('app').factory('UserService', ['$http', '$q', function($http, $q){

	var REST_SERVICE_URI = '//localhost:8443/user';

	var factory = {
			getUserById:getUserById,
			getUserByEmail:getUserByEmail,
			getAllUsers:getAllUsers,
			create:create,
			getPasswordResetToken:getPasswordResetToken,
			createPasswordResetTokenForUser:createPasswordResetTokenForUser,
			changePassword:changePassword,
			deleteUser:deleteUser,
			resetPassword:resetPassword
	};

	
	
	var factory = {

			resetPassword:resetPassword
	};
	
	return factory;
	
	function resetPassword(userEmail) {
		var deferred = $q.defer();
		console.log("POSTING TO "  + REST_SERVICE_URI + "/reset  WITH EMAIL OF " + userEmail.value);
		$http.post(REST_SERVICE_URI + "/reset", userEmail.value)
		.then(
				function (response) {
					console.log(response.data);
					console.error('Done resetting password');
					return reponse.data;
					deferred.resolve(response.data);
					
				},
				function(errResponse){
					displayError(errResponse);
					console.error('Error while resetting password');
					deferred.reject(errResponse);
				}
		);
		return deferred.promise;
	}

	function fetchAllUsers() {
		var deferred = $q.defer();
		$http.get(REST_SERVICE_URI)
		.then(
				function (response) {
					deferred.resolve(response.data);
				},
				function(errResponse){
					console.error('Error while fetching Users');
					deferred.reject(errResponse);
				}
		);
		return deferred.promise;
	}

	function createUser(user) {
		var deferred = $q.defer();
		$http.post(REST_SERVICE_URI, user)
		.then(
				function (response) {
					deferred.resolve(response.data);
				},
				function(errResponse){
					console.error('Error while creating User');
					deferred.reject(errResponse);
				}
		);
		return deferred.promise;
	}
	
	
	


	function updateUser(user, id) {
		var deferred = $q.defer();
		$http.put(REST_SERVICE_URI+id, user)
		.then(
				function (response) {
					deferred.resolve(response.data);
				},
				function(errResponse){
					console.error('Error while updating User');
					deferred.reject(errResponse);
				}
		);
		return deferred.promise;
	}

	function deleteUser(id) {
		var deferred = $q.defer();
		$http.delete(REST_SERVICE_URI+id)
		.then(
				function (response) {
					deferred.resolve(response.data);
				},
				function(errResponse){
					console.error('Error while deleting User');
					deferred.reject(errResponse);
				}
		);
		return deferred.promise;
	}

}]);*/




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
                        console.error('Error while fetching users');
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


