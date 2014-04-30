<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
    <a href="<%=request.getContextPath()%>/home">< Retour</a>
    <h1>Calendar</h1>

    <c:if test="${not empty authorizationUrlGoogleCalendar}">
        <a href="${authorizationUrlGoogleCalendar}">OAuth Off</a>
    </c:if>
    <c:if test="${empty authorizationUrlGoogleCalendar}">
        <ul>
            <li><a href="<%=request.getContextPath()%>/calendar/calendarInsert">Creation de 3 nouveaux calendriers</a></li>
            <li><a href="<%=request.getContextPath()%>/calendar/calendarList">Liste vos prochain évenement</a></li>
        </ul>
    </c:if>
</body>
</html>