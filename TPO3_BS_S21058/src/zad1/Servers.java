package zad1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class Servers {
    static boolean isRun = true;
    static Map<String, String> languageMap;
    static String language;
    static int port;


    public static void main(String[] args) throws IOException {
        language = args[0];
        languageMap = new TreeMap<>();
        languageMap.putAll(fillMap(language));
        ServerSocket serverSocket = new ServerSocket(ThreadLocalRandom.current().nextInt(3000, 4999 + 1));
        port = serverSocket.getLocalPort();
        System.out.println(port);
        Socket toProxy = new Socket("localhost", 9000);
        PrintWriter writer = new PrintWriter(toProxy.getOutputStream(), true);
        writer.println(language + "," + port);
        writer.close();
        toProxy.close();
        while (isRun) {
            Socket socket = serverSocket.accept();
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String str = reader.readLine();
            String[] strings = str.split(",");
            System.out.println(Arrays.toString(Arrays.stream(strings).toArray()));
            boolean chek = languageMap.containsKey(strings[0]);
            if (chek) {
                for (Map.Entry<String, String> value : languageMap.entrySet()) {
                    if (value.getKey().equals(strings[0])) {
                        Socket toClientSoket = new Socket("localhost", Integer.parseInt(strings[2]));
                        PrintWriter writerToClient = new PrintWriter(toClientSoket.getOutputStream(), true);
                        writerToClient.write(value.getValue());
                        writerToClient.close();
                        toClientSoket.close();
                    }
                }
            } else {
                System.out.println(chek);
                Socket toClientSoket = new Socket("localhost", Integer.parseInt(strings[2]));
                PrintWriter writerToClient = new PrintWriter(toClientSoket.getOutputStream(), true);
                writerToClient.write("Such word " + strings[0] + " does not exists");
                writerToClient.close();
                System.out.println(strings[2]);
            }
        }
    }

    public static Map<String, String> fillMap(String nameOfLanguage) {
        Map<String, String> map = new TreeMap<>();
        Path path = Paths.get(nameOfLanguage + ".txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(path)));
            String str = reader.readLine();
            while (str != null) {
                String[] strings = str.split(",");
                map.put(strings[0],strings[1]);
                str = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static int getPort() {
        return port;
    }

    public static Map<String, String> getLanguageMap() {
        return languageMap;
    }

    public static String getLanguage() {
        return language;
    }
}
