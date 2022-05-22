package zad1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientSubscribe {
    Topic topic;
    int clientPort;
   ArrayList<Integer>listOfSubs;
    ClientSubscribe(Topic topic, Integer clients) {
        listOfSubs=new ArrayList<>();
        this.topic = topic;
        this.clientPort = clients;
        listOfSubs.add(clients);
    }
    ClientSubscribe(Topic topic) {
        listOfSubs=new ArrayList<>();
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Subscribers for " +topic+
                " is client with PORT: " + listOfSubs +"\n";
    }

    public Topic getTopic() {
        return topic;
    }

    public Integer getClient() {
        return clientPort;
    }

    public ArrayList<Integer> getListOfSubs() {
        return listOfSubs;
    }

    public void addSubs(int clientPort){
        listOfSubs.add(clientPort);
        Set<Integer> set = new HashSet<>(listOfSubs);
        listOfSubs.clear();
        listOfSubs.addAll(set);
        set.clear();

    }
}
