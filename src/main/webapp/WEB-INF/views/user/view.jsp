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

<html>
<head>
<title>User</title>
</head>

<body>
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>

				<fmt:message key="user.details" />
			</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/user/${model.userDetails.id}/modify.htm'/>">
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span><fmt:message key="common.modify" />
			</a>

		</div>

		<div class="panel-body">
			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="access.username" />: </label>
					<p class="details">${model.userDetails.username}</p>
					<br>
					<label><fmt:message key="user.firstN" />: </label>
					<p class="details">${model.userDetails.firstName}</p>
					<br>
					<label><fmt:message key="user.lastN" />: </label>
					<p class="details">${model.userDetails.lastName}</p>
					<br>
					<label><fmt:message key="user.email" />: </label>
					<p class="details">${model.userDetails.email}</p>
				</div>
			</div>
		</div>
	</div>

	<c:if test="${model.groups !=null}">
		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list">
					<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
					<fmt:message key="group.list" />
				</h3>

			</div>
			<div class="panel-body">

				<table class="table table-striped table-bordered">
					<tr align="center">
						<td><div class="td-label"><fmt:message key="subject.sub" /></div></td>
						<td><div class="td-label"><fmt:message key="academicterm.at" /></div></td>
						<td><div class="td-label"><fmt:message key="group.gr" /></div></td>

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
							<td>
								<div class="td-content">
									<c:out value="${group.name}" />
								</div>
							</td>
							<td><a class="btn btn-success"
								href="<c:url value='/academicTerm/${group.course.academicTerm.id}/course/${group.course.id}/group/${group.id }.htm'/>">
									<fmt:message key="common.view" /> </a></td>

						</tr>
					</c:forEach>


				</table>
			</div>
		</div>
	</c:if>
	<c:if test="${model.courses!=null}">
		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list">
					<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
					<fmt:message key="course.list" />
				</h3>

			</div>
			<div class="panel-body">

				<table class="table table-striped table-bordered">
					<tr align="center">
						<td><div class="td-label"><fmt:message key="subject.sub" /></div></td>
						<td><div class="td-label"><fmt:message key="academicterm.at" /></div></td>

					</tr>
					<c:forEach items="${model.courses}" var="course">
						<tr align="center">
							<td><div class="td-content">
									<c:out value="${course.subject.info.name}" />
								</div></td>
							<td>
								<div class="td-content">
									<c:out value="${course.academicTerm.term}" />
								</div>
							</td>
							
							<td><a class="btn btn-success"
								href="<c:url value='/academicTerm/${course.academicTerm.id}/course/${course.id}.htm'/>">
									<fmt:message key="common.view" /> </a></td>

						</tr>
					</c:forEach>


				</table>
			</div>
		</div>
	</c:if>
</body>
</html>