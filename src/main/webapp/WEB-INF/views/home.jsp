<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.mettl.tool.mgmt.resposne.dto.UserObject" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>HawkEye</TITLE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1" />
<STYLE>
.warningMessage {
	color: red;
}
</STYLE>
<!-- Favicon-->
<link rel="icon" href="favicon.ico" type="image/x-icon">
<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css?family=Roboto:400,700&subset=latin,cyrillic-ext"
	rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet" type="text/css">

<!-- Bootstrap Core Css -->
<link href="/plugins/bootstrap/css/bootstrap.css" rel="stylesheet">

<!-- Waves Effect Css -->
<link href="/plugins/node-waves/waves.css" rel="stylesheet" />

<!-- Animation Css -->
<link href="/plugins/animate-css/animate.css" rel="stylesheet" />

<!-- Morris Chart Css-->
<link href="/plugins/morrisjs/morris.css" rel="stylesheet" />

<!-- Custom Css -->
<link href="/css/style.css" rel="stylesheet">
<link href="/css/themes/all-themes.css" rel="stylesheet" />
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans+Condensed:700,300,300italic'
	rel='stylesheet' type='text/css'>
<script type="text/javascript" src="/js/modernizr.custom.79639.js"></script>
<!-- Jquery Core Js -->
<script src="/plugins/jquery/jquery.min.js"></script>
<script src="/js/angular.min.js"></script>
<script src="/js/angular-animate.min.js"></script>
<script src="/js/angular-route.min.js"></script>
<script type="text/javascript">
	var app = angular.module('aceman', [ 'ngRoute' ]);
	var temp = "<%=((UserObject)session.getAttribute("user")).getUserType().toString()%>";
	console.log("test"+ temp);
	var userEml = "<%=((UserObject)session.getAttribute("user")).getEmail().toString()%>";
	console.log(userEml);
	var teamid = "<%=((UserObject)session.getAttribute("user")).getTeamId()%>";
	console.log(teamid); 
</script>
<script src="/js/custom/constants/acConstant.js"></script>
<script src="/js/custom/services/urlService.js"></script>
<script src="/js/custom/controllers/awsRequestCtrl.js"></script>
<script src="/js/custom/controllers/uiUserCtrl.js"></script>
<script src="/js/custom/controllers/uiTeamCtrl.js"></script>
<script src="/js/custom/controllers/sshUserCtrl.js"></script>
<script src="/js/custom/controllers/gerritCtrl.js"></script>
<script src="/js/custom/controllers/ldapCtrl.js"></script>
<script src="/js/custom/controllers/incidentCtrl.js"></script>
<script src="/js/custom/controllers/homeCtrl.js"></script>
<script src="/js/custom/controllers/auditEventCtrl.js"></script>


<script src="/js/custom/router.js"></script>

</HEAD>
<body ng-app="aceman" class="theme-blue">
	<!-- Page Loader -->
	<div class="page-loader-wrapper">
		<div class="loader">
			<div class="preloader">
				<div class="spinner-layer pl-red">
					<div class="circle-clipper left">
						<div class="circle"></div>
					</div>
					<div class="circle-clipper right">
						<div class="circle"></div>
					</div>
				</div>
			</div>
			<p>Please wait...</p>
		</div>
	</div>

	<!-- #END# Search Bar -->
	<div class="container-fluid">
		<div class=row>
			<div class="search-bar">
				<div class="search-icon">
					<i class="material-icons">search</i>
				</div>
				<input type="text" placeholder="START TYPING...">
				<div class="close-search">
					<i class="material-icons">close</i>
				</div>
			</div>
			<jsp:include page="header.jsp"></jsp:include>
			
		</div>
		<!-- second Row -->
		<div class="row" style="margin-top: 70px">
			<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
				<jsp:include page="leftSidebar.jsp"></jsp:include>
			</div>
			<div class="col-lg-9 col-md-9 col-sm-6 col-xs-12">
				<div class=container-fluid>
					<div class=row>
						<div class="block-header">
							<h2>DASHBOARD</h2>
						</div>
					</div>
					<!-- Widgets -->
					<div class="row clearfix">
						<a href="#/awsAccountList">
							<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
								<div class="info-box bg-pink hover-expand-effect">
									<div class="icon">
										<i class="material-icons">playlist_add_check</i>
									</div>
									<div class="content">
										<div class="text">AWS Account Configured</div>
										<div class="number count-to" data-from="0"
											data-to="{{teamCount}}" data-speed="15"
											data-fresh-interval="20"></div>
									</div>
								</div>
							</div>
						</a> <a href="#/awsDashboard">
							<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
								<div class="info-box bg-cyan hover-expand-effect">
									<div class="icon">
										<!-- <i class="material-icons">help</i> -->
										<i class="material-icons">playlist_add_check</i>
									</div>
									<div class="content">
										<div class="text">AWS Environment</div>
										<div class="number count-to" data-from="0"
											data-to="${totalAwsAccount}" data-speed="1000"
											data-fresh-interval="20"></div>
									</div>
								</div>
							</div>
						</a> 
						
						<a href="#/listTeam">
							<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
								<div class="info-box bg-light-green hover-expand-effect">
									<div class="icon">
										<i class="material-icons">playlist_add_check</i>
									</div>
									<div class="content">
										<div class="text">Total Team</div>
										<div class="number count-to" data-from="0" data-to="23"
											data-speed="1000" data-fresh-interval="20"></div>
									</div>
								</div>
							</div>
						</a> 
						
						<c:if test="${user.userType == 'ADMIN' || user.userType == 'MANAGER' }">
						<a href="#/listUIUser">
							<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
								<div class="info-box bg-orange hover-expand-effect">
									<div class="icon">
										<i class="material-icons">playlist_add_check</i>
									</div>
									<div class="content">
										<div class="text">Total User</div>
										<div class="number count-to" data-from="0" data-to="10"
											data-speed="1000" data-fresh-interval="20"></div>
									</div>
								</div>
							</div>
						</a>
						</c:if>
					</div>
					<!-- #END# Widgets -->
					<div class="row clearfix">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<div class="card">
								<div>
									<div ng-view></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<%-- <jsp:include page="rightSettingbar.jsp"></jsp:include> --%>
			<!-- <div class="container-fluid"> -->
		</div>
	</div>
</body>
<!-- Bootstrap Core Js -->
<script src="/plugins/bootstrap/js/bootstrap.js"></script>

<!-- Select Plugin Js -->
<script src="/plugins/bootstrap-select/js/bootstrap-select.js"></script>

<!-- Slimscroll Plugin Js -->
<script src="/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>

<!-- Waves Effect Plugin Js -->
<script src="/plugins/node-waves/waves.js"></script>

<!-- Jquery CountTo Plugin Js -->
<!-- <script src="/plugins/jquery-countto/jquery.countTo.js"></script> -->

<!-- Morris Plugin Js -->
<!-- <script src="/plugins/raphael/raphael.min.js"></script> -->
<!-- <script src="/plugins/morrisjs/morris.js"></script> -->
<!-- Custom Js -->
<script src="/js/admin.js"></script>
<!-- Demo Js -->
<!-- <script src="/js/demo.js"></script> -->
</html>



