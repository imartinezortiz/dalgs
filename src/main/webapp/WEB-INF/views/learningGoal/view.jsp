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
			<h3 class="panel-title list">
			<fmt:message key="learninggoal.details" /></h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<a class="btn list-btn btn-warning"
				href="<c:url value='/degree/${degreeId}/competence/${competenceId}/learninggoal/${model.learningGoal.id}/modify.htm'/>">				
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
				<fmt:message key="common.modify" /></a>
			</sec:authorize>
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="input.code" />: </label> 
					<p class="details">${model.learningGoal.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.name" />: </label> 
					<p class="details">${model.learningGoal.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.desc" />: </label> 
					<p class="details">${model.learningGoal.info.description}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="topic.top" />: </label> 
					<p class="details">${model.learningGoal.competence.info.name}</p>
				</div>
			</div>

		</div>
	</div>

	
</body>

</html>
