<%--

    This file is part of D.A.L.G.S.

    D.A.L.G.S is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    D.A.L.G.S is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.apache.taglibs.standard.tag.common.xml.IfTag"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title><fmt:message key="common.title" /></title>
</head>
<body>
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="externalActivity.list" />
				-
				<fmt:message key="group.gr" />
			</h3>

			<!-- If you are a professor who belongs to this course you can edit -->

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
				<c:forEach items="${model.externalActivities}"
					var="externalActivity">
					<tr align="center">
						<td><div class="td-content">
								<c:out value="${externalActivity.info.name}" />
							</div></td>
						<td>
							<div class="td-content">
								<c:out value="${externalActivity.info.description}" />
							</div>
						</td>
						<td><c:choose>
					

										<c:when test="${empty fn:trim(externalActivity.group)}">course
											<a class="btn btn-success"
												href="<c:url value='/academicTerm/${externalActivity.course.academicTerm.id}/course/${externalActivity.course.id}/externalactivity/${externalActivity.id}.htm'/>">
												<fmt:message key="common.view" />
											</a>
											<a class="btn btn-success"
												href="<c:url value='/academicTerm/${externalActivity.course.academicTerm.id}/course/${externalActivity.course.id}/externalactivity/${externalActivity.id}/move.htm'/>">
												<fmt:message key="common.move" />
											</a>


											<a class="btn btn-danger"
												href="<c:url value='/academicTerm/${externalActivity.course.academicTerm.id}/course/${externalActivity.course.id}/externalactivity/${externalActivity.id}/delete.htm'/>">
												<fmt:message key="common.delete" />
											</a>

										</c:when>
										<c:otherwise>group
											<a class="btn btn-success"
												href="<c:url value='/academicTerm/${externalActivity.group.course.academicTerm.id}/course/${externalActivity.group.course.id}/group/${externalActivity.group.id}/externalactivity/${externalActivity.id}.htm'/>">
												<fmt:message key="common.view" />
											</a>
											<a class="btn btn-success"
												href="<c:url value='/academicTerm/${externalActivity.group.course.academicTerm.id}/course/${externalActivity.group.course.id}/group/${externalActivity.group.id}/externalactivity/${externalActivity.id}/move.htm'/>">
												<fmt:message key="common.move" />
											</a>

											<a class="btn btn-danger"
												href="<c:url value='/academicTerm/${externalActivity.course.academicTerm.id}/course/${externalActivity.course.id}/group/${externalActivity.group.id}/externalactivity/${externalActivity.id}/delete.htm'/>">
												<fmt:message key="common.delete" />
											</a>

										</c:otherwise>

			
							</c:choose></td>


					</tr>
				</c:forEach>
			</table>
		</div>
	</div>



</body>
</html>
