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
				<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
				"${valueButton}" <fmt:message key="sub" />
			</h3>
		</div>
		<div class="panel-body">
			<form:form method="post" commandName="subject" role="form">
				<form:hidden path="id"/>
				
				<spring:message code="codSub" var="codSub"/>
				<spring:message code="nameSub" var="nameSub"/>
				<spring:message code="descSub" var="descSub"/>

				
				<div class="form-group">
					<label><fmt:message key="code" />: </label>
					<form:input path="info.code" class="form-control"
						placeholder="${codSub}" required="true" />
					<form:errors path="info.code" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label><fmt:message key="name" />: </label>
					<form:input path="info.name" class="form-control"
						placeholder="${nameSub}" required="true" />
					<form:errors path="info.name" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label><fmt:message key="desc" />: </label>
					<form:input path="info.description" class="form-control"
						placeholder="${descSub}" required="true" />
					<form:errors path="info.description" cssStyle="color: #ff0000" />

				</div>


				<input type="submit" class="btn btn-success" value="${valueButton}"
					name="${valueButton}" />

				<spring:message code="undelete" var="undelete"/>
				<c:if test="${unDelete == true}">
					<input type="submit" class="btn btn-success" value="${undelete}"
						name="Undelete" />
				</c:if>
			</form:form>
		</div>
	</div>

	<c:if test="${not empty errors}">
		<div align="center">
			<h3 class="panel-title list"><fmt:message key="errors" />:</h3>
			<br />
			<c:forEach items="${errors}" var="error">
				<c:out value="${error}" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</body>
</html>
