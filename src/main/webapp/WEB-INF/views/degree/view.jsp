<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><fmt:message key="title" /></title>
<style>
.error {
	color: red;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
				<fmt:message key="detailsDeg" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a class="btn list-btn btn-warning"
					href="<c:url value='/degree/${degreeId}/modify.htm'/>"> <span
					class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
					<fmt:message key="modify" />
				</a>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<c:choose>
				<c:when test="${model.showAll eq true}">
					<a
						href="<c:url value='/degree/${degreeId}.htm?showAll=false'>
    						</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_open_view.png" /> "
						style="float: right; margin-right: 1%; margin-top: -0.5%;">
					</a>
				</c:when>
				<c:otherwise>
					<a
						href="<c:url value='/degree/${degreeId}.htm?showAll=true'> 
    							</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_close_view.png" /> "
						style="float: right; margin-right: 1%;">
					</a>
				</c:otherwise>
			</c:choose></sec:authorize>
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="code" />: </label>
					<p class="details">${model.degree.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="name" />: </label>
					<p class="details">${model.degree.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="desc" />: </label>
					<p class="details">${model.degree.info.description}</p>
				</div>
			</div>

		</div>
	</div>
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
<fmt:message key="modList" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/degree/${degreeId}/module/add.htm'/>"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="add" />
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="name" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="desc" /></div></td>
				</tr>
				<c:forEach items="${model.modules}" var="module">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${module.info.name}" />
							</div></td>
						<td><div class="td-content">
								<c:out value="${module.info.description}" />
							</div></td>



						<td><c:choose>
								<c:when test="${module.isDeleted eq false}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/degree/${degreeId}/module/${module.id}.htm'/>"><fmt:message key="view" /></a>

									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/module/${module.id}/delete.htm'/>">
											<fmt:message key="delete" /> </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/module/${module.id}/restore.htm'/>">
											<fmt:message key="restore" /> </a>
									</sec:authorize>
								</c:otherwise>
							</c:choose></td>

					</tr>
				</c:forEach>


			</table>
		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="comList" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/degree/${degreeId}/competence/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="add" />
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="name" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="desc" /></div></td>

				</tr>
				<c:forEach items="${model.competences}" var="competence">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${competence.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${competence.info.description}" />
							</div>
						</td>


						<td><c:choose>
								<c:when test="${competence.isDeleted eq false}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/degree/${degreeId}/competence/${competence.id}.htm'/>"><fmt:message key="view" /></a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/competence/${competence.id}/delete.htm'/>">
											<fmt:message key="delete" /> </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/competence/${competence.id}/restore.htm'/>">
											<fmt:message key="restore" /> </a>
									</sec:authorize>
								</c:otherwise>
							</c:choose></td>

					</tr>
				</c:forEach>


			</table>
		</div>


	</div>


</body>

</html>
