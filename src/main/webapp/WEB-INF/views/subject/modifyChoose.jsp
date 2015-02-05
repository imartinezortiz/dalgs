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
			<h3 class="panel-title list">Modify Subject</h3>
		</div>
		<div class="panel-body">


			<form:form method="post" commandName="modifySubject" role="form">
				<div class="form-group">
					<label>Code: </label>
					<form:input path="code" class="form-control"
						placeholder="Code of the subject" required="true" />
				</div>
				<div class="form-group">
					<label>Name: </label>
					<form:input path="name" class="form-control"
						placeholder="Name of the subject" required="true" />
				</div>
				<div class="form-group">
					<label>Description: </label>
					<form:input path="description" class="form-control"
						placeholder="Description of the subject" required="true" />
				</div>
				
				
				
				<div class="form-group">
					<label>Degree List:</label>
					<form:select class="form-control 2" path="degree" id="degreeSelect">
						<form:option value="">-- Select an option --</form:option>
						<c:forEach items="${degrees}" var="degree">
							<c:choose>
								<c:when test="${degree.id == idDegree}">
									<form:option value="${degree.id}" selected='true'>${degree.code}-${degree.name}</form:option>
								</c:when>
								<c:otherwise>
									<form:option value="${degree.id}">${degree.code}-${degree.name}</form:option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
				</div>
				
				<input type="submit" class="btn btn-success" value="Update">

			</form:form>
		</div>
	</div>

</body>
</html>
