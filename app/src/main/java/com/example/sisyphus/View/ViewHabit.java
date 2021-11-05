/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.R;
import com.example.sisyphus.View.Dialog.deleteHabit;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A class to view the details of a habit
 */
public class ViewHabit extends AppCompatActivity {
    //setting UI elements
    TextView habitTitleText, startDateText, frequencyText, habitReasonText;

    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    /**
     * Create a view to display all details of a habit
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        //setting authentication object to current session (signed in user) and setting log message
        mAuth = FirebaseAuth.getInstance();
        final String TAG = "Sample";

        //attaching UI elements to variables
        habitTitleText = findViewById(R.id.habitTitleText);
        startDateText = findViewById(R.id.startDateText);
        frequencyText = findViewById(R.id.frequencyText);
        habitReasonText = findViewById(R.id.habitReasonText);

        //getting habit from intent
        Intent intent = getIntent();
        Habit receivedHabit = (Habit) intent.getSerializableExtra("habit");

        String receivedUser = mAuth.getUid();

        //setting up UI elements to display habit information
        ArrayList<String> receivedFrequency = receivedHabit.getFrequency();
        String receivedTitle = receivedHabit.getHabitName();
        String receivedDate = new SimpleDateFormat("dd/MM/yyyy").format(receivedHabit.getStartDate());
        String receivedReason = receivedHabit.getReason();
        habitTitleText.setText(receivedTitle);
        startDateText.setText(receivedDate);
        habitReasonText.setText(receivedReason);
        frequencyText.setText(setFrequencyText(receivedFrequency));


        final Button viewHabitEventButton = findViewById(R.id.viewHabitEventButton);
        viewHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to view Habit Event
             */
            public void onClick(View view) {
                Intent viewHabitEventInt = new Intent(view.getContext(), ListHabitEvent.class);
                viewHabitEventInt.putExtra("1", receivedTitle);
                startActivity(viewHabitEventInt);
            }
        });

        final Button addHabitEventButton = findViewById(R.id.addHabitEventButton);
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to view Add habit event
             */
            public void onClick(View view) {
                Intent addHabit = new Intent(view.getContext(), AddHabitEvent.class);
                addHabit.putExtra("1", receivedTitle);
                startActivity(addHabit);
            }
        });

        final Button editHabitButton = findViewById(R.id.editHabitButton);
        editHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to view Edit habit
             */
            public void onClick(View view) {
                Intent editHabit = new Intent(view.getContext(), HabitController.class);
                editHabit.putExtra("1", receivedTitle);
                startActivity(editHabit);
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to lost of all habits
             */
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabit.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button deleteHabitButton = findViewById(R.id.deleteHabitButton);
        deleteHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to delete a habit
             */
            public void onClick(View view) {
                //start fragment for deletion
                deleteHabit dialog = new deleteHabit();
                Bundle bundle = new Bundle();
                bundle.putString("selectedTitle",receivedTitle);
                bundle.putString("selectedUser",receivedUser);
                bundle.putString("selectedTag",TAG);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "DELETE");
            }
        });
    }

    /**
     * setFrequencyText
     *  A function to get display string for Frequency Field
     * @param frequencyArray
     * @return
     */
    public String setFrequencyText(ArrayList<String> frequencyArray) {
        //Initialize string builder
        StringBuilder stringBuilder = new StringBuilder();
        //Use for loop
        for (int j = 0; j < frequencyArray.size(); j++) {
            //Concat array value
            stringBuilder.append(frequencyArray.get(j).substring(0,3));
            //Check condition
            if (j != frequencyArray.size() - 1) {
                //When j value not equal to day list size -1
                //Add comma
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}