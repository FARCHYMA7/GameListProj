package com.example.gameslist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gameslist.R;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.services.DataService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}