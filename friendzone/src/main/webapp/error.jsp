<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isErrorPage="true" %>
<%--
Error page outputs information about exception, request uri, status code, stack trace
--%>
<html>
<head>
    <title>Show Error Page</title>
</head>
<body>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>
<h1>Opps...</h1>
<table width="100%" border="1">
    <tr valign="top">
        <td width="40%"><b>Error:</b></td>
        <td>${pageContext.exception}</td>
    </tr>
    <tr valign="top">
        <td><b>URI:</b></td>
        <td>${pageContext.errorData.requestURI}</td>
    </tr>
    <tr valign="top">
        <td><b>Status code:</b></td>
        <td>${pageContext.errorData.statusCode}</td>
    </tr>
    <tr valign="top">
        <td><b>Stack trace:</b></td>
        <td>
            <c:forEach var="trace"
                       items="${pageContext.exception.stackTrace}">
                <p>${trace}</p>
            </c:forEach>
        </td>
    </tr>
</table>
<form action="login.jsp" method="get">
    <input type="submit" value="${lang.error1}" /><br />
</form>
</body>
</html>