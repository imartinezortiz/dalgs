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

				User Details
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a class="btn list-btn btn-warning"
					href="<c:url value='/user/${model.user.id}/modify.htm'/>"> <span
					class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>Edit
				</a>
			</sec:authorize>

		</div>

		<div class="form-group">
			<div class="form-group view">
				<div class="panel-body">
					<label>UserName: </label>
					<p class="details">${model.user.username}</p>
					<br>
					<br> <label>Student </label>
					<p class="details">${model.user.firstName}
						&nbsp;${model.user.lastName}</p>
					<br>
					<label>Email: </label>
					<p class="details">${model.user.email}</p>
				</div>
			</div>
		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>

			<h3 class="panel-title list">Course List</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a class="btn list-btn btn-warning2" style="margin-top: 5px;"
					href="<c:url value='/user/${model.user.id}/group/add.htm'/>"><span
					class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					ADD GROUP
				</a>
			</sec:authorize>
		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">Subject</div></td>
					<td width="50%"><div class="td-label">Academic Term</div></td>
				</tr>
				<c:forEach items="${model.groups}" var="group">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${group.course.subject.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${group.course.academicTerm.term}" />
							</div>
						</td>

						<td><a class="btn btn-success"
							href="<c:url value='/academicTerm/${group.course.academicTerm.id}/course/${group.course.id}/group/${group.id }.htm'/>">
								View </a> <sec:authorize access="hasRole('ROLE_ADMIN')">
								<a class="btn btn-danger"
									href="<c:url 
									value='/academicTerm/${group.course.academicTerm.id}/course/${group.course.id}/group/${group.id }/user/${model.userDetails.id}delete.htm'/>">
									Delete </a>
							</sec:authorize></td>

					</tr>
				</c:forEach>



			</table>
		</div>
	</div>


</body>

</html>
