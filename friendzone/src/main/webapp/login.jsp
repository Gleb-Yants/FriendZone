<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>FriendZone</title>
</head>
<body>
<%
    if (session.getAttribute("user")!=null) response.sendRedirect("myPage.jsp");
%>
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