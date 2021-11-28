/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sisyphus.R;
import com.example.sisyphus.View.Register;
import com.example.sisyphus.View.SignIn;

/**
 * Initial screen of app
 *  redirects to signIn and register
 */
public class Entry extends AppCompatActivity {
    //setting UI elements
    Button signIn;
    Button signUp;

    /**
     * Creates view to provide option to sign in or sign up
     * @param savedInstanceState
     *  previous view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        //attaching UI elements to variables
        signIn = findViewById(R.id.goToSignIn);
        signUp = findViewById(R.id.goToSignUp);

        //transfers user to signIn page
        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signUpInt = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signUpInt);
            }
        });

        //transfers user to register page
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent registerInt = new Intent(getApplicationContext(), Register.class);
                startActivity(registerInt);
            }
        });
    }
}