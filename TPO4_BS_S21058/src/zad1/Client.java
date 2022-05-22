package zad1;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client {
    static InetSocketAddress address;
    static SocketChannel socketChannel;
    static private ByteBuffer input = ByteBuffer.allocate(2048);
    static private ByteBuffer out = ByteBuffer.allocate(2048);
    static private boolean stop = false;
    static private Selector selector;

    static DefaultListModel<Topic> defaultListModel = new DefaultListModel<>();
    static JList<Topic> topicJList = new JList<>();
    static ArrayList<Topic> topics;
    static public int clientPort;
    static Topic t;
    static JButton subscribe = new JButton("Subscribe");
    static JButton unSubscribe = new JButton("Unsubscribe");
    static JButton reset = new JButton("Reset");
    static JButton update;
    static Set<ArrayList<String>> set;

    public static void main(String[] args) throws IOException {
        topics = new ArrayList<>();
        set = new LinkedHashSet<>();
        topicJList = new JList<>();
        defaultListModel = new DefaultListModel<>();
        topicJList.setModel(defaultListModel);
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9000));
        clientPort = socketChannel.socket().getLocalPort();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        JFrame frame = new JFrame("CLIENT");
        JLabel label = new JLabel();
        JPanel panel = new JPanel();
        update = new JButton("Update");
        panel.add(label);
        panel.add(update);
        panel.add(subscribe);
        panel.add(unSubscribe);
        panel.add(reset);
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setLeftComponent(new JScrollPane(topicJList));
        jSplitPane.setRightComponent(new JScrollPane(panel));
        topicJList.getSelectionModel().addListSelectionListener(e1 -> {
            t = topicJList.getSelectedValue();
            if (!topicJList.isSelectionEmpty()) {
                label.setText("<html>Topic:" + t.nameOfTopic + "(" + t.themeOfTopic + ")" + "<br/>" +
                        t.content + "<br/>" +
                        "Authority by: " + t.authorPseudo + "<br/>" +
                        "Posted on: " + t.dateOfPublication + "</html>");
            } else {
                label.setText("");
            }
        });
        reset.addActionListener(e -> {
            topicJList.clearSelection();
        });
        frame.add(jSplitPane);
        frame.setVisible(true);
        frame.setBounds(500, 200, 500, 500);

        while (!stop) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                try {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (!selectionKey.isValid()) {
                        continue;
                    }
                    if (selectionKey.isConnectable()) {
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
                        if (socketChannel.isConnectionPending()) {
                            System.out.println("Connection...");
                            sc.finishConnect();
                        }

                        System.out.println("Connection successfully...");
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_WRITE);
                    }

                    if (selectionKey.isReadable()) {
                        read(selectionKey);
                       // System.out.println(topics);
                    }
                    try {
                        if (selectionKey.isWritable()) {
                            write(selectionKey);
                        }
                    } catch (CancelledKeyException e) {
                        selectionKey.cancel();
                        stop = true;
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void read(SelectionKey key) {
        try {
            int tmpSize=0;
            SocketChannel channel = (SocketChannel) key.channel();
            int read = channel.read(input);
            if (read > 0) {
                String fromServer = new String(input.array(), 0, read, "UTF-8");
//                System.out.println(fromServer+"\n");
                if (fromServer.contains("arrived")) {
                    System.out.println("FROM SERVER: " + fromServer);
                } else if(fromServer.contains("WAS")) {
                    System.out.println(fromServer+"\n");
                    input.clear();
                }else{
                    String[] arggg = fromServer.split("\n");
                    ArrayList<List<String>> list = new ArrayList<>();
                    for (int i = 0; i < arggg.length; i++) {
                        String[] s = arggg[i].split(",");
                        ArrayList<String> list1 = new ArrayList<String>(Arrays.asList(s));
                        set.addAll(Collections.singleton(list1));
                    }
                    list.addAll(set);
                    set.clear();
                    //System.out.println(list);
                    tmpSize=defaultListModel.size();
                    defaultListModel.clear();
                    for (int i = 0; i < list.size(); i++) {
                        defaultListModel.addElement(new Topic(list.get(i).get(0), list.get(i).get(1), list.get(i).get(2), list.get(i).get(3), list.get(i).get(4)));
                    }
                    if((defaultListModel.size()>tmpSize)&&tmpSize!=0){
                        System.out.println(defaultListModel.get(defaultListModel.size()-1)+" <- NEW TOPIC,GO AND CHECK");
                    }
                    tmpSize=0;
                    update.setVisible(true);
                    channel.register(selector, SelectionKey.OP_WRITE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        input.clear();
    }

    private static void write(SelectionKey key) throws ClosedChannelException {

        SocketChannel channel = (SocketChannel) key.channel();
        update.addActionListener(e -> {
            try {
                out.put("Update".getBytes(StandardCharsets.UTF_8));
                out.flip();
                channel.write(out);
                out.clear();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        subscribe.addActionListener(e -> {
            try {
                String s = "Subscribe," + t.nameOfTopic + "," + t.themeOfTopic + "," + t.content + "," + t.authorPseudo + "," + t.dateOfPublication;
               // System.out.println(s);
                out.put(s.getBytes(StandardCharsets.UTF_8));
                out.flip();
                channel.write(out);
                out.clear();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        unSubscribe.addActionListener(e -> {
            try {
                String s = "Unsubscribe," + t.nameOfTopic + "," + t.themeOfTopic + "," + t.content + "," + t.authorPseudo + "," + t.dateOfPublication;
              //  System.out.println(s);
                out.put(s.getBytes(StandardCharsets.UTF_8));
                out.flip();
                channel.write(out);
                out.clear();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        channel.register(selector, SelectionKey.OP_READ);
    }
}

