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
			<h3 class="panel-title list">Add Competence</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>

			<form:form method="post" commandName="subject">
				
				<div class="form-group">
					<label>Code: </label>
					<form:input path="code" class="form-control" readonly="true" required="true"/>
				</div>
				<div class="form-group">
					<label>Name: </label>
					<form:input path="name" class="form-control" id="name" readonly="true" required="true"/>

				</div>
				<div class="form-group">
					<label>Description: </label>
					<form:input class="form-control" path="description"
						id="description" readonly="true" required="true"/>
				</div>

				<div class="form-group">
					<label>Available Competences:</label>
					<div class="checkbox">
						<form:checkboxes items="${competences}" path="competences"
							itemLabel="name" />
						<br> <br>
						<br>
			
					</div>
				</div>

				<input type="submit" class="btn btn-success" value="Add" />

			</form:form>
		</div>
	</div>
</body>
</html>
