//sandeep
angular.module("aceman").controller(
		"listAuditEventsCtrl",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,urlService) {
					console.log("in this  incident list function.");
					
					console.log($scope.incident);
					$scope.page =0;
					urlService.fetchAuditEventslist(0)
					.then(function(resultJson) {
						console.log(resultJson);
						$scope.auditEventlist = resultJson;
						$scope.size = $scope.auditEventlist.length;
						console.log($scope.size);
					});
					
					
					$scope.fetchnext = function(page) {
						$scope.page  = $scope.page+1;
						
						urlService.fetchAuditEventslist($scope.page)
						.then(function(resultJson) {
							console.log(resultJson);
							$scope.auditEventlist = resultJson;
							$scope.size = $scope.auditEventlist.length;
						});
						
					};
					$scope.fetchprev = function(page) {
						$scope.page  = $scope.page-1;
						
						urlService.fetchAuditEventslist($scope.page)
						.then(function(resultJson) {
							console.log(resultJson);
							$scope.auditEventlist = resultJson;
							$scope.size = $scope.auditEventlist.length;
						});
						
					};
					
					
						
				}
		]);