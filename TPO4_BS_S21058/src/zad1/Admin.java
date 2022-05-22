package zad1;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Admin extends JFrame {
    static JFrame frame;
    static JPanel panel;
    static JList<Topic> topicJList;
    static DefaultListModel<Topic> defaultListModel;
    static JSplitPane jSplitPane;
    static JLabel label;
    static JPanel panel1;
    static JFrame afterButtonFrame;
    static Topic t;
    static JTextArea changeContent;
    static ArrayList<Topic> topics;
   static InetSocketAddress address;
    static SocketChannel clientChannel;

    public static void main(String[] args) throws IOException {
      address= new InetSocketAddress("localhost",9000);
       clientChannel =SocketChannel.open(address);
        topics = new ArrayList<>();
        frame = new JFrame("ADMIN");
        label = new JLabel();
        panel1 = new JPanel();
        topicJList = new JList<>();
        defaultListModel = new DefaultListModel<>();
        topicJList.setModel(defaultListModel);
        jSplitPane = new JSplitPane();
        fillTopicList();
        //---------------------------------------------------------------------------------------------------------------------------------------------------------------
        JButton updateButton = new JButton("Update Topic");
        updateButton.setBounds(0, 0, 500, 100);
        JButton removeButton = new JButton("Remove Topic");
        removeButton.setBounds(250, 100, 250, 50);
        JButton addButton = new JButton("Add Topic");
        addButton.setBounds(0, 100, 250, 50);
        //----------------------------------------------------------------------------------------------------------------------------------------------------------
        panel = new JPanel();
        panel.setLayout(null);
        panel.add(updateButton);
        panel.add(removeButton);
        panel.add(addButton);
        frame.add(panel);
        frame.setVisible(true);
        frame.setBounds(500, 200, 500, 185);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton update = new JButton("Update");
        JButton reset = new JButton("Reset");
        panel1.add(update);
        panel1.add(reset);
        StringBuilder str= new StringBuilder();
        for (int i=0;i<topics.size();i++) {
          str.append("Start,").append(topics.get(i).nameOfTopic).append(",").append(topics.get(i).themeOfTopic).append(",").append(topics.get(i).content).append(",").append(topics.get(i).authorPseudo).append(",").append(topics.get(i).dateOfPublication).append("\n");
        }
            ByteBuffer byteBuffer1 = ByteBuffer.wrap(str.toString().getBytes(StandardCharsets.UTF_8));
            clientChannel.write(byteBuffer1);
            byteBuffer1.clear();
        updateButton.addActionListener(e -> {
            JFrame frame = new JFrame("Updating");
            jSplitPane.setLeftComponent(new JScrollPane(topicJList));
            panel1.add(label);
            jSplitPane.setRightComponent(new JScrollPane(panel1));
            topicJList.getSelectionModel().addListSelectionListener(e1 -> {

                t = topicJList.getSelectedValue();
                method();
                afterButtonFrame.setVisible(false);
                if (!topicJList.isSelectionEmpty()) {
                    label.setText("<html>Topic:" + t.nameOfTopic + "(" + t.themeOfTopic + ")" + "<br/>" +
                            t.content + "<br/>" +
                            "Authority by: " + t.authorPseudo + "<br/>" +
                            "Posted on: " + t.dateOfPublication + "</html>");
                } else {
                    label.setText("");
                }
            });
            update.addActionListener(e2 -> {
                changeContent.setText("");
                afterButtonFrame.setVisible(true);
            });
            reset.addActionListener(e4 -> {
                topicJList.clearSelection();
            });

            frame.add(jSplitPane);
            frame.pack();
            frame.setBounds(500, 200, 500, 500);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });

        removeButton.addActionListener(e -> {
            JFrame frame = new JFrame("Remove");
            JLabel label = new JLabel();
            JPanel panel = new JPanel();
            JSplitPane jSplitPane = new JSplitPane();
            jSplitPane.setLeftComponent(new JScrollPane(topicJList));
            JButton remove = new JButton("Remove");
            panel.add(label);
            panel.add(remove);
            jSplitPane.setRightComponent(new JScrollPane(panel));
            topicJList.getSelectionModel().addListSelectionListener(e1 -> {
                t = topicJList.getSelectedValue();

            });
            remove.addActionListener(e3 -> {
                topics.removeIf(top -> top.authorPseudo.equals(t.authorPseudo) && top.themeOfTopic.equals(t.themeOfTopic));
                try {
                    byte[] massage = new String("Remove\n"+t.nameOfTopic+"\n"+t.themeOfTopic+"\n"+t.content+"\n"+t.authorPseudo+"\n"+t.dateOfPublication).getBytes();
                    ByteBuffer byteBuffer= ByteBuffer.wrap(massage);
                    clientChannel.write(byteBuffer);
                    byteBuffer.clear();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                defaultListModel.removeElement(t);
            });
            frame.add(jSplitPane);
            frame.pack();
            frame.setBounds(500, 200, 400, 400);
            frame.setVisible(true);
        });

        addButton.addActionListener(e5 -> {
            JFrame frame = new JFrame("Add");
            JLabel label = new JLabel();
            JPanel panel = new JPanel();
            JButton reset1=new JButton("Reset");
            panel.add(label);
            panel.add(reset1);
            JSplitPane jSplitPane = new JSplitPane();
            jSplitPane.setLeftComponent(new JScrollPane(topicJList));
            JButton addTopic = new JButton("Add");
            panel.add(addTopic);
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
            reset1.addActionListener(e -> {
                topicJList.clearSelection();
            });
            addTopic.addActionListener(e -> {
                JFrame addFrame=new JFrame();
                JTextField topicName=new JTextField();
                JTextField theme=new JTextField();
                JTextArea content=new JTextArea();
                Border border = BorderFactory.createLineBorder(Color.BLACK);
                content.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                JTextField author = new JTextField();
                JPanel panel2=new JPanel(new GridLayout(3,2));
//                JPanel panel3=new JPanel();
//                panel2.setBounds(0,0,400,250);
                panel2.add(topicName);
                panel2.add(theme);
                panel2.add(content);
                panel2.add(author);
                JButton apply=new JButton("Apply");
                panel2.add(apply);
                addFrame.add(panel2);
                addFrame.setBounds(500,200,400,300);
                addFrame.setVisible(true);
                apply.addActionListener(e1->{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String time = LocalDateTime.now().format(formatter);
                    String[] helpStr = content.getText().split("\n");
                    String template = "";
                    for (int i = 0; i < helpStr.length; i++) {
                        template += helpStr[i] + "<br/>";
                    }
                    defaultListModel.addElement(new Topic(topicName.getText(),theme.getText(),template,author.getText(),time));
                    topics.add(new Topic(topicName.getText(),theme.getText(),template.replaceAll("<br/>",""),author.getText(),time));
                    System.out.println(topics.size());
                    addFrame.dispose();
                    try {

                    byte[] massage = new String("Add\n"+topicName.getText()+"\n"+theme.getText()+"\n"+template.replaceAll("<br/>","")+"\n"+author.getText()+"\n"+time+"\n").getBytes();
                    ByteBuffer byteBuffer= ByteBuffer.wrap(massage);
                    clientChannel.write(byteBuffer);
                    byteBuffer.clear();
                        for (Topic top:topics) {
                            System.out.println(top.nameOfTopic + "\n" + top.themeOfTopic + "\n" + top.content + "\n" + top.authorPseudo + "\n" + top.dateOfPublication + "\n");
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                });
            });

            frame.add(jSplitPane);
            frame.pack();
            frame.setBounds(500, 200, 500, 400);
            frame.setVisible(true);
        });

    }

    public static void fillTopicList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2022-04-10 15:30:12", formatter);
        defaultListModel.addElement(new Topic("How to be healthy", "Sport", "To be healthy you need make exes everyday", "author1@gmail.com", String.valueOf(dateTime)));
        dateTime = LocalDateTime.parse("2022-04-14 12:10:54", formatter);
        defaultListModel.addElement(new Topic("Cooking deserts", "Cuisine", "Some recipe", "author2@gmail.com", String.valueOf(dateTime)));
        dateTime = LocalDateTime.parse("2022-03-26 20:40:05", formatter);
        defaultListModel.addElement(new Topic("Box tutorials", "Sport", "plot of topic", "author3@gmail.com", String.valueOf(dateTime)));

        dateTime = LocalDateTime.parse("2022-04-10 15:30:12", formatter);
        topics.add(new Topic("How to be healthy", "Sport", "To be healthy you need make exes everyday", "author1@gmail.com", String.valueOf(dateTime)));
        dateTime = LocalDateTime.parse("2022-04-14 12:10:54", formatter);
        topics.add(new Topic("Cooking deserts", "Cuisine", "Some recipe", "author2@gmail.com", String.valueOf(dateTime)));
        dateTime = LocalDateTime.parse("2022-03-26 20:40:05", formatter);
        topics.add(new Topic("Box tutorials", "Sport", "plot of topic", "author3@gmail.com", String.valueOf(dateTime)));
    }

    public static void method() {
        afterButtonFrame = new JFrame();
        afterButtonFrame.setBounds(500, 200, 400, 200);
        changeContent = new JTextArea();
        changeContent.setWrapStyleWord(true);
        changeContent.setLineWrap(true);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        changeContent.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        changeContent.setBounds(0, 50, 400, 150);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton apply = new JButton("Redact the Content");
        apply.setBounds(75, 15, 250, 25);
        panel.add(apply);
        panel.add(changeContent);
        afterButtonFrame.add(panel);

        apply.addActionListener(q -> {
            String[] helpStr = changeContent.getText().split("\n");
            String template = "";
            for (int i = 0; i < helpStr.length; i++) {
                template += helpStr[i] + "<br/>";
            }
            t.setContent(template);
            System.out.println(t.content);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String time = String.valueOf(LocalDateTime.now().format(formatter));
            //time=time.substring(0,22);
            //2022-04-28T12:56:21.22
            for (int i = 0; i <time.length() ; i++) {
                System.out.println((int)time.charAt(i));
            }
            t.setDateOfPublication(time.trim());
            afterButtonFrame.dispose();
            for (int i = 0; i < topics.size(); i++) {
                if (t.authorPseudo.equals(topics.get(i).authorPseudo) && t.themeOfTopic.equals(topics.get(i).themeOfTopic)) {
                    topics.get(i).setContent(t.content.replaceAll("<br/>", "\n"));
                    topics.get(i).setDateOfPublication(t.dateOfPublication);
                }
            }
            try {
                System.out.println(topics.size());
                byte[] massage = new String("Update\n"+t.nameOfTopic+"\n"+t.themeOfTopic+"\n"+t.content+"\n"+t.authorPseudo+"\n"+t.dateOfPublication+"\n").getBytes();
                ByteBuffer byteBuffer= ByteBuffer.wrap(massage);
                clientChannel.write(byteBuffer);
                byteBuffer.clear();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}
