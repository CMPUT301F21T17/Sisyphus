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
import android.widget.TextView;

import com.example.sisyphus.Model.User;
import com.example.sisyphus.R;
import com.example.sisyphus.View.Dialog.ChangePassword;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//
// This activity is meant to represent our settings pages
// In this page, users can select an item and it will lead them to an activity where they can
// edit the information they want.
//
// User information will be auto filled from the database in text views to be seen
// Change password leads to a special activity to accommodate safety precautions
//
public class Settings extends AppCompatActivity {

    public static String item;

    //setting UI elements
    TextView fName;
    TextView lName;
    TextView email;

    Button fNameEdit;
    Button lNameEdit;
    Button emailEdit;
    Button passwordEdit;
    Button back;

    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    //current user to display settings for
    User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersettings);

        // Insert here ties that retrieve users first name, last name, and email
        // Set it so that it replaces the text of fName, lName, and email

        // Initializing Textview for displaying current user information
        fName = findViewById(R.id.settingsFNameView);
        lName = findViewById(R.id.settingsLNameView);
        email = findViewById(R.id.settingsEmailView);
        // Initializing Buttons
        fNameEdit = findViewById(R.id.fNameEdit);
        lNameEdit = findViewById(R.id.lNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        back = findViewById(R.id.back);

        //setting authentication object to current session (signed in user) and connecting to database
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //gets passed user and displays their data in text-fields
        DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                activeUser = documentSnapshot.toObject(User.class);
                fName.setText(activeUser.getFirst());
                lName.setText(activeUser.getLast());
                email.setText(mAuth.getCurrentUser().getEmail());
            }
        });






        // Passes the users first name to the info edit activity and switches activities
        fNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fNameInt = new Intent(getApplicationContext(), InfoEdit.class);
                // Put in users first name
                fNameInt.putExtra(item, "fName");
                startActivity(fNameInt);
            }
        });
        // Passes users last name to the info edit activity and switches activities
        lNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lNameInt = new Intent(getApplicationContext(), InfoEdit.class);
                // Put in users first name
                lNameInt.putExtra(item, "lName");
                startActivity(lNameInt);
            }
        });

        // Passes users email to info edit activity then switches activities
        emailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailInt = new Intent(getApplicationContext(), InfoEdit.class);
                // Put in users first name
                emailInt.putExtra(item, "email");
                startActivity(emailInt);
            }
        });
        // Moves to the change password activity, in a separate activity to maintain security
        passwordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passInt = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(passInt);
            }
        });


        // Moves to the change password activity, in a separate activity to maintain security
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}