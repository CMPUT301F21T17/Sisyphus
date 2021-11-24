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
import android.widget.EditText;
import android.widget.Toast;

import com.example.sisyphus.Model.followProtocol;
import com.example.sisyphus.Model.searchAndRequest;
import com.example.sisyphus.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A class to search the Firebase for a user, along with any other necessary firebase documents
 *  Incomplete, and may not be fully completed depending on the layout of the project going forwards
 */
public class UserSearch extends AppCompatActivity {
    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    //setting UI elements
    Button search_Button;
    Button home_Button;
    EditText firstInput;
    EditText lastInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        home_Button = findViewById(R.id.home_button);
        search_Button = findViewById(R.id.search_user);
        firstInput = findViewById(R.id.editTextTextFirstName);
        lastInput = findViewById(R.id.editTextTextLastName);

        mAuth = FirebaseAuth.getInstance();


        //onClick listener to search for a user
        search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check inputs have information
                String inputCheck = firstInput.getText().toString();

                if(inputCheck.equals("")){
                    Toast.makeText(UserSearch.this, "Please enter a first name!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String first = inputCheck;
                    inputCheck = lastInput.getText().toString();

                    if(inputCheck.equals("")){
                        Toast.makeText(UserSearch.this, "Please enter a last name!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String last = inputCheck;
                        //pass information to other window for search + display
                        Intent searchUsers = new Intent(getApplicationContext(), DisplaySearch.class);
                        // Put in users first name
                        searchUsers.putExtra("fName", first);
                        searchUsers.putExtra("lName", last);
                        startActivity(searchUsers);

                    }
                }

            }
        });


        //onClick listener to return to main menu
        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSearch.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });



        final Button send = findViewById(R.id.buttonProtocolSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followProtocol test = new followProtocol();
                test.sendRequest("hU7X0519jIhFrkefBhVflVokgVo2");
            }
        });

        final Button accept = findViewById(R.id.buttonProtocolRequestAccept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followProtocol test = new followProtocol();
                //test.handleRequest("CedJ4UrIarfLmIn1l6Bc6Ziwmdl2");
                //test.commitFollow("CedJ4UrIarfLmIn1l6Bc6Ziwmdl2");
            }
        });



    }
}