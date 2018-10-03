
var app = angular.module('aceman');
/** testing**/
app.config(['$routeProvider', function ($routeProvider) {
  $routeProvider

  .when('/', {
    templateUrl : 'templates/home.html',
    controller: 'homeCtrl'
  })

  .when('/awsUserList', {
    templateUrl : '/templates/awsUserList.html',
    controller  : 'awsRequestCtrl'
  })
   .when('/awsAccountList', {
    templateUrl : '/templates/awsAccountList.html',
    controller  : 'awsAccountRequestCtrl'
  })
  .when('/awsDashboard', {
    templateUrl : '/templates/awsDashboard.html',
    controller  : ''
  })
   .when('/awsVpcList', {
    templateUrl : '/templates/awsVpcList.html',
    controller  : 'awsVpcsCtrl'
  })
  .when('/awsInstanceList', {
    templateUrl : '/templates/awsInstanceList.html',
    controller  : 'awsInstanceCtrl'
  })
   .when('/awsELBList', {
    templateUrl : '/templates/awsELBList.html',
    controller  : 'awsElbListCtrl'
  })
  .when('/awsConfigure', {
    templateUrl : '/templates/awsAccountConfigured.html',
    controller  : 'awsAccountConfiguredCtrl'
  })
  
  .when('/awsCreateUser', {
    templateUrl : '/templates/createawsUser.html',
    controller  : 'awsRequestCtrl'
  })

  .when('/registerUser', {
    templateUrl : '/templates/registerUIUser.html',
    controller  : 'uiUserSaveCtrl'
  })
  .when('/listUIUser', {
    templateUrl : '/templates/listUIUsers.html',
    controller  : 'uiUserListCtrl'
  })
  .when('/updateUser', {
    templateUrl : '/templates/updateUser.html',
    controller  : 'UpdateUserCtrl'
  })
   .when('/listSshUser', {
    templateUrl : '/templates/listSSHUsers.html',
    controller  : 'sshUserListCtrl'
  })
   .when('/registerTeam', {
    templateUrl : '/templates/registerTeam.html',
    controller  : 'uiTeamSaveCtrl'
  })
  .when('/listTeam', {
    templateUrl : '/templates/listTeam.html',
    controller  : 'uiTeamListCtrl'
  })
  .when('/teamDetail', {
    templateUrl : '/templates/teamDetailPage.html',
    controller  : 'uiTeamDetailsCtrl'
  })
  .when('/addSshKey', {
    templateUrl : '/templates/addSshKey.html',
    controller  : 'sshUserSaveCtrl'
  })
  .when('/grantAccess', {
    templateUrl : '/templates/grantSshAccess.html',
    controller  : 'grantAccessCtrl'
  })
  
  .when('/changeEmail', {
    templateUrl : '/templates/changeEmail.html',
    controller  : 'uiUserChangeEmail'
  })
    .when('/changePassword', {
    templateUrl : '/templates/changePassword.html',
    controller  : ''
  })
    .when('/changeUserName', {
    templateUrl : '/templates/changeUserName.html',
    controller  : ''
  })
  .when('/listGerritUserDetail', {
    templateUrl : '/templates/listGerritUserDetails.html',
    controller  : 'listgerritRequestCtrl'
  })
  .when('/getGerritUserName', {
    templateUrl : '/templates/getGerritUserDetails.html',
    controller  : 'getgerritRequestCtrl'
  })

  .when('/listLdapUserDetails', {
    templateUrl : '/templates/listLdapUserDetails.html',
    controller  : 'listLdapRequestCtrl'
  })
  .when('/addLdapUser', {
    templateUrl : '/templates/addLdapUser.html',
    controller  : 'addLdapRequestCtrl'
  })
  .when('/listAllIncidents', {
    templateUrl : '/templates/listAllIncidents.html',
    controller  : 'listIncidentRequestCtrl'
  })
  .when('/getIncidentDetail', {
    templateUrl : '/templates/incidentDetail.html',
    controller  : 'incidentsRequestCtrl'
  })
  .when('/addIncidentManual', {
    templateUrl : '/templates/addIncidentForm.html',
    controller  : 'addIncidentCtrl'
  })
  .when('/listAuditEvents', {
    templateUrl : '/templates/listAuditEvents.html',
    controller  : 'listAuditEventsCtrl'
  })
  .when('/listAutoscaleGroup', {
    templateUrl : '/templates/listAutoScaleGroup.html',
    controller  : 'awsListAutoscaleCtrl'
  })
  .when('/userAccessDetail', {
    templateUrl : '/templates/userAccessDetail.html',
    controller  : 'sshUserAccessDetailCtrl'
  })
  
  .otherwise({redirectTo: '/'});
}]);

