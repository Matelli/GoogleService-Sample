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

<table>
    <tr>
        <th colspan="2">Nom</th>
        <th>description</th>
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

</body>
</html>
