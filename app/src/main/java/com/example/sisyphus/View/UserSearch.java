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
    EditText idInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        home_Button = findViewById(R.id.home_button);
        search_Button = findViewById(R.id.search_user);
        idInput = findViewById(R.id.editTextTextPersonName);

        mAuth = FirebaseAuth.getInstance();


        //onClick listener to search for a user
        search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //instantiate userRequest object and make a call
                searchAndRequest searchObj = new searchAndRequest();
                boolean flag;
                String userID = idInput.getText().toString();

                //handling case of empty input
                if (userID.equals("")){
                    userID = "invalid";
                }

                flag = searchObj.userValid(userID);

                //if statement, display error fragment on fail (Invalid user!)
                //and search on success, transfer to a display menu for requests (currently canned)

                if(flag){
                    Intent intent = new Intent(UserSearch.this, SearchResults.class);
                    intent.putExtra("1", userID);
                    startActivity(intent);
                } else {
                    //not showing for some reason.  Secondary issue for later
                    Toast.makeText(UserSearch.this, "No such user ",
                            Toast.LENGTH_SHORT).show();
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
                test.handleRequest("CedJ4UrIarfLmIn1l6Bc6Ziwmdl2");
                test.commitFollow("CedJ4UrIarfLmIn1l6Bc6Ziwmdl2");
            }
        });



    }
}