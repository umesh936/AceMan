angular.module("aceman").controller(
		"awsRequestCtrl",
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
					$scope.isUserloading = true;
					urlService.fetchAwsUserList().then(function(resultJson) {
						console.log(resultJson);
						$scope.awsuserlist = resultJson;
						$scope.isUserloading = false;
					})

				} ]);

angular.module("aceman")
	.controller(
		"awsAccountRequestCtrl",
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
							console.log("in this function.");
							urlService.fetchAwsAccountList()
									.then(
											function(resultJson) {
												console.log(resultJson);
												$scope.awsAccountlist = resultJson;
												angular
														.forEach($scope.awsAccountlist,
																function(awsAccount) {
																	if (awsAccount.isDefault == true) {
																		if (typeof(Storage) !== "undefined") {
																			   sessionStorage.setItem("defaultAccName", awsAccount.logicalName);
																			   
																				$scope.defaultAccountName = sessionStorage.getItem('defaultAccName');
																				
																			}
																		
																	}
																});
											})

							$scope.makeAWSActive = function(id) {
								console.log(" AWS account Id : ");
								console.log(id);
								urlService.makeAwsAccountActive(id).then(
												function(resultJson) {
													console.log(resultJson);
													angular
															.forEach(
																	$scope.awsAccountlist,
																	function(
																			awsAccount) {
																		if (awsAccount.id == id) {
																			awsAccount.isDefault = true;
																			$rootScope.defaultAccountName = awsAccount.logicalName;
																		} else
																			awsAccount.isDefault = false;
																	});
												})
							}
						} ]);

angular
		.module("aceman")
		.controller(
				"awsAccountConfiguredCtrl",
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
							console
									.log("in Aws Account Configured  Controller.");
							$scope.saveNewAccount = function() {
								console.log("in saving AWS configuration.");
								var data = {
									'name' : $scope.awsname,
									'accessKey' : $scope.accessKey,
									'secretKey' : $scope.secretKey,
								};
								urlService
										.saveNewAwsAccount(data)
										.then(
												function(resultJson) {

													$scope.successMessagebool3 = true;
													$scope.successMessage = "New Aws Account Created - "
															+ data.name;
													$scope.awsname = null;
													$scope.accessKey = null;
													$scope.secretKey = null;
												});
							};
						} ]);

angular.module("aceman").controller(
		"awsVpcsCtrl",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"$location",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						$location, urlService) {
					$scope.isVpcListloading = true;
					console.log("in Vpc function.");
					urlService.fetchAwsVpcsList().then(function(resultJson) {
						console.log(resultJson);
						$scope.shownullmessage = false; 
						$scope.awsAwsVpclist = resultJson;
						
					if(resultJson.length == 0){
							$scope.shownullmessage = true;
						}
						$scope.isVpcListloading = false;
					})
					$scope.utype  = '';
					if (typeof(Storage) !== "undefined") {
						console.log("session storage value");
						
						$scope.utype  = sessionStorage.getItem('usertype');
						console.log($scope.utype);
					}
					
					
					
					$scope.selectVpc = function(itemId) {
						console.log("in select Vpc");
						console.log(itemId);
						$rootScope.selectVpc = itemId;
						$location.path("/awsInstanceList");
					};
					
					 
					
					$scope.makeWhiteList = function(itemId,itemName) {
						console.log("in make whitelist Vpc");
						console.log(itemId);
																					
						urlService.WhitelistVpc(itemId,itemName).then(function(resultJson) {
							console.log("whitelistd vpc successfully");
							var index = $scope.awsAwsVpclist.map(x => {
								  return x.vpcId;
								}).indexOf(itemId);

								$scope.awsAwsVpclist[index].isWhitelist = true;
								 console.log("------"+$scope.awsAwsVpclist[index].vpcId);
								 console.log("------"+$scope.awsAwsVpclist[index].isWhitelist );
						})
						
					};
					
					$scope.removeFromWhiteList = function(itemId) {
						console.log("in remove form whiteList Vpc");
						console.log(itemId);
						
						urlService.RemoveFromWhitelist(itemId).then(function(resultJson) {
							console.log("remove vpc from whitelist successfully");
							
							for (var i in $scope.awsAwsVpclist) {
							     if ($scope.awsAwsVpclist[i].vpcId == itemId) {
							    	 $scope.awsAwsVpclist[i].isWhitelist = false;
							    	 console.log("------"+$scope.awsAwsVpclist[i]);
							        break; // Stop this loop, we found it!
							     }
							   }
						})
						
					};
					
				} ]);

