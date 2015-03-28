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

	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<h3 class="panel-title list">			<span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>
			
			<fmt:message key="activity.add" /></h3>
		</div>
		<div class="panel-body">

			<spring:message code="activity.placeCode" var="pca" />
			<spring:message code="activity.placeName" var="pna" />
			<spring:message code="common.add" var="add" />
			<spring:message code="common.undelete" var="undelete" />
			<spring:message code="activity.placeDes" var="pda" />

			<form:form method="post" commandName="addactivity" role="form">
				<div class="form-group">
					<label><fmt:message key="input.code" />: </label>
					<form:input path="info.code" class="form-control" placeholder="${pca}" required="true"/>
					<form:errors path="info.code" cssStyle="color: #ff0000" />
				</div>
				<div class="form-group">
					<label><fmt:message key="input.name" />: </label>
					<form:input path="info.name" class="form-control" placeholder="${pna}" required="true"/>
					<form:errors path="info.name" cssStyle="color: #ff0000" />
				</div>
				<div class="form-group">
					<label><fmt:message key="input.desc" />: </label>
					<form:input path="info.description" class="form-control"  placeholder="${pda}" required="true"/>
					<form:errors path="info.description" cssStyle="color: #ff0000" />
				</div>

				
				<input type="submit" class="btn btn-success" value="${add}" />
				<c:if test="${unDelete == true}">
					<input type="submit" class="btn btn-success" value="${undelete}" name="Undelete"/>
				</c:if>

			</form:form>
		</div>
	</div>
	
	<c:if test="${not empty errors}">
	<div align="center">
		<h3 class="panel-title list">	<fmt:message key="error.errors" />: </h3>	
			<br/>
			<c:forEach items="${errors}" var="error">
				<c:out  value="${error}" /><br/>
			</c:forEach>
	</div>
	</c:if>
</body>
</html>
