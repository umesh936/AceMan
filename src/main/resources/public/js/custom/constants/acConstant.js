angular.module("aceman").constant("acConstant", {
	AwsUrl : {
		fetchAwsUserList : '/aws/desc/users',
		fetchAwsAccounts : '/aws/desc/accounts',
   fetchAwsAccountKeyPair: '/aws/desc/accounts/keypair',
			fetchAwsVpcs : '/aws/desc/vpc',
	   fetchAwsEc2ForVpc : '/aws/desc/vpc/{0}/{1}', 
			 fetchAwsEc2 : '/aws/desc/ec2',
			 fetchAwsELB : '/aws/desc/elb',
	   fetchAwsELBByName : '/aws/desc/elb/{0}/{1}',
       saveNewAwsAccount : '/aws/accounts',
    makeAwsAccountActive : '/aws/accounts/default/{0}',
    	 whitelistVpc	 : '/aws/whitelistVpc/{0}/{1}',
    removeWhitelistVpc	 : '/aws/whitelistVpc/{0}',
  fetchAwsAutoscalelist	 : '/aws/desc/autoscale',
  fetchAwsbyASGname : '/aws/desc/autoscale/{0}',
    
	},
	TeamUrl : {
		fetchTeamList : '/teams/desc',
		 saveTeamData : '/teams',
		 fetchUserByTeam: '/teams/{0}/users',
		 deleteTeamurl:'/teams/delete/{0}/{1}'
		 
	},
	UsersUrl : {
		
	 fetchUIUserList : '/ui-users/desc',
		saveUserData : '/ui-users/',
		 changeEmail : '/ui-users/users/email/{0}',
		 changeUname : '/ui-users/users/uname/{0}',
	  changePassword : '/ui-users/users/pwd',
	  fetchUIManager : '/ui-users/managers',
	addGroupIdofUser : '/ui-users/users/update/{0}/{1}/{2}',
	  	  removeUser : '/ui-users/users/delteam/{0}/{1}',
	  	  UpdateUser : '/ui-users/update/usertype',
  UpdateUserIsActive : '/ui-users/update/isactive',
  fetchUserWithNullTeamId :'/ui-users/teamid/notassociated',
	},
	SshUrl : {
		fetchSshUserList : '/ssh/access/activeusers',
		sshUserDataUrl : '/ssh/access/users/keys',
		fetchUserDetailByUid : '/ssh/users/{0}',
		fetchAccessDetailByUid : '/ssh/access/users/{0}',
		fetchAccessDetailByInstance : '/ssh/access/instance/{0}',
		grantUserSshAccess : '/ssh/access/users/{0}',
		revokeUserSshAccess: '/ssh/access/users/{0}/{1}'
		 
	},
	
	logMessages : {
		requestFetchError : "Error in fetching Request {0}.",
		cacheHit : "Found Element - {0} from Cache.",
	},
	gerritUrl :{
		fetchGerritUserList : '/gerrit/accounts?name={0}',
		makeGerritUserActive: '/gerrit/accounts/{0}',
		makeGerritUserDeactive : '/gerrit/accounts/{0}',	
	},
	
	ldapUrl :{
		fetchLdapUserList : '/ldap/accounts',
		grantUserLdapAccess: '/ldap/accounts/{0}',	
	},
	
	incidentUrl :{
		fetchIncidentList: '/incident/{0}',
		saveNewIncident : '/incident',
	},	
	
	auditLogUrl:{
		fetchAuditLogList: '/auditEvent/desc/{0}',
	},
	
});
