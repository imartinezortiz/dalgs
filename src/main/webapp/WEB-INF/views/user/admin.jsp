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
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.jsp"%>

<html>
<head>
<title>Admin</title>
</head>

<body>
	<div class="panel panel-primary group">
		<div class="panel-heading">
			<h3 class="panel-title list">
				<span class="glyphicon glyphicon-paperclip" aria-hidden="true">&nbsp;</span>
				<fmt:message key="user.details" />
			</h3>
			<a class="btn list-btn btn-warning"
				href="<c:url value='/user/${userDetails.id}/modify.htm'/>"> <span
				class="glyphicon glyphicon-edit" aria-hidden="true">&nbsp;</span><fmt:message key="common.modify" />
			</a>

		</div>

		<div class="panel-body">
			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="access.username" />: </label>
					<p class="details">${userDetails.username}</p>
					<br> <label><fmt:message key="user.firstN" />: </label>
					<p class="details">${userDetails.firstName}</p>
					<br> <label><fmt:message key="user.lastN" />: </label>
					<p class="details">${userDetails.lastName}</p>
					<br> <label><fmt:message key="user.email" />: </label>
					<p class="details">${userDetails.email}</p>
				</div>
			</div>


			
			
		</div>
	</div>
</body>
</html>