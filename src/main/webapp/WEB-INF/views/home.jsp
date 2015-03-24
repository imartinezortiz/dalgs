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
			href="<c:url value='/academicTerm/page/0.htm?showAll=false'/>">
			<h4 class="list-group-item-heading">				
			<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			<fmt:message key="at" /></h4>
			<p class="list-group-item-text">
			<fmt:message key="headAT" />
			</p>
		</a> 
		<a class="list-group-item " 
			href="<c:url value='/degree/page/0.htm?showAll=false'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<fmt:message key="deg" />
			</h4>
			<p class="list-group-item-text">
			<fmt:message key="headDeg" />
			</p>
		</a>

	
		
		<br><br>
		
		Direct Access to delete
		<a  class="list-group-item " 
			href="<c:url value='/academicTerm/1/course/1.htm'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				Course 1  (Coordinator)</h4>
		</a>
		
		 <a  class="list-group-item " 
			href="<c:url value='/academicTerm/1/course/1/group/1.htm'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				Group 1 - C1 (Student, Professor)</h4>
		</a>
	</div>
</body>
</html>
