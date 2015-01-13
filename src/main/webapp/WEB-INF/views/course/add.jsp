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
			<h3 class="panel-title list">Add Course</h3>
		</div>
		<div class="panel-body">
			<form:form method="post" commandName="addcourse" role="form">

				<div class="form-group">
					<label>Academic Term</label>
					<form:select class="form-control 2" path="academicTerm"
						id="degreeSelect">
						<form:option value="">-- Select an option --</form:option>
						<c:forEach items="${academicTerms}" var="at">
							<form:option value="${at.id}">${at.term}-${at.degree.name}</form:option>
						</c:forEach>
					</form:select>
					
				</div>

				<div class="form-group">
					<label>Subject: </label>
					<form:select class="form-control 2" path="subject"
						id="subjectSelect">
						<form:option value="">-- Select an option --</form:option>
						<c:forEach items="${subjects}" var="subject">
							<form:option value="${subject.id}">${subject.code}-${subject.name}-${subject.degree.name}</form:option>
						</c:forEach>
					</form:select>

				</div>

				<div class="form-group">
					<label>Activities: </label>
					<div class="checkbox">
						<form:checkboxes items="${activities}" path="activities"
							itemLabel="name" />
					</div>
				</div>

				<br>
				<input type="submit" class="btn btn-success" value="Add" />
			</form:form>
		</div>
	</div>
</body>
</html>
<div class="home-button">
	<a class="btn home" href="<c:url value="/home.htm"/>">Home</a>
</div>
</body>
</html>