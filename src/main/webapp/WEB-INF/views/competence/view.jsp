<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>

				<fmt:message key="competence.details" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn list-btn btn-warning"
					href="<c:url value='/degree/${degreeId}/competence/${competenceId}/modify.htm'/>"><fmt:message
						key="common.modify" /></a>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_PROFESSOR')">
				<c:choose>
					<c:when test="${model.showAll eq true}">
						<a
							href="<c:url value='/degree/${degreeId}/competence/${competenceId}.htm?showAll=false'>
    						</c:url>">
							<img
							src="<c:url value="/resources/images/theme/trash_open_view.png" /> "
							style="float: right; margin-right: 1%; margin-top: -0.5%;">
						</a>
					</c:when>
					<c:otherwise>
						<a
							href="<c:url value='/degree/${degreeId}/competence/${competenceId}.htm?showAll=true'> 
    							</c:url>">
							<img
							src="<c:url value="/resources/images/theme/trash_close_view.png" /> "
							style="float: right; margin-right: 1%;">
						</a>
					</c:otherwise>
				</c:choose>
			</sec:authorize>
		</div>

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="input.code" />: </label>
					<p class="details">${model.competence.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.name" />: </label>
					<p class="details">${model.competence.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.desc" />: </label>
					<p class="details">${model.competence.info.description}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.type" />: </label>
					<p class="details">${model.competence.info.type}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="degree.deg" />: </label>
					<p class="details">${model.competence.degree.info.name}</p>
				</div>
			</div>

		</div>
	</div>

	<div class="panel panel-primary group">
		<div class="panel-heading">

			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="learninggoal.list" />
			</h3>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<a class="btn list-btn btn-warning2" style="cursor: copy;"
					href="<c:url value='/degree/${degreeId}/competence/${competenceId}/learninggoal/add.htm'/>">
					<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
					<fmt:message key="common.add" />
				</a>
			</sec:authorize>

		</div>
		<div class="panel-body">
			<table class="table table-striped table-bordered">
				<tr align="center">
					<td width="20%"><div class="td-label">
							<fmt:message key="input.name" />
						</div></td>
					<td width="50%"><div class="td-label">
							<fmt:message key="input.desc" />
						</div></td>

				</tr>
				<c:forEach items="${model.learningGoals}" var="learningGoal">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${learningGoal.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${learningGoal.info.description}" />
							</div>
						</td>

						<td><c:choose>
								<c:when test="${learningGoal.isDeleted eq false}">
									<a class="btn list-btn btn-success"
										href="<c:url value='/degree/${degreeId}/competence/${competenceId}/learninggoal/${learningGoal.id}.htm'/>">
										<fmt:message key="common.view" />
									</a>
									<sec:authorize access="hasRole('ROLE_ADMIN')">

										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/competence/${competenceId}/learninggoal/${learningGoal.id}/delete.htm'/>">

											<fmt:message key="common.delete" />
										</a>
									</sec:authorize>
								</c:when>
								<c:otherwise>
									<sec:authorize access="hasRole('ROLE_ADMIN')">

										<a class="btn btn-danger"
											href="<c:url value='/degree/${degreeId}/competence/${competenceId}/learninggoal/${learningGoal.id}/restore.htm'/>">
											<fmt:message key="common.restore" />
										</a>
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
