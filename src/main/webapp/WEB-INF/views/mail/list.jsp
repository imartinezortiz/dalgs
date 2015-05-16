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
					<fmt:message key="common.mail" /></h4>
	

		</div>
		<table class="table ">
			<tr align="center">
			<td> #</td>
				<td><fmt:message key="mail.from" /></td>
				<td><fmt:message key="mail.subject" /></td>
				<td><fmt:message key="mail.date" /></td>
				<td><fmt:message key="mail.msg" /></td>
				
				
			</tr>


			<c:forEach items="${model.mails}" var="mail">

				<tr align="center">
					
					<td><c:out value="${mail.id}" /></td>
					<td><c:out value="${mail.owner}" /></td>
					<td><c:out value="${mail.subject}" /></td>
					<td><c:out value="${mail.sentDate}" /></td>
					<td><c:out value="${mail.message}" /></td>	
					
				</tr>

			</c:forEach>
		</table>

	</div>


</body>
</html>
