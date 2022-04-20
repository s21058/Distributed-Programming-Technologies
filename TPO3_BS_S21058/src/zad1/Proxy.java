package zad1;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy {
    static ServerSocket proxySocket;
    static Map<String, Integer> mapOfServers;

    public static void main(String[] args) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        mapOfServers = new TreeMap<>();
        try {
            proxySocket = new ServerSocket(9000);
            while (true) {
                Socket fromClientSocket = proxySocket.accept();
                System.out.println("accept");
                service.execute(new Handler(fromClientSocket));
            }
        } catch (IOException exception) {
            exception.printStackTrace();

        }
    }
}
