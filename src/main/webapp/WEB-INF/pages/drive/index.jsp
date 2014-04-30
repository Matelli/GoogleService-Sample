<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
    <a href="<%=request.getContextPath()%>/home">< Retour</a>
    <h1>Drive</h1>
    <ul>
        <li><a href="${authorizationUrlGoogleRetrieveAllFiles}">Retourne toutes les images et videos (peut prendre un certain temps selon la quantité d'élément dans votre Drive)</a></li>
        <li><a href="${authorizationUrlGoogleInsertFile}">Insertion d'un nouveau document</a></li>
    </ul>
</body>
</html>