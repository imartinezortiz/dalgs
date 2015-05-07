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


	<c:choose>
		<c:when test="${model.typeOfUser eq 'professor'}">


			<div class="panel panel-primary group">
				<div class="panel-heading">
					<h3 class="panel-title list">
						<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
						<fmt:message key="group.profList" />
					</h3>

					<sec:accesscontrollist hasPermission="ADMINISTRATION"
						domainObject="${model.group}">
						<a class="btn btn-cvs 2" style="float: right; margin-top: -10px;"
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/ROLE_PROFESSOR/upload.htm'/>">
							<span class="glyphicon glyphicon-upload" aria-hidden="true"></span>
							CSV
						</a>


					</sec:accesscontrollist>
				</div>
				<div class="panel-body">

					<table class="table table-striped table-bordered">
						<tr align="center">
							<td width="20%"><div class="td-label">
									<fmt:message key="user.lastN" />
								</div></td>
							<td width="50%"><div class="td-label">
									<fmt:message key="user.firstN" />
								</div></td>
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
								<td><c:if test="${prof.enabled eq true}">
										<a class="btn list-btn btn-success"
											href="<c:url value='/user/${prof.id}.htm'/>"><fmt:message
												key="common.view" />
												</a>
										<sec:accesscontrollist hasPermission="ADMINISTRATION"
											domainObject="${model.group}">

											<a class="btn btn-danger"
												href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${prof.id}/delete.htm'/>">
												<fmt:message key="common.delete" />
											</a>
										</sec:accesscontrollist>
									</c:if></td>

							</tr>
						</c:forEach>


					</table>
				</div>
			</div>
		</c:when>
		<c:otherwise>

			<div class="panel panel-primary group">
				<div class="panel-heading">
					<h3 class="panel-title list">
						<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
						<fmt:message key="group.studList" />
					</h3>
					<sec:accesscontrollist hasPermission="ADMINISTRATION"
						domainObject="${model.group}">

						<a class="btn btn-cvs 2" style="float: right; margin-top: -10px;"
							href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/ROLE_STUDENT/upload.htm'/>">
							<span class="glyphicon glyphicon-upload" aria-hidden="true"></span>
							CSV
						</a>

					</sec:accesscontrollist>

				</div>
				<div class="panel-body">

					<table class="table table-striped table-bordered">
						<tr align="center">
							<td width="20%"><div class="td-label">
									<fmt:message key="user.lastN" />
								</div></td>
							<td width="50%"><div class="td-label">
									<fmt:message key="user.firstN" />
								</div></td>
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
												href="<c:url value='/user/${student.id}.htm'/>"><fmt:message
													key="common.view" /></a>
											<sec:accesscontrollist hasPermission="ADMINISTRATION"
												domainObject="${model.group}">

												<a class="btn btn-danger"
													href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/user/${student.id}/delete.htm'/>">
													<fmt:message key="common.delete" />
												</a>
											</sec:accesscontrollist>
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose></td>

							</tr>
						</c:forEach>
					</table>
				</div>


			</div>
		</c:otherwise>
	</c:choose>
</body>

</html>
