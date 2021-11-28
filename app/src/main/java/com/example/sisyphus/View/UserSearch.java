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
import com.example.sisyphus.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A class to search the Firebase for users with matching name to input.
 */
public class UserSearch extends AppCompatActivity {
    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    //setting UI elements
    Button search_Button;
    EditText firstInput;
    EditText lastInput;

    Button back_Button, home_Button, calendar_Button, habit_Button, social_Button;


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

        habit_Button = findViewById(R.id.allhabitlist_button);
        //onClick listener to return to main menu
        habit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSearch.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        //onClick listener to transfer user to calendar page
        calendar_Button = findViewById(R.id.calendar_button);
        calendar_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSearch.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        home_Button = findViewById(R.id.home_button);
        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSearch.this, DailyHabitListView.class);
                startActivity(intent);
            }
        });

        social_Button = findViewById(R.id.social_button);
        social_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSearch.this, SocialView.class);
                startActivity(intent);
            }
        });

        back_Button = findViewById(R.id.back);
        back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSearch.this, SocialView.class);
                startActivity(intent);
            }
        });




    }
}