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
				<li><a href="<%=request.getContextPath()%>/home">SSO (OAuth 2.0)</a></li>
				<li class="active"><a href="<%=request.getContextPath()%>/calendar/">Calendar</a></li>
				<li><a href="<%=request.getContextPath()%>/drive/">Drive</a></li>
				<li><a href="<%=request.getContextPath()%>/userinfo/logout">Logout</a></li>
			</ul>
			<h3 class="text-muted">Matelli GoogleService</h3>
		</div>
		<div class="jumbotron">
			<h1>Service Calendar</h1>
			<p class="lead">Exemple d'utilisation du service Calendar avec l'utilisation d'un Refresh Token</p>
		</div>
		<div class="row marketing">
			<a href="<%=request.getContextPath()%>/calendar/">Retour</a>
			<h2>Création de calendrier</h2>
			<p>Voici les calendriers créer dans votre agenda</p>

			<ul>
			    <c:forEach var="nameCalendar" items="${listSummaryCalendar}">
			        <li>${nameCalendar}</li>
			    </c:forEach>
			</ul>
		</div>
		<div class="row marketing">
			<c:if test="${not empty listSummaryCalendar}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Données reçues par Google</h3>
					</div>
					<div class="panel-body">
						<pre>${listSummaryCalendar}</pre>
					</div>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>
