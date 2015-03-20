<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title>Admin</title>
</head>

<body>
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>

				User Details
			</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/user/${userDetails.id}/modify.htm'/>"> <span
				class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>Edit
			</a>

		</div>

		<div class="panel-body">
			<div class="form-group">
				<div class="form-group view">
					<label>UserName: </label>
					<p class="details">${userDetails.username}</p>
					<br> <br> <label>FirstName: </label>
					<p class="details">${userDetails.firstName}</p>
					<br> <label>Last Name: </label>
					<p class="details">${userDetails.lastName}</p>
					<br> <label>Email: </label>
					<p class="details">${userDetails.email}</p>
				</div>
			</div>


			<a class="list-group-item "
				href="<c:url value='/user/page/0.htm?showAll=false&typeOfUser=ROLE_PROFESSOR'/>">
				<h4 class="list-group-item-heading">
					<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					Professors
				</h4>
				<p class="list-group-item-text">Manage each of the users</p>
			</a> 
			<a class="list-group-item "
				href="<c:url value='/user/page/0.htm?showAll=false&typeOfUser=ROLE_STUDENT'/>">
				<h4 class="list-group-item-heading">
					<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					Students
				</h4>
				<p class="list-group-item-text"> Manage each of the users</p>
			</a>
			
		</div>
	</div>
</body>
</html>