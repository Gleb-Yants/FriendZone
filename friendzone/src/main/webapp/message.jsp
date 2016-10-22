<html>
<head>
    <title>Message</title>
</head>
<body>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>
    <h3><%=request.getAttribute("Message")%></h3>
    <form action="myPage.jsp" method="get">
        <input type="submit" value="${lang.message1}" /><br />
    </form>

</body>
</html>