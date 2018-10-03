<!-- Top Bar -->
<style>
.navbar-header {
    width: 100%;
}
#awsId{
	color:white;
	font-weight:bold;
	padding: 5px 5px;
}
.navbar-brand{
	padding:1px}
.navbar{
    max-height: 70px;
}
.navbar-brand > img {
display:inline;}
</style>
<script>
if (typeof(Storage) !== "undefined") {
		var temp2 = "<%= session.getAttribute("awsAccountName").toString() %>";
		console.log("in header.jsp page")
		console.log(temp2);
		document.getElementById('awsname').innerHTML  = temp2;
	}

</script>

<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
	<nav class="navbar">
		<div class="container-fluid">
			<div class="navbar-header">
				<a href="javascript:void(0);" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false"></a>
				<a href="javascript:void(0);"class="bars"></a>
				<div class="col-xs-8 col-sm-8 col-md-8 col-lg-8">
				
						<a class="navbar-brand" href="#/home"><img src="/images/hawkeye.png" style="width: 54px; filter: brightness(0) invert(1);">  H@wkEye</a>
				</div>
				 
				<div href="#/awsAccountList" id="awsId" class ="pull-right" >
					<a href="#/awsAccountList">
						<img  src="/images/aws-account.png" style="width: 35px;"> 
						<h5>{{defaultAccountName}}</h5>
						
						 
				 </div>
			
			</div>
			
						
			<div class="collapse navbar-collapse" id="navbar-collapse">
				<aside id="rightsidebar" class="right-sidebar">
					<ul class="nav nav-tabs tab-nav-right" role="tablist">
						<li role="presentation" class="active"><a href="#skins"
							data-toggle="tab">SKINS</a></li>
						<li role="presentation"><a href="#settings" data-toggle="tab">SETTINGS</a></li>
					</ul>
					<%-- <jsp:include page="rightSettingbar.jsp"></jsp:include> --%>
				</aside>
			</div>


		</div>
	</nav>
</div>
<!-- #Top Bar -->