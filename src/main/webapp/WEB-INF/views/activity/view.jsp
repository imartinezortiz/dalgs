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
				<fmt:message key="detailsAC" />
			</h3>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_PROFESSOR')">
				<a class="btn list-btn btn-warning"
					href="<c:url value='${activityId}/modify.htm'/>"> <span
					class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>Edit
				</a>
			</sec:authorize>

		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="code" /> : </label>
					<p class="details">${model.activity.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="name" /> :</label>
					<p class="details">${model.activity.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="desc" /> :</label>
					<p class="details">${model.activity.info.description}</p>
				</div>


			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="lgsList" />
			</h3>
		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">
							<fmt:message key="lg" />
						</div></td>
					<td width="50%"><div class="td-label">
							<fmt:message key="weight" />
						</div></td>

				</tr>
				<c:forEach items="${model.learningStatus}" var="ls">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${ls.learningGoal.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${ls.weight}" />
							</div>
						</td>


					</tr>
				</c:forEach>


			</table>
		</div>

	</div>


</body>

</html>
