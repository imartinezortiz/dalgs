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
			<h3 class="panel-title list">Subject Details</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/degree/${model.subject.degree.id}/subject/${subjectId}/modify.htm'/>">Modify</a>

		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label>Code: </label> 
					<p class="details">${model.subject.code}</p>
				</div>
				<div class="form-group view">
					<label>Name: </label> 
					<p class="details">${model.subject.name}</p>
				</div>
				<div class="form-group view">
					<label>Description: </label> 
					<p class="details">${model.subject.description}</p>
				</div>
				<div class="form-group view">
					<label>Degree: </label> 
					<p class="details">${model.subject.degree.name}</p>
				</div>
			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">Competence List </h3>
			<a class="btn list-btn btn-warning2"
				href="<c:url value='/degree/${model.subject.degree.id}/subject/${subjectId}/addCompetences.htm'/>">Add</a>

		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">Name</div></td>
					<td width="50%"><div class="td-label">Description</div></td>
			
				</tr>
				<c:forEach items="${model.competences}" var="competence">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${competence.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${competence.description}" />
							</div>
						</td>

						<td> 
						<a class="btn list-btn btn-success"
						href="<c:url value='/degree/${model.subject.degree.id}/competence/${competence.id}.htm'/>">View</a>
							<a 
							href="<c:url value='/degree/${model.subject.degree.id}/subject/${subjectId}/competence/${competence.id}/delete.htm'/>">
							<img WIDTH="20" HEIGHT="20" border="0" src="<c:url value="/resources/images/error.jpeg" /> " > 
							</a></td>

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
