/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import static android.content.ContentValues.TAG;
import static android.util.Base64.DEFAULT;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.net.Uri;

import android.os.Build;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

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
    private Bitmap takenPhoto;
    private String takenPhotoID = "";

    //setting UI elements
    private EditText location,date,comment;

    
    
    private ImageView habitPhoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOCATION = 2;


    private TextView topbarText;
    private Button add,cancel, back;


    private DatePickerDialog.OnDateSetListener mDateSetListener;


    // private float longitude;
    // private float latitude;
    private String place;

    /**
     * Activity result handler to receive data data from map activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            // longitude = extras.getFloat("LONGITUDE");
            // latitude = extras.getFloat("LATITUDE");
            place = extras.getString("LOCATION");
            location.setText(String.format(place));

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            takenPhoto = (Bitmap) extras.get("data");
            habitPhoto.setImageBitmap(takenPhoto);
            encodeBitmap(takenPhoto);
        }
    }

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
        topbarText = findViewById(R.id.topbarText);
        add = findViewById(R.id.buttonAdd);
        cancel = findViewById(R.id.buttonCancel);

        habitPhoto = findViewById(R.id.photoView);
        back = findViewById(R.id.back);


        //getting name of habit event and setting UI to display it
        Intent intent = getIntent();
        String habitName = intent.getStringExtra("1");

        topbarText.setText("Add " + habitName +" Event");





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


        //create a map intent
        location.setOnClickListener(view -> {
            Intent googleMaps = new Intent(view.getContext(), GoogleMaps.class);
            startActivityForResult(googleMaps, REQUEST_LOCATION);
        });

        habitPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();
            }

        });



        //onClick method to get data from text entry fields and format into habit event to be added.
        //this function is quite smelly, but due to the asynchronous nature of firebase data retrieval,
        //it was necessary to nest multiple queries and document retrievals
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                //getting input and creating date
                Date newDate = null;
                try {
                    newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                //initializing firebase authentication (session) object and establishing database connection
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                final CollectionReference collectionReference = db.collection("Users");

                //creating query to pull in all existing habit events to enforce constraints on habit events
                //(one per day, only on valid days, etc)
                Query namedUsers = collectionReference.document(mAuth.getUid()).collection("Habits")
                .document(habitName).collection("HabitEvent").whereEqualTo("date", newDate);

                //getting date inside listener
                Date finalNewDate = newDate;

                //handling query data
                namedUsers
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Boolean eventExists = false;
                                if (task.isSuccessful()) {
                                    //checks if event exists for the selected date already, sets flag if so
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventExists = true;
                                    }

                                    //if event not exist, check other constraints
                                    if(eventExists == false) {

                                        //getting habit info associated with this event for further checks
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

                                                        //compares input date to the start date.  If greater or equal, is valid
                                                        if(finalNewDate.compareTo(currentHabit.getStartDate()) >= 0){

                                                            //getting formatted version for later comparison
                                                            Date today = new Date();
                                                            String dateConvert = new SimpleDateFormat("yyyy-MM-dd").format(finalNewDate);

                                                            //compares input date to current.  If less, date is valid (not in the future)
                                                            if(finalNewDate.compareTo(today) <= 0){
                                                                //converting date to char version for comparison to freq array
                                                                LocalDate selected = LocalDate.parse(dateConvert);

                                                                //flag for keeping track of if date is in the list of valid dates
                                                                Boolean dateValid = false;

                                                                //check all days of the week the habit occurs on.  If any match the input, then valid
                                                                for(String s: currentHabit.getFrequency()){

                                                                    if(s.equals(selected.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("CANADA")).toUpperCase(Locale.ROOT))){
                                                                        //tripping flag
                                                                        dateValid = true;
                                                                    }
                                                                }

                                                                //if date matched, then all conditions passed!  Add the habit event
                                                                if(dateValid == true){
                                                                    //date good!
                                                                    HabitEvent newEvent = new HabitEvent(finalNewDate, location.getText().toString(), comment.getText().toString(), habitName, takenPhotoID);
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
                                                                //date in the future!
                                                                new errorFragment("Cannot add event in the future! Today's date is: " + today).show(getSupportFragmentManager(), "Display_Error");

                                                            }
                                                        } else {
                                                            //date before start date of habit!
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
                                        //habit event cannot appear twice on same date for same habit
                                        new errorFragment("Event already exists for this date!").show(getSupportFragmentManager(), "Display_Error");

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });


        //returning to previous menu
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    /**
     * function to start the camera activity
     */
    private void takePicture() {
        System.out.println("Got here");
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,REQUEST_IMAGE_CAPTURE);
    }


    /**
     * function to encode the image into a string and store in takenPhotoID
     * @param bitmap
     */
    public void encodeBitmap(Bitmap bitmap){
        System.out.println("Running");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        takenPhotoID = Base64.encodeToString(baos.toByteArray(), DEFAULT);
        System.out.println(takenPhotoID);
    }



}