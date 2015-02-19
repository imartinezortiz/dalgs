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

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
				Activity Details
			</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='${activityId}/modify.htm'/>">			
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>Edit</a>

		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label>Code: </label>
					<p class="details">
						${model.activity.info.code}
					</p>
					</div>
					<div class="form-group view">
					<label>Name:</label>
					<p class="details">
						${model.activity.info.name}
					</p>
				</div>
					<div class="form-group view">
					<label>Description:</label>
					<p class="details">
						${model.activity.info.description}
					</p>
				</div>
					

			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">						
			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
			Learning Goal Status List </h3>
		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">Learning Goal</div></td>
					<td width="50%"><div class="td-label">Percentage</div></td>
				
				</tr>
				<c:forEach items="${model.learningStatus}" var="ls">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${ls.learningGoal.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${ls.percentage}" />
							</div>
						</td>


					</tr>
				</c:forEach>


			</table>
		</div>

	</div>


</body>

</html>
