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


	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
				<fmt:message key="${typeform}"/> <fmt:message key="learninggoal.lg" />
			</h3>
		</div>
		<spring:message code="learninggoal.code" var="lgCode"/>
		<spring:message code="learninggoal.name" var="lgName"/>
		<spring:message code="learninggoal.desc" var="lgDesc"/>
		
		<div class="panel-body">
			<form:form method="post" commandName="learningGoal" role="form">
				<form:hidden path="id"/>
				<div class="form-group">
					<label><fmt:message key="input.code" />: </label>
					<form:input path="info.code" class="form-control"
						placeholder="${lgCode}" required="true" />
					<form:errors path="info.code" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label><fmt:message key="input.name" />: </label>
					<form:input path="info.name" class="form-control"
						placeholder="${lgName}" required="true" />
					<form:errors path="info.name" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label><fmt:message key="input.desc" />: </label>
					<form:input path="info.description" class="form-control"
						placeholder="${lgDesc}" required="true" />
					<form:errors path="info.description" cssStyle="color: #ff0000" />

				</div>
				
				<spring:message code="common.undelete" var="undelete"/>

				<input type="submit" class="btn btn-success" value="<fmt:message key="${typeform}"/>" name="${valueButton}" />
				<c:if test="${unDelete == true}">
					<input type="submit" class="btn btn-success" value="${undelete}"
						name="${undelete}" />
				</c:if>
			</form:form>
		</div>
	</div>
	<c:if test="${not empty errors}">
		<div align="center">
			<h3 class="panel-title list"><fmt:message key="error.errors" />:</h3>
			<br />
			<c:forEach items="${errors}" var="error">
				<c:out value="${error}" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</body>
</html>
