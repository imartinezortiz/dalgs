<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="common.title" /></title>

</head>
<body>
	<div class="list-group index">
		<a class="list-group-item "
			href="<c:url value='/academicTerm/page/0.htm?showAll=false'/>"> 
			<h4 class="list-group-item-heading">				
			<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			<fmt:message key="home.ats" /></h4>
			<p class="list-group-item-text">
			<fmt:message key="academicterm.header" />
			</p>
		</a> 
		<a class="list-group-item " 
			href="<c:url value='/degree/page/0.htm?showAll=false'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<fmt:message key="home.degs" />
			</h4>
			<p class="list-group-item-text">
			<fmt:message key="degree.header" />
			</p>
		</a>

<a class="list-group-item "
				href="<c:url value='/user/page/0.htm?showAll=false&typeOfUser=ROLE_PROFESSOR'/>">
				<h4 class="list-group-item-heading">
					<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					<fmt:message key="home.profs" />
				</h4>
				<p class="list-group-item-text"><fmt:message key="header.profs" /></p>
			</a> 
			<a class="list-group-item "
				href="<c:url value='/user/page/0.htm?showAll=false&typeOfUser=ROLE_STUDENT'/>">
				<h4 class="list-group-item-heading">
					<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					<fmt:message key="home.studs" />
				</h4>
				<p class="list-group-item-text"><fmt:message key="header.studs" /></p>
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