angular
		.module("aceman")
		.controller(
				"awsInstanceCtrl",
				[
						"$scope",
						"$q",
						"$timeout",
						"$log",
						"$rootScope",
						"$cacheFactory",
						"$location",
						"urlService",
						function($scope, $q, $timeout, $log, $rootScope,
								$cacheFactory, $location, urlService) {
							$scope.userType = '';
							if (typeof(Storage) !== "undefined") {
							    // Store
								$scope.uType = sessionStorage.getItem('usertype');
								console.log($scope.uType);
							}
							
							$scope.isEc2Listloading = true;
							console.log("Selected Vpc ");
							console.log($scope.selectVpc);
							
							
							if($scope.selectVpc != undefined)
							{
								// getData for VPC
								urlService
										.fetchAwsEc2ListForVPC(
												$scope.selectVpc.vpcId,$scope.selectVpc.region,
												function(data) {

													// Error Handling
													$scope.isEc2Listloading = false;
													$rootScope.selectVpc = undefined;
												})
										.then(
												function(resultJson) {
													console.log(resultJson);
													$scope.awsEc2list = {};
													$scope.awsEc2list.instance = resultJson;
													console
															.log($scope.awsEc2list.instance);
													$scope.isEc2Listloading = false;
													$rootScope.selectVpc = undefined;
												});
								
								
							}
							else if($scope.selectedElbInstance != undefined){
								

								urlService
										.fetchAwsELBByName(
												$scope.selectedElbInstance.name,$scope.selectedElbInstance.region,
												function(data) {
													$scope.isEc2Listloading = false;
													$rootScope.selectedElbInstance = undefined;
												})
										.then(
												function(resultJson) {
													console.log(resultJson);
													$scope.awsEc2list = {};
													$scope.awsEc2list.instance = resultJson;
													$scope.isEc2Listloading = false;
													$rootScope.selectedElbInstance = undefined;
												});
								
							}
							else if($scope.selectedASGroup != undefined){
								
								urlService
								.fetchAwsinstanceAutoscalebyName(
										$scope.selectedASGroup,
										function(data) {
											$scope.isEc2Listloading = false;
											$rootScope.selectedASGroup = undefined;
										})
								.then(
										function(resultJson) {
											console.log(resultJson);
											$scope.awsEc2list = {};
											$scope.awsEc2list.instance = resultJson;
											$scope.isEc2Listloading = false;
											$rootScope.selectedASGroup = undefined;
										});
								
								
							}
							else{
								
								$scope.shownullmessage = false;
								urlService.fetchAwsEc2List(function(data) {
									$scope.isEc2Listloading = false;
								}).then(function(resultJson) {
									console.log(resultJson);
									$scope.awsEc2list = resultJson;
									$scope.isEc2Listloading = false;
									if(resultJson.instance.length == 0){
										$scope.shownullmessage = true;
									}
								});
								
								
							}
		
											
							$scope.selectEc2 = function(ec2) {
								console.log("in select Ec2 Function");
								console.log($rootScope.userType);
								var val = $scope.uType
								console.log(ec2.instanceId)
								console.log(ec2.publicIpAddress)
								if(!ec2.publicIpAddress)
									$rootScope.selectInstanceIp = ec2.privateIpAddress;
								else 
								$rootScope.selectInstanceIp = ec2.publicIpAddress;
								
								$rootScope.selectInstance = ec2.instanceId;
								$rootScope.Iname = ec2.name;
								$rootScope.vpcId = ec2.vpcId;
								$rootScope.keyName = ec2.keyName;
								$rootScope.region = ec2.region;
								if (val == "ADMIN" || val == "MANAGER") {
									$location.path("/grantAccess");
								}
								
							};

						} ]);
