<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<a href="<%=request.getContextPath()%>/userinfo/">< Retour</a>
<h1>${user.given_name} ${user.family_name}</h1>
<table>
    <tr>
        <td><img src="${user.picture}" width="130px"></td>
        <td>
            <ul>
                <li>Id : ${user.id}</li>
                <li>Email : ${user.email}</li>
                <li>Gender : ${user.gender}</li>
                <li>Google + : <a href="${user.link}">${user.link}</a></li>
                <li>locale : ${user.locale}</li>
            </ul>
        </td>
    </tr>
</table>
</body>
</html>