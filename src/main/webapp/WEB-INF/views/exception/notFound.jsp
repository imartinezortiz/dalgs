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
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link rel="shortcut icon" href="<c:url value= "/resources/images/favicon.ico" />">

<link rel="stylesheet" href="<c:url value= "/resources/css/bootstrap.css" />">
<link rel="stylesheet" href="<c:url value= "/resources/css/style.css" />">
<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-1.11.1.min.js" /> "></script>
<script type="text/javascript"src=" <c:url value="/resources/scripts/bootstrap.js" /> "></script>
<script type="text/javascript" src=" <c:url value="/resources/scripts/bootstrap-filestyle.js" /> "></script>


<html>
<head>
<title><fmt:message key="common.error" /></title>


</head>
<body>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<span class="glyphicon  glyphicon-road" aria-hidden="true">&nbsp;</span>
			<h3 class="panel-title list pError">
				
					<fmt:message key="error.statusCode" />: 404
					<!--<c:choose>
						<c:when test="${not empty javax.servlet.error.status_code}">
							<%-- <%=request.getAttribute("javax.servlet.error.status_code")%> --%>
						</c:when>
						<c:otherwise>404</c:otherwise>
					</c:choose>	-->		
				</h3> 

		</div>

		<div class="panel-body">
			<div class="row">
				<div class="col-md-6">
					<img style=" margin-left: 30%; "class="img-responsive" alt="Responsive image"
						src="<c:url value="/resources/images/404.png" /> ">
				</div>
				<div class="col-md-6">
				<c:choose>
				<c:when test="${not empty javax.servlet.error.message}">
					<p class="pError" > <fmt:message key="error.reason" />:</p>
					<p style=" font-family: monospace;"><%=request.getAttribute("javax.servlet.error.message")%></p>
				</c:when>
				<c:otherwise>
					<h3 class="nf_error" align="center"><fmt:message key="error.notFound" /></h3>
				
				</c:otherwise>
				</c:choose>
				</div>
			</div>

		</div>

	</div>

</body>
</html>
<div class="home-button">
	<a class="btn home" href="<c:url value="/home.htm"/>"><fmt:message key="common.home" /></a>
</div>
</body>
</body>
</html>