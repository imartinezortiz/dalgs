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
	<div class="table-responsive list">
		<div class="panel-heading list">
			<h4>

				<span class="glyphicon glyphicon-list" aria-hidden="true">&nbsp;</span>
				<fmt:message key="common.mail" />
			</h4>


		</div>
		<table class="table table-striped ">
			<tr align="center">
				<td>#</td>
				<td><fmt:message key="mail.from" /></td>
				<td><fmt:message key="mail.to" /></td>
				<td><fmt:message key="mail.subject" /></td>
				<td><fmt:message key="mail.replies" /></td>
				<td><fmt:message key="mail.msg"></fmt:message></td>
			</tr>

	
			<c:forEach items="${mails}" var="mail">
				<c:choose>
					<c:when test="${mail.parent == null}">
						<tr align="center">

							<td><c:out value="${mail.id}" /></td>
							<td><c:out value="${mail.from}" /></td>
							<td><c:out value="${mail.to}" /></td>
							<td><c:out value="${mail.subject}" /></td>
							<td><span class="badge"> <c:out
										value="${fn:length(mail.replies)}" />
							</span> <c:if test="${not empty mail.replies}">
									<c:choose>
										<c:when test="${groupId == null && courseId == null}">
											<a class="glyphicon glyphicon-chevron-down"
												href="<c:url value='/mailbox.htm?messageId=${mail.id}'/>">

											</a>
										
										</c:when>
										
										<c:when test="${groupId != null}">
											<a class="glyphicon glyphicon-chevron-down"
												href="<c:url value='/academicTerm/${academicId}/course/${courseId}/group/${groupId}/messages.htm?messageId=${mail.id}'/>">

											</a>
										</c:when>
										<c:otherwise>
											<a class="glyphicon glyphicon-chevron-down"
												href="<c:url value='/academicTerm/${academicId}/course/${courseId}/messages.htm?messageId=${mail.id}'/>">

											</a>
										</c:otherwise>
									</c:choose>

								</c:if></td>
							<td><a class="glyphicon glyphicon-file"
								href="<c:url value='${mail.file}'/>"></a></td>
						</tr>

						<c:if test="${showReplies eq mail.id}">
							
							<c:forEach items="${mail.replies}" var="reply">
								<tr align="center">
									<td><c:out value="${reply.id}" /></td>
									<td><c:out value="${reply.from}" /></td>
									<td><c:out value="${reply.to}" /></td>
									<td><c:out value="${reply.subject}" /></td>
									<td>Reply</td>									
									<td><a class="glyphicon glyphicon-file"
										href="<c:url value='${reply.file}'/>"></a></td>
								</tr>
							</c:forEach>

						</c:if>


					</c:when>

				</c:choose>
			</c:forEach>
		</table>

	</div>


</body>
</html>
