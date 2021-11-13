/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    @Override
    /**
     * onCreate
     * a class to create and set the view for habit event
     */
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
        String receivedUser = intent.getStringExtra("user");
        String receivedHabitEventID = intent.getStringExtra("habit_eventID");
        HabitEvent receivedHabitEvent = (HabitEvent) intent.getSerializableExtra("habit_event");
        habitEventTitleText.setText(receivedHabitEvent.getHabitName());
        habitEventDateText.setText(new SimpleDateFormat("dd/MM/yyyy").format(receivedHabitEvent.getDate()));
        habitEventLocationText.setText(receivedHabitEvent.getLocation());
        habitEventCommentText.setText(receivedHabitEvent.getComment());
        habitEventPhoto.setImageBitmap(decodeFromFirebase(receivedHabitEvent.getPhotoID()));

        final Button editHabitEventButton = findViewById(R.id.edit_habitEvent_button);
        editHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function called when a HabitEvent is to be edited
             */
            public void onClick(View view) {
                //transfers to edit menu for selected habit event
                Intent editHabit = new Intent(view.getContext(), EditHabitEventView.class);
                editHabit.putExtra("editEvent",receivedHabitEvent);
                editHabit.putExtra("editEventID",receivedHabitEventID);
                startActivity(editHabit);
            }
        });

        final Button deleteHabitEventButton = findViewById(R.id.delete_habitEvent_button);
        deleteHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function called when a HabitEvent is to be deleted
             */
            public void onClick(View view) {
                //start fragment for deletion
                deleteHabitEvent dialog = new deleteHabitEvent();
                Bundle bundle = new Bundle();
                bundle.putString("selectedHabit",receivedHabitEvent.getHabitName());
                bundle.putString("selectedEventID",receivedHabitEventID);
                bundle.putString("selectedUser",receivedUser);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "DELETE");
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function called when habitList button is clicked
             */
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitEvent.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_backToList = findViewById(R.id.BackButton);
        button_backToList.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * function called to return to previous view
             */
            public void onClick(View view) {
                Intent toEventList = new Intent(ViewHabitEvent.this, ListHabitEvent.class);
                toEventList.putExtra("1",receivedHabitEvent.getHabitName());
                startActivity(toEventList);
            }
        });

    }
    public static Bitmap decodeFromFirebase(String image){
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
    }
}