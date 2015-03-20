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
		</div>
	</div>
</body>
</html>