angular.module("aceman").controller(
		"grantAccessCtrl",
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
					// console.log("Fetching Ssh user list now.");
					$scope.showAddButton = true;
					$scope.showListofAccessableUsers = true;
					$scope.showAccessForm = false;
					$scope.Dayslist = [ "1", "3", "7", "30", "Unlimited" ];
					$scope.showform = function() {
						$scope.showAddButton = false;
						$scope.showAccessForm = true;
						$scope.showListofAccessableUsers = false;
					}
					 $scope.shownullmessage = false;
					console.log("all accessable users list ");
					console.log($rootScope.selectInstance);
					urlService.fetchAccessDetailByIntanceId(
							$rootScope.selectInstance).then(
							function(resultJson) {
								 if(resultJson.length == 0){
									 $scope.shownullmessage = true;
								 }
								console.log(resultJson);
								$scope.sshUsersList = resultJson;
							});

					urlService.fetchUIUserList().then(function(resultJson) {
						console.log("Fetching Ssh user list now.");
						console.log(resultJson);
						$scope.newUsersList = resultJson;
					});

					$scope.grantAccessToUser = function() {
						console.log("in grant Access function.");
						console.log($scope.selectInstanceIp);

						var data = {
							'instanceId' : $scope.selectInstance,
							'instanceIP' : $scope.selectInstanceIp,
							'groupName' : $scope.grpname,
							'noOfDays' : $scope.noOfDays,
							'vpcId' : $scope.vpcId,
							'region' : $scope.region,

						};
						console.log(data);
						urlService.grantUserSshAccess(data,
								$scope.accessUserSelected).then(
								function(resultJson) {
									console.log();
									$scope.grpname = null;
									$scope.noOfDays = null;
									$scope.accessUserSelected = null;
									
								});
					};

					
					$scope.revokeAccess = function(userid) {
						console.log("in revoke Access function.");
//						var data = {
//								'instanceId' : $scope.selectInstance,
//									};
//						console.log(data);
						console.log(userid);
				
						urlService.revokeUserSshAccess(userid,$scope.selectInstance)
							.then(
								function(resultJson) {
									console.log();
									
									
								});
							};
				} ]);
angular.module("aceman").controller(
		"awsElbListCtrl",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"$location",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						$location, urlService) {
					$scope.isELBListloading = true;
					urlService.fetchAwsELBList(function(data) {
						// Error Handling
						$scope.isEc2Listloading = false;
					}).then(function(resultJson) {
						$scope.isEc2Listloading = false;
						console.log(resultJson);
						$scope.awsELBlist = resultJson;
						$scope.isELBListloading = false;
					}, function(data) {
						// Error Handling
						$scope.isELBListloading = false;
					});

					$scope.selectElb = function(itemId) {
						console.log("in select ELB Function");
						$rootScope.selectedElbInstance = itemId;
						$location.path("/awsInstanceList");
					};

				} ]);


angular.module("aceman").controller(
		"awsListAutoscaleCtrl",
		[
				"$scope",
				"$q",
				"$timeout",
				"$log",
				"$rootScope",
				"$cacheFactory",
				"$location",
				"urlService",
				function($scope, $q, $timeout, $log, $rootScope, $cacheFactory,
						$location, urlService) {
					
					console.log("in autoscale ctrl");
					$scope.isAutoScaleloading = true;
					urlService.fetchAwsAutoscaleList().then(function(resultJson) {
						console.log(resultJson);
						$scope.autoscaleGroupList = resultJson;
						$scope.isAutoScaleloading = false;
					})

					$scope.selectAutoscale = function(grpName) {
						console.log("in select autoscale Function");
						console.log(grpName);
						$rootScope.selectedASGroup = grpName;
						$location.path("/awsInstanceList");
					};

				} ]);