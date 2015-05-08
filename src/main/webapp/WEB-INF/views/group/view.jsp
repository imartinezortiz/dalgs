<%--

    This file is part of D.A.L.G.S.

    D.A.L.G.S is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    D.A.L.G.S is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><fmt:message key="common.title" /></title>
<style>
.error {
	color: red;
}
</style>

</head>
<body>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
				<fmt:message key="group.details" />
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn list-btn btn-warning"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/modify.htm'/>">
					<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
					<fmt:message key="common.modify" />
				</a>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_PROFESSOR')">
				<c:choose>
					<c:when test="${model.showAll eq true}">
						<a
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}.htm?showAll=false'>
    						</c:url>">
							<img
							src="<c:url value="/resources/images/theme/trash_open_view.png" /> "
							style="float: right; margin-right: 1%; margin-top: -10px;">
						</a>
					</c:when>
					<c:otherwise>
						<a
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}.htm?showAll=true'> 
    							</c:url>">
							<img
							src="<c:url value="/resources/images/theme/trash_close_view.png" /> "
							style="float: right; margin-right: 1%; margin-top:-2px;">
						</a>
					</c:otherwise>
				</c:choose>
			</sec:authorize>
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<p>
						<label><fmt:message key="group.name" />: &nbsp; </label>${model.group.name}
						&nbsp; (${model.group.course.subject.info.name} -
						${model.group.course.academicTerm.term})
					</p>

					<p>
						<label><fmt:message key="course.courCoor" />: &nbsp;</label>${model.group.course.coordinator.lastName},
						${model.group.course.coordinator.firstName}
					</p>

				</div>
			</div>

		</div>
	</div>



	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="activity.list" />
				-
				<fmt:message key="course.cour" />
			</h3>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">
							<fmt:message key="input.name" />
						</div></td>
					<td width="50%"><div class="td-label">
							<fmt:message key="input.desc" />
						</div></td>
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
									<fmt:message key="common.view" />
								</a>
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
				<fmt:message key="activity.list" />
				-
				<fmt:message key="group.gr" />
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->
			<sec:accesscontrollist hasPermission="ADMINISTRATION"
				domainObject="${model.group}">


				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/activity/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="common.add" />
				</a>
			</sec:accesscontrollist>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">
							<fmt:message key="input.name" />
						</div></td>
					<td width="50%"><div class="td-label">
							<fmt:message key="input.desc" />
						</div></td>
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
										<fmt:message key="common.view" />
									</a>
									<sec:accesscontrollist hasPermission="ADMINISTRATION"
										domainObject="${model.group}">

										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/activity/${activity.id}/delete.htm'/>">
											<fmt:message key="common.delete" />
										</a>
									</sec:accesscontrollist>
								</c:when>
								<c:otherwise>
									<sec:accesscontrollist hasPermission="ADMINISTRATION"
										domainObject="${model.group}">
										<a class="btn btn-danger"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/activity/${activity.id}/restore.htm'/>">
											<fmt:message key="common.restore" />
										</a>
									</sec:accesscontrollist>
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<sec:accesscontrollist hasPermission="ADMINISTRATION"
		domainObject="${model.group}">
		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list">
					<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
					<fmt:message key="externalActivity.list" />
					-
					<fmt:message key="group.gr" />
				</h3>

				<!-- If you are a professor who belongs to this course you can edit -->

			</div>
			<div class="panel-body">

				<table class="table table-striped table-bordered">
					<tr align="center">
						<td width="20%"><div class="td-label">
								<fmt:message key="input.name" />
							</div></td>
						<td width="50%"><div class="td-label">
								<fmt:message key="input.desc" />
							</div></td>
					</tr>
					<c:forEach items="${model.externalActivities}"
						var="externalActivity">
						<tr align="center">
							<td><div class="td-content">
									<c:out value="${externalActivity.info.name}" />
								</div></td>
							<td>
								<div class="td-content">
									<c:out value="${externalActivity.info.description}" />
								</div>
							</td>
							<td><c:choose>
									<c:when test="${externalActivity.isDeleted eq false}">
										<a class="btn btn-success"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/externalactivity/${externalActivity.id}.htm'/>">
											<fmt:message key="common.view" />
										</a>
										<a class="btn btn-success"
											href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/externalactivity/${externalActivity.id}/move.htm'/>">
											<fmt:message key="common.move" />
										</a>
										<sec:accesscontrollist hasPermission="ADMINISTRATION"
											domainObject="${model.group}">

											<a class="btn btn-danger"
												href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/externalactivity/${externalActivity.id}/delete.htm'/>">
												<fmt:message key="common.delete" />
											</a>
										</sec:accesscontrollist>
									</c:when>
									<c:otherwise>
										<sec:accesscontrollist hasPermission="ADMINISTRATION"
											domainObject="${model.group}">
											<a class="btn btn-danger"
												href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/externalactivity/${externalActivity.id}/restore.htm'/>">
												<fmt:message key="common.restore" />
											</a>
										</sec:accesscontrollist>
									</c:otherwise>
								</c:choose></td>


						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		</sec:accesscontrollist>
	
	
	
	<table id="groupUser" class="panel panel-primary group">
	<tr>
	<td>
	<a 
		href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/professor/view.htm'/>">
		<div class="panel panel-primary group" id="classListUser">
			<div class="panel-heading">
				<h3 class="panel-title list">
					<span class="glyphicon glyphicon-user" aria-hidden="true">&nbsp;</span>
					<fmt:message key="group.profList" />
				</h3>

			</div>

		</div>
	</a>
	</td><td>
	<a
		href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/student/view.htm'/>">
		<div class="panel panel-primary group" id="classListUser">
			<div class="panel-heading">
				<h3 class="panel-title list">
					<span class="glyphicon glyphicon-user" aria-hidden="true">&nbsp;</span>
					<fmt:message key="group.studList" />
				</h3>
			</div>
		</div>
	</a></td>
	</tr>
</table>

</body>

</html>