<%--
Registration page
--%>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>
<b>${lang.registration1}<b>
    <form action="registrator" method="post">

        <input type="text" name="email" /> <br />

        <input type="password" name="password" /> <br />

        <input type="submit" value="${lang.registration2}" /><br />

    </form>
</body>
</html>
