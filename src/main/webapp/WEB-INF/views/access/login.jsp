
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
		<fmt:message key="pleaseSigin" /></h2>
			<spring:message code="username" var="username"/>
			<spring:message code="pass" var="pass"/>
			
			<br>
			<label class="sr-only" for="j_username"></label>
			<input class="form-control" placeholder="${username}" required autofocus id="j_username" name="j_username" size="20" maxlength="50" type="text"/>
			
			<br>
				
			<label class="sr-only" for="j_password"></label>
			<input class="form-control" placeholder="${pass}" required id="j_password" name="j_password" size="20" maxlength="50" type="password"/>
		
			<br>
			 <button class="btn btn-lg btn-primary btn-block" 
			 style=" background: rgb(192, 192, 192);  border: white 2px outset;  font-weight: bolder;  font-size: 18px;  padding: 5px;"
			 type="submit"><fmt:message key="signin" /></button>
		
	</form>
	<c:if test="${not empty message}">
		<div class="alert alert-error">
		<a href="#" class="close" data-dismiss="alert">&times;</a>
    	<c:if test="${!fn:contains(message, 'Success')}">
    	<strong><fmt:message key="errors" />! </strong><fmt:message key="errorSub" /> <br>
    	</c:if>
    	${message}
		</div> 
   </c:if>

	</div>
</body>
</html>