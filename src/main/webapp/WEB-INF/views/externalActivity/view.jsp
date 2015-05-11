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
				<fmt:message key="externalActivity.details" />
			</h3>
			
		</div>

	
	

		<div class="panel-body">


			<div class="form-group">
				<div class="form-group view">
					<label><fmt:message key="input.code" /> : </label>
					<p class="details">${model.activity.info.code}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.name" /> :</label>
					<p class="details">${model.activity.info.name}</p>
				</div>
				<div class="form-group view">
					<label><fmt:message key="input.desc" /> :</label>
					<p class="details">${model.activity.info.description}</p>
				</div>


			</div>

		</div>
	</div>




</body>

</html>
