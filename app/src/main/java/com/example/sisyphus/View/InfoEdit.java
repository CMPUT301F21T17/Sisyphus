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
import android.widget.TextView;

import com.example.sisyphus.Model.User;
import com.example.sisyphus.R;
import com.example.sisyphus.View.Dialog.errorFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//
// This activity represents the area where users can edit their information
//
// Users are sent from the settings activity, with an extra from the intent that labels
// which bit of user information is being corrected.
// User will be able to see the current iteration of this information in a textview
// Then under the new section, they can edit the information.
// Upon pressing confirm, firebase is updated with the new information and user is returned to
// the settings activity.
//


public class InfoEdit extends AppCompatActivity {

    //setting UI elements
    TextView infoCurrentItem;
    EditText infoEditItem;
    Button editCancel;
    Button editConfirm;
    Button back;

    //initializing firebase authentication (session) object and setting up variables for
    //properly accessing firebase.  User object references user to be edited
    FirebaseAuth mAuth;
    User activeUser;
    String TAG = "editUser";

    /**
     * function called to create a view to allow users to edit their user information
     * @param savedInstanceState
     *  state of previous instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoedit);

        // Initializing Text and Edit Views
        infoCurrentItem = findViewById(R.id.infoCurrentItem);
        infoEditItem = findViewById(R.id.infoEditItem);
        // Initializing Buttons
        editCancel = findViewById(R.id.editCancel);
        editConfirm = findViewById(R.id.editConfirm);

        //this UI has 3 settings, determined by what is passed in the intent (first, last, email)
        Intent intent = getIntent();
        String item = intent.getStringExtra(Settings.item);

        //setting authentication object to current session (signed in user) and connecting to database
        mAuth = FirebaseAuth.getInstance();
        System.out.println(mAuth.getCurrentUser().getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //connects to firebase to get user information to be edited
        DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //selects what value to display based on mode passed via intent
                activeUser = documentSnapshot.toObject(User.class);
                if (item.equals("fName")) {
                    infoCurrentItem.setText(activeUser.getFirst());
                } else if (item.equals("lName")) {
                    infoCurrentItem.setText(activeUser.getLast());
                } else if (item.equals("email")) {
                    infoCurrentItem.setText(mAuth.getCurrentUser().getEmail());
                }

            }
        });


        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when back button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Abandons the changes and just returns back to settings
        editCancel.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when cancel button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //confirms edit and updates appropriate field in firebase
        editConfirm.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when confirm button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                // Send data to database that changes the item specified
                // If item is first name, then replace databases first name, and so on
                if (item.equals("fName") && (infoEditItem.getText().toString().equals("") == false)){
                    //connect to database and attempt to update selected field
                    DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());

                    userRef
                            .update("first", infoEditItem.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    Intent confirmInt = new Intent(getApplicationContext(), Settings.class);
                                    startActivity(confirmInt);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                    new errorFragment("Something went wrong. Please try again!").show(getSupportFragmentManager(), "Display_Error");

                                }
                            });
                } else if (item.equals("lName") && (infoEditItem.getText().toString().equals("") == false)) {
                    //connect to database and attempt to update selected field
                    DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());

                    userRef
                            .update("last", infoEditItem.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    Intent confirmInt = new Intent(getApplicationContext(), Settings.class);
                                    startActivity(confirmInt);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                    new errorFragment("Something went wrong. Please try again!").show(getSupportFragmentManager(), "Display_Error");
                                }
                            });
                } else if (item.equals("email") && (infoEditItem.getText().toString().equals("") == false)) {
                    //update user authentication data
                    mAuth.getCurrentUser().updateEmail(infoEditItem.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent confirmInt = new Intent(getApplicationContext(), Settings.class);
                            startActivity(confirmInt);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new errorFragment("Email invalid! Please enter valid email! (eg. JohnDoe@email.com)").show(getSupportFragmentManager(), "Display_Error");
                        }
                    });
                } else {
                    new errorFragment("Edited field cannot be empty!").show(getSupportFragmentManager(), "Display_Error");
                }
            }
        });
    }
}