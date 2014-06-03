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
				<li><a href="<%=request.getContextPath()%>/">SSO (OAuth 2.0)</a></li>
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
			<p><i>Afin d'accéder aux différents exemples d'utilisation de Google Calendar, veuillez-vous identifier avec votre compte
			Google. Cette opération générera un Refresh Token avec les autorisations nécessaires pour utiliser le service Calendar.</i></p>	
		    <c:if test="${not empty authorizationUrlGoogleCalendar}">
		        <p class="text-center"><a href="${authorizationUrlGoogleCalendar}">OAuth OFF</a></p>
		    </c:if>
		    <c:if test="${empty authorizationUrlGoogleCalendar}">
	        	<p class="text-muted text-center">OAuth ON</p>
				<div class="row marketing">
					<h3><a href="<%=request.getContextPath()%>/calendar/calendarInsert">Création de calendrier</a></h2>
				    <p>Cela vous rajoutera 3 nouveaux calendriers dans votre Google Agenda.</p>
				    <h3><a href="<%=request.getContextPath()%>/calendar/calendarList">Liste vos prochain évenement</a></h3>
				    <p>Cela listera tous vos calendriers, puis sélectionner l'un de vos calendriers afin d'accéder au prochain évenement.</p>
				</div>
	    	</c:if>
	    </div>
	</div>
</body>
</html>