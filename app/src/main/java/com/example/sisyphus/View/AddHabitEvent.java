/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sisyphus.Model.FirebaseStore;
import com.example.sisyphus.Model.HabitEvent;
import com.example.sisyphus.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A class to Add Habit Events
 */
public class AddHabitEvent extends AppCompatActivity {
    //initializing firebase authentication (session) object
    private FirebaseAuth mAuth;
    private Bitmap takenPhoto;

    //setting UI elements
    private EditText location,date,comment;
    private TextView habitTitle;
    private Button add,cancel;
    private ImageView habitPhoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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
        habitPhoto = findViewById(R.id.photoView);
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

        habitPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });



        //onClick method to get data from text entry fields and format into habit event to be added
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting input and creating event
                Date newDate = null;
                try {
                    newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                HabitEvent newEvent = new HabitEvent(newDate, location.getText().toString(), comment.getText().toString(), habitName,takenPhoto);

                //storing event in firebase and returning to previous menu
                FirebaseStore fb = new FirebaseStore();
                fb.storeHabitEvent(mAuth.getUid(), habitName, newEvent);
                Intent toEventList = new Intent(AddHabitEvent.this, ListHabitEvent.class);
                toEventList.putExtra("1",habitName);
                startActivity(toEventList);
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

    private void takePicture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            takenPhoto = (Bitmap) extras.get("data");
            habitPhoto.setImageBitmap(takenPhoto);
        }
    }


}