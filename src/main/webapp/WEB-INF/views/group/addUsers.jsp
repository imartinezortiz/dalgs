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
				Add ${typeOfUser} &nbsp; ( ${group.course.subject.info.name} - ${group.name} )
			</h3>
		</div>
		<div class="panel-body">
			<div class="form-group">
				<div class="form-group">
					<form:form method="post" commandName="group" role="form">

						<form:hidden path="id" />
						<label>Available ${typeOfUser}:</label>
						<div class="checkbox">
							<c:choose>
								<c:when test="${typeOfUser eq 'Proffesors'}">
									<form:checkboxes items="${users}" path="professors"
										itemLabel="fullName" />
									<br>
									<br>
									<br>
								</c:when>
								<c:otherwise>
									<form:checkboxes items="${users}" path="students"
										itemLabel="fullName" />
									<br>
									<br>
									<br>
								</c:otherwise>
							</c:choose>

						</div>
						<input type="submit" class="btn btn-success" value="Add"
							name="AddProfessors" />
					</form:form>
				</div>
			</div>


		</div>

	</div>


</body>
</html>
