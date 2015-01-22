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

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">Course Details</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/academicTerm/${academicId}/course/${courseId}/modify.htm'/>">Modify</a>

		</div>

		<div class="panel-body">
			<div class="form-group">
				<div class="form-group view">
					<label>Academic Term: </label> 
					<p class="details">${model.course.academicTerm.term}</p>

				</div>
				
				
			</div>

		</div>
	</div>
	
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list"> Subject</h3>			
		</div>
		<div class="panel-body">
		<div class="form-group view">
				<label>Subject: </label> 
					<p class="details">${model.course.subject.code} - ${model.course.subject.name}</p>
				</div>
		</div>
	</div>
	
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list"> Activity List</h3>
			<a  class="btn list-btn btn-warning2" href="<c:url value='/academicTerm/${academicId}/course/${courseId}/add.htm'/>"> Add Activity </a>
			
		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">Name</div></td>
					<td width="50%"><div class="td-label">Description</div></td>
				</tr>
				<c:forEach items="${model.activities}" var="activity">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${activity.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${activity.description}" />
							</div>
						</td>

						<td><a class="btn btn-success" 
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}.htm'/>">
									View </a> 
									<a class="btn btn-danger"
								href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}/delete.htm'/>">
									Delete </a>
							<%-- <a 
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}/delete.htm'/>">
							<img WIDTH="20" HEIGHT="20" border="0" src="<c:url value="/resources/images/error.jpeg" /> " > 
							</a> --%>
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
