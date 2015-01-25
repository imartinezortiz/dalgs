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
			<a class="btn btn-add2" href="<c:url value='/subject/add.htm'/>">
				Add Subject </a>
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
							<c:out value="${subject.code}" />
						</div>
					</td>

					<td><div class="td-content">
							<c:out value="${subject.name}" />
						</div></td>
					<td>
						<div class="td-content">
							<c:out value="${subject.degree.name}" />
						</div>
					</td>

					<td><a class="btn list-btn btn-success"
						href="<c:url value='view/${subject.id}.htm'/>">View</a> <!-- <a href="modify.html"  class="btn list-btn btn-warning">Modify</a>-->
						<a class="btn list-btn btn-danger"
						href="<c:url value='delete/${subject.id}.htm'/>">Delete</a></td>

				</tr>
			</c:forEach>


		</table>
	</div>

</body>
</html>
