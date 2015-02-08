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
			 Users</h4>
			<a class="btn list-btn btn-warning2"
				href="<c:url value='/user/add.htm'/>"> 
				<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
				
				Add User
			</a>
		</div>
		<table class="table table-striped table-bordered">
					<tr align="center">
						<td width="30%"><div class="td-label">Last Name</div></td>
						<td width="30%"><div class="td-label">First Name</div></td>
						<td width="40%"><div class="td-label">Email</div></td>

					</tr>
					<c:forEach items="${model.users}" var="user">
						<tr align="center">
							<td><div class="td-content">
									<c:out value="${user.lastName}" />
								</div></td>
							<td>
								<div class="td-content">
									<c:out value="${user.firstName}" />
								</div>
							</td>
							<td>
								<div class="td-content">
									<c:out value="${user.email}" />
								</div>
							</td>

							<td><a class="btn btn-success"
								href="<c:url value='/user/${user.id}.htm'/>"> View </a> <a
								class="btn btn-danger"
								href="<c:url value='/user/${user.id}/delete.htm'/>"> Delete
							</a></td>

						</tr>
					</c:forEach>


				</table>

		<nav>
			<ul class="pagination">
				<c:if test="${model.currentPage > 0}">

					<li><a
						href="<c:url value='/user/page/${model.currentPage - 1}.htm'/>"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>

				<c:forEach var="index" begin="0" end="${model.numberOfPages -1}"
					step="1">



					<li><a href="<c:url value='/user/page/${index}.htm'/>">
							${index + 1} </a></li>



				</c:forEach>

				<c:if test="${model.currentPage < model.numberOfPages -1}">

					<li><a
						href="<c:url value='/user/page/${model.currentPage + 1}.htm'/>"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</div>



</body>
</html>
