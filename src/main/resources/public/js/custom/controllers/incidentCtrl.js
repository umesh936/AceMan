//sandeep

angular.module("aceman").controller(
		"listIncidentRequestCtrl",
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
					urlService.fetchIncidentList(0)
					.then(function(resultJson) {
						console.log(resultJson);
						$scope.incident = resultJson;

					});
					
					
					$scope.fetchnext = function(page) {
						$scope.page  = $scope.page+1;
						
						urlService.fetchIncidentList($scope.page)
						.then(function(resultJson) {
							console.log(resultJson);
							$scope.incident = resultJson;

						});
						
					};
					$scope.fetchprev = function(page) {
						$scope.page  = $scope.page-1;
						
						urlService.fetchIncidentList($scope.page)
						.then(function(resultJson) {
							console.log(resultJson);
							$scope.incident = resultJson;

						});
						
					};
					
					
					$scope.submitReason = function(index) {
						var temp = $scope.incident[index];
						var data = {
								'incidentId' : temp.incidentId,
								'incidentName' : temp.incidentName,
								'incidentCategory' : temp.incidentCategory,
								'serviceAttached' : temp.serviceAttached,
								'reportedBy' :temp.reportedBy,
								'reason' : temp.reason,
								'rectification': temp.rectification,
								'isprocessed':true,
								'rectifiedPerson':temp.rectifiedPerson,
								'incidentTime' : temp.incidentTime,
								'incidentDowntime' : temp.incidentDowntime,
								'reportedTime' : temp.reportedTime,
						};
							urlService.saveNewIncident(data).then(function(resultJson) {
							
							$scope.successMessage = "incident submitted successfully!";
						    $scope.successMessagebool = true;
						});
						
						console.log(data);
					};	
				}
		]);
angular.module("aceman").controller(
		"addIncidentCtrl",
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

					$scope.saveNewIncident = function() {
						console.log("add incident function.");
						var data = {
							'incidentId' : $scope.incidentId,
							'incidentName' : $scope.incidentName,
							'incidentCategory' : $scope.incidentCategory,
							'serviceAttached' : $scope.serviceAttached,
							'reportedBy' : $scope.reportedBy,
							'incidentTime' : $scope.incidentTime,
							'incidentDowntime' : $scope.incidentDowntime,
							'reportedTime' : $scope.reportedTime,
							'isprocessed':false,
						};
						urlService.saveNewIncident(data).then(function(resultJson) {
							
							$scope.successMessage = "incident submitted successfully!";
						    $scope.successMessagebool = true;
							/*
						    $scope.incidentId = null;
							$scope.incidentName = null;
							$scope.incidentCategory = null;
							$scope.serviceAttached = null;
							$scope.reportedBy = null;
							$scope.incidentTime = null;
							$scope.incidentDowntime = null;
							$scope.reportedTime = null;*/
							
							location.reload();
							
						});
					};

				} 
			]);
