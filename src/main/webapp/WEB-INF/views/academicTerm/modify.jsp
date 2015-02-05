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
			<h3 class="panel-title list">				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
			Modify Activity</h3>
		</div>
		<div class="panel-body">


			<form:form method="post" commandName="academicTerm" role="form">
				<div class="form-group">
					<label>Term: </label>
					<form:input path="term" class="form-control" required="true" length="20"  />
				
				</div>		
				
				<input type="submit" class="btn btn-success" value="Update">

			</form:form>
		</div>
	</div>

</body>
</html>
