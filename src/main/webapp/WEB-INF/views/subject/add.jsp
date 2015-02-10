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
							<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
			Add Subject</h3>
		</div>
		<div class="panel-body">
			<form:form method="post" commandName="addsubject" role="form">
				<div class="form-group">
					<label>Code: </label>
					<form:input path="info.code" class="form-control"
						placeholder="Code of the subject" required="true" />
				</div>
				<div class="form-group">
					<label>Name: </label>
					<form:input path="info.name" class="form-control"
						placeholder="Name of the subject" required="true" />
				</div>
				<div class="form-group">
					<label>Description: </label>
					<form:input path="info.description" class="form-control"
						placeholder="Description of the subject" required="true" />
				</div>

								<br>
				<input type="submit" class="btn btn-success" value="Add" />
			</form:form>
		</div>
	</div>
</body>
</html>
