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

	<div class="panel panel-primary group category">
		<div class="panel-heading">
			<h3 class="panel-title list">			<span class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span>
			Modify Activity</h3>
		</div>
		<div class="panel-body">
			<%-- 	<form:form  method="post" modelAttribute="modifyProduct" > (ResquestParam)  --%>

			<form:form method="post"  action="modify.htm" commandName="modifyactivity">
				<div class="form-group">
					<label>Code: </label>
					<form:input path="info.code" class="form-control"
						placeholder="Code of the activity" required="true" />
				</div>
				<div class="form-group">
					<label>Name: </label>
					<form:input path="info.name" class="form-control" id="name"
						required="true" />

				</div>
				<div class="form-group">
					<label>Description: </label>
					<form:input class="form-control" path="info.description"
						id="description" required="true" />
				</div>

								

				<div class="panel-body">
					<label>Competence Status List</label>
				
					<table class="table table-condensed">
						<tr align="center">
							<td width="20%"><div class="td-label">Competence</div></td>
							<td width="50%"><div class="td-label">Percentage</div></td>
						</tr>
						
						<c:forEach items="${learningGoalStatus}" var="learnStatus" varStatus="status">		
							<tr align="center">
								<td><div class="td-content">
										<c:out value="${learnStatus.learningGoal.info.name}" />
								
									</div></td>
								<td>
									<div class="td-content">
										<c:out value="${learnStatus.percentage}" />
										
									</div>
								</td>
								<td>
									<a href="<c:url value='competenceStatus/${learnStatus.learningGoal.id}/delete.htm'/>">
										<img style="width: 25px; height: 10px; margin-top: 3%;" border="0" src="<c:url value="/resources/images/delete.png" /> " > 
									</a>
							</td>
								
							</tr>
							
						</c:forEach>
						
						


					</table>
				</div>
				<input type="submit" class="btn btn-primary btn-lg addActivity" value="Modify Activity" />

			</form:form >
			<div class="addLearningStatus">
			<form:form method="post" action="addLearningStatus.htm" commandName="addlearningstatus">
					<h4 style=" color: forestgreen;">   		
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					New Learning Goal Status</h4>
					<br>
					<label>Competence:</label>
					<form:select class="form-control 2" path="learningGoal"
						id="learningGoal">
						<form:option value=""> --Select an option-- </form:option>
						<c:forEach items="${learningGoals}" var="learning">
							<form:option value="${learning.id}">${learning.info.name}</form:option>
						</c:forEach>
					</form:select>
					<br>
					<label>Learning Goal Percentage:</label>
					<form:input class="form-control" path="percentage" id="percentage"
						required="true" />


				
					 <input type="submit" class="btn btn-success CompSta" name="button1"
						value="Add Learning Goal Status" /> 

			</form:form>
			</div>
		</div>
	</div>
	
		<div align="center">
		<h3 class="panel-title list">	Errors: </h3>	
			<br/>
			<c:forEach items="${errors}" var="error">

				
					<c:out  value="${error}" /><br/>
				



			</c:forEach>
		
	</div>
</body>
</html>
