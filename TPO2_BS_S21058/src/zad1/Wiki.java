package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class Wiki extends JFrame {
    String city;

    public Wiki(String city, String country) {
        this.city = city;
        String[] urls = new String[]{"https://en.wikipedia.org/wiki/Russo-Ukrainian_War",
                "https://vchasnoua.com/donbass/71762-foto-video-18-kozhen-den-hyne-150-mariupoltsiv-tse-nesterpnyi-bil-radnyk-miskoho-holovy-mariupolia-rozpoviv-pro-nyshchennia-rashystamy-myrnoho-naselennia"};
        JFXPanel jfxPanel = new JFXPanel();
        this.add(jfxPanel);
        try {

            Platform.runLater(() -> {
                WebView webView = new WebView();
                WebEngine engine = webView.getEngine();
                if (!country.equals("Russia")) {
                    engine.load("https://en.wikipedia.org/wiki/" + this.city);
                } else {
                    if (My_GUI.getCount() == 2) {
                        engine.load(urls[0]);
                    } else {
                        engine.load(urls[1]);
                    }
                }
                jfxPanel.setScene(new Scene(webView));
            });

            setPreferredSize(new Dimension(1000, 1000));
            setDefaultCloseOperation(Wiki.DISPOSE_ON_CLOSE);
            pack();
            setVisible(true);
        } catch (NullPointerException e) {
            System.err.println("");
        }
    }
}
