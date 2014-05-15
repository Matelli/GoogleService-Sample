<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
				<li class="active"><a href="<%=request.getContextPath()%>/">SSO (OAuth 2.0)</a></li>
				<li><a href="<%=request.getContextPath()%>/calendar/">Calendar</a></li>
				<li><a href="<%=request.getContextPath()%>/drive/">Drive</a></li>
				<li><a href="<%=request.getContextPath()%>/userinfo/logout">Logout</a></li>
			</ul>
			<h3 class="text-muted">Matelli GoogleService</h3>
		</div>
		<div class="jumbotron">
			<h1>SSO (OAuth 2.0)</h1>
			<p class="lead"></p>
		</div>
		<div class="row marketing">
			<div class="col-lg-3">
				<img width="130px" src="${user.picture}" />
			</div>
			<div class="col-lg-9">
				Bonjour, ${user.given_name} ${user.family_name}<br> Id :
				${user.id}<br> Email : ${user.email}<br> Gender :
				${user.gender}<br> Google + : <a href="${user.link}">${user.link}</a><br>
				locale : ${user.locale}
			</div>
		</div>
		<div class="row marketing">	
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Données reçues par Google</h3>
				</div>
				<div class="panel-body">
					<pre><script>
						document.write(JSON.stringify(${user}, null, 4));
					</script></pre>
				</div>
			</div>
		</div>
	</div>
</body>
</html>