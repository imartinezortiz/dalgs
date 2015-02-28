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
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			
			<a class="btn list-btn btn-warning2"
				href="<c:url value='/user/add.htm'/>"> 
				<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
				
				Add User
			</a>
			</sec:authorize>
		</div>
		<table class="table table-striped table-bordered">
					<tr align="center">
						<td><div class="td-label">Last Name</div></td>
						<td><div class="td-label">First Name</div></td>
						<td><div class="td-label">Email</div></td>
						<td><div class="td-label">Status</div></td>

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
								<td>
								<div class="td-content">
									<c:choose>
      									<c:when test="${user.enabled}">Enabled
      									<br />
      									</c:when>

      									<c:otherwise>Disabled.
      									<br />
      									</c:otherwise>
									</c:choose>
								</div>
							</td>

							<td><a class="btn btn-success"
								href="<c:url value='/user/${user.id}.htm'/>"> View </a> 
						<sec:authorize access="hasRole('ROLE_ADMIN')">
									
									<a class="btn btn-danger"
										href="<c:url value='/user/${user.id}/status.htm'/>">
									<c:choose>
      									<c:when test="${user.enabled}">
      									 Disabled
				
      									</c:when>

      									<c:otherwise>
										Enabled
      									</c:otherwise>
									</c:choose>
									</a>
								</sec:authorize></td>

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
