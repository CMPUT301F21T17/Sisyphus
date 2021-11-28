/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;

import android.widget.TextView;

import com.example.sisyphus.Model.HabitEvent;
import com.example.sisyphus.R;
import com.example.sisyphus.View.Dialog.deleteHabitEvent;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;

/**
 * A class for displaying the details of a habit event
 */
public class ViewHabitEvent extends AppCompatActivity {
    //setting UI elements
    TextView habitEventTitleText, habitEventDateText, habitEventLocationText, habitEventCommentText;

    ImageView habitEventPhoto;

    private HabitEvent receivedHabitEvent;
    private String receivedUser, receivedHabitEventID;

    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    /**
     * A function called to display a habit event
     * @param savedInstanceState
     *  saved instances' state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_event);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        //attaching UI elements to variables
        habitEventTitleText = findViewById(R.id.habit_title);
        habitEventDateText = findViewById(R.id.habit_event_date);
        habitEventLocationText = findViewById(R.id.habit_event_location);
        habitEventCommentText = findViewById(R.id.habit_event_comment);
        habitEventPhoto = findViewById(R.id.habit_event_image);

        //getting habit event info and displaying in UI elements
        Intent intent = getIntent();
        receivedUser = intent.getStringExtra("user");
        receivedHabitEventID = intent.getStringExtra("habit_eventID");
        receivedHabitEvent = (HabitEvent) intent.getSerializableExtra("habit_event");
        habitEventTitleText.setText(receivedHabitEvent.getHabitName());
        habitEventDateText.setText(new SimpleDateFormat("dd/MM/yyyy").format(receivedHabitEvent.getDate()));
        habitEventLocationText.setText("Location: " + receivedHabitEvent.getLocation());
        habitEventCommentText.setText("Comment: " + receivedHabitEvent.getComment());

        if(receivedHabitEvent.getPhotoID().equals("")){
            //do nothing!
        } else {
            habitEventPhoto.setImageBitmap(decodeFromFirebase(receivedHabitEvent.getPhotoID()));
        }



        Button overflow = (Button) findViewById(R.id.search);
        overflow.setOnClickListener(new View.OnClickListener() {
            /**
             * A function called to display the drop down menu
             * @param v
             *  current view
             */
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        final Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            /**
             * A function called to return to previous view
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * function to decode the image code
     * @param image
     *  string of image to be converted to bitmap
     * @return Bitmap of image
     *  bitmap of image that was converted
     */
    public static Bitmap decodeFromFirebase(String image){
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
    }

    /**
     * Method to open popup menu
     * @param v
     *  current view
     */
    public void showPopup(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.Theme_App_Overflow_Event);
        PopupMenu popup = new PopupMenu(wrapper, v, Gravity.LEFT, R.style.Theme_App_Overflow_Event, 0);
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.event_dropdown, popup.getMenu());

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
        menuInflater.inflate(R.menu.event_dropdown, menu);
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
        if(itemId == R.id.editEvent)
        {
            Intent editHabit = new Intent(this, EditHabitEventView.class);
            editHabit.putExtra("editEvent",receivedHabitEvent);
            editHabit.putExtra("editEventID",receivedHabitEventID);
            startActivity(editHabit);

        }else if(itemId == R.id.deleteEvent)
        {
            //start fragment for deletion
            deleteHabitEvent dialog = new deleteHabitEvent();
            Bundle bundle = new Bundle();
            bundle.putString("selectedHabit",receivedHabitEvent.getHabitName());
            bundle.putString("selectedEventID",receivedHabitEventID);
            bundle.putString("selectedUser",receivedUser);
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "DELETE");
            finish();

        }
        return true;
    }
}