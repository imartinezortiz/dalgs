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

			<form:form method="post" commandName="addcompetence">
				<div class="form-group">
					<label>Code: </label>
					<form:input path="code" class="form-control" placeholder="Code of the competence" required="true"/>
				</div>	
				<div class="form-group">
					<label>Name: </label>
					<form:input path="name" class="form-control" id="name" placeholder="Name of the competence" required="true"/>

				</div>
				<div class="form-group">
					<label>Description: </label>
					<form:input class="form-control" path="description"
						id="description" placeholder="Description of the competence" required="true"/>
				</div>

				<div class="form-group">
					<label>Degree List:</label>
					

					<form:select class="form-control 2" path="degree" id="degreeSelect">
						<form:option value="">-- Select an option --</form:option>
						<c:forEach items="${degrees}" var="degree">
							<form:option value="${degree.id}">${degree.code}-${degree.name}</form:option>
						</c:forEach>
					</form:select>

				</div>

				<input type="submit" class="btn btn-success" value="Add" />

			</form:form>
		</div>
	</div>
</body>

</html>