<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title></title>
</head>
<body>
<a href="<%=request.getContextPath()%>/calendar/">< Retour</a>
<h1>Calendar</h1>
Voici les calendriers créer dans votre agenda

<ul>
    <c:forEach var="nameCalendar" items="${listSummaryCalendar}">
        <li>${nameCalendar}</li>
    </c:forEach>
</ul>

</body>
</html>
