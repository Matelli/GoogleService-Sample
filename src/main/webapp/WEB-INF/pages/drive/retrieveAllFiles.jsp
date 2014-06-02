<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<a href="<%=request.getContextPath()%>/drive">< Retour</a>
<h1>Drive</h1>

    <c:if test="${not empty error}">
        ${error}
    </c:if>

    <table>
        <c:forEach var="file" items="${files}">
            <tr>
                <td><img src="${file.thumbnailLink}" border="1px" width="150px"/></td>
                <td>
                    <ul>
                        <li><img src="${file.iconLink}" /> <a href="${file.alternateLink}" target="_blank">Edit</a> | <a href="${file.webContentLink}">download</a> </li>
                        <li>title : ${file.title}</li>
                        <li>createdDate : ${file.createdDate}</li>
                        <li>lastModifyingUserName : ${file.lastModifyingUserName}</li>
                        <li>mimeType : ${file.mimeType}</li>
                        <li>lastModifyingUserName : ${file.lastModifyingUserName}</li>
                        <li>modifiedDate : ${file.modifiedDate}</li>
                        <li>lastViewedByMeDate : ${file.lastViewedByMeDate}</li>
                        <li>shared : ${file.shared}</li>
                    </ul>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>