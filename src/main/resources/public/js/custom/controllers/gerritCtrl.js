//sandeep

angular.module("aceman").controller(
		"getgerritRequestCtrl",
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
					console.log("in this  gerrit Fetch function.");

					$scope.getGerritUserbyEmail = function() {
						$rootScope.gerritUserEmail = $scope.gerritUserEmail;
						$location.path("/listGerritUserDetail");
					}
				} ]);
angular.module("aceman").controller(
		"listgerritRequestCtrl",
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
					console.log("in this  gerrit Fetch function.");
					console.log($scope.gerritUserEmail);
					$scope.isgerritListloading=true;
					urlService.fetchGerritUserList($scope.gerritUserEmail)
							.then(function(resultJson) {
								console.log(resultJson);
								$scope.gerritUsers = resultJson;
								$scope.isgerritListloading=false;

							});
				
				
				$scope.makeGerritUserActive = function(user){
					urlService.makeGerritUserActive(user.id).then(
							function(resultJson) {
								console.log(resultJson);
								user.isActive = true;

							})
				}
				
				$scope.makeGerritUserDeactive = function(user){
					urlService.makeGerritUserDeactive(user.id).then(
							function(resultJson) {
								console.log(resultJson);
								user.isActive = false;

							})
				}
			}

		]);

