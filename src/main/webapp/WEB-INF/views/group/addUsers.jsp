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
			Add Professors (${group.name} - ${model.group.course.subject.info.name} - ${model.group.academicTerm.term})
			</h3>
		</div>
		<div class="panel-body">
			<div class="form-group">
  				<div class="form-group">
  					<form:form method="post" commandName="group" role="form">
  				
					<label>Available Professors:</label>
					<div class="checkbox">
						<form:checkboxes items="${professors}" path="professors"
							itemLabel="username" />
						<br> <br>
						<br>
			
					</div>
					<input type="submit" class="btn btn-success" value="Add"  name="AddProfessors" />
					</form:form>
				</div>
			</div>
								
				
			</div>
			
		</div>
		

</body>
</html>
