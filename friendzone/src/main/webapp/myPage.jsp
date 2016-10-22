<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>My Page</title>
</head>
<body>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>
<jsp:useBean id="user" class="model.User" scope="session" />
<jsp:setProperty property="*" name="user"/>
<b>${lang.myPage1} <jsp:getProperty property="firstName" name="user"/> <jsp:getProperty property="lastName" name="user"/><b>

    <br /><br />
    <img src="getImage?avatar=${sessionScope.user.photo}"
                 width="189" height="255" alt="${lang.myPage2}">
    <br /><br><br>

    <form action="friendsList" method="get">
    <input type="submit" value="${lang.myPage3}" /><br />
</form><br />

    <jsp:getProperty property="aboutMe" name="user"/><br /><br />

    <form action="settings.jsp" method="get">
        <input type="submit" value="${lang.myPage4}" /><br />
    </form>

    <form action="logOut" method="get">
        <input type="submit" value="${lang.myPage5}" /><br />
    </form>


</body>
</html>
