package com.example.gameslist.models;

import com.example.gameslist.models.DataModel;

import java.util.ArrayList;

public class User {
    public String userName;
    public String password;
    public ArrayList<DataModel> dataSet;

    public User(String userName, String password, ArrayList<DataModel> dataSet) {
        this.userName = userName;
        this.password = password;
        this.dataSet = dataSet;
    }
}

