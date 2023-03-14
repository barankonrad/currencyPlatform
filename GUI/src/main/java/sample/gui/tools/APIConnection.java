package sample.gui.tools;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

            Gson gson = new Gson();
            List<CurrencyItem> list = new LinkedList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonObject dataObject = gson.fromJson(reader, JsonObject.class).get("data").getAsJsonObject();
            dataObject.entrySet()
                .forEach(entry -> list.add(new CurrencyItem(entry.getKey(), entry.getValue().getAsDouble())));


            list.forEach(System.out::println);
        }
        catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
