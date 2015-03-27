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
				<fmt:message key="user.add" />
			</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>
			<spring:message code="access.username" var="username" />
			<spring:message code="user.ufirstN" var="ufirstN" />
			<spring:message code="user.ulastN" var="ulastN" />
			<spring:message code="access.pass" var="pass" />
			<spring:message code="user.validEmail" var="validEmail" />

			<form:form method="post" commandName="addUser" role="form">
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

				<div class="form-group">
					<label><fmt:message key="access.pass" />: </label>
					<form:input type="password" path="password" class="form-control"
						placeholder="${pass}" required="true" />
				</div>

				<div class="form-group">
					<label><fmt:message key="user.email" />: </label>
					<form:input path="email" class="form-control"
						placeholder="${validEmail}" required="true" />
				</div>

				<div class="form-group">
					<label><fmt:message key="user.roles" />: </label>
					<div class="checkbox">
						<form:checkboxes items="${roles}" path="roles" />
						<br> <br> <br>
					</div>
				</div>

				<spring:message code="common.add" var="add" />
				<input type="submit" class="btn btn-success" value="${add}" />

			</form:form>
		</div>
	</div>
</body>
</html>
