<%@page import="org.apache.taglibs.standard.tag.common.xml.IfTag"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="title" /></title>
</head>
<body>



	<div class="table-responsive list">
		<div class="panel-heading list">
			<h4>Courses</h4>
			<a class="btn btn-add2" href="<c:url value='/course/add.htm'/>">
				Add Course </a>
		</div>
		<table class="table table-striped table-bordered">
			<tr align="center">
				<td><div class="td-label">Academic Term</div></td>
				<td><div class="td-label">Subject</div></td>
				<td><div class="td-label">Degree</div></td>
				
				<td><div class="td-label">Actions</div></td>
			</tr>
			<c:forEach items="${model.courses}" var="course">
				<tr align="center">
					<td>
						<div class="td-content">
							<c:out value="${course.academicTerm.term}" />
						</div>
					</td>
					<td>
						<div class="td-content">
							<c:out value="${course.subject.name}" />
						</div>
					</td>
					<td>
						<div class="td-content">						
							<c:out value="${course.subject.degree.name}" />
						</div>
					</td>


					<td><a class="btn list-btn btn-success"
						href="<c:url value='view/${course.id}.htm'/>">View</a> 
						<a class="btn list-btn btn-danger"
						href="<c:url value='delete/${course.id}.htm'/>">Delete</a></td>

				</tr>
			</c:forEach>


		</table>
	</div>
	<div class="home-button">
		<a class="btn home" href="<c:url value="/home.htm"/>">Home</a>
	</div>




</body>
</html>
