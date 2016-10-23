<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
Main page for log in. Page contains redirect to my page if user yet logged in,
fields for email and pass to log in, registration field and language changer
--%>
<html>
<head>
    <title>FriendZone</title>
</head>
<body>
<c:if test="${sessionScope.user!=null}">
    <jsp:forward page="myPage.jsp" />
</c:if>
<%--
Getting language properties from session or setting default english
--%>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>

<header>
    <h2>${lang.login1}</h2>
</header>
<b>${lang.login2}<b>
    <form action="myPage.jsp" method="post">

        <input type="text" name="login" value="" /> <br />

        <input type="password" name="pass" value="" /> <br />

        <input type="submit" value="${lang.login3}" /><br />

    </form>

    <form action="registration.jsp" method="get">
        <input type="submit" value="${lang.login4}" /><br />
    </form>

    <form action="langChanger" method="get">
        <select name="language" size="1">
            <option value=en>${lang.login5}</option>
            <option value=ru>${lang.login6}</option></select>
        <input type="submit" value="${lang.login7}" /><br />
    </form>
</body>
</html>