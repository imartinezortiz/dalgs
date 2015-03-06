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

			<h4>Competence List</h4>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			
			<a class="btn list-btn btn-warning2" style="cursor:copy;"
				href="<c:url value='/competence/add.htm'/>"> Add </a>
				</sec:authorize>
		</div>
		<table class="table table-striped table-bordered">
			<tr align="center">
				<td>Code</td>
				<td>Name</td>
				<td>Description</td>
				<td>Degree</td>
				<td>Actions</td>
			</tr>


			<c:forEach items="${model.competences}" var="competence">

				<tr align="center">

					<td><c:out value="${competence.code}" /></td>
					<td><c:out value="${competence.name}" /></td>
					<td><c:out value="${competence.description}" /></td>
					<td><c:out value="${competence.degree.name}" /></td>



					<td><a href="<c:url value='view/${competence.id}.htm'/>"
						class="btn btn-warning 2">View</a> 
						<sec:authorize access="hasRole('ROLE_ADMIN')">
						<a
						href="<c:url value='delete/${competence.id}.htm'/>"
						class="btn btn-danger">Delete</a>
						</sec:authorize>
						</td>

				</tr>

			</c:forEach>
		</table>
	</div>

</body>
</html>
