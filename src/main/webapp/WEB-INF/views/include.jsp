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
<!-- archivo de cabecera para incluir en todos los archivos JSP que crearemos con posterioridad. -->
<%@ page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!-- Contains Function -->
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- spring:out formatea la salida -->




<link rel="shortcut icon"
	href="<c:url value= "/resources/images/favicon.ico" />">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="<c:url value= "/resources/css/bootstrap.css" />">

<!--Owner Style CSS -->
<link rel="stylesheet"
	href="<c:url value= "/resources/css/style.css" />">

<!-- Latest compiled and minified JavaScript -->
<script type="text/javascript"
	src="<c:url value="/resources/scripts/jquery-1.11.1.min.js" /> ">
	
</script>

<script type="text/javascript"
	src=" <c:url value="/resources/scripts/bootstrap.js" /> ">
	
</script>

<script type="text/javascript"
	src=" <c:url value="/resources/scripts/bootstrap-dropdown.js" /> ">
	
</script>

<script type="text/javascript"
	src=" <c:url value="/resources/scripts/bootstrap-filestyle.js" /> ">
	
</script>

<!-- FONTS -->
<link href='http://fonts.googleapis.com/css?family=Gruppo'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Prosto+One'
	rel='stylesheet' type='text/css'>

<div id="locale">
	<a href="?lang=es_ES"> <img style="max-width: 40px;"
		src="<c:url value="/resources/images/es_ES.png" /> " />
	</a><br> <a href="?lang=en_UK"> <img style="max-width: 40px;"
		src="<c:url value="/resources/images/en_UK.png" /> ">
	</a></div>
<div class="page-header logo">
<img alt="tfg" class="img-rounded logo2" style="margin-top: -0.9%;" 
		src="<c:url value="/resources/images/theme/Education_-_Grad_Hat.png" /> ">	
	<h1 class="logo">

		DALGS <small>TFG 2014/ 2015</small>
	</h1>
	<img alt="tfg" class="img-rounded logo" style="margin-top: 0.25%;"
		src="<c:url value="/resources/images/theme/ucm-ws.png" /> "> <img
		alt="tfg" class="img-rounded logo"
		src="<c:url value="/resources/images/theme/ucm.png" /> ">
</div>




<c:url value="/" var="homeUrl" />
<c:url value="/user.htm" var="userUrl" />
<c:url value="/admin.htm" var="adminUrl" />
<c:url value="/logout.htm" var="logoutUrl" />


<%-- <c:out value="${upload}"></c:out>
<c:out value="${contextPath}"></c:out>
 --%>
<div class="list-group index">

	<nav class="navbar navbar-default">
	
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand active" href="${homeUrl}"><img
					style="width: 40px; margin-top: -25%;"
					src="<c:url value="/resources/images/theme/house.png" /> "></a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
						<li><a href="${userUrl}"> <span
								class="glyphicon glyphicon-user" aria-hidden="true"></span>
										<fmt:message key="access.login" />
 					<span class="sr-only">(current)</span></a></li>
					</sec:authorize>

					<sec:authorize
						access="hasRole('ROLE_STUDENT') or hasRole('ROLE_PROFESSOR')">
						<li><a href="${userUrl}"> <span
								class="glyphicon glyphicon-user" aria-hidden="true"></span> <fmt:message key="user.user" />
								<span class="sr-only">(current)</span></a></li>
					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_ADMIN')">
						<li><a href="${adminUrl}"> <span
								class="glyphicon glyphicon-user" aria-hidden="true"></span>
								Admin
						</a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('ROLE_ADMIN' ,  'ROLE_USER')">
						<li><a href="${logoutUrl}"> <span
								class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
										<fmt:message key="access.logout" />

						</a></li>
					</sec:authorize>

				</ul>



				<ul class="nav navbar-nav navbar-right">
				
				<c:url value="/activity/download.htm" var="activitiesUrl" />
				<c:url value="/competence/download.htm" var="competencestUrl" />
				<c:url value="/degree/download.htm" var="degreesUrl" />
				<c:url value="/module/download.htm" var="modulesUrl" />
				<c:url value="/subject/download.htm" var="subjectsUrl" />
				<c:url value="/topic/download.htm" var="topicstUrl" />
				<c:url value="/user/download.htm" var="usersUrl" />
								
				<sec:authorize access="hasRole('ROLE_ADMIN')">	
				  <li class="dropdown" id="menu1">
				    <a class="dropdown-toggle" data-toggle="dropdown" href="#menu1">
				     <span class="glyphicon glyphicon-download" aria-hidden="true"></span>  CSV
				      <b class="caret"></b>
				    </a>
				    <ul class="dropdown-menu">	
				     <li><a href="${activitiesUrl}"><fmt:message key="activity.ac" /></a></li>
				      <li class="divider"></li>    
				      <li><a href="${competencestUrl}"><fmt:message key="competence.com" /></a></li>
				      <li class="divider"></li>
				      <li><a href="${degreesUrl}"><fmt:message key="degree.deg" /></a></li>
				      <li class="divider"></li>
				      <li><a href="${modulesUrl}"><fmt:message key="module.module" /></a></li>
				      <li class="divider"></li>
				      <li><a href="${subjectsUrl}"><fmt:message key="subject.sub" /></a></li>
				      <li class="divider"></li>
				      <li><a href="${topicstUrl}"><fmt:message key="topic.top" /></a></li>
				      <li class="divider"></li>
				      <li><a href="${usersUrl}"><fmt:message key="user.user" /></a></li>
				    </ul>
				  </li>
			  </sec:authorize>
					
					<sec:authorize access="isAuthenticated()">
					<p class="navbar-text navbar-right" style="font-size: 15px;">

						<span class="glyphicon glyphicon-eye-open" aria-hidden="true">
						</span> <fmt:message key="access.signedIn" />
 						<a href="#" class="navbar-link">
 						<%=SecurityContextHolder.getContext().getAuthentication().getName()%>
 						</a>&nbsp;&nbsp;
					</p></sec:authorize>
				</ul>

			</div>
		</div>
	</nav>
</div>