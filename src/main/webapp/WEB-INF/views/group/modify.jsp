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
			<h3 class="panel-title list">Modify Group</h3>
		</div>
		<div class="panel-body">


			<form:form method="post" commandName="modifyGroup" role="form">
				
				<div class="form-group">
					<label>Name: </label>
					<form:input path="name" class="form-control"
						placeholder="Name of the group" required="true"/>
				</div>
				

				<input type="submit" class="btn btn-success" value="Update">

			</form:form>
		</div>
	</div>




</body>

</html>