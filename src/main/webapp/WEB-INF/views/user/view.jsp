<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title>User</title>
</head>

<body>
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>

				<fmt:message key="detailsUser" />
			</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/user/${model.userDetails.id}/modify.htm'/>">
				<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span><fmt:message key="modify" />
			</a>

		</div>

		<div class="panel-body">
			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="username" />: </label>
					<p class="details">${model.userDetails.username}</p>
					<br>
					<br> <label><fmt:message key="firstN" />: </label>
					<p class="details">${model.userDetails.firstName}</p>
					<br>
					<label><fmt:message key="lastN" />: </label>
					<p class="details">${model.userDetails.lastName}</p>
					<br>
					<label><fmt:message key="email" />: </label>
					<p class="details">${model.userDetails.email}</p>
				</div>
			</div>
		</div>
	</div>

	<c:if test="${model.groups !=null}">
		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list">
					<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
					<fmt:message key="grList" />
				</h3>

			</div>
			<div class="panel-body">

				<table class="table table-striped table-bordered">
					<tr align="center">
						<td><div class="td-label"><fmt:message key="sub" /></div></td>
						<td><div class="td-label"><fmt:message key="at" /></div></td>
						<td><div class="td-label"><fmt:message key="gr" /></div></td>

					</tr>
					<c:forEach items="${model.groups}" var="group">
						<tr align="center">
							<td><div class="td-content">
									<c:out value="${group.course.subject.info.name}" />
								</div></td>
							<td>
								<div class="td-content">
									<c:out value="${group.course.academicTerm.term}" />
								</div>
							</td>
							<td>
								<div class="td-content">
									<c:out value="${group.name}" />
								</div>
							</td>
							<td><a class="btn btn-success"
								href="<c:url value='/academicTerm/${group.course.academicTerm.id}/course/${group.course.id}/group/${group.id }.htm'/>">
									<fmt:message key="view" /> </a></td>

						</tr>
					</c:forEach>


				</table>
			</div>
		</div>
	</c:if>
	<c:if test="${model.courses!=null}">
		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list">
					<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
					<fmt:message key="courList" />
				</h3>

			</div>
			<div class="panel-body">

				<table class="table table-striped table-bordered">
					<tr align="center">
						<td><div class="td-label"><fmt:message key="sub" /></div></td>
						<td><div class="td-label"><fmt:message key="at" /></div></td>

					</tr>
					<c:forEach items="${model.courses}" var="course">
						<tr align="center">
							<td><div class="td-content">
									<c:out value="${course.subject.info.name}" />
								</div></td>
							<td>
								<div class="td-content">
									<c:out value="${course.academicTerm.term}" />
								</div>
							</td>
							
							<td><a class="btn btn-success"
								href="<c:url value='/academicTerm/${course.academicTerm.id}/course/${course.id}.htm'/>">
									<fmt:message key="view" /> </a></td>

						</tr>
					</c:forEach>


				</table>
			</div>
		</div>
	</c:if>
</body>
</html>