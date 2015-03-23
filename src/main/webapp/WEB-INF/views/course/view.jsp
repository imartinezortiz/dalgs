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

				Course Details
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->

			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn list-btn btn-warning"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/modify.htm'/>">
					<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>Edit
				</a>
			</sec:authorize>
			<c:choose>
				<c:when test="${model.showAll eq true}">
					<a
						href="<c:url value='/academicTerm/${academicId}/course/${courseId}.htm?showAll=false'>
    						</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_open_view.png" /> "
						style="float: right; margin-right: 1%; margin-top: -0.5%;">
					</a>
				</c:when>
				<c:otherwise>
					<a
						href="<c:url value='/academicTerm/${academicId}/course/${courseId}.htm?showAll=true'> 
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
					<p class="details">
						<label>Academic Term: </label> ${model.course.academicTerm.term}
					</p>

					<p>
						<label>Course Coordinator: &nbsp;</label>${model.course.coordinator.lastName},
						${model.course.coordinator.firstName}
					</p>

				</div>


			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>

			<h3 class="panel-title list">Subject</h3>
		</div>
		<div class="panel-body">
			<div class="form-group view">
				<p class="details"><label>Subject: </label>
				${model.course.subject.info.code}-
					${model.course.subject.info.name}</p>
			</div>
		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				Activity List
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->
			<sec:accesscontrollist hasPermission="ADMINISTRATION"
				domainObject="${model.course}">


				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>

					Add
				</a>
			</sec:accesscontrollist>

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

						<td><c:choose>
								<c:when test="${activity.isDeleted eq false}">
									<a class="btn btn-success"
										href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}.htm'/>">
										View </a>
									<sec:accesscontrollist hasPermission="ADMINISTRATION"
										domainObject="${model.course}">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}/delete.htm'/>">
											Delete </a>
									</sec:accesscontrollist>
								</c:when>
								<c:otherwise>
									<sec:accesscontrollist hasPermission="ADMINISTRATION"
										domainObject="${model.course}">
										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}/restore.htm'/>">
											Restore </a>
									</sec:accesscontrollist>
								</c:otherwise>
							</c:choose></td>

					</tr>
				</c:forEach>


			</table>
		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				Groups List
			</h3>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>

					Add
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td> </td>
					<td><div class="td-label">Name</div></td>
				</tr>
				<c:forEach items="${model.groups}" var="group">
					<tr align="center">
					<td> <sec:authorize access="hasRole('ROLE_ADMIN')">
					<a	href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${group.id}/clone.htm'/>"
								class="btn btn-clone">Clone</a> </sec:authorize></td>
						<td><div class="td-content">
								<c:out value="${group.name}" />
							</div></td>

						<td><c:choose>
								<c:when test="${group.isDeleted eq false}">
									<a class="btn btn-success"
										href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${group.id}.htm'/>">
										View </a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${group.id}/delete.htm'/>">
											Delete </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${group.id}/restore.htm'/>">
											Restore </a>
									</sec:authorize>
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>


			</table>
		</div>
	</div>


</body>

</html>
