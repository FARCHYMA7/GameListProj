package com.example.gameslist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.gameslist.R;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.services.DataService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FragmentOnClickedGame extends Fragment {

    TextView gameTitle, descTitle, desc, genreTitle, genre, publisherTitle, publisher, dateTitle, date, devTitle, dev;
    VideoView trailer;

    WebView webView;

    private static ArrayList<String> arrvideos = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_clicked_game, container, false);

        gameTitle = view.findViewById(R.id.dialog_GameName);
        descTitle = view.findViewById(R.id.dialog_titleDesc);
        desc = view.findViewById(R.id.dialog_gameDescription);
        genreTitle = view.findViewById(R.id.dialog_titleGen);
        genre = view.findViewById(R.id.dialog_genre);
        publisherTitle = view.findViewById(R.id.dialog_titlePub);
        publisher = view.findViewById(R.id.dialog_publisher);
        dateTitle = view.findViewById(R.id.dialog_titleDate);
        date = view.findViewById(R.id.dialog_date);
        devTitle = view.findViewById(R.id.dialog_titleDev);
        dev = view.findViewById(R.id.dialog_dev);
        webView = view.findViewById(R.id.dialog_webView);

        String game = getArguments().getString("gameName");
        gameTitle.setText(game);
        ArrayList<DataModel> localDataSet = DataService.getArrGames();
        DataModel specificGame = null;

        for (DataModel dm : localDataSet) {
            if (dm.getTitle() == game) {
                specificGame = dm;
                break;
            }
        }
        assert specificGame != null;
        desc.setText(specificGame.getShortDescription());
        genre.setText(specificGame.getGenre());
        publisher.setText(specificGame.getPublisher());
        dev.setText(specificGame.getDeveloper());
        date.setText(specificGame.getReleaseDate());

        String sURL = String.format("https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s+trailer&key=AIzaSyCBQDLydJAXxlOoHGn7erzru4WhQ59EzVo", game);

        try {
            URL url = new URL(sURL);

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();

            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));


            JsonObject rootobj = root.getAsJsonObject();

            JsonArray jsonArr = rootobj.get("items").getAsJsonArray();

            JsonObject id = jsonArr.get(0).getAsJsonObject();

            JsonObject idObj = id.get("id").getAsJsonObject();



            String videoId = String.valueOf(idObj.get("videoId"));

            // String videoUrl = String.format("https://www.youtube.com/watch?v=%s", videoId).replace("\"", "");


            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Enable JavaScript

            String embedURL = String.format("https://www.youtube.com/embed/%s", videoId).replace("\"", "");

            String frameVideo = String.format("<html><body><iframe width=\"100%%\" height=\"100%%\" src=\"%s\" frameborder=\"0\" allowfullscreen></iframe></body></html>", embedURL);

            webView.loadData(frameVideo, "text/html", "utf-8");

            return view;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        String imageUrl = specificGame.getImageGame();
//        Glide.with(requireContext()).load(imageUrl).into(imageView);



    }
}