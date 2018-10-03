angular.module("aceman").controller(
		"sshUserSaveCtrl",
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
					urlService.fetchAllKeyForUser().then(function(resultJson) {
						console.log(resultJson);
						$scope.sshUsersKeyList = resultJson;
					})

					$scope.saveUserKey = function() {
						console.log("Saving User Key.");
						console.log($scope.ssh_key);
						var data = {
							'key' : $scope.ssh_key,
							'logicalName' : $scope.ssh_lg_name,
						};
						urlService.saveSshUserData(data).then(
								function(resultJson) {
									console.log(resultJson);
									
									$scope.successMessage = "SSH Key Addded successfully!";
								    $scope.successMessagebool = true;
								    location.reload();
//									$scope.ssh_key = "";
//									$scope.ssh_lg_name = "";
//									$('#myModal').modal('show')
								})
					}
				} ]);

angular.module("aceman").controller(
		"sshUserListCtrl",
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
				
					console.log("Fetching Ssh user list now.");
						urlService.fetchSSHUserList().then(
								function(resultJson) {
									console.log(resultJson);
									$scope.sshUsersList = resultJson;
								});
									
				} ]);

