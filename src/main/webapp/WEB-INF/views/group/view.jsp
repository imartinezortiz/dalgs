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
				Group Details
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->
			<sec:accesscontrollist hasPermission="ADMINISTRATION"
				domainObject="${model.group}">

				<a class="btn list-btn btn-warning"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/modify.htm'/>">
					<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
					Edit
				</a>
			</sec:accesscontrollist>
			<c:choose>
				<c:when test="${model.showAll eq true}">
					<a
						href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}.htm?showAll=false'>
    						</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_open_view.png" /> "
						style="float: right; margin-right: 1%; margin-top: -0.5%;">
					</a>
				</c:when>
				<c:otherwise>
					<a
						href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}.htm?showAll=true'> 
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
					<p>
						<label>Name: &nbsp; </label>${model.group.name}</p>

					<p>
						<label>Course Coordinator: &nbsp;</label>${model.group.course.coordinator}</p>

				</div>
			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>

				Professor List
			</h3>

			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/professor/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					Add
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">LastName</div></td>
					<td width="50%"><div class="td-label">FirstName</div></td>
				</tr>
				<c:forEach items="${model.group.professors}" var="prof">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${prof.lastName}" />
							</div></td>
						<td><div class="td-content">
								<c:out value="${prof.firstName}" />
							</div></td>


						<td><c:choose>
								<c:when test="${prof.enabled eq true}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/user/${prof.id}.htm'/>">View</a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${prof.id}/delete.htm'/>">
											Delete </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${prof.id}/restore.htm'/>">
											Restore </a>
									</sec:authorize>
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
				Student List
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/student/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					Add
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">LastName</div></td>
					<td width="50%"><div class="td-label">FirstName</div></td>
				</tr>
				<c:forEach items="${model.group.students}" var="student">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${student.lastName}" />
							</div></td>
						<td><div class="td-content">
								<c:out value="${student.firstName}" />
							</div></td>


						<td><c:choose>
								<c:when test="${student.enabled eq true}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/user/${student.id}.htm'/>">View</a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${student.id}/delete.htm'/>">
											Delete </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${student.id}/restore.htm'/>">
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
