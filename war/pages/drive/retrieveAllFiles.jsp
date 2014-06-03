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
			<a href="<%=request.getContextPath()%>/drive/">Retour</a>
			<h2>Toutes les images et videos</h2>
			<c:if test="${not empty error}">
    			${error}
			</c:if>
			<c:if test="${not empty files}">
				<div class="col-lg-12">
					<c:forEach var="file" items="${files}">
						<div class="row marketing">
							<div class="col-lg-3">
								<img src="${file.thumbnailLink}" border="1px" width="100px"/>
							</div>
							<div class="col-lg-9">
								<img src="${file.iconLink}" /> <a href="${file.alternateLink}" target="_blank">Edit</a> | <a href="${file.webContentLink}">download</a><br/>
				                title : ${file.title}<br/>
				                createdDate : ${file.createdDate}<br/>
				                lastModifyingUserName : ${file.lastModifyingUserName}<br/>
				                mimeType : ${file.mimeType}<br/>
				                lastModifyingUserName : ${file.lastModifyingUserName}<br/>
				                modifiedDate : ${file.modifiedDate}<br/>
				                lastViewedByMeDate : ${file.lastViewedByMeDate}<br/>
								shared : ${file.shared}<br/>
				        	</div>
						</div>
					</c:forEach>
				</div>
			</c:if>
		</div>
		<c:if test="${not empty files}">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Données reçues par Google</h3>
				</div>
				<div class="panel-body">
					<pre><script>
						document.write(JSON.stringify(${files}, null, 4));
					</script></pre>
				</div>
			</div>
		</c:if>
	</div>
</body>
</html>