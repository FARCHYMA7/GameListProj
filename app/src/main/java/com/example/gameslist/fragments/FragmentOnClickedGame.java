package com.example.gameslist.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameslist.R;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.services.GamesAPI;
import com.example.gameslist.services.YoutubeAPI;
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

public class FragmentOnClickedGame extends Fragment {

    private TextView gameTitle, desc, url, publisher, date, dev;
    private WebView webView;
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
        desc = view.findViewById(R.id.dialog_gameDescription);
        url = view.findViewById(R.id.dialog_url);
        publisher = view.findViewById(R.id.dialog_publisher);
        date = view.findViewById(R.id.dialog_date);
        dev = view.findViewById(R.id.dialog_dev);
        webView = view.findViewById(R.id.dialog_webView);

        String title = getArguments().getString("gameName");
        gameTitle.setText(title);
        ArrayList<DataModel> localDataSet = GamesAPI.getArrGames();
        DataModel specificGame = localDataSet.get(0);

        for (DataModel dm : localDataSet) {
            if (dm.getTitle().compareTo(title) == 0) { //searching the chosen game in the Games api dataSet
                specificGame = dm;
                break;
            }
        }
        try {
            desc.setText(specificGame.getShortDescription());
            date.setText(specificGame.getReleaseDate());
            publisher.setText(specificGame.getPublisher());
            dev.setText(specificGame.getDeveloper());
            url.setText(specificGame.getGameUrl());

        }
        catch (Exception e) {
            Toast.makeText(getActivity(), "List Error",
                    Toast.LENGTH_SHORT).show();
        }

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(String.valueOf(url.getText())));
                startActivity(i);

            }
        });

            String videoId = YoutubeAPI.getVideoID(title);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Enable JavaScript

            String embedURL = String.format("https://www.youtube.com/embed/%s", videoId).replace("\"", "");
            String frameVideo = String.format("<html><body><iframe width=\"100%%\" height=\"100%%\" src=\"%s\" frameborder=\"0\" allowfullscreen></iframe></body></html>", embedURL);
            webView.loadData(frameVideo, "text/html", "utf-8");

            return view;

    }
}