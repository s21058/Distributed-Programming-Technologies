package zad1;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public class Handler  implements Runnable {

    Socket fromClientSocket;

    public Handler(Socket fromClientSocket) {
        this.fromClientSocket = fromClientSocket;
    }

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(fromClientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String str = reader.readLine();
            String[] strings = str.split(",");
            if (strings.length == 2) {
                Proxy.mapOfServers.put(strings[0], Integer.valueOf(strings[1]));
                System.out.println("Active Servers: " + Proxy.mapOfServers.entrySet());
            } else {
                boolean check = Proxy.mapOfServers.containsKey(strings[1]);
                if (check) {
                    System.out.println(Arrays.toString(Arrays.stream(strings).toArray()));
                    for (Map.Entry<String, Integer> value : Proxy.mapOfServers.entrySet()) {
                        if (value.getKey().equals(strings[1])) {
                            Socket socket = new Socket("localHost", value.getValue());
                            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                            InetAddress address = fromClientSocket.getInetAddress();
                            printWriter.write(strings[0] + "," + address + "," + strings[2]);
                            printWriter.close();
                            socket.close();
                        }
                    }
                } else {
                    Socket toClient = new Socket("localhost", Integer.parseInt(strings[2]));
                    PrintWriter writer = new PrintWriter(toClient.getOutputStream(), true);
                    writer.write("Such language code " + "[" + strings[1] + "] " + "does not exists");
                    writer.close();
                    toClient.close();
                    fromClientSocket.close();

                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
