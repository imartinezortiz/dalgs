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
			
			Course Details</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/academicTerm/${academicId}/course/${courseId}/modify.htm'/>">
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>Edit</a>

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
			<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
		
			<h3 class="panel-title list"> Subject</h3>			
		</div>
		<div class="panel-body">
		<div class="form-group view">
				<label>Subject: </label> 
					<p class="details">${model.course.subject.info.code} - ${model.course.subject.info.name}</p>
				</div>
		</div>
	</div>
	
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">						
			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
			 Activity List</h3>
			<a  class="btn list-btn btn-warning2" href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/add.htm'/>">
								<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
			
			 Add Activity </a>
			
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
								<c:out value="${activity.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${activity.info.description}" />
							</div>
						</td>

						<td><a class="btn btn-success" 
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}.htm'/>">
									View </a> 
									<a class="btn btn-danger"
								href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}/delete.htm'/>">
									Delete </a>
							
							</td>

					</tr>
				</c:forEach>


			</table>
		</div>
	</div>

<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">						
			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
			 Groups List</h3>
			<a  class="btn list-btn btn-warning2" href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/add.htm'/>">
								<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
			
			 Add Group </a>
			
		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="50%"><div class="td-label">Name</div></td>
				</tr>
				<c:forEach items="${model.groups}" var="group">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${group.name}" />
							</div></td>
						<td><a class="btn btn-success" 
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${group.id}.htm'/>">
									View </a> 
									<a class="btn btn-danger"
								href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${group.id}/delete.htm'/>">
									Delete </a>
							
							</td>

					</tr>
				</c:forEach>


			</table>
		</div>
	</div>


</body>

</html>
