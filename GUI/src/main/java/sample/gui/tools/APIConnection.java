package sample.gui.tools;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class APIConnection{

    private class CurrencyItem{
        private final String symbol;
        private final double exchangeRate;

        public CurrencyItem(String symbol, double exchangeRate){
            this.symbol = symbol;
            this.exchangeRate = exchangeRate;
        }

        public String getSymbol(){
            return symbol;
        }

        public double getExchangeRate(){
            return exchangeRate;
        }

        @Override
        public String toString(){
            return symbol + ": " + exchangeRate;
        }
    }

    private final String mainUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=" + PrivateData.apiKey;

    public void getCurrencies(){
        try{
            URL url = new URL(mainUrl + "&currencies=&base_currency=PLN");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode != 200){
                throw new RuntimeException("HttpsResponseCode: " + responseCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder response = new StringBuilder();
            reader.lines().forEach(response::append);
            JSONTokener parser = new JSONTokener(String.valueOf(response));
            JSONObject json = new JSONObject(parser).getJSONObject("data");

            List<CurrencyItem> list = new LinkedList<>();
            json.keySet().forEach(key -> list.add(new CurrencyItem(key, json.getDouble(key))));

            list.forEach(System.out::println);
        }
        catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
