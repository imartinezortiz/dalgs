<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><fmt:message key="common.title" /></title>
<style>
.error {
	color: red;
}
</style>

</head>
<body>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
			<h3 class="panel-title list"><fmt:message key="module.details" /></h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn list-btn btn-warning"
					href="<c:url value='/degree/${model.module.degree.id}/module/${moduleId}/modify.htm'/>">
					<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
					<fmt:message key="common.modify" />
				</a>
			</sec:authorize><sec:authorize access="hasRole('ROLE_ADMIN')">
			<c:choose>
				<c:when test="${model.showAll eq true}">
					<a
						href="<c:url value='/degree/${model.module.degree.id}/module/${moduleId}.htm?showAll=false'>
    						</c:url>">
						<img
						src="<c:url value="/resources/images/theme/trash_open_view.png" /> "
						style="float: right; margin-right: 1%; margin-top: -0.5%;">
					</a>
				</c:when>
				<c:otherwise>
					<a
						href="<c:url value='/degree/${model.module.degree.id}/module/${moduleId}.htm?showAll=true'> 
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
					<label><fmt:message key="input.code" />: </label>
					<p class="details">${model.module.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.name" />: </label>
					<p class="details">${model.module.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.desc" />: </label>
					<p class="details">${model.module.info.description}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="degree.deg" />: </label>
					<p class="details">${model.module.degree.info.name}</p>
				</div>
			</div>

		</div>
	</div>
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>

				<fmt:message key="topic.list" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">

				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="common.add" />
				</a>
				<a style="cursor: copy;" class="btn list-btn btn-warning2"
					href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/upload.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span> CSV
			
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">

			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label"><fmt:message key="input.name" /></div></td>
					<td width="50%"><div class="td-label"><fmt:message key="input.desc" /></div></td>
				</tr>
				<c:forEach items="${model.topics}" var="topic">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${topic.info.name}" />
							</div></td>
						<td><div class="td-content">
								<c:out value="${topic.info.description}" />
							</div></td>



						<td><c:choose>
								<c:when test="${topic.isDeleted eq false}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/${topic.id}.htm'/>">
										<fmt:message key="common.view" /></a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/${topic.id}/delete.htm'/>">
											<fmt:message key="common.delete" /> </a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/module/${moduleId}/topic/${topic.id}/restore.htm'/>">
											<fmt:message key="common.restore" />  </a>
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
