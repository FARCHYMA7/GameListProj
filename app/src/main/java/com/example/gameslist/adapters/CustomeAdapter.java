package com.example.gameslist.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
    static ArrayList<DataModel> localLikedGamesDataSet;
    private Context context;
    static String currentUser;
    static DatabaseReference reference;
    static Query checkUserDatabase;
    String liked;

    public CustomeAdapter(ArrayList<DataModel> dataSet, Context context, String currentUser, String liked) {

        this.dataSet = dataSet;
        this.context = context;
        this.currentUser = currentUser;
        this.liked = liked;

        reference = FirebaseDatabase.getInstance().getReference("users");
        checkUserDatabase = reference.orderByChild("userName").equalTo(currentUser);
        checkUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                localLikedGamesDataSet = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) { // retrive the liked games from the firebase for each user to put it in local dataSet
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
                        localLikedGamesDataSet.add(new DataModel(title, image, description, gameUrl, genre, platform, publisher, developer, releaseDate));
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
        CheckBox cb_heart;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_Title);
            textViewGenre = itemView.findViewById(R.id.tv_Genre);
            textViewPlatform = itemView.findViewById(R.id.tv_Platform);
            imageGame = itemView.findViewById(R.id.image_game);
            cb_heart = itemView.findViewById(R.id.cb_heart);
            textViewMore = itemView.findViewById(R.id.more_info);
            textViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("gameName", dataSet.get(getAdapterPosition()).getTitle());

                    try{ // i have 2 fragments that has this button

                        Navigation.findNavController(itemView).navigate(R.id.action_fragmentGamelist2_to_fragmentOnClickedGame,bundle);
                    }catch (Exception e) {
                        Navigation.findNavController(itemView).navigate(R.id.action_fragmentMyList_to_fragmentOnClickedGame,bundle);
                    }
                }
            });

            cb_heart.setOnClickListener(new View.OnClickListener() { // handling the like and unlike - local and in firebase
                @Override
                public void onClick(View v) {
                    reference = FirebaseDatabase.getInstance().getReference("users");
                    if (cb_heart.isChecked()) {
                        if (!isItemExist(localLikedGamesDataSet, dataSet.get(getAdapterPosition()).getTitle())) {
                            reference.child(currentUser).child("dataSet").child(String.valueOf(localLikedGamesDataSet.size())).setValue(dataSet.get(getAdapterPosition()));
                        }
                    }
                    else {
                        removeItem(localLikedGamesDataSet, dataSet.get(getAdapterPosition()).getTitle());
                        reference.child(currentUser).child("dataSet").removeValue();
                        reference.child(currentUser).child("dataSet").setValue(localLikedGamesDataSet);

                        try{
                            Toast.makeText(v.getContext(), "Go to your list to see the changes",
                                    Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", currentUser);
                            Navigation.findNavController(v).navigate(R.id.action_fragmentMyList_to_fragmentGamelist2, bundle);
                        }catch(Exception e) {
                            Log.d("checkError", "oops");
                        }
                    }
                }

                public boolean isItemExist(ArrayList<DataModel> dataSet, String nameCheck) {
                    for (DataModel i : dataSet) {
                        if (i.getTitle().compareTo(nameCheck) == 0)
                            return true;
                    }
                    return false;
                }

                public void removeItem(ArrayList<DataModel> dataSet, String nameCheck) { // the regular dataSet.remove didnt work
                    for (DataModel i : dataSet) {
                        if (i.getTitle().compareTo(nameCheck) == 0){
                            dataSet.remove(i);
                            break;
                        }
                    }
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
        CheckBox cb_heart = holder.cb_heart;

        if (liked.compareTo("yes") == 0) // when the user comeback from the his list i need to put his liked , another check to be sure
            cb_heart.setChecked(true);
        else if (localLikedGamesDataSet == null)
            cb_heart.setChecked(false);
        else if (isItemExist(localLikedGamesDataSet, dataSet.get(position).getTitle())) //check if this item is in the localLiked to know if i need to put the liked button on
            cb_heart.setChecked(true);
        else
            cb_heart.setChecked(false);

        textViewTitle.setText(dataSet.get(position).getTitle());
        textViewGenre.setText(dataSet.get(position).getGenre());
        textViewPlatform.setText(dataSet.get(position).getPlatform());
        String imageUrl = dataSet.get(position).getImageGame();
        Glide.with(context).load(imageUrl).into(imageGame);

    }

    public boolean isItemExist(ArrayList<DataModel> dataSet, String nameCheck) {
        for (DataModel i : dataSet) {
            if (i.getTitle().compareTo(nameCheck) == 0)
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
