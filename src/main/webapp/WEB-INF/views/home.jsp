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
<title><fmt:message key="common.title" /></title>

</head>
<body>
	<div class="list-group index">
		<a class="list-group-item "
			href="<c:url value='/academicTerm/page/0.htm?showAll=false'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<fmt:message key="home.ats" />
			</h4>
			<p class="list-group-item-text">
				<fmt:message key="academicterm.header" />
			</p>
		</a> <a class="list-group-item "
			href="<c:url value='/degree/page/0.htm?showAll=false'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<fmt:message key="home.degs" />
			</h4>
			<p class="list-group-item-text">
				<fmt:message key="degree.header" />
			</p>
		</a>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
		
		 <a class="list-group-item "
			href="<c:url value='/user/page/0.htm?showAll=false&typeOfUser=ROLE_PROFESSOR'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<fmt:message key="home.profs" />
			</h4>
			<p class="list-group-item-text">
				<fmt:message key="header.profs" />
			</p>
		</a> <a class="list-group-item "
			href="<c:url value='/user/page/0.htm?showAll=false&typeOfUser=ROLE_STUDENT'/>">
			<h4 class="list-group-item-heading">
				<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<fmt:message key="home.studs" />
			</h4>
			<p class="list-group-item-text">
				<fmt:message key="header.studs" />
			</p>
		</a> 
		</sec:authorize>
		<br>
		

	</div>
</body>
</html>
