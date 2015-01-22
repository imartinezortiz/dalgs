<%@page import="org.apache.taglibs.standard.tag.common.xml.IfTag"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="title" /></title>
</head>
<body>
<div class="table-responsive list">
    <div class="panel-heading list">
        <h4>Academic Terms</h4>
        <a  class="btn list-btn btn-warning2" href="<c:url value='/academicTerm/add.htm'/>"> Add Academic Term </a>
    </div>
    <table class="table table-striped table-bordered">
		<tr align="center">
			
			<td>Term</td>
			<td>Code Degree</td>
			<td>Name Degree</td>
<!--			<td>Name</td>
			<td>Description</td>
			<td>Subject</td>
			<td>Actions</td>-->
		</tr>
		
		
		<c:forEach items="${model.academicTerms}" var="academicTerm">
		
			<tr align="center">
				<td><c:out value="${academicTerm.term}" /></td>
				<td><c:out value="${academicTerm.degree.code}" /></td>
				<td><c:out value="${academicTerm.degree.name}" /></td>
			<!-- <td><c:out value="${activity.name}" /></td>
				<td><c:out value="${activity.description}" /></td>
				<td><c:out value="${activity.subject.name}" /></td>
				-->
				
			
  				<td>
  					<a href="<c:url value='/academicTerm/${academicTerm.id}.htm'/>" class="btn btn-warning 2">View</a>
  					<a href="<c:url value='/academicTerm/${academicTerm.id}/delete.htm'/>" class="btn btn-danger">Delete</a>
  				</td>
		
			</tr>

		</c:forEach>
	</table></div>
<div class="home-button"><a class="btn home" href="<c:url value="/home.htm"/>">Home</a></div></body>

</body>
</html>
