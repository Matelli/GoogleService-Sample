<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
    <table border="0">
        <tr>
            <td>
                <img width="130px" src="${user.picture}" />
            </td>
            <td>
                <a href="<%=request.getContextPath()%>/userinfo/logout"><b>Logout</b></a><br>
                Bonjour, ${user.given_name} ${user.family_name}<br>
                Id : ${user.id}<br>
                Email : ${user.email}<br>
                Gender : ${user.gender}<br>
                Google + : <a href="${user.link}">${user.link}</a><br>
                locale : ${user.locale}<br>
            </td>
        </tr>
    </table>
	<h1>Exemple d'utilisation des Services Googles</h1>
    <ul>
        <li><a href="<%=request.getContextPath()%>/drive/">Drive</a></li>
        <li><a href="<%=request.getContextPath()%>/calendar/">Calendar</a></li>
    </ul>
</body>
</html>