package com.example.gameslist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.gameslist.R;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.services.DataService;

import java.util.ArrayList;

public class FragmentOnClickedGame extends Fragment {

    TextView gameTitle, descTitle, desc, genreTitle, genre, publisherTitle, publisher, dateTitle, date, devTitle, dev;
    VideoView trailer;

    ImageView imageView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_on_clicked_game, container, false);

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
        imageView = view.findViewById(R.id.dialog_imageView);

        String game = getArguments().getString("gameName");
        gameTitle.setText(game);
        ArrayList<DataModel> localDataSet = DataService.getArrGames();
        DataModel specificGame = null;

        for(DataModel dm : localDataSet)
        {
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

        String imageUrl = specificGame.getImageGame();
        Glide.with(requireContext()).load(imageUrl).into(imageView);

        return view;
    }
}