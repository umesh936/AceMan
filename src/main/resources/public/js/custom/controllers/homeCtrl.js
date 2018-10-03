angular
		.module("aceman")
		.controller(
				"homeCtrl",
				[
						"$scope",
						"$q",
						"$timeout",
						"$log",
						"$rootScope",
						"$cacheFactory",
						"urlService",
						function($scope, $q, $timeout, $log, $rootScope,
								$cacheFactory, urlService) {
							console.log("---- in controler" + temp);
							if (typeof(Storage) !== "undefined") {
							   sessionStorage.setItem("usertype", temp);
							   sessionStorage.setItem("loggedinUserTeamid", teamid);
							}

							console.log(temp);
							$rootScope.userType = temp;
							$rootScope.userEmail = userEml;
							console.log($rootScope.userEmail);
							urlService
									.fetchAwsAccountList()
									.then(
											function(resultJson) {
												console.log(resultJson);
												$scope.awsAccountlist = resultJson;
												angular
														.forEach(
																$scope.awsAccountlist,
																function(awsAccount) {
																	if (awsAccount.isDefault == true) {
																		console.log("default found")
																		$rootScope.defaultAccountName = awsAccount.logicalName;
																	}
																});
											})
						} ]);
