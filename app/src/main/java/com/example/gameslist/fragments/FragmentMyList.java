package com.example.gameslist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.gameslist.adapters.CustomeAdapter;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.R;
import com.example.gameslist.services.DataService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentMyList extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    static ArrayList<DataModel> localLikedGames;
    static ArrayList<DataModel> localDataSet;
    CustomeAdapter adapter;
    Button btn_logout;
    Button btn_addItem;
    Button btn_saveItems;
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
        View view =  inflater.inflate(R.layout.fragment_my_list, container, false);


        localLikedGames = new ArrayList<>();
        currentUser = getArguments().getString("username");
        adapter = new CustomeAdapter(localLikedGames, requireContext(), currentUser);


        reference = FirebaseDatabase.getInstance().getReference("users");
        checkUserDatabase = reference.orderByChild("userName").equalTo(currentUser);

        checkUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int i = 0;

                for (DataSnapshot item : snapshot.getChildren()) {
                    while (item.child("dataSet").child(String.valueOf(i)).exists()) {
                        String title = item.child("dataSet").child(String.valueOf(i)).child("title").getValue(String.class);
                        String image = item.child("dataSet").child(String.valueOf(i)).child("imageGame").getValue(String.class);
                        String description = item.child("dataSet").child(String.valueOf(i)).child("shortDescription").getValue(String.class);
                        String gameUrl = item.child("dataSet").child(String.valueOf(i)).child("gameUrl").getValue(String.class);
                        String genre = item.child("dataSet").child(String.valueOf(i)).child("genre").getValue(String.class);
                        String platform = item.child("dataSet").child(String.valueOf(i)).child("platform").getValue(String.class);
                        String publisher = item.child("dataSet").child(String.valueOf(i)).child("publisher").getValue(String.class);
                        String developer = item.child("dataSet").child(String.valueOf(i)).child("developer").getValue(String.class);
                        String releaseDate = item.child("dataSet").child(String.valueOf(i)).child("releaseDate").getValue(String.class);
                        localLikedGames.add(new DataModel(title, image, description, gameUrl, genre, platform, publisher, developer, releaseDate));
                        adapter.notifyItemInserted(localLikedGames.size() - 1);
                        i++;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_logout = view.findViewById(R.id.btn_logout);
        userName = view.findViewById(R.id.textView_user);


        String textToWrite = currentUser + "'s List";
        userName.setText(textToWrite);

        recyclerView = view.findViewById(R.id.res);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentMyList_to_fragmentLogin3);
            }
        });


        return view;

    }

}