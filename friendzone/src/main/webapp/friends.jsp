<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Friends</title>
</head>
<body>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>
<jsp:useBean id="user" class="model.User" scope="session" />
<jsp:setProperty property="*" name="user"/>

<h>${lang.friends1}</h><br>

<form action="lookUp" method="post">
    <input type="text" name="searchQuery" value="" /> <br />
    <input type="submit" value="${lang.friends2}" /><br />
</form>
<b>${lang.friends3}</b><br>
<c:forEach items="${requestScope.FriendsList}" var="friend">
    <a href="friendGetter?id=${friend.id}">${friend.firstName} ${friend.lastName}</a><br>
</c:forEach>
<br>
<b>${lang.friends4}</b><br>
<c:forEach items="${requestScope.Notifications}" var="friend">
    <a href="friendGetter?id=${friend.id}">${friend.firstName} ${friend.lastName}</a><br>
</c:forEach>
<br>
    <form action="myPage.jsp" method="get">
        <input type="submit" value="${lang.friends5}" /><br />
    </form>
</body>
</html>
