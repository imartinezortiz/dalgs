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
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><fmt:message key="common.title" /></title>
<style>
.error {
	color: red;
}
</style>

</head>
<body>

	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>

			<h3 class="panel-title list">
				<fmt:message key="user.mod" />
			</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>
			<spring:message code="access.username" var="username" />
			<spring:message code="user.ufirstN" var="ufirstN" />
			<spring:message code="user.ulastN" var="ulastN" />
			<spring:message code="access.pass" var="pass" />
			<spring:message code="user.validEmail" var="validEmail" />

			<form:form method="post" commandName="modifyUser" role="form">
				<div class="form-group">
					<label><fmt:message key="access.username" />: </label>
					<form:input path="username" class="form-control"
						placeholder="${username}" required="true" />
				</div>

				<div class="form-group">
					<label><fmt:message key="user.firstN" />:</label>
					<form:input path="firstName" class="form-control"
						placeholder="${ufirstN}" required="true" />
				</div>
				<div class="form-group">
					<label><fmt:message key="user.lastN" />: </label>
					<form:input path="lastName" class="form-control"
						placeholder="${ulastN}" required="true" />
				</div>

				

				<spring:message code="common.modify" var="modify" />
				<input type="submit" class="btn btn-success" value="${modify}" />

			</form:form>
		</div>
	</div>
</body>
</html>
