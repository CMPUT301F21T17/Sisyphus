/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sisyphus.Model.FirebaseStore;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.HabitEvent;
import com.example.sisyphus.R;
import com.example.sisyphus.View.Dialog.errorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A class to Add Habit Events
 */
public class AddHabitEvent extends AppCompatActivity {
    //initializing firebase authentication (session) object
    private FirebaseAuth mAuth;

    //setting UI elements
    private EditText location,date,comment;
    private TextView habitTitle;
    private Button add,cancel;


    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String TAG = "Query duplicate habit events";

    /**
     * function to create HabitEvent creation view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        //attaching UI elements to variables
        location = findViewById(R.id.editTextLocation);
        date = findViewById(R.id.editTextDate);
        comment = findViewById(R.id.editTextComment);
        habitTitle = findViewById(R.id.textViewHabitName);
        add = findViewById(R.id.buttonAdd);
        cancel = findViewById(R.id.buttonCancel);

        //getting name of habit event and setting UI to display it
        Intent intent = getIntent();
        String habitName = intent.getStringExtra("1");

        habitTitle.setText(habitName);





        //creating the calendar for user to input date of habit event
        date.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(AddHabitEvent.this, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //Transparent Background
            dialog.show();
        });
        //formatting date selected from calendar for output
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String newDate = day + "/" + month + "/" + year;
            date.setText(newDate);
        };



        //onClick method to get data from text entry fields and format into habit event to be added
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //error checking:
                //-not a duplicate date
                //-on a date the habit occurs
                //-after or on start date
                //-before or on current date
                //-comment is character limited (get Sihan's code)


                //getting input and creating event
                Date newDate = null;
                try {
                    newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //initializing firebase authentication (session) object and establishing database connection
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference collectionReference = db.collection("Users");
                Query namedUsers = collectionReference.document(mAuth.getUid()).collection("Habits")
                .document(habitName).collection("HabitEvent").whereEqualTo("date", newDate);

                Date finalNewDate = newDate;
                namedUsers
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Boolean eventExists = false;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventExists = true;
                                    }

                                    if(eventExists == false) {
                                        //remaining error checking in here!
                                        System.out.println("got here!");

                                        DocumentReference habitRef = db.collection("Users").document(mAuth.getUid())
                                                .collection("Habits").document(habitName);
                                        habitRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();

                                                    //should always execute, since we know the habit exists
                                                    if (document.exists()) {
                                                        Habit currentHabit = document.toObject(Habit.class);

                                                        if(finalNewDate.compareTo(currentHabit.getStartDate()) > 0){
                                                            Date today = new Date();
                                                            String dateConvert = new SimpleDateFormat("yyyy-MM-dd").format(finalNewDate);
                                                            if(finalNewDate.compareTo(today) <= 0){
                                                                //converting date to char version for comparison to freq array
                                                                LocalDate selected = LocalDate.parse(dateConvert);


                                                                Boolean dateValid = false;
                                                                for(String s: currentHabit.getFrequency()){

                                                                    if(s.equals(selected.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("CANADA")).toUpperCase(Locale.ROOT))){
                                                                        dateValid = true;
                                                                    }
                                                                }

                                                                if(dateValid == true){
                                                                    //date good!

                                                                    HabitEvent newEvent = new HabitEvent(finalNewDate, location.getText().toString(), comment.getText().toString(), habitName);
                                                                    //storing event in firebase and returning to previous menu
                                                                    FirebaseStore fb = new FirebaseStore();
                                                                    fb.storeHabitEvent(mAuth.getUid(), habitName, newEvent);
                                                                    Intent toEventList = new Intent(AddHabitEvent.this, ListHabitEvent.class);
                                                                    toEventList.putExtra("1",habitName);
                                                                    startActivity(toEventList);
                                                                } else {
                                                                    //date not a valid date habit occurs on!
                                                                    new errorFragment("Cannot add event on day habit does not occur on! See habit details for valid days!").show(getSupportFragmentManager(), "Display_Error");
                                                                }


                                                            } else {
                                                                //date after today!
                                                                new errorFragment("Cannot add event in the future! Today's date is: " + dateConvert).show(getSupportFragmentManager(), "Display_Error");

                                                            }
                                                        } else {
                                                            //date before start date!
                                                            new errorFragment("Cannot add event before habit start date! Start date is: " + currentHabit.getStartDate()).show(getSupportFragmentManager(), "Display_Error");

                                                        }


                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                    } else {

                                        new errorFragment("Event already exists for this date!").show(getSupportFragmentManager(), "Display_Error");

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });



        //onClick listener to cancel add and return to previous menu
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }


}