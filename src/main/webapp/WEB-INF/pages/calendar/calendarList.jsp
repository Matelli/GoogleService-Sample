<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title></title>
</head>
<body>
<a href="<%=request.getContextPath()%>/calendar/">< Retour</a>
<h1>Calendar</h1>
<table>
    <tr>
        <th>accessRole</th>
        <th>description</th>
        <th>summary</th>
        <th>timeZone</th>
    </tr>
    <c:forEach var="calendar" items="${calendarListEntries}">
        <tr>
            <td>${calendar.accessRole}</td>
            <td>${calendar.description}</td>
            <td>${calendar.summary}</td>
            <td>${calendar.timeZone}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
