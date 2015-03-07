<%@page import="org.apache.taglibs.standard.tag.common.xml.IfTag"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="title" /></title>
</head>
<body>



	<div class="table-responsive list">
		<div class="panel-heading list">

			<h4>			<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
			Degree List</h4>
						<sec:authorize access="hasRole('ROLE_ADMIN')">
			
			<a  style="cursor:copy;" class="btn btn-add2" href="<c:url value='/degree/add.htm'/>">
							<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
				Add  </a></sec:authorize>
				<c:choose>
   				<c:when  test="${model.showAll eq true}">
   					<a href="<c:url value='/degree/page/${model.currentPage}.htm?showAll=false'>
    						</c:url>">
   					<img src="<c:url value="/resources/images/trash_open.png" /> "  
			 		style=" float: right;  margin-top: -1;  margin-right: 1%;"></a> 
    			</c:when>
    			<c:otherwise>
    					<a href="<c:url value='/degree/page/${model.currentPage}.htm?showAll=true'> 
    							</c:url>">
			 			<img src="<c:url value="/resources/images/trash_close.png" /> " 
			 			 style="float: right; margin-right: 1%;margin-top: 3;"></a> 
				</c:otherwise>
			</c:choose>
		</div>
		<table class="table table-striped table-bordered">
			<tr align="center">
				<td>Code</td>
				<td>Name</td>
				<td width="50%">Description</td>
				<td>Actions</td>
			</tr>
			<c:forEach items="${model.degrees}" var="degree">
				<tr align="center">
					<td><c:out value="${degree.info.code}" /></td>
					<td><c:out value="${degree.info.name}" /></td>
					<td><c:out value="${degree.info.description}" /></td>

					<td><a class="btn list-btn btn-success"
						href="<c:url value='/degree/${degree.id}.htm'/>">View</a> <!-- <a href="modify.html"  class="btn list-btn btn-warning">Modify</a>-->
						<sec:authorize access="hasRole('ROLE_ADMIN')">
						<a class="btn list-btn btn-danger"
						href="<c:url value='/degree/${degree.id}/delete.htm'/>">Delete</a>
						</sec:authorize></td>

				</tr>
			</c:forEach>


		</table>
		
		<nav>
			<ul class="pagination">
				<c:if test="${model.currentPage > 0}">

					<li><a
						href="<c:url value='/degree/page/${model.currentPage - 1}.htm?showAll=${showAll}'/>"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>

				<c:forEach var="index" begin="0" end="${model.numberOfPages -1}"
					step="1">



					<li><a href="<c:url value='/degree/page/${index}.htm?showAll=${showAll}'/>">
							${index + 1} </a></li>



				</c:forEach>

				<c:if test="${model.currentPage < model.numberOfPages -1}">

					<li><a
						href="<c:url value='/degree/page/${model.currentPage + 1}.htm?showAll=${showAll}'/>"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</div>
</body>
</html>
