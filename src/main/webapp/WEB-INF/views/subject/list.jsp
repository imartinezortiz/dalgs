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

			<h4>Subjects</h4>
			<sec:authorize access="hasRole('ROLE_ADMIN')"><a class="btn btn-add2" href="<c:url value='/subject/add.htm'/>">
				Add Subject </a></sec:authorize>
		</div>
		<table class="table table-striped table-bordered">
			<tr align="center">
				<td width="50%"><div class="td-label">Code</div></td>
				<td width="20%"><div class="td-label">Name</div></td>
				<td width="50%"><div class="td-label">Degree</div></td>
				<td width="30%"><div class="td-label">Actions</div></td>
			</tr>
			<c:forEach items="${model.subjects}" var="subject">
				<tr align="center">
					<td>
						<div class="td-content">
							<c:out value="${subject.info.code}" />
						</div>
					</td>

					<td><div class="td-content">
							<c:out value="${subject.info.name}" />
						</div></td>
					<td>
						<div class="td-content">
							<c:out value="${subject.degree.infoDegree.name}" />
						</div>
					</td>

					<td><a class="btn list-btn btn-success"
						href="<c:url value='view/${subject.id}.htm'/>">View</a> <!-- <a href="modify.html"  class="btn list-btn btn-warning">Modify</a>-->
						<sec:authorize access="hasRole('ROLE_ADMIN')"><a class="btn list-btn btn-danger"
						href="<c:url value='delete/${subject.id}.htm'/>">Delete</a></sec:authorize></td>

				</tr>
			</c:forEach>


		</table>
	</div>

</body>
</html>
