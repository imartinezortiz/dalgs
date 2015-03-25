<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><fmt:message key="title" /></title>
<style>
.error {
	color: red;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>

			<h3 class="panel-title list">
				<fmt:message key="userMod" />
			</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>
			<spring:message code="username" var="username" />
			<spring:message code="ufirstN" var="ufirstN" />
			<spring:message code="ulastN" var="ulastN" />
			<spring:message code="pass" var="pass" />
			<spring:message code="validEmail" var="validEmail" />

			<form:form method="post" commandName="modifyUser" role="form">
				<div class="form-group">
					<label><fmt:message key="username" />: </label>
					<form:input path="username" class="form-control"
						placeholder="${username}" required="true" />
				</div>

				<div class="form-group">
					<label><fmt:message key="firstN" />:</label>
					<form:input path="firstName" class="form-control"
						placeholder="${ufirstN}" required="true" />
				</div>
				<div class="form-group">
					<label><fmt:message key="lastN" />: </label>
					<form:input path="lastName" class="form-control"
						placeholder="${ulastN}" required="true" />
				</div>

				<div class="form-group">
					<label><fmt:message key="pass" />: </label>
					<form:input type="password" path="password" class="form-control"
						placeholder="${pass}" required="true" />
				</div>



				<spring:message code="modify" var="modify" />
				<input type="submit" class="btn btn-success" value="${modify}" />

			</form:form>
		</div>
	</div>
</body>
</html>
