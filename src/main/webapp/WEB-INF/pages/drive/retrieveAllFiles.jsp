<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<a href="<%=request.getContextPath()%>/home">< Retour</a>
<h1>Drive</h1>

    <c:if test="${not empty error}">
        ${error}
    </c:if>

    <table>
        <c:forEach var="file" items="${files}">
            <tr>
                <td><img src="${file.thumbnailLink}" border="1px" width="70px"/></td>
                <td>
                    <ul>
                        <li><img src="${file.iconLink}" /> <a href="${file.alternateLink}">Edit</a> | <a href="${file.embedLink}">Html</a></li>
                        <li>title : ${file.title}</li>
                        <li>createdDate : ${file.createdDate}</li>
                        <li>lastModifyingUserName : ${file.lastModifyingUserName}</li>
                    </ul>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>