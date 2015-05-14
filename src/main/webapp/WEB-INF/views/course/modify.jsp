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


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<h3 class="panel-title list">				
			<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
			<fmt:message key="course.mod" /></h3>
		</div>
		<div class="panel-body">


			<form:form method="post" commandName="modifyCourse" role="form">
				<div>
				<form:hidden path="id"/>
				</div>
				
				
				
				<div class="form-group">
					<label><fmt:message key="subject.sub" />: ${idSubject}</label>
					<form:select class="form-control 2" path="subject" >
						<form:option value="">-- <fmt:message key="common.selectOp" /> --</form:option>
						<c:forEach items="${subjects}" var="subject">
							<c:choose>
								<c:when test="${subject.id == idSubject}">
									<form:option value="${subject.id}" selected='true'>${subject.info.code} - ${subject.info.name}</form:option>
								</c:when>
								<c:otherwise>
									<form:option value="${subject.id}">${subject.info.code} - ${subject.info.name} </form:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
					<form:errors path="subject" cssStyle="color: #ff0000" />
				</div>

				<div class="form-group">
					<label><fmt:message key="course.coordinator" />:</label>
					<form:select class="form-control 2" path="coordinator">
						<form:option value="">-- <fmt:message key="common.selectOp" /> --</form:option>
						<c:forEach items="${professors}" var="prof">
							<c:choose>
								<c:when test="${prof.id == idCoordinator}">
									<form:option value="${prof.id}" selected='true'>${prof.lastName}, ${prof.firstName}</form:option>
								</c:when>
								<c:otherwise>
									<form:option value="${prof.id}">${prof.lastName}, ${prof.firstName}</form:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
					<form:errors path="coordinator" cssStyle="color: #ff0000" />
				</div>
				<spring:message code="common.modify" var="modify"/>
				<input type="submit" class="btn btn-success" value="${modify}" >

			</form:form>
		</div>
	</div>
	<c:if test="${not empty errors}">
	<div align="center" class="error">
		<h3 class="panel-title list"><fmt:message key="common.errors" />: </h3>	
			<br/>
			<c:forEach items="${errors}" var="error">
				<c:out  value="${error}" /><br/>
			</c:forEach>
	</div>
	</c:if>
</body>
</html>
