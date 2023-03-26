package sample.gui.tools;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class APIConnection{

    private APIConnection(){
    }

    private static final String UrlMain = "https://api.freecurrencyapi.com/v1/";
    private static final String currenciesRequest = UrlMain + "currencies?apikey=" + PrivateData.apiKey;

    private static String getUrlLatest(String base, String want){
        return UrlMain +
            String.format("latest?apikey=%s&currencies=%s&base_currency=%s", PrivateData.apiKey, want, base);
    }

    private static String getUrlHistorical(String base, String want, String from, String to){
        return UrlMain + String.format("historical?apikey=%s&date_from=%s&date_to=%s&base_currency=%s&currencies=%s",
            PrivateData.apiKey, from, to, base, want);
    }

    private static final Gson gson = new Gson();

    private static HttpsURLConnection establishConnection(URL url){
        try{
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode != 200)
                throw new RuntimeException("HttpsResponseCode: " + responseCode);

            return connection;
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static List<String> getCurrencyList(){
        try{
            URL url = new URL(currenciesRequest);
            HttpsURLConnection connection = establishConnection(url);

            List<String> list = new LinkedList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonObject dataObject = gson.fromJson(reader, JsonObject.class).getAsJsonObject("data");

            dataObject.entrySet().forEach(entry -> list.add(entry.getKey()));
            Collections.sort(list);

            return list;
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static double getLatest(String base, String want){
        try{
            URL url = new URL(getUrlLatest(base, want));
            HttpsURLConnection connection = establishConnection(url);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonObject dataObject = gson.fromJson(reader, JsonObject.class).getAsJsonObject("data");

            return dataObject.entrySet().iterator().next().getValue().getAsDouble();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static List<Pair<LocalDate, Double>> getHistorical(String base, String target, LocalDate from, LocalDate to){
        try{
            URL url = new URL(getUrlHistorical(base, target, from.toString(), to.toString()));
            HttpsURLConnection connection = establishConnection(url);

            List<Pair<LocalDate, Double>> list = new LinkedList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonObject data = gson.fromJson(reader, JsonObject.class).getAsJsonObject("data");
            data.entrySet().forEach(entry -> {
                LocalDate date = LocalDate.parse(entry.getKey());
                double rate = entry.getValue().getAsJsonObject().get(target).getAsDouble();

                list.add(new Pair<>(date, rate));
            });

            return list;
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
