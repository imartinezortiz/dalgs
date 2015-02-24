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
		
			<h3 class="panel-title list">Add User</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>

			<form:form method="post" commandName="addUser" role="form">
  				<div class="form-group">
  					<label>Username: </label>
					<form:input path="username" class="form-control" placeholder="Username" required="true"/>
				</div>
				
				<div class="form-group">
  					<label>First Name:</label>
					<form:input path="firstName" class="form-control" placeholder="User first name" required="true"/>
				</div>
				<div class="form-group">
  					<label>Last Name: </label>
					<form:input path="lastName" class="form-control" placeholder="User last name" required="true"/>
				</div>
				
				<div class="form-group">
  					<label>Password: </label>
					<form:input path="password" class="form-control" placeholder="Password" required="true"/>
				</div>
				
				<div class="form-group">
  					<label>Email: </label>
					<form:input path="email" class="form-control" placeholder="Valid email" required="true"/>
				</div>
				
				<div class="form-group">
  					<label>Roles: </label>
						<div class="checkbox">
						<form:checkboxes items="${roles}" path="roles"/>
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
