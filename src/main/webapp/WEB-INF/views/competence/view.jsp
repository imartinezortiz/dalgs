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
			<h3 class="panel-title list">
			<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
			Competence Details </h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/degree/${model.competence.degree.id}/competence/${competenceId}/modify.htm'/>">Edit</a>

		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label>Code: </label> 
					<p class="details">${model.competence.code}</p>
				</div>
				<div class="form-group view">
					<label>Name: </label> 
					<p class="details">${model.competence.name}</p>
				</div>
				<div class="form-group view">
					<label>Description: </label> 
					<p class="details">${model.competence.description}</p>
				</div>
			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
		
			<h3 class="panel-title list">						
			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
			Subject List</h3>
			
		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">Name</div></td>
					<td width="50%"><div class="td-label">Description</div></td>
			
				</tr>
				<c:forEach items="${model.subjects}" var="subject">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${subject.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${subject.description}" />
							</div>
						</td>

						<td>
						<a class="btn list-btn btn-success"
						href="<c:url value='/degree/${degreeId}/subject/${subject.id}.htm'/>">View</a>
						<a class="btn btn-danger"
							href="<c:url value='/degree/${model.competence.degree.id}/competence/${competenceId}/subject/${subject.id}/delete.htm'/>">
							
							Delete
						
						</a></td>

					</tr>
				</c:forEach>


			</table>
		</div>

	</div>


</body>

</html>
