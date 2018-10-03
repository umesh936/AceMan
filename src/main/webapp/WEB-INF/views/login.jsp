<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta name="google-signin-client_id" content="${clienId}">
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="https://apis.google.com/js/platform.js" async defer></script>
<!------ Include the above in your HEAD tag ---------->

<style>

body {
	background-color: #ddd;
}

.login {
	margin-top: 25px !important;
	padding: 5px;
	width: auto;
	min-width: 320px;
	max-width: 400px;
	height: 400px;
	background-color: #f3f3f3;
	margin: 0 auto;
	border-radius: 10px;
	border: 1px solid##c5c5c5;
	text-align: center;
}

.login-inner {
	margin: 0 auto;
	max-width: 380px;
	width: auto;
}

.login>h4 {
	font-size: 20px;
	margin-left: 5px;
	font-weight: 600;
}

.email {
	margin-bottom: 5px;
}

.password {
	margin-bottom: 5px;
}

.submit {
	margin-top: 5px;
}

.forgot {
	min-width: 50px;
	width: 49%;
	float: right;
	margin-top: 20px;
}

.register {
	width: 50%;
	float: left;
	margin-top: 20px;
}

@media ( max-width : 320px) {
	.forgot {
		font-size: 9px;
		font-weight: 700;
		padding: 8px;
	}
}
</style>
<script>
function onSignIn(googleUser) {
	  var profile = googleUser.getBasicProfile();
	  var id_token = googleUser.getAuthResponse().id_token;
	 
	  var auth2 = gapi.auth2.getAuthInstance();
	     auth2.disconnect(); 
	     
	  var xhr = new XMLHttpRequest();
	  xhr.open('POST', '/google/tokensignin');
	  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	  xhr.onload = function() {
	    console.log('Signed in as: ' + xhr.responseText);
	    if (xhr.readyState === 4) {
		      if (xhr.status === 200) {
		    	  window.location.href='/home';
		      } else {
		    	  window.location.href='/login?error=Email%20Not%20Supported.';
		      }
		  }
	  };
	  xhr.send('idtoken=' + id_token);
	}
</script>
</head>


<body>
	<div class="container">
		<div class="login">
			<h4>Login to Admin Panel - HawkEye </h4>
			<hr>
			<form class="login-inner" action="/login" method="post">
				<%
					if (request.getParameter("error") != null) {
				%>
				<div class="mb-3 text-danger"><%=request.getParameter("error")%></div>
				<%
					}
				%>
				<input type="email" class="form-control email" name="email"
					id="email" placeholder="Enter email"> <input
					type="password" class="form-control password" name="password"
					id="password" placeholder="Password"> <label
					class="checkbox-inline"> <input type="checkbox"
					id="remember" value="Remember me"> Remember me
				</label> <input class="btn btn-block btn-lg btn-success submit"
					type="submit" value="Login">
			</form>
			<div class="g-signin2" data-onsuccess="onSignIn"></div>
		</div>
	</div>
	<!-- /container -->
</body>
</html>