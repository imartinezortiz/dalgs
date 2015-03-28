<%@ page contentType="text/html" pageEncoding="UTF-8"%>
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
				<fmt:message key="user.details" />
			</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/user/${userDetails.id}/modify.htm'/>"> <span
				class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span><fmt:message key="common.modify" />
			</a>

		</div>

		<div class="panel-body">
			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="access.username" />: </label>
					<p class="details">${userDetails.username}</p>
					<br> <br> <label><fmt:message key="user.firstN" />: </label>
					<p class="details">${userDetails.firstName}</p>
					<br> <label><fmt:message key="user.lastN" />: </label>
					<p class="details">${userDetails.lastName}</p>
					<br> <label><fmt:message key="user.email" />: </label>
					<p class="details">${userDetails.email}</p>
				</div>
			</div>


			
			
		</div>
	</div>
</body>
</html>