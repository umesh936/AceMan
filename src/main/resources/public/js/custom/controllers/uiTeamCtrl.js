angular.module("aceman").controller(
		"uiTeamListCtrl",
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
					console.log("Get team  List.");
					
					$scope.utype  = '';
					if (typeof(Storage) !== "undefined") {	
						$scope.utype  = sessionStorage.getItem('usertype');
						$scope.loggedinUserTeamid = sessionStorage.getItem('loggedinUserTeamid');
						console.log($scope.utype);
					}
					
					$scope.isTeamloading=true;
					urlService.fetchTeamNameList().then(
							function(resultJson) {
								console.log(resultJson);
								$scope.teams = resultJson;
								$scope.isTeamloading=false;
								}
					);
					
					$scope.selectItem = function(itemId,teamName) {
						console.log("in select Item");
						
						if (typeof(Storage) !== "undefined") {
							   sessionStorage.setItem("teamId", itemId); 
							   sessionStorage.setItem("teamName", teamName); 
							   
							}
						
						
						$rootScope.teamId = itemId;
						
						$location.path("/teamDetail");
					};
					
					
					$scope.deleteTeam = function(teamid,teamName) {
						
						console.log(teamName);
						var text;
						var z = confirm("Are you sure you want to delete Team. It will be Deleted Permanently ");
						if (z == true) {
							console.log(text);
							
							
							urlService.deleteTeam(teamid,teamName).then(
									function(resultJson) {
										console.log(resultJson);
										var index = $scope.teams.map(x => {
											return x.id;}
										).indexOf(teamid);

										$scope.teams.splice(index, 1);
										console.log($scope.teams);
										console.log("team deleted from Hawkeye");
																			
							});
						} 
						
						else {
						    text = "Cancel ";
						}
						
					}
				
				} ]);

angular.module("aceman").controller(
		"uiTeamDetailsCtrl",
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
					console.log("Get User  List for team.");
					console.log($rootScope.teamId);
					$scope.teamId = null;
					if (typeof(Storage) !== "undefined") {
						$scope.teamId = sessionStorage.getItem('teamId'); 
						$scope.teamName = sessionStorage.getItem('teamName'); 
						$scope.loggedinUserTeamid = sessionStorage.getItem('loggedinUserTeamid');
						$scope.loggedinUserUserType = sessionStorage.getItem('usertype');
							
						}
					urlService.fetchUserByTeam($scope.teamId).then(function(resultJson) {
						console.log(resultJson);
						$scope.teamUser = resultJson;
					});
					
					
					$scope.showDropDown = false;
					$scope.showUsersInTeam = true;
					
					urlService.UserListWithoutTeam().then(function(resultJson){
						console.log("user data whose team id is null  detail page");
						console.log(resultJson);
						$scope.usersList = resultJson ||[];
					});
					
					$scope.addTeamMembers = function() {
						$scope.showDropDown = true;					
					};
										
					
					
					$scope.addMember = function() {
						console.log("in adding member to team");
						var data = {
							'teamName' : $scope.teamName,
							'userUid' : parseInt($scope.userUid),
							'teamid' :$scope.teamId,
						};
						console.log("---- data taken to add user in teams");
						console.log(data);
						
										
						urlService.addMemberToTeam(data.teamid, data.userUid,$scope.teamName).then(function(resultJson) {
							
							$scope.successMessagebool1 = true;
							console.log(data.teamName);
														
							var i = $scope.usersList.map(x => {
								  return x.id;
								}).indexOf(data.userUid);
							console.log(i);
							console.log($scope.usersList[i]);
							$scope.successMessage1 = "User "+ $scope.usersList[i].name + " has Added to " + $scope.teamName  ;
							$scope.teamUser = $scope.teamUser ? $scope.teamUser : [];
							$scope.teamUser.push($scope.usersList[i]);
							$scope.usersList.splice(i, 1); // deleted added user from drop down
							console.log("after addition in userlist", $scope.teamUser)
							
							console.log("member added to team succesfully");
							
							
						});
					};
					
					
					$scope.deletefromTeam = function(userId,teamName) {
						console.log("in deleting member from team");	
						console.log(teamName);
						
						console.log($scope.usersList);
						urlService.deleteTeamMember(userId,teamName).then(function(resultJson) {
							console.log("member deleted from team succesfully");
							var index = $scope.teamUser.map(x => {
								  return x.id;
								}).indexOf(userId);
								
							$scope.usersList.push($scope.teamUser[index]);
								$scope.teamUser.splice(index, 1);
								
								console.log($scope.teamUser);
								
						});
					};
	
				} ]);
angular.module("aceman").controller(
		"uiTeamSaveCtrl",
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

					console.log("fetching managers list");
					urlService.fetchManagerList().then(function(resultJson) {
						console.log("fetching managers list");
						console.log(resultJson);
						$scope.managersList = resultJson;
						
					});
					
					
					$scope.saveNewTeam = function() {
						console.log("in saving Team function.");
						var data = {
							'teamName' : $scope.teamName,
							'managerUid' : $scope.managerUid,
							'createdBy' : $rootScope.userEmail,
							'description' : $scope.teamDesc,

						};
						console.log("---- data taken from register team forms");
						console.log(data);
						
						$scope.successMessagebool = false;		
						urlService.saveNewTeam(data).then(function(resultJson) {
							
							$scope.newcreatedTeamid = resultJson;
							
							urlService.addMemberToTeam($scope.newcreatedTeamid , parseInt(data.managerUid)).then(function(resultJson) {
								console.log("manager is also added to newly added team");						
							});
							$scope.successMessage ="New Team "+ data.teamName + " Added !";
							$scope.successMessagebool = true;
							
					
							$scope.teamName = null;
							$scope.managerUid = null;
							$scope.teamCreatedBy = null;
							$scope.teamDesc = null;
						});
						
						
						
						
						
						
					};

				} ]);
