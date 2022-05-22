<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Welcome to start page"%>
</h1>
<br/>
<form action="sending-to-another-servlet" method="get">
    Car Type: <input type="text"  name="type"/><br><br>

    Car Mark: <input type="text"  name="mark"/><br><br>

    Year of Production: <input type="text"  name="year"/><br><br>

    Fuel Consumption: <input type="text"  name="fuel consumption"/><br><br>
    <input type="submit">
</form>
</body>
</html>