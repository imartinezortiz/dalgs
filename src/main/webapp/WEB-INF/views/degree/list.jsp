<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
				<fmt:message key="degList" />
			</h4>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn btn-add2"
					href="<c:url value='/degree/add.htm'/>"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="add" />
				</a>
			</sec:authorize><sec:authorize access="hasRole('ROLE_ADMIN')">
			<c:choose>
				<c:when test="${model.showAll eq true}">
					<a
						href="<c:url value='/degree/page/${model.currentPage}.htm?showAll=false'>
    						</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_open.png" /> "
						style="float: right; margin-top: -1; margin-right: 1%;">
					</a>
				</c:when>
				<c:otherwise>
					<a
						href="<c:url value='/degree/page/${model.currentPage}.htm?showAll=true'> 
    							</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_close.png" /> "
						style="float: right; margin-right: 1%; margin-top: 3;">
					</a>
				</c:otherwise>
			</c:choose></sec:authorize>
		</div>
		<table class="table table-striped table-bordered">
			<tr align="center">
				<td><fmt:message key="code" /></td>
				<td><fmt:message key="name" /></td>
				<td width="40%"><fmt:message key="desc" /></td>
				
			</tr>
			<c:forEach items="${model.degrees}" var="degree">
				<tr align="center">
					<td><c:out value="${degree.info.code}" /></td>
					<td><c:out value="${degree.info.name}" /></td>
					<td><c:out value="${degree.info.description}" /></td>

					<td><c:choose>
							<c:when test="${degree.isDeleted eq false}">
								<a class="btn btn-success"
									href="<c:url value='/degree/${degree.id}.htm'/>"> <fmt:message key="view" /> </a>
								<sec:authorize access="hasRole('ROLE_ADMIN')">
									<a class="btn list-btn btn-danger"
										href="<c:url value='/degree/${degree.id}/delete.htm'/>"> <fmt:message key="delete" /> </a>
								</sec:authorize>
							</c:when>
							<c:otherwise>							
							<sec:authorize access="hasRole('ROLE_ADMIN')">
								<a href="<c:url value='/degree/${degree.id}/restore.htm'/>"
									class="btn btn-success"><fmt:message key="restore" /></a></sec:authorize>
							</c:otherwise>
						</c:choose></td>

				</tr>
			</c:forEach>


		</table>

		<nav>
			<ul class="pagination">
				<c:if test="${model.currentPage > 0}">

					<li><a
						href="<c:url value='/degree/page/${model.currentPage - 1}.htm?showAll=${model.showAll}'/>"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>

				<c:forEach var="index" begin="0" end="${model.numberOfPages -1}"
					step="1">



					<li><a
						href="<c:url value='/degree/page/${index}.htm?showAll=${model.showAll}'/>">
							${index + 1} </a></li>



				</c:forEach>

				<c:if test="${model.currentPage < model.numberOfPages -1}">

					<li><a
						href="<c:url value='/degree/page/${model.currentPage + 1}.htm?showAll=${model.showAll}'/>"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</div>
</body>
</html>
