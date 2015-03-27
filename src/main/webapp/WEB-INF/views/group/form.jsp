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
	<div align="center">
		<h3 class="panel-title list">	<fmt:message key="error.errors" />: </h3>	
			<br/>
			<c:forEach items="${errors}" var="error">
				<c:out  value="${error}" /><br/>
			</c:forEach>
	</div>
	</c:if>
</body>
</html>
