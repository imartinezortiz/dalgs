<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link rel="shortcut icon"
	href="<c:url value= "/resources/images/favicon.ico" />">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="<c:url value= "/resources/css/bootstrap.css" />">

<!--Owner Style CSS -->
<link rel="stylesheet"
	href="<c:url value= "/resources/css/style.css" />">

<!-- Latest compiled and minified JavaScript -->
<script type="text/javascript"
	src="<c:url value="/resources/scripts/jquery-1.11.1.min.js" /> ">
	
</script>

<script type="text/javascript"
	src=" <c:url value="/resources/scripts/bootstrap.js" /> ">
	
</script>

<script type="text/javascript"
	src=" <c:url value="/resources/scripts/bootstrap-filestyle.js" /> ">
	
</script>


<html>
<head>
<title>Error</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>

	<div class="panel panel-primary group">
		<div class="panel-heading">
			<span class="glyphicon  glyphicon-road" aria-hidden="true">&nbsp;</span>
			<h3 class="panel-title list pError">
				
					Status Code:
					<%=request.getAttribute("javax.servlet.error.status_code")%>
			</h3> 

		</div>

		<div class="panel-body">
			<div class="row">
				<div class="col-md-6">
					<img style=" margin-left: 30%; "class="img-responsive" alt="Responsive image"
						src="<c:url value="/resources/images/404.png" /> ">
				</div>
				<div class="col-md-6">
					<p class="pError" > Reason:</p>
					<p style=" font-family: monospace;">	
						<%=request.getAttribute("javax.servlet.error.message")%>
					</p>
				</div>
			</div>

		</div>

	</div>

</body>
</html>
<div class="home-button">
	<a class="btn home" href="<c:url value="/home.htm"/>">Home</a>
</div>
</body>
</body>
</html>