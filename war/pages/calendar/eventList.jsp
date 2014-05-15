<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
	<link href="http://getbootstrap.com/examples/jumbotron-narrow/jumbotron-narrow.css" rel="stylesheet">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script>
		$(document).ready(function() {
			var listDate = document.getElementsByClassName('date');
		    for (var i = 0; i < listDate.length; ++i) {
		    	if (listDate[i].innerText != "") {
					var aDate = new Date(listDate[i].innerText);
					listDate[i].innerText = aDate.toLocaleString();
				}
		    };
		});
	</script>
</head>
<body>
	<div class="container">
		<div class="header">
			<ul class="nav nav-pills pull-right">
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
			<a href="<%=request.getContextPath()%>/calendar/calendarList">Retour</a>
			<h2>Liste des prochains évenements</h2>
			<p>Voici les calendriers créer dans votre agenda "${calendarId}"</p>
			<c:if test="${not empty events}">
				<table class="table">
				    <tr>
				        <th>Nom</th>
				        <th>Start</th>
				        <th>End</th>
				    </tr>
				    <c:forEach var="event" items="${events}">
				        <tr>
				            <td><a href="${event.htmlLink}" target="_blank">${event.summary}</a></td>
				            <td class="date" style="white-space: nowrap;">${event.start.dateTime}</td>
				            <td class="date" style="white-space: nowrap;">${event.end.dateTime}</td>
				        </tr>
				    </c:forEach>
				</table>
			</c:if>
		</div>
		<div class="row marketing">
			<c:if test="${not empty events}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Données reçues par Google</h3>
					</div>
					<div class="panel-body">
						<pre><script>
							document.write(JSON.stringify(${events}, null, 4));
						</script></pre>
					</div>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>
