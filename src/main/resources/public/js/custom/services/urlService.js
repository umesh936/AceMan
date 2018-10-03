angular
		.module("aceman")
		.service(
				"urlService",
				[
						"$http",
						"$log",
						"$q",
						"acConstant",
						function($http, $log, $q, acConstants) {
							var awsUrls = acConstants.AwsUrl;
							var teamUrls = acConstants.TeamUrl;
							var userUrl = acConstants.UsersUrl;
							var sshUserUrl = acConstants.SshUrl;
							var gerritUrl = acConstants.gerritUrl;
							var ldapUrl = acConstants.ldapUrl;
							var incidentUrl = acConstants.incidentUrl;
							var auditEventUrl = acConstants.auditLogUrl;

							window.String.format = function(input) {
								var args = arguments;
								return input.replace(/\{(\d+)\}/g, function(
										match, capture) {
									return args[1 * capture + 1];
								});
							};
							
							// /////////////////////////////////////////////////////////
							// //////////////// AUDIT EVENT REALTED SERVICE
							// ///////////////////
							// /////////////////////////////////////////////////////////
							
							function fetchAuditEventslist(page) {
								var request = $http({
									method : "get",
									url : String
											.format(auditEventUrl.fetchAuditLogList,page),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							
							// /////////////////////////////////////////////////////////
							// //////////////// INCIDENT REALTED SERVICE
							// ///////////////////
							// /////////////////////////////////////////////////////////
														
							function fetchIncidentList(page) {
								var request = $http({
									method : "get",
									url : String
											.format(incidentUrl.fetchIncidentList,page),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							function saveNewIncident(data) {
								var request = $http({
									method : "post",
									url : String.format(incidentUrl.saveNewIncident),
									data : JSON.stringify(data),

								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							// /////////////////////////////////////////////////////////
							// //////////////// LDAP REALTED SERVICE
							// ///////////////////
							// /////////////////////////////////////////////////////////

							function fetchLdapUserList() {
								var request = $http({
									method : "get",
									url : String
											.format(ldapUrl.fetchLdapUserList),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							// TODO
							function grantUserLdapAccess(data,
									accessUserSelected) {
								var request = $http({
									method : "post",
									url : String.format(
											ldapUrl.grantUserLdapAccess,
											accessUserSelected),
									data : JSON.stringify(data),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							// /////////////////////////////////////////////////////////
							// //////////////// GERRIT REALTED SERVICE
							// ///////////////////
							// /////////////////////////////////////////////////////////

							function fetchGerritUserList(useremail) {
								var request = $http({
									method : "get",
									url : String
											.format(gerritUrl.fetchGerritUserList,useremail),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							function makeGerritUserActive(id) {
								var request = $http({
									method : "put",
									url : String.format(gerritUrl.makeGerritUserActive,id),
									
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							function makeGerritUserDeactive(id) {
								var request = $http({
									method : "delete",
									url : String.format(gerritUrl.makeGerritUserDeactive,id),
									
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							// /////////////////////////////////////////////////////////
							// //////////////// AWS REALTED SERVICE
							// ///////////////////
							// /////////////////////////////////////////////////////////
							
							function fetchAwsAutoscaleList(){
								console.log("fetchAwsAutoscaleList url service");
								var request = $http({
									method : "get",
									url : String
											.format(awsUrls.fetchAwsAutoscalelist),
								});
								return (request
										.then(handleSuccess, handleError));
								
							}
							
							function fetchAwsinstanceAutoscalebyName(grpname){
								console.log("fetchAwsinstanceAutoscalebyName url service");
								var request = $http({
									method : "get",
									url : String
											.format(awsUrls.fetchAwsbyASGname,grpname),
								});
								return (request
										.then(handleSuccess, handleError));
								
							}
							
							
							function fetchAwsUserList() {
								var request = $http({
									method : "get",
									url : String
											.format(awsUrls.fetchAwsUserList),
								});
								return (request
										.then(handleSuccess, handleError));
							}

							function fetchAwsAccountList() {
								var request = $http({
									method : "get",
									url : String
											.format(awsUrls.fetchAwsAccounts),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							function fetchAwsAccountKeyPair() {
								var request = $http({
									method : "get",
									url : awsUrls.fetchAwsAccountKeyPair,
								});
								return (request
										.then(handleSuccess, handleError));
							}

							function fetchAwsVpcsList(errorFunction) {
								var request = $http({
									method : "get",
									url : String.format(awsUrls.fetchAwsVpcs),
								});
								if (errorFunction == undefined)
									return (request.then(handleSuccess,
											handleError));
								else
									return (request.then(handleSuccess,
											errorFunction));
							}
							function fetchAwsEc2List(errorFunction) {
								var request = $http({
									method : "get",
									url : String.format(awsUrls.fetchAwsEc2),
								});
								if (errorFunction == undefined)
									return (request.then(handleSuccess,
											handleError));
								else
									return (request.then(handleSuccess,
											errorFunction));
							}
							function fetchAwsELBList(errorFunction) {
								var request = $http({
									method : "get",
									url : String.format(awsUrls.fetchAwsELB),
								});
								if (errorFunction == undefined)
									return (request.then(handleSuccess,
											handleError));
								else
									return (request.then(handleSuccess,
											errorFunction));
							}

							function fetchAwsELBByName(name, region,errorFunction) {
								var request = $http({
									method : "get",
									url : String.format(
											awsUrls.fetchAwsELBByName, name,region),
								});
								if (errorFunction == undefined)
									return (request.then(handleSuccess,
											handleError));
								else
									return (request.then(handleSuccess,
											errorFunction));
							}

							function fetchAwsEc2ListForVPC(vpcId, region, errorFunction) {
								var request = $http({
									method : "get",
									url : String.format(
											awsUrls.fetchAwsEc2ForVpc, vpcId,region),
								});
								if (errorFunction == undefined)
									return (request.then(handleSuccess,
											handleError));
								else
									return (request.then(handleSuccess,
											errorFunction));
							}
							function saveNewAwsAccount(data) {
								var request = $http({
									method : "post",
									url : String
											.format(awsUrls.saveNewAwsAccount),
									data : JSON.stringify(data),

								});
								return (request
										.then(handleSuccess, handleError));
							}
							function makeAwsAccountActive(id) {
								var request = $http({
									method : "post",
									url : String.format(
											awsUrls.makeAwsAccountActive, id),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							
							function WhitelistVpc(id,name) {
								var request = $http({
									method : "post",
									url : String.format(
											awsUrls.whitelistVpc, id, name),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							
							function RemoveFromWhitelist(id) {
								var request = $http({
									method : "delete",
									url : String.format(awsUrls.removeWhitelistVpc, id),
									
								});
								return (request
										.then(handleSuccess, handleError));
							}

							// //////////////////////////////////////////////////////////
							// ///////////// UI TEAM REALTED SERVICES
							// //////////////////
							// //////////////////////////////////////////////////////////

							function fetchTeamNameList() {
								var request = $http({
									method : "get",
									url : String.format(teamUrls.fetchTeamList),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							//14- aug
							function fetchManagerList() {
								var request = $http({
									method : "get",
									url : String
									.format(userUrl.fetchUIManager),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							function addMemberToTeam(gid,uid,teamName){
									var request = $http({
									method : "put",
									url : String.format(userUrl.addGroupIdofUser,
											gid,uid,teamName),
								});
								return (request
										.then(handleSuccess, handleError));
							
							}
							
							function UserListWithoutTeam() {
								var request = $http({
									method : "get",
									url : String
									.format(userUrl.fetchUserWithNullTeamId),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							
							function deleteTeamMember(userId,teamName){
								
								console.log(userId);
								var request = $http({
									method : "put",
						url : String.format(userUrl.removeUser, userId,teamName),
								})
								
								return (request
										.then(handleSuccess, handleError));
				
							}
							
							function deleteTeam(teamid,teamName){
								
								console.log(teamid+teamName);
								var request = $http({
									method : "delete",
									url : String.format(teamUrls.deleteTeamurl,teamid,teamName),
								});
								
								return (request
										.then(handleSuccess, handleError));
				
							}
							
												
							//----
							function saveNewTeam(data) {
								var request = $http({
									method : "post",
									url : String.format(teamUrls.saveTeamData),
									data : JSON.stringify(data),

								});
								return (request
										.then(handleSuccess, handleError));
							}

							// /////////////////////////////////////////////////////////
							// ///////////// UI USER REALTED SERVICE
							// //////////////////
							// /////////////////////////////////////////////////////////
							function saveUserData(data) {
								var request = $http({
									method : "post",
									url : String.format(userUrl.saveUserData),
									data : JSON.stringify(data),

								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							function UpdateUserType(data){
								
								console.log(data);
								var request = $http({
									method : "put",
									url : String.format(userUrl.UpdateUser),
									data : JSON.stringify(data),
								});
								
								return (request
										.then(handleSuccess, handleError));
				
							}
							// activate or deactivate user
							function changeUserIsactive(data){
								
								console.log(data);
								var request = $http({
									method : "put",
									url : String.format(userUrl.UpdateUserIsActive),
									data : JSON.stringify(data),
								});
								
								return (request
										.then(handleSuccess, handleError));
				
							}
							

							function fetchUIUserList() {
								var request = $http({
									method : "get",
									url : String
											.format(userUrl.fetchUIUserList),
								});
								return (request
										.then(handleSuccess, handleError));
							}

							function fetchUserByTeam(teamid) {
								var request = $http({
									method : "get",
									url : String.format(
											teamUrls.fetchUserByTeam, teamid),
								});
								return (request
										.then(handleSuccess, handleError));
							}

							function changeEmail(newEmail) {
								var request = $http({
									method : "put",
									url : String.format(userUrl.changeEmail,
											newEmail),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							function changeUname(uname) {
								var request = $http({
									method : "put",
									url : String.format(userUrl.changeUname,
											uname),
								});
								return (request
										.then(handleSuccess, handleError));
							}

							function changePassword(data) {
								var request = $http({
									method : "put",
									url : String.format(userUrl.changeUname),
									data : JSON.stringify(data),
								});
								return (request
										.then(handleSuccess, handleError));
							}

							// /////////////////////////////////////////////////////////
							// ///////////// SSH USER REALTED SERVICE
							// //////////////////
							// /////////////////////////////////////////////////////////
							function saveSshUserData(data) {
								var request = $http({
									method : "post",
									url : sshUserUrl.sshUserDataUrl,
									data : JSON.stringify(data),

								});
								return (request
										.then(handleSuccess, handleError));
							}
							function grantUserSshAccess(data,
									accessUserSelected) {
								var request = $http({
									method : "post",
									url : String.format(
											sshUserUrl.grantUserSshAccess,
											accessUserSelected),
									data : JSON.stringify(data),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							
							function revokeUserSshAccess(userid,instanceId) {
								var request = $http({
									method : "delete",
									url : String.format(
											sshUserUrl.revokeUserSshAccess,
											userid,instanceId),
									});
								
								return (request
										.then(handleSuccess, handleError));
							}
							function fetchAllKeyForUser() {
								var request = $http({
									method : "get",
									url : sshUserUrl.sshUserDataUrl,
								});
								return (request
										.then(handleSuccess, handleError));
							}

							function fetchSSHUserList() {
								var request = $http({
									method : "get",
									url : String
											.format(sshUserUrl.fetchSshUserList),
								});
								return (request
										.then(handleSuccess, handleError));
							}

							function fetchUserDetailById(userId) {
								var request = $http({
									method : "get",
									url : String.format(
											sshUserUrl.fetchUserDetailByUid,
											userId),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							function fetchAccessDetailByUserId(userId) {
								var request = $http({
									method : "get",
									url : String.format(
											sshUserUrl.fetchAccessDetailByUid,
											userId),
								});
								return (request
										.then(handleSuccess, handleError));
							}
							function fetchAccessDetailByIntanceId(instanceId) {
								var request = $http({
									method : "get",
									url : String
											.format(
													sshUserUrl.fetchAccessDetailByInstance,
													instanceId),
								});
								return (request
										.then(handleSuccess, handleError));
							}

							// /////////////////////////////////////////////////////////
							// ///////////// ///////////////
							// /////////////////////
							// /////////////////////////////////////////////////////////

							function handleError(response) {
								if (!angular.isObject(response.data)
										|| !response.data.message) {
									return ($q
											.reject("An unknown error occurred."));
								}
								return ($q.reject(response.data.message));
							}

							function handleSuccess(response) {
								return (response.data);
							}

							return ({
								fetchAwsUserList : fetchAwsUserList,
								fetchTeamNameList : fetchTeamNameList,
								fetchAwsAccountList : fetchAwsAccountList,
								fetchAwsAccountKeyPair : fetchAwsAccountKeyPair,
								fetchAwsVpcsList : fetchAwsVpcsList,
								fetchAwsEc2List : fetchAwsEc2List,
								fetchAwsELBList : fetchAwsELBList,
								fetchAwsELBByName : fetchAwsELBByName,
								fetchUIUserList : fetchUIUserList,
								fetchUserByTeam : fetchUserByTeam,
								fetchAwsEc2ListForVPC : fetchAwsEc2ListForVPC,
								fetchAccessDetailByIntanceId : fetchAccessDetailByIntanceId,
								fetchUserDetailById : fetchUserDetailById,
								fetchSSHUserList : fetchSSHUserList,
								fetchAllKeyForUser : fetchAllKeyForUser,
								saveNewTeam : saveNewTeam,
								saveUserData : saveUserData,
								saveNewAwsAccount : saveNewAwsAccount,
								saveSshUserData : saveSshUserData,
								changeEmail : changeEmail,
								changeUname : changeUname,
								changePassword : changePassword,
								grantUserSshAccess : grantUserSshAccess,
								makeAwsAccountActive : makeAwsAccountActive,
								fetchGerritUserList : fetchGerritUserList,
								makeGerritUserActive : makeGerritUserActive,
								makeGerritUserDeactive : makeGerritUserDeactive,
								fetchLdapUserList : fetchLdapUserList,
								grantUserLdapAccess : grantUserLdapAccess,
								fetchIncidentList : fetchIncidentList,
								saveNewIncident :saveNewIncident,
								fetchManagerList : fetchManagerList,
								addMemberToTeam : addMemberToTeam,
								deleteTeamMember : deleteTeamMember,
								UpdateUserType : UpdateUserType,
								UserListWithoutTeam : UserListWithoutTeam,
								changeUserIsactive : changeUserIsactive,
								WhitelistVpc : WhitelistVpc,
								RemoveFromWhitelist: RemoveFromWhitelist,
								deleteTeam  : deleteTeam,
								fetchAuditEventslist: fetchAuditEventslist,
								fetchAwsAutoscaleList : fetchAwsAutoscaleList,
								fetchAwsinstanceAutoscalebyName : fetchAwsinstanceAutoscalebyName,
								fetchAccessDetailByUserId:  fetchAccessDetailByUserId,
								revokeUserSshAccess: revokeUserSshAccess,
								//getIncidentDetail : getIncidentDetail,
								
							});

						} ]);
