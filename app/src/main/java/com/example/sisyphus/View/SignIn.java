/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A class for signing in users
 */
public class SignIn extends AppCompatActivity {
    //setting UI elements
    EditText signInEmail;
    EditText signInPassword;
    Button signInConfirm;

    //initializing firebase authentication (session) object and setting log message
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    /**
     * Create a view to collect information to log in a user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        //attaching UI elements to variables
        EditText signInEmail = findViewById(R.id.signInEmail);
        EditText signInPassword = findViewById(R.id.signInPassword);
        Button signInConfirm = findViewById(R.id.registerConfirm);

        signInConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStore;
                String passStore;
                emailStore = signInEmail.getText().toString();
                passStore = signInPassword.getText().toString();

                //if username and password non-null
                //can be modified to add security constraints in future
                if(emailStore != "" && passStore != ""){
                    mAuth.signInWithEmailAndPassword(emailStore, passStore)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //sets session to be associated with user info entered ("signs in")
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        //Pass intent to next class here!

                                        Intent startEmptyMain = new Intent(view.getContext(), EmptyMainMenu.class);
                                        startActivity(startEmptyMain);


                                        } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignIn.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}