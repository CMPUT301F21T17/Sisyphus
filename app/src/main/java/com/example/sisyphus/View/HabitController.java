/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sisyphus.Model.FirebaseStore;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Class for manipulating Habits from inside the app
 */
public class HabitController extends AppCompatActivity {
    private EditText startDate, frequency,reason;
    private TextView habitName;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button confirm,cancel,deleteButton;
    private FirebaseStore testbase = new FirebaseStore();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    /**
     * create view to get information for creating a habit
     * @param savedInstanceState
     */
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_controller);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ImageView backButton = findViewById(R.id.backButton);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
        deleteButton = findViewById(R.id.deleteButton);
        habitName = findViewById(R.id.habitName);
        startDate = findViewById(R.id.startDate);
        frequency = findViewById(R.id.frequency);
        reason = findViewById(R.id.reason);
        ArrayList<String> days = new ArrayList<>();
        String dummyUser = mAuth.getUid();
        Intent habitinfo = getIntent();
        String dummyhabitname = habitinfo.getStringExtra("1");


        //getting habit info from database
        DocumentReference docRef = db.collection("Users").document(dummyUser).collection("Habits").document(dummyhabitname);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Habit habit1 = documentSnapshot.toObject(Habit.class);
                days.addAll(habit1.getFrequency());
                habitName.setText(dummyhabitname);
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                startDate.setText(simpleDateFormat.format(habit1.getStartDate()));
                frequency.setText(habit1.getFrequency().toString().replace("[","").replace("]",""));
                reason.setText(habit1.getReason());
            }
        });

            //Onclick for the the addHabit event button and storing data
        confirm.setOnClickListener(view -> {
            Date dateInput = null;
            try {
                dateInput = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String reasonInput = reason.getText().toString().trim();
            Habit modifiedHabit = new Habit(dummyhabitname,dateInput,days,reasonInput);
            FirebaseStore fb = new FirebaseStore();
            fb.storeHabit(dummyUser,modifiedHabit);
            Intent intent = new Intent(view.getContext(),ViewHabit.class);
            intent.putExtra("habit",modifiedHabit);
            startActivity(intent);
        });

        cancel.setOnClickListener(view -> {
          finish();
        });
        backButton.setOnClickListener(view -> {
            finish();
        });

        //Deleting a Habit from database
        deleteButton.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HabitController.this);
                    builder.setCancelable(true);
                    builder.setTitle("Message");
                    builder.setMessage("Are you sure you want to delete this habit");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    testbase.deleteHabit(dummyUser,dummyhabitname);
                                    Intent intent = new Intent(view.getContext(),AllHabitListView.class);
                                    startActivity(intent);
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    });
        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();

        });
        //creating the calendar for user to input startdate
        startDate.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(HabitController.this, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //Black Background
            dialog.show();
        });
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

        frequency.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(HabitController.this);
            //Set title
            builder.setTitle("Select Day");
            //Set dialog non cancelable
            builder.setCancelable(false);
            if (days.contains("SUNDAY")){
                selectedDay[0] = true;
                dayList.add(0);
            }
            if (days.contains("MONDAY")){
                selectedDay[1] = true;
                dayList.add(1);
            }
            if (days.contains("TUESDAY")){
                selectedDay[2] = true;
                dayList.add(2);
            }
            if (days.contains("WEDNESDAY")){
                selectedDay[3] = true;
                dayList.add(3);
            }
            if (days.contains("THURSDAY")){
                selectedDay[4] = true;
                dayList.add(4);
            }
            if (days.contains("FRIDAY")){
                selectedDay[5] = true;
                dayList.add(5);
            }
            if (days.contains("SATURDAY")){
                selectedDay[6] = true;
                dayList.add(6);
            }
            days.clear();
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

    }
}