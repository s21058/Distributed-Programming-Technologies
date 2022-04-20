package zad1;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Client extends JFrame {
    static String request;
    static PrintWriter writer;
    static ServerSocket serverSocket;
    static BufferedReader bufferedReader;
    static Socket sock;
    static int count;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setBounds(500, 200, 500, 200);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JTextField field = new JTextField();
        JTextArea area = new JTextArea();
        Font font = new Font("Serif", Font.BOLD, 25);
        area.setBounds(250, 20, 200, 80);
        field.setBounds(20, 20, 200, 30);
        area.setFont(font);
        panel.add(area);
        Socket socket = new Socket("localhost", 9000);
        count=0;
        panel.add(field);
        JButton apply = new JButton("Apply");
        apply.setBounds(20, 50, 200, 50);
        apply.addActionListener(e -> {
             request=field.getText();
             System.out.println(request);
             String[]strings=request.split(",");
             try {
                 PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
                 writer.write(strings[0]+","+strings[1]+","+strings[2]);
              //   count++;
                 writer.close();
                 serverSocket=new ServerSocket(Integer.parseInt(strings[2]));
                 Socket socket1=serverSocket.accept();
                 InputStreamReader inputStreamReader=new InputStreamReader(socket1.getInputStream());
                 BufferedReader reader= new BufferedReader(inputStreamReader);
                 String fromReader=reader.readLine();
                 System.out.println(fromReader);
                 area.setText(fromReader);
                 if(fromReader.contains("not")){
                     serverSocket.close();
                     main(args);
                     frame.dispose();
                 }else{
                     socket.close();
                     socket1.close();
                     serverSocket.close();
                     reader.close();
                 }

             } catch (IOException exception) {
                 exception.printStackTrace();
             }
        });
        panel.add(apply);
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public static void close(ServerSocket socket,Frame frame) throws IOException {
        writer.close();
        try {
            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        socket.close();
        sock.close();
        frame.dispose();
    }
}
