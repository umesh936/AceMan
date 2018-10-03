//sandeep

angular.module("aceman").controller(
		"listLdapRequestCtrl",
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
					console.log("in thfetchLdapUserListis  ldap Fetch function.");
					urlService.fetchLdapUserList().then(function(resultJson) {
						console.log(resultJson);
						$scope.ldapUsers = resultJson;
					});
						
				} ]);


