/**
 *
 *  @author Bozhko Sviatoslav S21058
 *
 */

package zad1;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;

public class Service {
    String country;
    String code;
    String currentCurrency;
    String moneyCountry;
    String exchangeToWhat;
    boolean flag=false;
    public Service(String country) {
        this.country = country;
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry(Locale.ENGLISH).equals(country)) {
                code = locale.getCountry();
                moneyCountry = Currency.getInstance(locale).getCurrencyCode();
            }
        }
        System.out.println(moneyCountry);
    }

    public String getWeather(String city) {
        String apiKey = "102ada082fe573478881677150222028";
        String json = "";
        try {
            URL urlGetWeather = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + code + "&appid=" + apiKey + "");
            HttpURLConnection request = (HttpURLConnection) urlGetWeather.openConnection();
            request.connect();
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(new InputStreamReader((InputStream) request.getContent()));
            json = object.get("main") + "\n" + object.get("weather") + "\n" + object.get("wind");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
    }

    public Double getRateFor(String currency) {
//        String apiKey="52e56830cde09d89a964160f851f6f7b";
        currentCurrency = currency;
        String dontWorkWithoutThis;
       exchangeToWhat ="";
        double rez = 0;
        try {
            URL url = new URL("https://api.exchangerate.host/convert?from="+currentCurrency+"&to="+moneyCountry);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            JSONParser jsonParser = new JSONParser();
            JSONObject object = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));
            JSONObject object1= (JSONObject) object.get("info");
            dontWorkWithoutThis = String.valueOf(object1.get("rate"));
            rez= Double.parseDouble(dontWorkWithoutThis);
            exchangeToWhat=moneyCountry;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.out.println(rez + " " + exchangeToWhat + " można kupić jeden " + currentCurrency);
        return rez;
    }

    public Double getNBPRate() {
        double rez = 0;
        if (!"PLN".equals(moneyCountry)) {
        try {
            URL urlGetExchangedCurrencyUrl;
                urlGetExchangedCurrencyUrl = urlForNBP(flag);
                HttpURLConnection connection = (HttpURLConnection) urlGetExchangedCurrencyUrl.openConnection();
                connection.connect();
                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject) parser.parse(new InputStreamReader((InputStream) connection.getContent()));
                JSONArray array = (JSONArray) object.get("rates");
                JSONObject object1 = (JSONObject) array.get(0);
                rez = (double) object1.get("mid");
            } catch(IOException | ParseException e){
                flag=true;
                getNBPRate();
            }
            System.out.println(rez + " " + currentCurrency + " można kupić jeden  " + moneyCountry);
            return rez;
        } else{
            rez=1;
            System.out.println(rez + " PLN " + " można kupić jeden " + moneyCountry);
            return rez;
        }
    }
    public URL urlForNBP(boolean przelocznik) throws MalformedURLException {
        if (flag == false){
            return new URL("http://api.nbp.pl/api/exchangerates/rates/a/"+moneyCountry+"/?format=json");
        }else {
            return new URL("http://api.nbp.pl/api/exchangerates/rates/b/"+moneyCountry+"/?format=json");
        }
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(String currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public String getMoneyCountry() {
        return moneyCountry;
    }

    public void setMoneyCountry(String moneyCountry) {
        this.moneyCountry = moneyCountry;
    }

    public String getExchangeToWhat() {
        return exchangeToWhat;
    }

    public void setExchangeToWhat(String exchangeToWhat) {
        this.exchangeToWhat = exchangeToWhat;
    }
}