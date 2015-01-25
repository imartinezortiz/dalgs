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
<!-- spring:out formatea la salida -->

<link rel="shortcut icon"
	href="<c:url value= "/resources/images/image200.ico" />">

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

<!-- FONTS -->
<link href='http://fonts.googleapis.com/css?family=Gruppo' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Prosto+One' rel='stylesheet' type='text/css'>



<div class="page-header logo">
	<h1 class="logo">
		Badges for Subjects <small>TFG 2014/ 2015</small>
	</h1>
	<img alt="tfg" class="img-rounded logo"
		src="<c:url value="/resources/images/logo.png" /> ">
</div>


<c:url value="/" var="homeUrl" />
<c:url value="/user.htm" var="userUrl" />
<c:url value="/admin.htm" var="adminUrl" />
<c:url value="/logout.htm" var="logoutUrl" />

<div class="list-group index">

<nav class="navbar navbar-default">
	<div class="container-fluid" >
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand active" href="${homeUrl}"><img style=" width: 40px; margin-top: -25%;" 
				src="<c:url value="/resources/images/house.png" /> "></a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="${userUrl}"> 					
				<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
				User <span
						class="sr-only">(current)</span></a></li>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li><a href="${adminUrl}">
									<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
					Admin</a></li>
				</sec:authorize>
								<li><a href="${logoutUrl}">				
								<span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
								Logout</a></li>
				
			</ul>
			<ul class="nav navbar-nav navbar-right">
						<li><a href="${userUrl}"> 					
				<span class="glyphicon glyphicon-upload" aria-hidden="true"></span>
				CVS <span
						class="sr-only">(current)</span></a></li>
				<p class="navbar-text navbar-right" style="font-size: 15px;">
				
				<span class="glyphicon glyphicon-eye-open" aria-hidden="true"> </span>
				
				Signed in as <a href="#" class="navbar-link"><%=SecurityContextHolder.getContext().getAuthentication().getName()%></a>&nbsp&nbsp</p>

			</ul>
		</div>
	</div>
</nav>
</div>

