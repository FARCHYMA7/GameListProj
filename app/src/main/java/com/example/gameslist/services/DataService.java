package com.example.gameslist.services;

import android.os.StrictMode;

import com.example.gameslist.models.DataModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DataService {

    private static ArrayList<DataModel> arrGames = new ArrayList<>();

    public static ArrayList<DataModel> getArrGames() {

        String sURL = "https://www.freetogame.com/api/games";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(sURL);

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();

            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            JsonArray rootobj = root.getAsJsonArray();

            for(JsonElement je : rootobj) {

                JsonObject obj = je.getAsJsonObject();
                JsonElement elementTitle = obj.get("title");
                JsonElement elementImage = obj.get("thumbnail");
                JsonElement elementDescription = obj.get("short_description");
                JsonElement elementUrl = obj.get("game_url");
                JsonElement elementGenre = obj.get("genre");
                JsonElement elementPlatform = obj.get("platform");
                JsonElement elementPublisher = obj.get("publisher");
                JsonElement elementDeveloper = obj.get("developer");
                JsonElement elementReleaseDate = obj.get("release_date");

                String title = elementTitle.toString().replace("\"", "");
                String image = elementImage.toString().replace("\"", "");
                String description = elementDescription.toString().replace("\"", "");
                String gameUrl = elementUrl.toString().replace("\"", "");
                String genre = elementGenre.toString().replace("\"", "");
                String platform = elementPlatform.toString().replace("\"", "");
                String publisher = elementPublisher.toString().replace("\"", "");
                String developer = elementDeveloper.toString().replace("\"", "");
                String releaseDate = elementReleaseDate.toString().replace("\"", "");

                arrGames.add(new DataModel(title, image, description, gameUrl, genre, platform, publisher, developer, releaseDate));

            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return arrGames;
    }

}
