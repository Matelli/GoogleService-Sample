<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title></title>
</head>
<body>
<a href="<%=request.getContextPath()%>/calendar/calendarList">< Retour</a>
<h1>Liste des evenements</h1>
Voici les calendriers créer dans votre agenda "${calendarId}"

<table>
    <tr>
        <th>Nom</th>
        <th>created</th>
        <th>creator</th>
        <th>Start</th>
        <th>End</th>
        <th>updated</th>
    </tr>
    <c:forEach var="event" items="${events}">
        <tr>
            <td><a href="${event.htmlLink}">${event.summary}</a></td>
            <td>${event.created}</td>
            <td>${event.creator.email}</td>
            <td>${event.start.dateTime}</td>
            <td>${event.end.dateTime}</td>
            <td>${event.updated}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
