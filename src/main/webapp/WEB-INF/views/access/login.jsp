<%--

    This file is part of D.A.L.G.S.

    D.A.L.G.S is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    D.A.L.G.S is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
		<fmt:message key="access.pleaseSigin" /></h2>
			<spring:message code="access.username" var="username"/>
			<spring:message code="access.pass" var="pass"/>
			
			<br>
			<label class="sr-only" for="j_username"></label>
			<input class="form-control" placeholder="${username}" required autofocus id="j_username" name="j_username" size="20" maxlength="50" type="text"/>
			
			<br>
				
			<label class="sr-only" for="j_password"></label>
			<input class="form-control" placeholder="${pass}" required id="j_password" name="j_password" size="20" maxlength="50" type="password"/>
		
			<br>
			<fmt:message key="access.remember-me" />
			 <input type="checkbox" name="remember-me" /><br><br><br>
			 <button class="btn btn-lg btn-primary btn-block" 
			 style=" background: rgb(192, 192, 192);  border: white 2px outset;  font-weight: bolder;  font-size: 18px;  padding: 5px;"
			 type="submit"><fmt:message key="access.signin" /></button>
			 
			 <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		
	</form>
	<c:if test="${not empty message}">
		<div class="alert alert-error">
		<a href="#" class="close" data-dismiss="alert">&times;</a>
    	<c:if test="${!fn:contains(message, 'Success')}">
    	<strong><fmt:message key="common.errors" />! </strong><fmt:message key="error.errorSub" /> <br>
    	</c:if>
    	${message}
		</div> 
   </c:if>

	</div>
</body>
</html>