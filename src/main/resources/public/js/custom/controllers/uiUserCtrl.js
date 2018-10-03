angular.module("aceman").controller(
		"uiUserSaveCtrl",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						urlService) {
					$scope.saveNewUser = function() {
						console.log("Saving User.");
						urlService.saveUserData(data).then(
								function(resultJson) {
									console.log(resultJson);
								}
						)
					}
				} ]);

angular.module("aceman").controller(
		"uiUserListCtrl",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"$location",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,$location,
						urlService) {
					console.log("Fetching user list now.");
					
					if (typeof(Storage) !== "undefined") {
					   	$scope.type = sessionStorage.getItem('usertype');
					}
					
					urlService.fetchUIUserList().then(
							function(resultJson) {
								console.log(resultJson);
								$scope.uiUsers = resultJson;
								}
							);
					
					$scope.deleteuser = function (currentUser){
						
						var text;
						var z = confirm("Are you sure you want to delete User. It will be Deleted Permanently ");
						if (z == true) {
							console.log(text);
							var data = {
									'IsActive' : 0,
									'id':currentUser.id,
								};
							console.log(data);
							urlService.changeUserIsactive(data).then(
									function(resultJson) {
										console.log(resultJson);
										var index = $scope.uiUsers.map(x => {
											return x.id;}
										).indexOf(currentUser.id);

										$scope.uiUsers.splice(index, 1);
										console.log($scope.uiUsers);
								
								console.log("User deleted from Hawkeye");
							});
						    
						} 
						
						else {
						    text = "Cancel ";
						}
						
					
						
					}
					
					$scope.UserDetail = function (currentUser){
						console.log("in update User function");
						$rootScope.CurrUser = currentUser;
						
						if (typeof(Storage) !== "undefined") {
						   	$scope.type = sessionStorage.getItem('usertype');
						}
						if(	$scope.type == "ADMIN" ){
							console.log();
							
						$location.path("/updateUser");
						}
						
						
					}
				} ]);
angular.module("aceman").controller(
		"UpdateUserCtrl",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						urlService) {
					console.log("update user type");
					var curruser = $rootScope.CurrUser.name;
					console.log(curruser);
					
					if (typeof(Storage) !== "undefined") {
					   	$scope.type = sessionStorage.getItem('usertype');
					}
					$scope.UpdateAccessToUser = function(){
							var data = {
								'usertype' : $scope.usertype,
								'id':parseInt($rootScope.CurrUser.id),
							};
							urlService.UpdateUserType(data).then(
								function(resultJson) {
										console.log(resultJson);
										$scope.successMessage = "User Type changed to " + data.usertype;
										$scope.successMessagebool = true;
										console.log("User Type Changed");
										}
								
						);
					}
				} ]);

angular.module("aceman").controller(
		"uiUserChangeEmail",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						urlService) {
					$scope.updateEmail = function() {
						console.log("change Email.");
						urlService.changeEmail().then(
								function(resultJson) {
									console.log(resultJson);
									$scope.uiUsers = resultJson;}
						);
					}
				} ]);
angular.module("aceman").controller(
		"uiUserChangeUname",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						urlService) {
					console.log("change Uname.");
					urlService.fetchUIUserList().then(
							function(resultJson) {
									console.log(resultJson);
									$scope.uiUsers = resultJson;
									}
							);
				} ]);

angular.module("aceman").controller(
		"uiUserChangePassword",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						urlService) {
					console.log("change Password.");
					urlService.fetchUIUserList().then(
							function(resultJson) {
									console.log(resultJson);
									$scope.uiUsers = resultJson;
									}
							);
				} ]);
