<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.TPO5_BS_S21058.Car" %>
<%--
  Created by IntelliJ IDEA.
  User: Sviatoslav
  Date: 15.05.2022
  Time: 21:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Student List</title>
</head>
<body>
<h1 align="center">Displaying Cars List </h1>
<hr>
<table border ="1" width="500" align="center">
    <tr bgcolor="00FF7F">
        <th><b>Car Type</b></th>
        <th><b>Car Mark</b></th>
        <th><b>Year Production</b></th>
        <th><b>Fuel Consumption</b></th>
    </tr>
<%----%>
    <%
        ArrayList<Car> car = (ArrayList<Car>)request.getAttribute("Car");
        for(Car s: car){
    %>
    <tr>
        <td><%=s.getType()%></td>
        <td><%=s.getMark()%></td>
        <td><%=s.getYearOfProduction()%></td>
        <td><%=s.getFuelConsumption()%></td>
    </tr>
    <%}
    car.clear();%>
</table>
<hr/>
</body>
</html>
