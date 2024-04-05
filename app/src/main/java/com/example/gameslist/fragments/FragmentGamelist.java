package com.example.gameslist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gameslist.adapters.CustomeAdapter;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.R;
import com.example.gameslist.services.DataService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class FragmentGamelist extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    static ArrayList<DataModel> localDataSet;
    CustomeAdapter adapter;
    EditText input;
    Button btn_logout;
    Button btn_addItem;
    Button btn_myList;
    TextView userName;
    String currentUser;
    DatabaseReference reference;
    Query checkUserDatabase;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_gamelist, container, false);

        localDataSet = DataService.getArrGames();
        currentUser = getArguments().getString("username");
        adapter = new CustomeAdapter(localDataSet, requireContext(), currentUser);

//        reference = FirebaseDatabase.getInstance().getReference("users");
//        checkUserDatabase = reference.orderByChild("userName").equalTo(currentUser);

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_myList = view.findViewById(R.id.btn_mylist);
        userName = view.findViewById(R.id.textView_user);


        String textToWrite = "Hello " + currentUser;
        userName.setText(textToWrite);

        recyclerView = view.findViewById(R.id.res);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentGamelist2_to_fragmentLogin3);
            }
        });

        btn_myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username", currentUser);
                Navigation.findNavController(view).navigate(R.id.action_fragmentGamelist2_to_fragmentMyList, bundle);
            }
        });


        return view;

    }

}