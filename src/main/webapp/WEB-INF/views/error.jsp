<%@ include file="/WEB-INF/views/include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
<title><fmt:message key="title" /></title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="error" /></title>
</head>
<body>
	<div class="bs-callout bs-callout-warning 2">
		<img alt="tfg" class="img-rounded2"
			src="<c:url value="/resources/images/error.jpeg" /> ">

		<div class="inlineImage">
			<h3 class="error"><fmt:message key="msgError" /></h3>
		</div>
		
	
	</div>


</body>
</html>
<div class="home-button"><a class="btn home" href="<c:url value="/home.htm"/>"><fmt:message key="home" /></a></div></body>
</body>
</html>