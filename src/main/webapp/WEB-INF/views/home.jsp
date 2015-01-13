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
		href="<c:url value='/academicTerm/list.htm'/>">
		<h4 class="list-group-item-heading">Academic Term</h4>
		<p class="list-group-item-text">Manage each of the academic term</p>
	</a>
	</a>
	<a class="list-group-item " href="<c:url value='/activity/list.htm'/>">
		<h4 class="list-group-item-heading">Activities</h4>
		<p class="list-group-item-text">Manage each of the activities
			which belong to a Course</p>
	</a>
	</a>
	<a class="list-group-item "
		href="<c:url value='/competence/list.htm'/>">
		<h4 class="list-group-item-heading">Competences</h4>
		<p class="list-group-item-text">Manage each of the competences
			which belong to a Subject</p>

		
	</a>
	<a href="<c:url value='/course/list.htm'/>" class="list-group-item">
		<h4 class="list-group-item-heading">Courses</h4>
		<p class="list-group-item-text">Manage each of the courses which
			belong to an Academic Term</p>
	</a>
	<a class="list-group-item " href="<c:url value='/degree/list.htm'/>">
		<h4 class="list-group-item-heading">Degrees</h4>
		<p class="list-group-item-text">Manage each of the degrees which
			belong to a career</p>
	</a>
	<a class="list-group-item active"
		href="<c:url value='/subject/list.htm'/>">
		<h4 class="list-group-item-heading">Subjects</h4>
		<p class="list-group-item-text">Manage each of the subjects which
			belong to a career</p> <a href="#" class="list-group-item disabled">
			<h4 class="list-group-item-heading">Badges</h4>
			<p class="list-group-item-text">Manage each of the badges which
				belong to a Competence or to an Activity</p>
	</a>
	</div>
</body>
</html>
