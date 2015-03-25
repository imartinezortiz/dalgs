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
				<fmt:message key="detailsGr" />
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn list-btn btn-warning"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/modify.htm'/>">
					<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
					<fmt:message key="modify" />
				</a>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_PROFESSOR')"><c:choose>
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
			</c:choose></sec:authorize>
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<p>
						<label><fmt:message key="name" />: &nbsp; </label>${model.group.name} &nbsp; (${model.group.course.subject.info.name} - ${model.group.course.academicTerm.term})</p>

					<p>
						<label><fmt:message key="courCoor" />: &nbsp;</label>${model.group.course.coordinator.lastName}, ${model.group.course.coordinator.firstName}</p>

				</div>
			</div>

		</div>
	</div>



	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="acList" /> - <fmt:message key="cour" />
			</h3>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="name" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="desc" /></div></td>
				</tr>
				<c:forEach items="${model.activitiesCourse}" var="activity">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${activity.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${activity.info.description}" />
							</div>
						</td>

						<td><c:if test="${activity.isDeleted eq false}">
								<a class="btn btn-success"
									href="<c:url value='/academicTerm/${academicId}/course/${courseId}/activity/${activity.id}.htm'/>">
									<fmt:message key="view" /> </a>
							</c:if></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="acList" /> - <fmt:message key="gr" />
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->
			<sec:accesscontrollist hasPermission="ADMINISTRATION" domainObject="${model.group}">


				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/activity/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="add" />
				</a>
			</sec:accesscontrollist>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="name" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="desc" /></div></td>
				</tr>
				<c:forEach items="${model.activitiesGroup}" var="activity">
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
										href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/activity/${activity.id}.htm'/>">
										<fmt:message key="view" /> </a>
									<sec:accesscontrollist hasPermission="ADMINISTRATION"
										domainObject="${model.group}">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/activity/${activity.id}/delete.htm'/>">
											<fmt:message key="delete" /> </a>
									</sec:accesscontrollist>
								</c:when>
								<c:otherwise>
									<sec:accesscontrollist hasPermission="ADMINISTRATION"
										domainObject="${model.group}">
										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/activity/${activity.id}/restore.htm'/>">
											<fmt:message key="restore" /> </a>
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
<fmt:message key="profList" />
			</h3>

			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/professor/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="change" />
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="lastN" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="firstN" /></div></td>
				</tr>
				<c:forEach items="${model.group.professors}" var="prof">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${prof.lastName}" />
							</div></td>
						<td><div class="td-content">
								<c:out value="${prof.firstName}" />
							</div></td>


				<%-- 		<td>
						<a class="btn list-btn btn-success"
										href="<c:url value='/user/${prof.id}.htm'/>">View </a>
						</td> --%>
						<td>
								<c:if test="${prof.enabled eq true}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/user/${prof.id}.htm'/>"><fmt:message key="view" /></a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${prof.id}/delete.htm'/>">
											<fmt:message key="delete" /> </a>
									</sec:authorize>
								</c:if>
								
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
				<fmt:message key="studList" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/student/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="change" />
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="lastN" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="firstN" /></div></td>
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
										href="<c:url value='/user/${student.id}.htm'/>"><fmt:message key="view" /></a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${student.id}/delete.htm'/>">
											<fmt:message key="delete" /> </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${student.id}/restore.htm'/>">
											<fmt:message key="restore" /> </a>
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
