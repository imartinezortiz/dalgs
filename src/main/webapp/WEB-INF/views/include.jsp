<!-- archivo de cabecera para incluir en todos los archivos JSP que crearemos con posterioridad. -->

<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> <!-- spring:out formatea la salida -->

<link rel="shortcut icon" href="<c:url value= "/resources/images/image200.ico" />" >

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="<c:url value= "/resources/css/bootstrap.css" />"  >

<!--Owner Style CSS -->
<link rel="stylesheet" href="<c:url value= "/resources/css/style.css" />"  >

<!-- Latest compiled and minified JavaScript -->
<script type="text/javascript" src="<c:url value="/resources/scripts/jquery-1.11.1.min.js" /> " > </script>
<script type="text/javascript" src=" <c:url value="/resources/scripts/bootstrap.js" /> " > </script>



<div class="page-header logo">
    <h1 class="logo">Badges for Subjects
        <small>TFG 2014/ 2015</small>
    </h1>
    <img  alt="tfg" class="img-rounded logo" src="<c:url value="/resources/images/logo.png" /> " > 
</div>


