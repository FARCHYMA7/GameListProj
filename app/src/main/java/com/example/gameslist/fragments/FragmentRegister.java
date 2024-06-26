package com.example.gameslist.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameslist.models.DataModel;
import com.example.gameslist.R;
import com.example.gameslist.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragmentRegister extends Fragment {

    private TextInputEditText editTextUserName, editTextPassword;
    private Button buttonRegister;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        editTextUserName = view.findViewById(R.id.regUsername);
        editTextPassword = view.findViewById(R.id.regPassword);
        buttonRegister = view.findViewById(R.id.btn_register);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.loginInReg);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentRegister3_to_fragmentLogin3);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                progressBar.setVisibility(View.VISIBLE);
                String userName = String.valueOf(editTextUserName.getText()).trim();
                String password = String.valueOf(editTextPassword.getText()).trim();

                if (TextUtils.isEmpty(userName)) {
                    editTextUserName.setError("Enter username");
                    editTextUserName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter password");
                    editTextPassword.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(userName.length()< 5)
                {
                    editTextUserName.setError("User name must be at least 5");
                    editTextUserName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(!(userName.matches("(.*[a-z].*)")))
                {
                    editTextUserName.setError("There must be at least one english letter");
                    editTextUserName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (!((userName.matches("(.*[0-9].*)"))))
                {
                    editTextUserName.setError("There must be at least one digit");
                    editTextUserName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(password.length()< 8)
                {
                    editTextPassword.setError("Password size must be at least 8");
                    editTextPassword.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!((password.matches("(.*[0-9].*)"))))
                {
                    editTextPassword.setError("There must be at least one digit");
                    editTextPassword.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(!(password.matches("^(?=.*[!@#$%^&*)(_+=-]).*$")))
                {
                    editTextPassword.setError("There must be at least one special symbol");
                    editTextPassword.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }


                if(!(password.matches("(.*[A-Z].*)")))
                {
                    editTextPassword.setError("There must be at least one Capital letter");
                    editTextPassword.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                checkIfUserExist(userName, password, view);
            }
        });

        return view;
    }

    public void checkIfUserExist(String userName, String password, View view){

        DatabaseReference checkReference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = checkReference.orderByChild("userName").equalTo(userName);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(getActivity(), "User Already Exist",
                            Toast.LENGTH_SHORT).show();
                    editTextUserName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    ArrayList<DataModel> userLikedGames = new ArrayList<>();
                    User newUser = new User(userName, password, userLikedGames);
                    reference.child(userName).setValue(newUser);
                    Toast.makeText(getActivity(), "Account created",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", userName);
                    bundle.putString("search", "no");
                    bundle.putString("choice", "no");
                    Navigation.findNavController(view).navigate(R.id.action_fragmentRegister3_to_fragmentGamelist2, bundle);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}