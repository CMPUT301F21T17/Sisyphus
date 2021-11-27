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
import com.google.firebase.auth.FirebaseAuth;

/**
 * Homepage of app.  Will be modified and added to greatly as time goes on
 */
public class EmptyMainMenu extends AppCompatActivity {
    //initializing firebase authentication (session) object and default message for log
    final String TAG = "Sample";
    FirebaseAuth mAuth;

    /**
     * Creates the homepage of the app with settings and menu bar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_main_menu);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        final Button button_Settings = findViewById(R.id.settingsButton);
        //onClick listener to transfer user to settings page
        button_Settings.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(EmptyMainMenu.this, Settings.class);
                startActivity(intent);
            }
        });


        //INTENTS FOR THE BOTTOM BAR!

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        //onClick listener to transfer user to habit list page
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        //onClick listener to transfer user to calendar page
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, CalendarActivity.class);
                startActivity(intent);
            }
        });


        final Button button_Home = findViewById(R.id.home_button);
        button_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });

        //currently sends to user search class.  May be worth changing later
        final Button button_Prof = findViewById(R.id.profile_button);
        button_Prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, SocialView.class);
                startActivity(intent);
            }
        });

        //END OF INTENTS FOR THE BOTTOM BAR!


        final Button button_Search = findViewById(R.id.search_button);
        //onClick listener to transfer user to search page.  Currently a dummy access method, will be moved
        button_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, UserSearch.class);
                startActivity(intent);
            }
        });

        final Button button_Request = findViewById(R.id.request_button);
        //onClick listener to transfer user to search page.  Currently a dummy access method, will be moved
        button_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, FollowRequestListView.class);
                startActivity(intent);
            }
        });
    }

}