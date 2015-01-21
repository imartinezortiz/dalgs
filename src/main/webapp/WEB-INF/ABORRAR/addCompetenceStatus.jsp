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
			<h3 class="panel-title list">Add Activity</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>

		<div class="addCompetenceStatus">
			<form:form method="post" commandName="addcompetencestatus">
					<h4> New Competence Status</h4>
					<label>Competence:</label>
					<form:select class="form-control 2" path=""
						id="competence">
						<form:option value=""> --Select an option-- </form:option>
						<c:forEach items="${competences}" var="comp">
							<form:option value="${comp.id}">${comp.name}</form:option>
						</c:forEach>
					</form:select>
					<label>Competence Percentage:</label>
					<form:input class="form-control" path="percentage" id="percentage"
						required="true" />



					 <input type="submit" class="btn btn-success CompSta" name="button1"
						value="Add Competence Status" /> -->

			</form:form>
			</div>
			</div>
		</div>
	
</body>
</html>
<div class="home-button">
	<a class="btn home" href="<c:url value="/home.htm"/>">Home</a>
</div>
</body>
</body>
</html>