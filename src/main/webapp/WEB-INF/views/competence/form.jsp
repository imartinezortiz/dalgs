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
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
				"${valueButton}" Competence
			</h3>
		</div>
		<div class="panel-body">

			<form:form method="post" commandName="competence" role="form">
				<div>
				<form:hidden path="id"/>
				</div>
				<div class="form-group">
					<label>Code: </label>
					<form:input path="info.code" class="form-control"
						placeholder="Code of the competence" required="true" />
					<form:errors path="info.code" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label>Name: </label>
					<form:input path="info.name" class="form-control"
						placeholder="Name of the competence" required="true" />
					<form:errors path="info.name" cssStyle="color: #ff0000" />

				</div>
				<div class="form-group">
					<label>Description: </label>
					<form:input path="info.description" class="form-control"
						placeholder="Description of the competence" required="true" />
					<form:errors path="info.description" cssStyle="color: #ff0000" />

				</div>


				<input type="submit" class="btn btn-success" value="${valueButton}"
					name="${valueButton}" />
				<c:if test="${unDelete == true}">
					<input type="submit" class="btn btn-success" value="Undelete"
						name="Undelete" />
				</c:if>
			</form:form>
		</div>
	</div>
	<c:if test="${not empty errors}">
		<div align="center">
			<h3 class="panel-title list">Errors:</h3>
			<br />
			<c:forEach items="${errors}" var="error">
				<c:out value="${error}" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</body>
</html>
