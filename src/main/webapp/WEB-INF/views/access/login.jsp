
 <%@ include file="/WEB-INF/views/include.jsp"%>
 
<html>
<head>
	<link rel="stylesheet" type="text/css" media="screen" href="resources/css/style.css"/>
	<title>Login</title>
</head>
 
<body> 
<div class="container">


	<form class="form-signin" action="j_spring_security_check" method="post" >
		<h2 class="form-signin-heading">								
		<span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>
		Please sign in</h2>
			
			<br>
			<label class="sr-only" for="j_username"></label>
			<input class="form-control" placeholder="User Name" required autofocus id="j_username" name="j_username" size="20" maxlength="50" type="text"/>
			
			<br>
				
			<label class="sr-only" for="j_password">Password</label>
			<input class="form-control" placeholder="Password" required id="j_password" name="j_password" size="20" maxlength="50" type="password"/>
		
			<br>
			 <button class="btn btn-lg btn-primary btn-block" 
			 style=" background: rgb(192, 192, 192);  border: white 2px outset;  font-weight: bolder;  font-size: 18px;  padding: 5px;"
			 type="submit">Sign in</button>
		
	</form>
	<c:if test="${not empty message}">
		<div class="alert alert-error">
		<a href="#" class="close" data-dismiss="alert">&times;</a>
    	<strong>Error!</strong> A problem has been occurred while submitting
    	${message}
		</div> 
   </c:if>

	</div>
</body>
</html>