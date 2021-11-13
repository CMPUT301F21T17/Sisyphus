/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import static android.util.Base64.DEFAULT;
import static com.example.sisyphus.View.AddHabitEvent.REQUEST_IMAGE_CAPTURE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sisyphus.Model.FirebaseStore;
import com.example.sisyphus.Model.HabitEvent;
import com.example.sisyphus.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A class to Edit Habit Events
 */
public class EditHabitEventView extends AppCompatActivity {
    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;

    //setting UI elements
    EditText location;
    EditText date;
    EditText comment;
    TextView habitTitle;
    Button add;
    Button cancel;
    ImageView photo;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Bitmap takenPhoto;
    private String takenPhotoID;

    /**
     * Create editor for Habit Events
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit_event);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();

        //attaching UI elements to variables
        location = findViewById(R.id.editTextLocation);
        date = findViewById(R.id.editTextDate);
        comment = findViewById(R.id.editTextComment);
        habitTitle = findViewById(R.id.textViewHabitName);
        add = findViewById(R.id.buttonAdd);
        cancel = findViewById(R.id.buttonCancel);
        photo = findViewById(R.id.editEventPhoto);

        //getting habit name from intent (habit events require the name to be accessed)
        Intent intent = getIntent();
        HabitEvent EditEvent = (HabitEvent) intent.getSerializableExtra("editEvent");
        String EditEventID = intent.getStringExtra("editEventID");
        String habitName = EditEvent.getHabitName();
        Bitmap EditEventPhoto = decodeFromFirebase(EditEvent.getPhotoID());

        //setting UI to display data from selected habit event for editing
        habitTitle.setText(EditEvent.getHabitName());
        location.setText(EditEvent.getLocation());
        date.setText(new SimpleDateFormat("dd/MM/yyyy").format(EditEvent.getDate()));
        comment.setText(EditEvent.getComment());
        photo.setImageBitmap(EditEventPhoto);


        //creating the calendar for user to input habit event date
        date.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(EditHabitEventView.this, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //Transparent Background
            dialog.show();
        });
        //formatting date selected from calendar for output
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String newDate = day + "/" + month + "/" + year;
            date.setText(newDate);
        };

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });



        //onClick method to get data from text entry fields and format into habit event fields to be edited
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data from text-entry fields and convert to habit event
                Date newDate = null;
                try {
                    newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                HabitEvent newEvent = new HabitEvent(newDate, location.getText().toString(), comment.getText().toString(), habitName,takenPhotoID);

                //connect to database and store modified habit event before returning to previous menu
                FirebaseStore fb = new FirebaseStore();
                fb.editHabitEvent(mAuth.getUid(), habitName,EditEventID,newEvent);
                Intent toEventDetail = new Intent(EditHabitEventView.this, ViewHabitEvent.class);
                //necessary details for previous menu
                toEventDetail.putExtra("user",mAuth.getUid());
                toEventDetail.putExtra("habit_event",newEvent);
                toEventDetail.putExtra("habit_eventID", EditEventID);
                startActivity(toEventDetail);
            }
        });

        //cancels edit and returns to previous menu
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
            photo.setImageBitmap(takenPhoto);
            encodeBitmap(takenPhoto);
        }
    }

    public void encodeBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        takenPhotoID = Base64.encodeToString(baos.toByteArray(), DEFAULT);
    }

    public static Bitmap decodeFromFirebase(String image){
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
    }
}