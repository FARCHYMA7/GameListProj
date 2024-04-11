package com.example.gameslist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gameslist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;

public class FragmentFilter extends Fragment {

    private String currentUser;
    private Spinner spinnerGenre;
    private EditText search_et;
    private Button search_btn;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        spinnerGenre = view.findViewById(R.id.spinnerGenre);
        search_et = view.findViewById(R.id.search_et);
        search_btn = view.findViewById(R.id.search_btn);
        currentUser = getArguments().getString("username");
        String[] items = new String[]{"Genre", "Year", "Platform", "Developer", "Name"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinnerGenre.setAdapter(adapter);
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView3);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = new Bundle();
                bundle.putString("username", currentUser);
                int id = item.getItemId();
                if (id == R.id.clear3) {
                    Navigation.findNavController(view).navigate(R.id.action_fragmentFilter_to_fragmentGamelist2, bundle);
                }
                return true;
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = String.valueOf(search_et.getText());
                if (!search.isEmpty()) {
                    String choice = String.valueOf(spinnerGenre.getSelectedItem());
                    Bundle bundle = new Bundle();
                    bundle.putString("username", currentUser);
                    bundle.putString("search", search);
                    bundle.putString("choice", choice);
                    Navigation.findNavController(view).navigate(R.id.action_fragmentFilter_to_fragmentGamelist2, bundle);
                }
                else {
                    search_et.setError("Enter text");
                    search_et.requestFocus();
                }

            }
        });

        return view;
    }
}