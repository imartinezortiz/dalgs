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
			<h3 class="panel-title list">							
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
			Add Degree</h3>
		</div>
		<div class="panel-body">
			<form:form method="post" commandName="addDegree" role="form">
				<div class="form-group">
					<label>Code: </label>
					<form:input path="code" class="form-control" placeholder="Code of the degree" required="true"/>
				</div>
				<div class="form-group">
					<label>Name: </label>
					<form:input path="name" class="form-control"
						placeholder="Name of the degree" required="true" />
				</div>
				<div class="form-group">
					<label>Description: </label>
					<form:input path="description" class="form-control"
						placeholder="Description of the degree" required="true"/>
				</div>

				<br>
				<input type="submit" class="btn btn-success" value="Add" />
			</form:form>
		</div>
	</div>
</body>
</html>
