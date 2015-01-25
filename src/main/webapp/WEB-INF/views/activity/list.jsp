<%@page import="org.apache.taglibs.standard.tag.common.xml.IfTag"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="title" /></title>
</head>
<body>
<div class="table-responsive list">
    <div class="panel-heading list">
  		<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
        <h4>Activities</h4>
        <a  class="btn list-btn btn-warning2" href="<c:url value='/activity/add.htm'/>"> Add Activity </a>
    </div>
    <table class="table table-striped table-bordered">
		<tr align="center">
			<td>Code</td>
			<td>Name</td>
			<td>Description</td>
			<td>Course</td>
<!-- 			<td>Percentage - Competence</td>
 -->			<td>Actions</td>
		</tr>
		
		
		<c:forEach items="${model.activities}" var="activity">
		
			<tr align="center">
				<td><c:out value="${activity.code}" /></td>
				<td><c:out value="${activity.name}" /></td>
				<td><c:out value="${activity.description}" /></td>
				<td>
					<c:out value="${activity.course.subject.name}" />
					<c:out value="${activity.course.academicTerm.term}" />
				</td>
			<%-- 	<td>
					<c:out value="${activity.competenceStatus.percentage}" />
					<c:out value="${activity.competenceStatus.competence.name}" />
				</td> --%>
				
			
  				<td>		
  				
  					<a href="<c:url value='${activity.id}view.htm'/>" class="btn btn-warning 2">View</a>
  					<a href="<c:url value='${activity.id}/delete.htm'/>" class="btn btn-danger">Delete</a>
  				</td>
		
			</tr>

		</c:forEach>
	</table></div>

</body>
</html>
