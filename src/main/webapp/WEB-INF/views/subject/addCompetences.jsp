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


</head>
<body>

	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<h3 class="panel-title list"><fmt:message key="competence.add" /></h3>
		</div>
		<div class="panel-body">

			<form:form method="post" commandName="subject">
				
				<div class="form-group">
					<label><fmt:message key="input.code" />: </label>
					<form:input path="info.code" class="form-control" readonly="true" />
				</div>
				<div class="form-group">
					<label><fmt:message key="input.name" />: </label>
					<form:input path="info.name" class="form-control" id="name" readonly="true" />

				</div>
				<div class="form-group">
					<label><fmt:message key="input.desc" />: </label>
					<form:input class="form-control" path="info.description"
						id="description" readonly="true"/>
				</div>
				
				<div class="form-group view">
					<label><fmt:message key="subject.credits" />: </label>
						<form:input class="form-control" path="info.credits"
						id="credits" readonly="true"/>
				</div>
				<div class="form-group view">				
					<label style="cursor: pointer;"><fmt:message key="subject.url" /> </label>
						<form:input class="form-control" path="info.url_doc"
						id="url_doc" readonly="true"/>
					
				</div>

				<div class="form-group">
					<label><fmt:message key="subject.availableCom" />:</label>
					<div class="checkbox">
						<form:checkboxes items="${competences}" path="competences"
							itemLabel="info.name" />
						<br> <br>
						<br>
			
					</div>
				</div>
				<spring:message code="common.add" var="add"/>
				<input type="submit" class="btn btn-success" value="${add}" />

			</form:form>
		</div>
	</div>
</body>
</html>
