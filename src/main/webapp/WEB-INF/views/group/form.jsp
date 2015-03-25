<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><fmt:message key="title" /></title>
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
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
			<fmt:message key="${typeform}"/> <fmt:message key="gr" /></h3>
		</div>
		<div class="panel-body">
			<form:form method="post" commandName="group" role="form">
				<div>
				<form:hidden path="id"/>
				</div>
				<spring:message code="groupName" var="prev"/>
				<div class="form-group">
					<label><fmt:message key="name" />: </label>
					<form:input path="name" class="form-control"
						placeholder="${groupName}" required="true" />
					<form:errors path="name" cssStyle="color: #ff0000" />
				</div>
				

				
				<input type="submit" class="btn btn-success" value="${valueButton}" name="${valueButton}" />
				<c:if test="${unDelete == true}">
				<spring:message code="undelete" var="undelete"/>
					<input type="submit" class="btn btn-success" value="${undelete}" name="Undelete"/>
				</c:if>
			</form:form>
		</div>
	</div>
	
	<c:if test="${not empty errors}">
	<div align="center">
		<h3 class="panel-title list">	<fmt:message key="errors" />: </h3>	
			<br/>
			<c:forEach items="${errors}" var="error">
				<c:out  value="${error}" /><br/>
			</c:forEach>
	</div>
	</c:if>
</body>
</html>
