<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>

    <c:if test="${not empty error}">
        <div style="background-color:#FDF7F7; padding:5px; color: #D9534F; border: 1px solid #D9534F">${error}</div>
    </c:if>
    <h1>Connexion SSO Google</h1>

    <a href="${authorizationUrlGoogle}">
        <img width="250px" src="https://developers.google.com/+/images/branding/sign-in-buttons/Red-signin_Long_base_44dp.png" />
    </a>
</body>
</html>