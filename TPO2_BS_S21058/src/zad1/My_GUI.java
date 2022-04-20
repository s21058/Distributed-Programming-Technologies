package zad1;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class My_GUI extends JFrame {
    public Service service;
    String hCountry;
    String hcurrency;
    String hCity;
    String forShowWeather;
    String forShowExchange;
   static int count;
    public My_GUI(String weatherJson, Double rate1, Double rate2) {
count=0;
        JFrame frame = new JFrame();
        frame.setBounds(550, 200, 800, 800);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        Font font = new Font("Serif",Font.ITALIC,30);
        Font font1 = new Font("Serif",Font.ITALIC,20);
        JTextField country = new JTextField();
        country.setBackground(new Color(219, 219, 219));
        country.setFont(font);

        JTextArea showWeather = new JTextArea();
        showWeather.setFont(font1);
        showWeather.setBackground(new Color(255,157,105));


        JTextArea showCurrency = new JTextArea();
        showCurrency.setFont(font1);
        showCurrency.setBackground(new Color(255,157,105));


        JLabel write_country = new JLabel("Write country");
        write_country.setFont(font);
        write_country.setOpaque(true);
        write_country.setBackground(new Color(255, 157, 105));


        JLabel write_city = new JLabel("Write city");
        write_city.setFont(font);
        write_city.setOpaque(true);
        write_city.setBackground(new Color(255, 157, 105));

        JLabel write_currency = new JLabel("Write currency");
        write_currency.setFont(font);
        write_currency.setOpaque(true);
        write_currency.setBackground(new Color(255, 157, 105));


        JTextField city = new JTextField();
        city.setBackground(new Color(219, 219, 219));
        city.setFont(font);

        JTextField currency = new JTextField();
        currency.setBackground(new Color(219, 219, 219));
        currency.setFont(font);

        JButton weather = new JButton("Weather");
        weather.addActionListener(e -> {
            if(hcurrency!=null){
                forShowWeather=service.getWeather(hCity);
                forShowWeather=forShowWeather.replace("{","");
                forShowWeather=forShowWeather.replace("\"","");
                forShowWeather=forShowWeather.replace("}","");
                forShowWeather=forShowWeather.replace("[","");
                showWeather.setText(forShowWeather);
            }else {
                hCity = city.getText();
                hCountry = country.getText();
                service = new Service(hCountry);
                forShowWeather=service.getWeather(hCity);
                forShowWeather=forShowWeather.replace("{","");
                forShowWeather=forShowWeather.replace("\"","");
                forShowWeather=forShowWeather.replace("}","");
                forShowWeather=forShowWeather.replace("[","");
                showWeather.setText(forShowWeather);
            }
        });
        JButton exchange = new JButton("Exchange");
        exchange.addActionListener(e->{
            if(hCity==null||hCountry==null){
                hCity=city.getText();
                hCountry=country.getText();
                service=new Service(hCountry);
                service.getWeather(hCity);
                hcurrency=currency.getText();
                service.getRateFor(hcurrency);
                service.getNBPRate();
            }else {
                hcurrency = currency.getText();
                forShowExchange = String.valueOf(service.getRateFor(hcurrency));
                showCurrency.setText("One " + service.getCurrentCurrency() + " is cost: " + forShowExchange + " " + service.getMoneyCountry() + "\n" +
                        service.getNBPRate() + service.moneyCountry + " costs for one  PLN");
            }
        });
        JButton wiki = new JButton("Wiki");
        wiki.addActionListener(e -> {
            count++;
            if(hCity==null||hcurrency==null){
                hCity=city.getText();
                hCountry=country.getText();
                service=new Service(hCountry);
                hcurrency=currency.getText();
            }else {
                if(hCountry.equals("Ukraine")){
                    mainPanel.setBackground(new Color(0,102,204));
                    showWeather.setBackground(new Color(0,102,204));
                    showCurrency.setBackground(new Color(0,102,204));
                    weather.setBackground(new Color(255,255,0));
                    exchange.setBackground(new Color(255,255,0));
                    wiki.setBackground(new Color(255,255,0));
                    country.setBackground(new Color(255,255,0));
                    city.setBackground(new Color(255,255,0));
                    currency.setBackground(new Color(255,255,0));
                    write_city.setBackground(new Color(255,255,0));
                    write_country.setBackground(new Color(255,255,0));
                    write_currency.setBackground(new Color(255,255,0));
                }
                SwingUtilities.invokeLater(()->new Wiki(hCity, hCountry));
                }
            });

        JPanel panel = new JPanel();
        panel.setBounds(0, 355, 800, 400);
        panel.setLayout(new GridLayout(3,9));

        panel.add(write_country);
        panel.add(write_city);
        panel.add(write_currency);

        panel.add(country);
        panel.add(city);
        panel.add(currency);

        panel.add(weather);
        panel.add(exchange);
        panel.add(wiki);


        mainPanel.add(showWeather);
        showWeather.setBounds(40,100,300,200);

        mainPanel.add(showCurrency);
        showCurrency.setBounds(420,100,300,200);
        mainPanel.add(panel);

        mainPanel.setBackground(new Color(255, 157, 105));

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static int getCount() {
        return count;
    }
}
