<html>
<head>
    <title>Settings</title>
</head>
<body>
<jsp:useBean id="lang" class="java.util.Properties" scope="session" >
    <c:set var = "lang" scope = "session" value = "${applicationScope.en}" />
</jsp:useBean>
<b1>${lang.settings1}</b1>
<br><br />
    <b>${lang.settings2}</b><br />
    <form method="post" action="uploadServlet" enctype="multipart/form-data">
            <tr>
                <td>Photo: </td>
                <td><input type="file" name="photo" size="50"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="${lang.settings3}">
                </td>
            </tr>
    </form>
<form action="changer" method="post">
    <input type="hidden" name="command" value="fName" />
    <input type="text" name="fName" value="" /> <br />
    <input type="submit" value="${lang.settings4}" /><br />
</form>
<form action="changer" method="post">
    <input type="hidden" name="command" value="lName" />
    <input type="text" name="lName" value="" /> <br />
    <input type="submit" value="${lang.settings5}" /><br />
</form>
<form action="changer" method="post">
    <input type="hidden" name="command" value="phone" />
    <input type="text" name="phone" value="" /> <br />
    <input type="submit" value="${lang.settings6}" /><br />
</form>
<form action="changer" method="post">
    <input type="hidden" name="command" value="me" />
    <input type="text" name="me" value="" /> <br />
    <input type="submit" value="${lang.settings7}" /><br />
</form>
<form action="langChanger" method="get">
    <select name="language" size="1">
        <option value=en>${lang.settings8}</option>
        <option value=ru>${lang.settings9}</option></select>
    <input type="submit" value="${lang.settings10}" /><br />
</form>
<form action="myPage.jsp" method="get">
    <input type="submit" value="${lang.settings11}" /><br />
</form>
</body>
</html>
