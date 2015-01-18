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
			<h3 class="panel-title list">Modify Activity</h3>
		</div>
		<div class="panel-body">


			<form:form method="post" commandName="modifyAcademicTerm" role="form">
				<div class="form-group">
					<label>Term: </label>
					<form:input path="term" class="form-control" required="true" />
				</div>		

				<div class="form-group">
					<label>Degree List:</label>
					<form:select class="form-control 2" path="degree"
						id="degreeSelect">
						<c:forEach items="${degrees}" var="degree">
							<c:choose>

								<c:when test="${degree.id == idDegree}">
									<form:option value="${degree.id}" selected='true'>${degree.code}-${degree.name}</form:option>

								</c:when>

								<c:otherwise>

									<form:option value="${degree.id}">${degree.code}-${degree.name}</form:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>

<div class="form-group">
					<label>Available Courses:</label>
					<div class="checkbox">
						<form:checkboxes items="${courses}" path="courses"
							  itemValue="id" itemLabel="subject.name" />
						<br> <br>
						<br>
			
					</div>
				</div>
					

				</div>
				<input type="submit" class="btn btn-success" value="Update">

			</form:form>
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