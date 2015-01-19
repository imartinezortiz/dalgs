<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="title" /></title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="css/bootstrap.css">

<!-- Latest compiled and minified JavaScript -->
<script src="scripts/jquery-1.11.1.min.js" type="text/javascript"></script>

<script src="scripts/bootstrap.js" type="text/javascript"></script>
</head>
<body>
<div class="list-group index">
	<a class="list-group-item "
		href="<c:url value='/academicTerm/terms.htm'/>">
		<h4 class="list-group-item-heading">Academic Term</h4>
		<p class="list-group-item-text">Manage each of the academic term</p>
	</a>

	<a class="list-group-item " href="<c:url value='/degree/list.htm'/>">
		<h4 class="list-group-item-heading">Degrees</h4>
		<p class="list-group-item-text">Manage each of the degrees which
			belong to a career</p>
	</a>
	 <a href="#" class="list-group-item disabled">
			<h4 class="list-group-item-heading">Badges</h4>
			<p class="list-group-item-text">Manage each of the badges which
				belong to a Competence or to an Activity</p>
	</a>
	</div>
</body>
</html>
