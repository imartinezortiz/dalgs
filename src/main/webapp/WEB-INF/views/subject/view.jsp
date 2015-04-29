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
			<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
			<h3 class="panel-title list">Subject Details</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn list-btn btn-warning"
					href="<c:url value='/degree/${degreeId}/module/${model.subject.topic.module.id}/topic/${model.subject.topic.id}/subject/${subjectId}/modify.htm'/>">
					<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
					<fmt:message key="common.modify" />
				</a>
			</sec:authorize>
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="input.code" />: </label>
					<p class="details">${model.subject.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.name" />: </label>
					<p class="details">${model.subject.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.desc" />: </label>
					<p class="details">${model.subject.info.description}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="topic.top" />: </label>
					<p class="details">${model.subject.topic.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="subject.credits" />: </label>
					<p class="details">${model.subject.info.credits}</p>
				</div>
				<div class="form-group view">
					<a  class="btn doc" href='//${model.subject.info.url_doc}' >
					<label style="cursor: pointer;"><fmt:message key="subject.url" /> </label></a>
					
				</div>
			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="competence.list" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/degree/${degreeId}/module/${model.subject.topic.module.id}/topic/${model.subject.topic.id}/subject/${subjectId}/addCompetences.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="common.add" />
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="input.name" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="input.desc" /></div></td>

				</tr>
				<c:forEach items="${model.competences}" var="competence">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${competence.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${competence.info.description}" />
							</div>
						</td>

						<td><c:choose>
								<c:when test="${competence.isDeleted eq false}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/degree/${degreeId}/competence/${competence.id}.htm'/>"><fmt:message key="common.view" /></a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/module/${model.topic.module.id}/topic/${model.topic.id}/subject/${subjectId}/competence/${competence.id}/delete.htm'/>">
											<fmt:message key="common.delete" /> </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/module/${model.topic.module.id}/topic/${model.topic.id}/subject/${subjectId}/competence/${competence.id}/restore.htm'/>">
											<fmt:message key="common.restore" /> </a>
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
