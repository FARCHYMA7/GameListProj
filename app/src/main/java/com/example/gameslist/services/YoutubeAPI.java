package com.example.gameslist.services;


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


public class YoutubeAPI {

    private static String videoId;

    public static String getVideoID(String title) {

        String sURL = String.format("https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s+trailer&key= ", title);

        try { //handling the youtube api to get the trailer of the specific game
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            JsonObject rootobj = root.getAsJsonObject();
            JsonArray jsonArr = rootobj.get("items").getAsJsonArray();
            JsonObject id = jsonArr.get(0).getAsJsonObject();
            JsonObject idObj = id.get("id").getAsJsonObject();

            videoId = String.valueOf(idObj.get("videoId"));

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return videoId;
    }
}
