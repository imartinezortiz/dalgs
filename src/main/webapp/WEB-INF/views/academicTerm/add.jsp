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
		
			<h3 class="panel-title list">Add AcademicTerm</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>

			<form:form method="post" commandName="addAcademicTerm" role="form">
  				<div class="form-group">
  					<label>Term: </label>
					<form:input path="term" class="form-control" placeholder="Code of the academicTerm" required="true"/>
				</div>
				

				<div class="form-group">
					<label>Degrees List:</label>
					

					<form:select class="form-control 2" path="degree" id="degreeSelect">
						<form:option value=""> --Select an option-- </form:option>
						<c:forEach items="${degrees}" var="degree">
							<form:option value="${degree.id}">${degree.info.code}-${degree.info.name}</form:option>
						</c:forEach>
					</form:select>

				</div>

				<input type="submit" class="btn btn-success" value="Add" />

			</form:form>
		</div>
	</div>
</body>
</html>
