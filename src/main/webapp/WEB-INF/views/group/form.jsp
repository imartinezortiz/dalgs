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
			<h3 class="panel-title list">							
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
			<fmt:message key="${typeform}"/> <fmt:message key="group.gr" /></h3>
		</div>
		<div class="panel-body">
			<form:form method="post" commandName="group" role="form">
				<div>
				<form:hidden path="id"/>
				</div>
				<spring:message code="group.name" var="groupName"/>
				<div class="form-group">
					<label><fmt:message key="input.name" />: </label>
					<form:input path="name" class="form-control"
						placeholder="${groupName}" required="true" />
					<form:errors path="name" cssStyle="color: #ff0000" />
				</div>
				

				
				<input type="submit" class="btn btn-success" value="<fmt:message key="${typeform}"/>" name="${valueButton}" />
				<c:if test="${unDelete == true}">
				<spring:message code="common.undelete" var="undelete"/>
					<input type="submit" class="btn btn-success" value="${undelete}" name="Undelete"/>
				</c:if>
			</form:form>
		</div>
	</div>
	
	<c:if test="${not empty errors}">
	<div align="center" class="error">
		<h3 class="panel-title list">	<fmt:message key="common.errors" />: </h3>	
			<br/>
			<c:forEach items="${errors}" var="error">
				<c:out  value="${error}" /><br/>
			</c:forEach>
	</div>
	</c:if>
</body>
</html>
