<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- Left Sidebar -->
<aside id="leftsidebar" class="sidebar">
	<!-- User Info -->
	<div class="user-info">
		<div class="image">
			<img src="/images/loggeduser.png" width="48" height="48" alt="User" />
		</div>
		<div class="info-container">
			<div class="name" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false">${username}</div>
			<div class="email">${email}</div>
			<div class="btn-group user-helper-dropdown">
				<i class="material-icons" data-toggle="dropdown"
					aria-haspopup="true" aria-expanded="true">keyboard_arrow_down</i>
				<ul class="dropdown-menu pull-right">
					<li role="seperator" class="divider"></li>
					<li><a href="/logout"><i class="material-icons">input</i>Sign
							Out</a></li>
				</ul>
			</div>
		</div>
	</div>
	<!-- #User Info -->
	<!-- Menu -->
	<div class="menu">
		<ul class="list">
			<li class="header">Panel</li>
			<li class="active"><a href="#/home"> <i
					class="material-icons">home</i> <span>Home</span>
			</a></li>
			
						
			<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>AWS Management</span>
			</a>
				<ul class="ml-menu">
					<c:if test="${user.userType == 'ADMIN' }">
						<li><a href="#/awsConfigure"><span>Configure</span></a></li>
					</c:if>

					<li><a href="#/awsUserList"><span>List User</span></a></li>
				</ul></li>
			
			<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>SSH Management</span>
			</a>
			
				<ul class="ml-menu">
					<li><a href="#/addSshKey"><span>Add SSH Key</span></a></li>
					<c:if test="${user.userType == 'ADMIN' || user.userType == 'MANAGER' }">
					<li><a href="#/listSshUser"><span>List User</span></a></li>
					</c:if>
				</ul></li>
				
				

			
			<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>Team Management</span>
			</a>
				<ul class="ml-menu">
					<c:if test="${user.userType == 'ADMIN' || user.userType == 'MANAGER' }">
						<li><a href="#/registerTeam"><span>Create Team</span></a></li>
					</c:if>
					
					<li><a href="#/listTeam"><span>List Team</span></a></li>
				</ul></li> 
				
			<c:if test="${user.userType == 'ADMIN' || user.userType == 'MANAGER' }">
			<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>User Management</span>
			</a> 
					<ul class="ml-menu">
						<li><a href="#/listUIUser"><span>List User</span></a></li>
					</ul>
				</li></c:if>
				
				<c:if test="${user.userType == 'ADMIN' || user.userType == 'MANAGER' }">
				<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>Gerrit Management</span>
					</a> 
					<ul class="ml-menu">
						<li><a href="#/getGerritUserName"><span>List Gerrit User</span></a></li>
					</ul>
				</li></c:if>
				
				<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>Audit Events</span>
					</a> 
					<ul class="ml-menu">
						<li><a href="#/listAuditEvents"><span>List Audit Events</span></a></li>
					</ul>
				</li> 
				
				<c:if test="${user.userType == 'ADMIN' || user.userType == 'MANAGER' }">
			<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>Incident Management</span>
			</a> 
					<ul class="ml-menu">
						<li><a href="#/listAllIncidents"><span>Show Incidents</span></a></li>
						<li><a href="#/addIncidentManual"><span>Add Incident</span></a></li>
					</ul>
						
				</li></c:if>
				
				<c:if test="${user.userType == 'ADMIN'}">
				<li><a href="javascript:void(0);" class="menu-toggle"> <i
					class="material-icons">layers</i> <span>LDAP Management</span>
			</a> 
					<ul class="ml-menu">
						<li><a href="#/listLdapUserDetails"><span>List Ldap Users</span></a></li>
						<li><a href="#/addLdapUser"><span>Add Ldap Users</span></a></li>
					</ul>
						
				</li> </c:if>
				
				
				
		</ul>
	</div>
	<!-- #Menu -->
	<!-- Footer -->
	<div class="legal">
		<div class="copyright">
			&copy; 2018 - 2019 <a href="javascript:void(0);">HawkEye</a>.
		</div>
		<div class="version">
			<b>Version: </b> 1.0.0
		</div>
	</div>
	<!-- #Footer -->
</aside>
<!-- #END# Left Sidebar -->