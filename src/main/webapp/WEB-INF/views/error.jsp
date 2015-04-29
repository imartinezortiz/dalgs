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
<%@ include file="/WEB-INF/views/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
<title><fmt:message key="common.title" /></title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="common.error" /></title>
</head>
<body>
	<div class="bs-callout bs-callout-warning 2">
		<img alt="tfg" class="img-rounded2"
			src="<c:url value="/resources/images/error.jpeg" /> ">

		<div class="inlineImage">
			
				<h3 class="error"><fmt:message key="error.msgError" /></h3>
			

		</div>
		
	
	</div>


</body>
</html>
<div class="home-button"><a class="btn home" href="<c:url value="/home.htm"/>"><fmt:message key="common.home" /></a></div></body>
</body>
</html>