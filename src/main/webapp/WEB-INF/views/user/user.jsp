<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
	<title>User</title>
</head>

<body>
		<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
							<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
			
			User Details</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/user/${model.userDetails.id}/modify.htm'/>">
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>Edit</a>

		</div>

		<div class="panel-body">
			<div class="form-group">
				<div class="form-group view">
					<label>UserName: </label> 
					<p class="details">${model.userDetails.username}</p>
					<br><br>
					<label>FirstName: </label> 
					<p class="details">${model.userDetails.firstName}</p>
					<br><label>Last Name: </label> 
					<p class="details">${model.userDetails.lastName}</p>
					<br><label>Email: </label> 
					<p class="details">${model.userDetails.email}</p>
				</div>
			</div>
		
		<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">						
			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
			 Courses List</h3>
			
		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">Subject</div></td>
					<td width="50%"><div class="td-label">Academic Term</div></td>
				</tr>
				<c:forEach items="${model.userDetails.courses}" var="course">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${course.subject.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${course.academicTerm.term}" />
							</div>
						</td>

						<td><a class="btn btn-success" 
							href="<c:url value='/academicTerm/${course.academicTerm.id}/course/${course.id}.htm'/>">
									View </a> 	
									<sec:authorize access="hasRole('ROLE_ADMIN')">
								<a class="btn btn-danger"
									href="<c:url value='/academicTerm/${model.academicTerm.id}/course/${course.id}/user/${model.userDetails.id}delete.htm'/>">
									Delete </a>
								</sec:authorize>					
							</td>

					</tr>
				</c:forEach>


			</table>
		</div>
	</div>
		
		</div>
	</div>
</body>
</html>