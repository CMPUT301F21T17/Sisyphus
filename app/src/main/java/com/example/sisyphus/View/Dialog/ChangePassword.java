/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sisyphus.R;
import com.example.sisyphus.View.Settings;
import com.google.firebase.auth.FirebaseAuth;

//
// This activity represents the area where a user can change their password
//
// Users are sent here from settings, then given the opportunity to change their password
// To do this, users must confirm their current password to verify the user
// Afterwards, user puts the new password into the field and repeats it in the confirm
// After assuring the passwords match AND the current password is correct, the database is updated
// with the correct password and users are sent back to the settings page.
//
public class ChangePassword extends AppCompatActivity {
    //setting UI elements
    EditText passwordCurrent;
    EditText passwordNew;
    EditText passwordConfirmNew;

    Button passCancel;
    Button passConfirm;

    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        // Initializing Edit Texts
        passwordCurrent = findViewById(R.id.passwordCurrent);
        passwordNew = findViewById(R.id.passwordNew);
        passwordConfirmNew = findViewById(R.id.passwordConfirmNew);
        // Initializing Buttons
        passCancel = findViewById(R.id.passCancel);
        passConfirm = findViewById(R.id.passConfirm);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        // Cancel just switches to previous activity on click
        passCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelInt = new Intent(getApplicationContext(), Settings.class);
                startActivity(cancelInt);
            }
        });

        //onClick listener which validates password in input and stores the new password
        passConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validates password conditions
                if (passwordNew.getText().toString().equals(passwordConfirmNew.getText().toString()) && passwordNew.getText().toString().length() > 5) {

                    // Implements password change, then switch activities back to the previous
                    mAuth.getCurrentUser().updatePassword(passwordNew.getText().toString());

                    Intent confirmInt = new Intent(getApplicationContext(), Settings.class);
                    startActivity(confirmInt);
                }
            }
        });
    }
}