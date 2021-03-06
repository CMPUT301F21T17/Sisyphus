/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Switch;


import androidx.annotation.NonNull;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.sisyphus.Model.FirebaseStore;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.R;
import com.example.sisyphus.View.Dialog.errorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * A class that provides a UI to add habits to firebase
 */
public class AddHabit extends AppCompatActivity {
    //setting UI elements
    private TextInputLayout habitName, reason;
    private EditText startDate, frequency;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private SwitchCompat privateToggle;


    //initializing firebase authentication (session) object
    private FirebaseAuth mAuth;

    /**
     * default constructor
     */
    public AddHabit() {
    }

    /**
     * function to create a habit creation view
     * @param savedInstanceState
     *  previous view
     */
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        String currentUser = mAuth.getUid();

        //attaching UI elements to variables
        privateToggle = findViewById(R.id.privateSwitch);

        Button checkButton = findViewById(R.id.checkButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button back = findViewById(R.id.back);
        habitName = findViewById(R.id.habitNameContainer);

        startDate = findViewById(R.id.startDate);
        frequency = findViewById(R.id.frequency);
        reason = findViewById(R.id.reasonContainer);

        //setting up storage for dates
        ArrayList<String> days = new ArrayList<>();

        //Onclick for the the check button and storing data
        checkButton.setOnClickListener(view -> {
            //gets input from text entry fields and formats, creating new habit
            String habit = Objects.requireNonNull(habitName.getEditText()).getText().toString().trim();
            Date dateInput = null;
            try {
                dateInput = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //error checking for empty habit
            if(habit.equals("")){
                new errorFragment("Please give the habit a name!").show(getSupportFragmentManager(), "Display_Error");
                return;
            }

            //error checking for empty date
            if(dateInput == null){
                new errorFragment("Please select a date!").show(getSupportFragmentManager(), "Display_Error");
                return;
            }


            String reasonInput = Objects.requireNonNull(reason.getEditText()).getText().toString().trim();

            //error checking for empty reason
            if(reasonInput.equals("")){
                new errorFragment("Please add a reason!").show(getSupportFragmentManager(), "Display_Error");
                return;
            }

            //error checking for no days selected
            if(days.size() == 0){
                new errorFragment("Please select at least one date for the habit to occur!").show(getSupportFragmentManager(), "Display_Error");
                return;
            }

            Habit habitInput = new Habit(habit, privateToggle.isChecked(),dateInput, days, reasonInput, -1);

            //establishing connection to firebase, storing data, and then returning to previous menu

            FirebaseStore fb = new FirebaseStore();
            fb.storeHabit(currentUser,habitInput);
            Intent toHabitList = new Intent(AddHabit.this, AllHabitListView.class);
            startActivity(toHabitList);
        });


        //creating the calendar for user to input startdate
        startDate.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(AddHabit.this, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //Black Background
            dialog.show();
        });
        //formatting date selected from calendar for output
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = day + "/" + month + "/" + year;
            startDate.setText(date);
        };

        //Creating the pop up dialog to set up frequency
        boolean[] selectedDay;
        ArrayList<Integer> dayList = new ArrayList<>();
        String[] dayArray = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        //creating a multiselect day picker for frequency
        selectedDay = new boolean[dayArray.length];

        //onClick method to get dates habit occurs via checkbox menu
        frequency.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddHabit.this);
            //Set title
            builder.setTitle("Select Day");
            //Set dialog non cancelable
            builder.setCancelable(false);

            builder.setMultiChoiceItems(dayArray, selectedDay, (dialogInterface, i, b) -> {
                if (b) {
                    //When checkbox selected
                    //Add position in day list
                    dayList.add(i);
                    //Sort day list
                    Collections.sort(dayList);
                } else {
                    //When checkbox unselected
                    //Remove position from day list
                    for (int j = 0; j < dayList.size(); j++) {
                        if (dayList.get(j) == i) {
                            dayList.remove(j);
                            break;
                        }
                    }
                }
            });

            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                //Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                //Use for loop
                for (int j = 0; j < dayList.size(); j++) {
                    //Concat array value
                    stringBuilder.append(dayArray[dayList.get(j)]);
                    days.add(dayArray[dayList.get(j)]);
                    //Check condition
                    if (j != dayList.size() - 1) {
                        //When j value not equal to day list size -1
                        //Add comma
                        stringBuilder.append(",");
                    }
                }
                //Set text on text view
                frequency.setText(stringBuilder.toString());
            });

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setNeutralButton("Clear All", (dialogInterface, i) -> {
                for (int j = 0; j < selectedDay.length; j++) {
                    //Remove all selection
                    selectedDay[j] = false;
                    //Clear day list
                    dayList.clear();
                    //Clear text view value
                    frequency.setText("");
                }
            });
            //Show dialog
            builder.show();
        });

        //On Click for the cancel button
        cancelButton.setOnClickListener(view -> {
            //Intent back
            finish();
        });

        //On Click for the Back button
        back.setOnClickListener(view -> {
            //Intent back
            finish();
        });

    }
}
