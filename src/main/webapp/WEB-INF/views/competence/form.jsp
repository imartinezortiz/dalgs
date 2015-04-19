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

	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
				<fmt:message key="${typeform}"/>
				<fmt:message key="competence.com" />

			</h3>
		</div>
		<div class="panel-body">
			<spring:message code="competence.code" var="comCode" />
			<spring:message code="competence.name" var="comName" />
			<spring:message code="competence.desc" var="comDesc" />
			<spring:message code="competence.type" var="comType" />
			<form:form method="post" commandName="competence" role="form">
				<div>
					<form:hidden path="id" />
				</div>
				<div class="form-group">
					<label><fmt:message key="input.code" />: </label>
					<form:input path="info.code" class="form-control"
						placeholder="${comCode}" required="true" />
					<form:errors path="info.code" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label><fmt:message key="input.name" />: </label>
					<form:input path="info.name" class="form-control"
						placeholder="${comName}" required="true" />
					<form:errors path="info.name" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label><fmt:message key="input.desc" />: </label>
					<form:input path="info.description" class="form-control"
						placeholder="${comDesc}" required="true" />
					<form:errors path="info.description" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label><fmt:message key="input.type" />: </label>
					<form:input path="info.type" class="form-control"
						placeholder="${comType}" required="true" />
					<form:errors path="info.type" cssStyle="color: #ff0000" />

				</div>

				<spring:message code="common.undelete" var="undelete" />


				<input type="submit" class="btn btn-success" value="<fmt:message key="${typeform}"/>"
					name="${valueButton}" />
				<c:if test="${unDelete == true}">
					<input type="submit" class="btn btn-success" value="${undelete}"
						name="Undelete" />
				</c:if>
			</form:form>
		</div>
	</div>
	<c:if test="${not empty errors}">
		<div align="center">
			<h3 class="panel-title list">
				<fmt:message key="common.errors" />
				:
			</h3>
			<br />
			<c:forEach items="${errors}" var="error">
				<c:out value="${error}" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</body>
</html>
