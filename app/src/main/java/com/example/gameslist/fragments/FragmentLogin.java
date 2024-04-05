package com.example.gameslist.fragments;

import android.content.Intent;
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

import com.example.gameslist.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FragmentLogin extends Fragment {


    TextInputEditText editTextUserName;
    TextInputEditText editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.frgament_login, container, false);

        editTextUserName = view.findViewById(R.id.username);
        editTextPassword = view.findViewById(R.id.password);
        buttonLogin = view.findViewById(R.id.btn_login);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.registerFromLogin);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentLogin3_to_fragmentRegister3);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String userName, password;
                userName = String.valueOf(editTextUserName.getText());
                password = String.valueOf(editTextPassword.getText());


                if (TextUtils.isEmpty(userName)) {
                    editTextUserName.setError("Enter username");
                    editTextUserName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter password");
                    editTextPassword.requestFocus();
//                   Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                checkUser(userName, password, view);
            }
        });

       return view;
    }

    public void checkUser(String userName, String password, View view){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("userName").equalTo(userName);


        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    editTextUserName.setError(null);
                    String passwordFromDB = snapshot.child(userName).child("password").getValue(String.class);

                    assert passwordFromDB != null;
                    if (passwordFromDB.equals(password)){
                        editTextUserName.setError(null);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", userName);
                        Navigation.findNavController(view).navigate(R.id.action_fragmentLogin3_to_fragmentGamelist2, bundle);


                    }
                    else {
                        Toast.makeText(getActivity(), "Invalid Password",
                                Toast.LENGTH_SHORT).show();
                        editTextPassword.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "User does not exist",
                            Toast.LENGTH_SHORT).show();
                    editTextUserName.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}