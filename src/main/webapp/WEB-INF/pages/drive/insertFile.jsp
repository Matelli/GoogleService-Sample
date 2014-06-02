<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<a href="<%=request.getContextPath()%>/drive">< Retour</a>
<h1>Drive</h1>

<c:if test="${not empty error}">
    ${error}
</c:if>

<c:if test="${not empty fileInserted}">
    <img src="${fileInserted.iconLink}" /> title : <a href="${fileInserted.alternateLink}" target="_blank">${fileInserted.title}</a><br>
    id : ${fileInserted.id}<br>
    createdDate : ${fileInserted.createdDate}<br>
    mimeType : ${fileInserted.mimeType}
</c:if>
<c:if test="${empty fileInserted}">
    Erreur : Aucun fichier mis sur Google Drive
</c:if>
</body>
</html>