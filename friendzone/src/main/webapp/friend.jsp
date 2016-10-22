<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Friend</title>
</head>
<body>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>
<jsp:useBean id="Friend" class="model.Friend" scope="request" />
<jsp:setProperty property="*" name="Friend"/>
<b>${lang.friend1}<jsp:getProperty property="firstName" name="Friend"/> <jsp:getProperty property="lastName" name="Friend"/><b>
    <br ><br />
    <img src=getImage?avatar=<jsp:getProperty property="photo" name="Friend"/>
         width="189" height="255" alt="${lang.friend2}">
    <br ><br />
    <jsp:getProperty property="aboutMe" name="Friend"/>
    <br >
    <form action="friendAdding" method="post">
        <input type="hidden" name="friend_id" value="${requestScope.Friend.id}" />
        <input type="submit" value="${lang.friend3}" /><br />
    </form>

    <form action="conversation.xhtml" method="get">
        <c:set var="friendId" value="${requestScope.Friend.id}" scope="session"  />
        <input type="submit" value="${lang.friend4}" /><br />
    </form>

    <form action="myPage.jsp" method="get">
        <input type="submit" value="${lang.friend5}" /><br />
    </form>


</body>
</html>
