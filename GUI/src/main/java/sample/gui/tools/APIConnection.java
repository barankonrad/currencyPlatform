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

    private APIConnection(){
    }

    private static final String mainUrl = "https://api.freecurrencyapi.com/v1/";
    private static final String currenciesRequest = mainUrl + "currencies?apikey=" + PrivateData.apiKey;

    private static String getLatestRates(String base, String want){
        return mainUrl +
            String.format("latest?apikey=%s&currencies=%s&base_currency=%s", PrivateData.apiKey, want, base);
    }

    private static final Gson gson = new Gson();

    public static List<String> getCurrencyList(){
        try{
            URL url = new URL(currenciesRequest);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode != 200){
                throw new RuntimeException("HttpsResponseCode: " + responseCode);
            }

            List<String> list = new LinkedList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonObject dataObject = gson.fromJson(reader, JsonObject.class).getAsJsonObject("data");

            dataObject.entrySet().forEach(entry -> list.add(entry.getKey()));

            return list;
        }
        catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static double getLatest(String base, String want){
        try{
            URL url = new URL(getLatestRates(base, want));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode != 200){
                throw new RuntimeException("HttpsResponseCode: " + responseCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonObject dataObject = gson.fromJson(reader, JsonObject.class).getAsJsonObject("data");

            return dataObject.entrySet().iterator().next().getValue().getAsDouble();
        }
        catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
