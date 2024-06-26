package com.example.gameslist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gameslist.adapters.CustomeAdapter;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.R;
import com.example.gameslist.services.GamesAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FragmentGamelist extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private static ArrayList<DataModel> localDataSet;
    private CustomeAdapter adapter;
    private TextView userName;
    private String currentUser;
    private String search;
    private String choice;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gamelist, container, false);

        localDataSet = new ArrayList<>();
        localDataSet = GamesAPI.getArrGames();

        assert getArguments() != null;
        search = getArguments().getString("search");
        choice = getArguments().getString("choice");
        currentUser = getArguments().getString("username");

        if (search != null && search.compareTo("no") != 0 && choice.compareTo("no") != 0) {
            if (choice.compareTo("Genre") == 0) {
                ArrayList<DataModel> filtered = filterdGenre(localDataSet, search);
                adapter = new CustomeAdapter(filtered, requireContext(), currentUser, "no");
            } else if (choice.compareTo("Developer") == 0) {
                ArrayList<DataModel> filtered = filterdDeveloper(localDataSet, search);
                adapter = new CustomeAdapter(filtered, requireContext(), currentUser, "no");
            } else if (choice.compareTo("Name") == 0) {
                ArrayList<DataModel> filtered = filterdName(localDataSet, search);
                adapter = new CustomeAdapter(filtered, requireContext(), currentUser, "no");
            } else if (choice.compareTo("Year") == 0) {
                ArrayList<DataModel> filtered = filterdYear(localDataSet, search);
                adapter = new CustomeAdapter(filtered, requireContext(), currentUser, "no");
            } else if (choice.compareTo("Platform") == 0) {
                ArrayList<DataModel> filtered = filterdPlatform(localDataSet, search);
                adapter = new CustomeAdapter(filtered, requireContext(), currentUser, "no");
            }
        }
        else{
            adapter = new CustomeAdapter(localDataSet, requireContext(), currentUser, "no");
        }


        userName = view.findViewById(R.id.textView_user);
        String textToWrite = "Hello " + currentUser;
        userName.setText(textToWrite);

        recyclerView = view.findViewById(R.id.res);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("username", currentUser);
                int id = item.getItemId();
                if (id == R.id.list) {
                    Navigation.findNavController(view).navigate(R.id.action_fragmentGamelist2_to_fragmentMyList, bundle);
                }
                else if (id == R.id.filter) {
                    Navigation.findNavController(view).navigate(R.id.action_fragmentGamelist2_to_fragmentFilter, bundle);
                }
                else if (id == R.id.logout) {
                    Navigation.findNavController(view).navigate(R.id.action_fragmentGamelist2_to_fragmentLogin3);
                }

                return true;

            }
        });

        return view;

    }

    public ArrayList<DataModel> filterdGenre(ArrayList<DataModel> dataSet, String genre) {
        ArrayList<DataModel> filterd = new ArrayList<>();
        for (DataModel i : dataSet) {
            if (i.getGenre().compareTo(genre) == 0)
            {
                filterd.add(i);
            }
        }
        return filterd;
    }

    public ArrayList<DataModel> filterdDeveloper(ArrayList<DataModel> dataSet, String developer) {
        ArrayList<DataModel> filterd = new ArrayList<>();
        for (DataModel i : dataSet) {
            if (i.getDeveloper().compareTo(developer) == 0)
                filterd.add(i);
        }
        return filterd;
    }

    public ArrayList<DataModel> filterdYear(ArrayList<DataModel> dataSet, String year) {
        ArrayList<DataModel> filterd = new ArrayList<>();

        for (DataModel i : dataSet) {
            if (i.getReleaseDate().substring(0,4).compareTo(year) == 0)
                filterd.add(i);
        }
        return filterd;
    }

    public ArrayList<DataModel> filterdPlatform(ArrayList<DataModel> dataSet, String Platform) {
        ArrayList<DataModel> filterd = new ArrayList<>();

        for (DataModel i : dataSet) {
            if (i.getPlatform().compareTo(Platform) == 0)
                filterd.add(i);
        }
        return filterd;
    }



    public ArrayList<DataModel> filterdName(ArrayList<DataModel> dataSet, String title) {
        ArrayList<DataModel> filterd = new ArrayList<>();
        for (DataModel i : dataSet) {
            if (i.getTitle().compareTo(title) == 0)
            {
                filterd.add(i);
                break;
            }

        }
        return filterd;
    }
}