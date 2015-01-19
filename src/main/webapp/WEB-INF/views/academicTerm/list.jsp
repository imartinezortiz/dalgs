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
        <a  class="btn list-btn btn-warning2" href="<c:url value='/academicTerm/add.htm'/>"> Add Term </a>
    </div>
    <table class="table table-striped table-bordered">
		<tr align="center">
			<td>Term</td>

		</tr>
		
		
		<c:forEach items="${model.academicTerms}" var="term" varStatus="loop">
		
			<tr align="center">
				<td><c:out value="${term}" /></td>

				
			
  				<td>
  					<a href="<c:url value='${term}/degrees.htm'/>" class="btn btn-success">View
				</a>
  					
  					<a href="<c:url value='${term}/delete.htm'/>" class="btn btn-danger">Delete</a>
  				</td>
		
			</tr>

		</c:forEach>
	</table></div>
<div class="home-button"><a class="btn home" href="<c:url value="/home.htm"/>">Home</a></div></body>

</body>
</html>
