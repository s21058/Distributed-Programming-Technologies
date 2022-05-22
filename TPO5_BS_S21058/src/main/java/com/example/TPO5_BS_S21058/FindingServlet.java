package com.example.TPO5_BS_S21058;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@WebServlet(name = "FiningServlet",urlPatterns = "/FindingServlet")
public class FindingServlet extends HttpServlet {
    static String url = "jdbc:mysql://127.0.0.1:3306/CardInfo";
    private Connection connection;
    static PreparedStatement preparedStatement;
    private ArrayList<Car> cars;
    static PrintWriter out;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
           String stringe= (String)request.getAttribute("Car");
           String[]strings=stringe.split(",");
            String str=strings[0];
            String str1=strings[1];
            String str2=strings[2];
            String str3=strings[3];
        ConnectionToDb(url,str,str1,str2,str3);
        doPost(request, response);
        cars.clear();
        str="";
        str1="";
        str2="";
        str3="";
    }
    public void doPost(HttpServletRequest request,HttpServletResponse response){
        request.setAttribute("Car",cars);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ShowJsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    public void ConnectionToDb(String url, String str, String str1, String str2, String str3) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, "root", "root");
            if (connection == null) {
                out.println("<html><body>");
                out.println("<h1>" + "Failed Connection to DataBase" + "</h1>");
                out.println("</body></html>");
            }
            if (str.trim().isEmpty() && !str1.trim().isEmpty()) {
                preparedStatement = connection.prepareStatement("SELECT typeCar,mark,yearProd,fuelConsumtion FROM cars WHERE  mark=? AND yearProd>=? AND fuelConsumtion>=?");
                preparedStatement.setString(1, str1);
                preparedStatement.setString(2, str2);
                preparedStatement.setString(3, str3);
            } else if (!str.trim().isEmpty() && str1.trim().isEmpty()) {
                preparedStatement = connection.prepareStatement("SELECT typeCar,mark,yearProd,fuelConsumtion FROM cars WHERE typeCar=? AND yearProd>=? AND fuelConsumtion>=?");
                preparedStatement.setString(1, str);
                preparedStatement.setString(2, str2);
                preparedStatement.setString(3, str3);
            } else if (!str.trim().isEmpty() && !str1.trim().isEmpty()) {
                preparedStatement = connection.prepareStatement("SELECT typeCar,mark,yearProd,fuelConsumtion FROM cars WHERE  typeCar=? AND mark=? AND yearProd>? AND fuelConsumtion>?");
                preparedStatement.setString(1, str);
                preparedStatement.setString(2, str1);
                preparedStatement.setString(3, str2);
                preparedStatement.setString(4, str3);
            } else if(!str2.trim().isEmpty()&&str3.trim().isEmpty()){
                preparedStatement = connection.prepareStatement("SELECT typeCar,mark,yearProd,fuelConsumtion FROM cars WHERE  yearProd=?");
                preparedStatement.setString(1, str2);
            }else if(str2.trim().isEmpty()&&!str3.trim().isEmpty()){
                preparedStatement = connection.prepareStatement("SELECT typeCar,mark,yearProd,fuelConsumtion FROM cars WHERE  fuelConsumtion=?");
                preparedStatement.setString(1, str3);
            }else if(!str2.trim().isEmpty()&&!str3.trim().isEmpty()){
                preparedStatement = connection.prepareStatement("SELECT typeCar,mark,yearProd,fuelConsumtion FROM cars WHERE  yearProd=? AND fuelConsumtion=?");
                preparedStatement.setString(1, str2);
                preparedStatement.setString(2, str3);
            }else if(str.trim().isEmpty()&&str1.trim().isEmpty()&&str2.trim().isEmpty()&&str3.trim().isEmpty()){
                preparedStatement=connection.prepareStatement("SELECT typeCar,mark,yearProd,fuelConsumtion FROM cars WHERE  typeCar=null AND mark=null AND yearProd>null AND fuelConsumtion=null");
            }
            var resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = preparedStatement.getMetaData();
            int countColumn = metaData.getColumnCount();
            ArrayList<String> carArrayList = new ArrayList<>();
            cars = new ArrayList<>();
            while (resultSet.next()) {
                for (int i = 1; i <= countColumn; i++) {
                    carArrayList.add(resultSet.getString(i));
                }
                cars.add(new Car(carArrayList.get(0), carArrayList.get(1), Integer.parseInt(carArrayList.get(2)), Integer.parseInt(carArrayList.get(3))));
//                System.out.println(carArrayList);
                carArrayList.clear();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
