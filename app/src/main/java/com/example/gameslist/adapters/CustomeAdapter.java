package com.example.gameslist.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gameslist.models.DataModel;
import com.example.gameslist.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> {
    static ArrayList<DataModel> dataSet;
    static ArrayList<DataModel> localDataSet;
    static ArrayList<DataModel> bizo;
    Context context;
    static String currentUser;
    static DatabaseReference reference;
    static Query checkUserDatabase;


    public CustomeAdapter(ArrayList<DataModel> dataSet, Context context, String currentUser) {

        this.dataSet = dataSet;
        this.context = context;
        this.currentUser = currentUser;
        localDataSet = new ArrayList<>();

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
                        localDataSet.add(new DataModel(title, image, description, gameUrl, genre, platform, publisher, developer, releaseDate));
                        //adapter.notifyItemInserted(localLikedGames.size() - 1);
                        i++;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewGenre, textViewPlatform, textViewMore;
        ImageView imageGame;

        Button btn_like;

        CustomeAdapter adapter;

        public MyViewHolder(View itemView) {
            super(itemView);


            textViewTitle = itemView.findViewById(R.id.tv_Title);
            textViewGenre = itemView.findViewById(R.id.tv_Genre);
            textViewPlatform = itemView.findViewById(R.id.tv_Platform);
            imageGame = itemView.findViewById(R.id.image_game);
            btn_like = itemView.findViewById(R.id.btn_like);
            textViewMore = itemView.findViewById(R.id.more_info);

            textViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("gameName", dataSet.get(getAdapterPosition()).getTitle());

                    try{

                        Navigation.findNavController(itemView).navigate(R.id.action_fragmentGamelist2_to_fragmentOnClickedGame,bundle);
                    }catch (Exception e) {
                        Navigation.findNavController(itemView).navigate(R.id.action_fragmentMyList_to_fragmentOnClickedGame,bundle);
                    }






                }
            });

            btn_like.setOnClickListener(new View.OnClickListener() { //maybe another adapter?
                @Override
                public void onClick(View v) {

                    reference = FirebaseDatabase.getInstance().getReference("users");
                    checkUserDatabase = reference.orderByChild("userName").equalTo(currentUser);
                    if (!isItemExist(localDataSet, dataSet.get(getAdapterPosition()).getTitle())) {
                        localDataSet.add(dataSet.get(getAdapterPosition()));
                        reference.child(currentUser).child("dataSet").removeValue();
                        reference.child(currentUser).child("dataSet").setValue(localDataSet);
                    }



                }

                public boolean isItemExist(ArrayList<DataModel> dataSet, String nameCheck) {
                    for (DataModel i : dataSet) {
                        if (i.getTitle().compareTo(nameCheck) == 0)
                            return true;
                    }

                    return false;
                }
            });


        }

    }


    @NonNull
    @Override
    public CustomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listgame_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter.MyViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewGenre = holder.textViewGenre;
        TextView textViewPlatform = holder.textViewPlatform;
        ImageView imageGame = holder.imageGame;

        textViewTitle.setText(dataSet.get(position).getTitle());
        textViewGenre.setText(dataSet.get(position).getGenre());
        textViewPlatform.setText(dataSet.get(position).getPlatform());

        String imageUrl = dataSet.get(position).getImageGame();
        Glide.with(context).load(imageUrl).into(imageGame);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
