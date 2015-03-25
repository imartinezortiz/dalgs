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
			<h3 class="panel-title list"><fmt:message key="comAdd" /></h3>
		</div>
		<div class="panel-body">

			<form:form method="post" commandName="subject">
				
				<div class="form-group">
					<label><fmt:message key="code" />: </label>
					<form:input path="info.code" class="form-control" readonly="true" required="true"/>
				</div>
				<div class="form-group">
					<label><fmt:message key="name" />: </label>
					<form:input path="info.name" class="form-control" id="name" readonly="true" required="true"/>

				</div>
				<div class="form-group">
					<label><fmt:message key="desc" />: </label>
					<form:input class="form-control" path="info.description"
						id="description" readonly="true" required="true"/>
				</div>

				<div class="form-group">
					<label><fmt:message key="availableCom" />:</label>
					<div class="checkbox">
						<form:checkboxes items="${competences}" path="competences"
							itemLabel="info.name" />
						<br> <br>
						<br>
			
					</div>
				</div>
				<spring:message code="add" var="add"/>
				<input type="submit" class="btn btn-success" value="${add}" />

			</form:form>
		</div>
	</div>
</body>
</html>
