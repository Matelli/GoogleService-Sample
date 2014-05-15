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
				<li><a href="<%=request.getContextPath()%>/">SSO (OAuth 2.0)</a></li>
				<li><a href="<%=request.getContextPath()%>/calendar/">Calendar</a></li>
				<li class="active"><a href="<%=request.getContextPath()%>/drive/">Drive</a></li>
				<li><a href="<%=request.getContextPath()%>/userinfo/logout">Logout</a></li>
			</ul>
			<h3 class="text-muted">Matelli GoogleService</h3>
		</div>
		<div class="jumbotron">
			<h1>Service Drive</h1>
			<p class="lead"></p>
		</div>
		<div class="row marketing">
			<h3><a href="${authorizationUrlGoogleRetrieveAllFiles}">Toutes les images et videos</a></h2>
		    <p>Retourne toutes les images et videos (peut prendre un certain temps selon la quantité d'élément dans votre Drive). Ceci vous montre comment faire des recherches dans un compte Google Drive.</p>
		    
		    <!-- <h3><a href="${authorizationUrlGoogleInsertFile}">Nouveau document</a></h3>
		    <p>Cela vous rajoutera un nouveau fichier à la racine de votre Google Drive.</p> -->
		</div>
	</div>
</body>
</html>