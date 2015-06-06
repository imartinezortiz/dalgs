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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
				<fmt:message key="activity.details" />
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
					<label><fmt:message key="input.code" /> : </label>
					<p class="details">${model.activity.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.name" /> :</label>
					<p class="details">${model.activity.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.desc" /> :</label>
					<p class="details">${model.activity.info.description}</p>
				</div>
			<%-- 	<div class="form-group view">
					<label><fmt:message key="activity.doc" />: </label>
					<p class="details">${model.activity.info.url}</p>
					<a href="/dalgs/WEB-INF/Enunciado.docx"> Doc
	</a>
				</div>
 --%>
			</div>

		</div>
	</div>
	
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="attachment.list" />
			</h3>
		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
							<td><div class="td-label"><fmt:message key="common.file" /></div></td>
							<td><div class="td-label"><fmt:message key="input.desc" /></div></td>
					

				</tr>
				<c:forEach items="${model.activity.attachments}" var="att" varStatus="attachment">		
					<tr align="center">
							<td><div class="td-content">${att.name}</div></td>
						<td><div class="td-content">${att.description}</div></td>
						<td><a class="glyphicon glyphicon-file" href="<c:url value='${att.file}'/>"></a></td>
						
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="learninggoalstatus.list" />
			</h3>
		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">
							<fmt:message key="learninggoal.lg" />
						</div></td>
					<td width="50%"><div class="td-label">
							<fmt:message key="input.weight" />
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
