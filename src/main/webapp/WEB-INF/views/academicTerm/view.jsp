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
 <div class="panel-heading list">
     
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">Academic Term Details</h3>
			
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label>Term: </label>
					<p class="details">
						${model.academicTerm.term}
					</p>
					</div>
					<div class="form-group view">
					<label>Degree:</label>
					<p class="details">
						${model.academicTerm.degree.name}
					</p>
				</div>

			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">Course List </h3>
					<a class="btn list-btn btn-warning"
				href="<c:url value='/academicTerm/${term}/degree/${idDegree}/course/add.htm'/>">Add Course</a>
		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">SubjectCode</div></td>
					<td width="50%"><div class="td-label">Subject Name</div></td>
				
				</tr>
				<c:forEach items="${model.courses}" var="course">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${course.subject.code}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${course.subject.name}" />
							</div>
						</td>

						<td>
							<a class="btn btn-success"
							href="<c:url value='/academicTerm/${term}/degree/${idDegree}/course/${course.id}/view.htm'/>">
							View
							</a>
							<a  class="btn btn-danger"
							href="<c:url value='/academicTerm/${term}/degree/${idDegree}/course/${course.id}/delete.htm'/>">
							Delete
							</a>
						</td>

					</tr>
				</c:forEach>


			</table>
		</div>

	</div>
	<div class="home-button">
		<a class="btn home" href="<c:url value="/home.htm"/>">Home</a>
	</div>

</body>

</html>
