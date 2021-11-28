/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.PopupMenu;

import com.example.sisyphus.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Homepage of app.  Will be modified and added to greatly as time goes on
 */
public class EmptyMainMenu extends AppCompatActivity {
    //initializing firebase authentication (session) object and default message for log
    final String TAG = "Sample";
    FirebaseAuth mAuth;
    Button dropDown;

    /**
     * Creates the homepage of the app with settings and menu bar
     * @param savedInstanceState
     *  previous view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_main_menu);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        dropDown = (Button) findViewById(R.id.dropDown);
        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                showPopup(v);
            }
        });

        //INTENTS FOR THE BOTTOM BAR!
        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        //onClick listener to transfer user to habit list page
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when all habit list button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        //onClick listener to transfer user to calendar page
        button_calendar.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when calendar button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, CalendarActivity.class);
                startActivity(intent);
            }
        });


        final Button button_Home = findViewById(R.id.home_button);
        button_Home.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when home button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });

        final Button button_Prof = findViewById(R.id.social_button);
        button_Prof.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when social button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, SocialView.class);
                startActivity(intent);
            }
        });

        //END OF INTENTS FOR THE BOTTOM BAR!
    }

    /**
     * Method to open popup menu
     * @param v
     *  current view
     */
    public void showPopup(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.Theme_App);
        PopupMenu popup = new PopupMenu(wrapper, v, Gravity.LEFT, R.style.Theme_App, 0);
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.dropdown, popup.getMenu());

        popup.show();
    }

    /**
     * Method to create an options menu
     * @param menu
     *  menu to be created
     * @return
     *  true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dropdown, menu);

        return true;
    }

    /**
     * function to handle options menu clicks
     * @param item
     *  Item in menu selected
     * @return
     *  true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the main activity layout object.
        // Get clicked menu item id.
        int itemId = item.getItemId();
        if(itemId == R.id.followRequests)
        {
            // Change to Follow Requests Screen
        }else if(itemId == R.id.settings)
        {
            Intent intent = new Intent(EmptyMainMenu.this, Settings.class);
            startActivity(intent);

        }else if(itemId == R.id.logout)
        {
            // implement logout
        }
        return true;
    }
}