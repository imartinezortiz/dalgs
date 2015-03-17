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
			<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
			<h3 class="panel-title list">
			Subject Details</h3>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
			<a class="btn list-btn btn-warning"
				href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/${topicId}/modify.htm'/>">				
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
				Edit</a>
			</sec:authorize>
				
			<c:choose>
				<c:when test="${model.showAll eq true}">
					<a
						href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/${topicId}.htm?showAll=false'>
    						</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_open_view.png" /> "
						style="float: right; margin-right: 1%; margin-top: -0.5%;">
					</a>
				</c:when>
				<c:otherwise>
					<a
						href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/${topicId}.htm?showAll=true'> 
    							</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_close_view.png" /> "
						style="float: right; margin-right: 1%;">
					</a>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label>Code: </label> 
					<p class="details">${model.topic.info.code}</p>
				</div>
				<div class="form-group view">
					<label>Name: </label> 
					<p class="details">${model.topic.info.name}</p>
				</div>
				
				<div class="form-group view">
					<label>Description: </label> 
					<p class="details">${model.topic.info.description}</p>
				</div>
			
				<div class="form-group view">
					<label>Degree: </label> 
					<p class="details">${model.topic.module.info.name}</p>
				</div>
			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>

				Subject List
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<a style="cursor:copy;" class="btn list-btn btn-warning2"
				href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/${topicId}/subject/add.htm'/>"> <span
				class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
				Add 
			</a></sec:authorize>

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
								<c:out value="${subject.info.name}" />
							</div></td>
						<td><div class="td-content">
								<c:out value="${subject.info.description}" />
							</div></td>

						<td><a class="btn list-btn btn-success"
							href="<c:url value='/degree/${degreeId}/module/${subject.topic.module.id}/topic/${subject.topic.id}/subject/${subject.id}.htm'/>">View</a>
							<sec:authorize access="hasRole('ROLE_ADMIN')">
							<a class="btn btn-danger"
							href="<c:url value='/degree/${degreeId}/module/${subject.topic.module.id}/topic/${subject.topic.id}/subject/${subject.id}/delete.htm'/>">
								Delete
						</a></sec:authorize></td>

					</tr>
				</c:forEach>


			</table>
		</div>
	</div>

</body>

</html>
