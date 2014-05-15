<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
	<link href="http://getbootstrap.com/examples/jumbotron-narrow/jumbotron-narrow.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="header">
			<ul class="nav nav-pills pull-right">
				<li><a href="https://github.com/Matelli/GoogleService">Github</a></li>
			</ul>
			<h3 class="text-muted">Matelli GoogleService</h3>
		</div>
	    <c:if test="${not empty error}">
	        <div class="alert alert-danger">${error}</div>
	    </c:if>
	    <h1>Connexion SSO Google</h1>
	
	    <a href="${authorizationUrlGoogle}">
	        <img width="250px" src="https://developers.google.com/+/images/branding/sign-in-buttons/Red-signin_Long_base_44dp.png" />
	    </a>
	</div>
</body>
</html>