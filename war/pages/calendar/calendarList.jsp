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
			<a href="<%=request.getContextPath()%>/calendar/">Retour</a>
			<h2>Liste de vos calendriers</h2>
			<c:if test="${not empty error}">
    			${error}
			</c:if>
			<c:if test="${not empty calendarListEntries}">
				<table class="table">
				    <tr>
				        <th colspan="2">Nom</th>
				        <th>Description</th>
				        <th>TimeZone</th>
				    </tr>
				    <c:forEach var="calendar" items="${calendarListEntries}">
				        <tr>
				            <td><div style="width:10px; height:10px; background-color:${calendar.backgroundColor}"></div></td>
				            <td><a href="listEvents/${calendar.id}/">${calendar.summary}</a></td>
				            <td>${calendar.description}</td>
				            <td>${calendar.timeZone}</td>
				        </tr>
				    </c:forEach>
				</table>
			</c:if>
		</div>
		<div class="row marketing">
			<c:if test="${not empty calendarListEntries}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Données reçues par Google</h3>
					</div>
					<div class="panel-body">
						<pre><script>
							document.write(JSON.stringify(${calendarListEntries}, null, 4));
						</script></pre>
					</div>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>
