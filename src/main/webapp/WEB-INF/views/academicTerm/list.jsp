<%@page import="org.apache.taglibs.standard.tag.common.xml.IfTag"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="title" /></title>
</head>
<body>
	<div class="table-responsive list">
		<div class="panel-heading list">
			<h4>  
			
			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
			 Academic Terms</h4>
			 
	
			
 			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<a class="btn list-btn btn-warning2" style="cursor:copy;"
				href="<c:url value='/academicTerm/add.htm'/>"> 
				<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
				
				Add Academic Term
			</a>
			 </sec:authorize> 
			<c:choose>
   				<c:when  test="${showAll=='true'}">
   					<a href="<c:url value='/academicTerm/page/${model.currentPage}.htm?showAll'/>">
   					<img src="<c:url value="/resources/images/trash_open.png" /> "  
			 		style=" float: right;  margin-top: -1;  margin-right: 1%;"></a> 
    			</c:when>
    			<c:otherwise>
    					<a href="<c:url value='/academicTerm/page/${model.currentPage}.htm?showAll=${showAll}'/>">
			 			<img src="<c:url value="/resources/images/trash_close.png" /> " 
			 			 style="float: right; margin-right: 1%;margin-top: 3;"></a> 
				</c:otherwise>
			</c:choose>
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
					<td><c:out value="${academicTerm.degree.info.code}" /></td>
					<td><c:out value="${academicTerm.degree.info.name}" /></td>
					<!-- <td><c:out value="${activity.name}" /></td>
				<td><c:out value="${activity.description}" /></td>
				<td><c:out value="${activity.subject.name}" /></td>
				-->


					<td><a
						href="<c:url value='/academicTerm/${academicTerm.id}.htm'/>"
						class="btn btn-success">View</a> 
 						<sec:authorize access="hasRole('ROLE_ADMIN')">
						<a
						href="<c:url value='/academicTerm/${academicTerm.id}/delete.htm'/>"
						class="btn btn-danger">Delete</a>
						</sec:authorize>
				</td>

				</tr>

			</c:forEach>
		</table>


		<nav>
			<ul class="pagination">
				<c:if test="${model.currentPage > 0}">

					<li><a
						href="<c:url value='/academicTerm/page/${model.currentPage - 1}.htm'/>"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>

				<c:forEach var="index" begin="0" end="${model.numberOfPages -1}"
					step="1">



					<li><a href="<c:url value='/academicTerm/page/${index}.htm'/>">
							${index + 1} </a></li>



				</c:forEach>

				<c:if test="${model.currentPage < model.numberOfPages -1}">

					<li><a
						href="<c:url value='/academicTerm/page/${model.currentPage + 1}.htm'/>"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</div>



</body>
</html>
