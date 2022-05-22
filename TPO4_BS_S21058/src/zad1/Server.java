package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class Server {
    static ArrayList<Topic> topicArrayList;
    static ByteBuffer input = ByteBuffer.allocate(2048);
    static ByteBuffer out = ByteBuffer.allocate(2048);
    static Selector selector;
    static ServerSocketChannel serverChanel;
    static ClientSubscribe clientSubscribe;
    static ArrayList<ClientSubscribe> subscribers;
    static boolean updateFlag;
    static boolean removeFlag;
    static boolean addFlag;
    static ArrayList<Topic> tmpRemoveTopic;
    static ArrayList<Topic> tmpAddTopic;
    static ArrayList<Topic> tmpUpdateTopic;


    public static void main(String[] args) throws IOException {
        topicArrayList = new ArrayList<>();
        tmpRemoveTopic =new ArrayList<>();
        tmpAddTopic=new ArrayList<>();
        tmpUpdateTopic=new ArrayList<>();
        subscribers = new ArrayList<>();
        serverChanel = ServerSocketChannel.open();
        serverChanel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 9000);
        serverChanel.bind(inetSocketAddress);
        selector = Selector.open();
        serverChanel.register(selector, SelectionKey.OP_ACCEPT);
//        int ops = serverChanel.validOps();
//        SelectionKey key = serverChanel.register(selector, ops, null);
        System.out.println("SERVER:Waiting for new connection\n");

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (!selectionKey.isValid()) {
                    continue;
                }
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverChanel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("SERVER: connection accepted: " + socketChannel.getLocalAddress() + "\n");

                }
                if (selectionKey.isWritable()) {
                    write(selectionKey);
                }
                if (selectionKey.isReadable()) {
                    read(selectionKey);
                    System.out.println(topicArrayList);
                }
            }
        }
    }

    public static void write(SelectionKey key) {
        try {
            SocketChannel channel = (SocketChannel) key.channel();
//              out.put(helpStr.getBytes(StandardCharsets.UTF_8));
            String helpStr = "";
            for (int i = 0; i < topicArrayList.size(); i++) {
                helpStr += topicArrayList.get(i).nameOfTopic + "," + topicArrayList.get(i).themeOfTopic + "," + topicArrayList.get(i).content + "," + topicArrayList.get(i).authorPseudo + "," + topicArrayList.get(i).dateOfPublication + "\n";
            }
            // System.out.println(helpStr);
            if (updateFlag) {
                System.out.println("UPDATE FLAG TRUE");
                for (int i = 0; i < subscribers.size(); i++) {
                    if (subscribers.get(i).listOfSubs.contains(channel.socket().getPort())) {
                        System.out.println("Second FLAG TRUE");
                        if (subscribers.get(i).topic.nameOfTopic.equals(tmpUpdateTopic.get(0).nameOfTopic)
                                && subscribers.get(i).topic.themeOfTopic.equals(tmpUpdateTopic.get(0).themeOfTopic)
                                && subscribers.get(i).topic.authorPseudo.equals(tmpUpdateTopic.get(0).authorPseudo)) {
                            if (!subscribers.get(i).topic.content.equals(tmpUpdateTopic.get(0).content)) {
                                System.out.println("THIRD IF TRUE");
                                String s = "TOPIC WAS UPDATED,PLEASE CHECK IT\n";
                                out.put(s.getBytes(StandardCharsets.UTF_8));
                                out.flip();
                                channel.write(out);
                                out.clear();
                                updateFlag = false;
                            }
                        }
                    }
                }
                //tmpUpdateTopic.clear();
            }
            if (addFlag) {
                addFlag = false;
                subscribers.add(new ClientSubscribe(tmpAddTopic.get(0)));
                tmpAddTopic.clear();
            }
            if (removeFlag) {
                for (int i = 0; i < subscribers.size(); i++) {
                    if (subscribers.get(i).listOfSubs.contains(channel.socket().getPort())) {
                            if (subscribers.get(i).topic.nameOfTopic.equals(tmpRemoveTopic.get(0).nameOfTopic)) {
                                String s = "TOPIC WAS REMOVED,YOUR SUBSCRIBE WAS CANCELED\n";
                                out.put(s.getBytes(StandardCharsets.UTF_8));
                                out.flip();
                                channel.write(out);
                                out.clear();
                                removeFlag = false;
                                subscribers.remove(subscribers.get(i));
                            }
                    }else{
                        out.put(helpStr.getBytes());
                        out.flip();
                        channel.write(out);
                        out.clear();
                    }
                }
            } else {
                out.put(helpStr.getBytes());
                out.flip();
                channel.write(out);
                out.clear();
            }

            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void read(SelectionKey key) {
        try {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            int read = 0;
            read = clientChannel.read(input);
            if (read > 0) {
                String fromClient = new String(input.array(), 0, read, "UTF-8");
                if (fromClient.contains("Subscribe")) {
                    String[] help = fromClient.split(",");
                    clientSubscribe = new ClientSubscribe(new Topic(help[1], help[2], help[3], help[4], help[5]), clientChannel.socket().getPort());
                    System.out.println(subscribers.size());
                    for (int i = 0; i < subscribers.size(); i++) {
                        if (subscribers.get(i).topic.nameOfTopic.equals(clientSubscribe.topic.nameOfTopic)) {
                            if (subscribers.get(i).listOfSubs.size() > 0) {
                                for (int j = 0; j < subscribers.get(i).listOfSubs.size(); j++) {
                                    if (!subscribers.get(i).listOfSubs.get(j).equals(clientSubscribe.clientPort)) {
                                        subscribers.get(i).addSubs(clientSubscribe.clientPort);
                                    }
                                }
                            } else {
                                subscribers.get(i).addSubs(clientSubscribe.clientPort);
                            }
                        }
                    }

                    System.out.println(subscribers);
                } else if (fromClient.equals("Unsubscribe")) {
                    String[] help = fromClient.split(",");
                    clientSubscribe = new ClientSubscribe(new Topic(help[1], help[2], help[3], help[4], help[5]), clientChannel.socket().getPort());
                    for (int i = 0; i < subscribers.size(); i++) {
                        if (subscribers.get(i).topic.nameOfTopic.equals(clientSubscribe.topic.nameOfTopic)) {
                            if (subscribers.get(i).listOfSubs.contains(clientSubscribe.clientPort)) {
                                subscribers.get(i).listOfSubs.removeIf(t -> t.equals(clientSubscribe.clientPort));
                                System.out.println("REMOVED");
                            }
                        }
                    }
                    System.out.println(subscribers);
                } else {
                    String[] arggg = fromClient.split("\n");
                    if (arggg.length > 1) {
                        ArrayList<ArrayList<String>> list = new ArrayList<>();
                        for (int i = 0; i < arggg.length; i++) {
                            ArrayList<String> list1 = new ArrayList<>();
                            String[] s = arggg[i].split(",");
                            list1.addAll(Arrays.asList(s));
                            list.add(list1);
                        }

                        if (list.get(0).get(0).contains("Update")) {
                            updateFlag=true;
                            addFlag=false;
                            removeFlag=false;
                            tmpUpdateTopic.clear();
                            for (int i = 0; i < topicArrayList.size(); i++) {
                                if (topicArrayList.get(i).nameOfTopic.equals(list.get(1).get(0))) {
                                    if (topicArrayList.get(i).themeOfTopic.equals(list.get(2).get(0))) {
                                        topicArrayList.get(i).setDateOfPublication(list.get(5).get(0));
                                        topicArrayList.get(i).setContent(list.get(3).get(0));
                                        tmpUpdateTopic.add(new Topic(list.get(1).get(0), list.get(2).get(0), list.get(3).get(0), list.get(4).get(0), list.get(5).get(0)));
                                        System.out.println("Updated:  \n" + topicArrayList.get(i).content);
                                        System.out.println(topicArrayList.get(i).dateOfPublication);
                                    }
                                }
                            }
//                            writeToAll(selectionKeySet,s);
                        } else if (list.get(0).get(0).equals("Add")) {
                            topicArrayList.add(new Topic(list.get(1).get(0), list.get(2).get(0), list.get(3).get(0), list.get(4).get(0), list.get(5).get(0)));
                            tmpAddTopic.add(new Topic(list.get(1).get(0), list.get(2).get(0), list.get(3).get(0), list.get(4).get(0), list.get(5).get(0)));
                            System.out.println("Added: \n" + topicArrayList.get(topicArrayList.size() - 1).nameOfTopic);
                            addFlag=true;
                            updateFlag=false;
                            removeFlag=false;
                        } else if (list.get(0).get(0).equals("Remove")) {
                            tmpRemoveTopic.clear();
                            topicArrayList.removeIf(t -> t.nameOfTopic.equals(list.get(1).get(0)) && t.themeOfTopic.equals(list.get(2).get(0)));
                            tmpRemoveTopic.add(new Topic(list.get(1).get(0), list.get(2).get(0), list.get(3).get(0), list.get(4).get(0), list.get(5).get(0)));
                            System.out.println("Removed");
                            removeFlag=true;
                            updateFlag=false;
                            addFlag=false;
                        } else if (list.get(0).get(0).equals("Start")) {
                            for (int i = 0; i < list.size(); i++) {
                                topicArrayList.add(new Topic(list.get(i).get(1), list.get(i).get(2), list.get(i).get(3), list.get(i).get(4), list.get(i).get(5)));

                                subscribers.add(new ClientSubscribe((new Topic(list.get(i).get(1), list.get(i).get(2), list.get(i).get(3), list.get(i).get(4), list.get(i).get(5)))));
                            }
                            System.out.println("Topics uploaded");
                        }

                    } else {
                        System.out.println("FROM CLIENT: " + fromClient);
                        fromClient = "";
                        input.clear();
                        clientChannel.register(selector, SelectionKey.OP_WRITE);
                    }
                }
                clientChannel.register(selector, SelectionKey.OP_WRITE);
                input.clear();
            } else {
                for (int i = 0; i < subscribers.size(); i++) {
                    if (subscribers.get(i).listOfSubs.contains(clientChannel.socket().getPort())) {
                        subscribers.get(i).listOfSubs.removeIf(t -> t.equals(clientChannel.socket().getPort()));
                    }
                }
                key.channel();
                clientChannel.close();
                System.out.println("Timeout");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
