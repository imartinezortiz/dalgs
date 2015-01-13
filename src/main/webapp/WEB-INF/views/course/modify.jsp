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
			<h3 class="panel-title list">Modify Course</h3>
		</div>
		<div class="panel-body">


			<form:form method="post" commandName="modifyCourse" role="form">
	
				
				<div class="form-group">
					<label>Academic Term:</label>
					<form:select class="form-control 2" path="academicTerm" id="termSelect">
						<form:option value="">-- Select an option --</form:option>
						<c:forEach items="${academicTerms}" var="at">
							<c:choose>
								<c:when test="${at.id == idAcademicTerm}">
									<form:option value="${at.id}" selected='true'>${at.term}</form:option>
								</c:when>
								<c:otherwise>
									<form:option value="${at.id}">${at.term}</form:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
				</div>
				
				<div class="form-group">
					<label>Subject:</label>
					<form:select class="form-control 2" path="subject" >
						<form:option value="">-- Select an option --</form:option>
						<c:forEach items="${subjects}" var="subject">
							<c:choose>
								<c:when test="${subject.id == idSubject}">
									<form:option value="${subject.id}" selected='true'>${subject.code}-${subject.name}-${subject.degree.name}</form:option>
								</c:when>
								<c:otherwise>
									<form:option value="${subject.id}">${subject.code}-${subject.name}-${subject.degree.name}</form:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
				</div>
				
				<div class="form-group">
					<label>Available Activities:</label>
					<div class="checkbox">
						<form:checkboxes items="${activities}" path="activities"
							itemLabel="name" />
						<br> <br>
						<br>
			
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