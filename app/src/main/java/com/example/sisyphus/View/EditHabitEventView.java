/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import static android.util.Base64.DEFAULT;
import static com.example.sisyphus.View.AddHabitEvent.REQUEST_IMAGE_CAPTURE;
import static com.example.sisyphus.View.AddHabitEvent.REQUEST_LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    String TAG = "Editing habit event";

    private String place;

    Button back;


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Bitmap takenPhoto;
    private String takenPhotoID = "";

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
            photo.setImageBitmap(takenPhoto);
            encodeBitmap(takenPhoto);
        }
    }

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
        habitTitle = findViewById(R.id.topbarText);
        add = findViewById(R.id.buttonAdd);
        cancel = findViewById(R.id.buttonCancel);

        photo = findViewById(R.id.editEventPhoto);

        back = findViewById(R.id.back);


        //getting habit name from intent (habit events require the name to be accessed)
        Intent intent = getIntent();
        HabitEvent EditEvent = (HabitEvent) intent.getSerializableExtra("editEvent");
        String EditEventID = intent.getStringExtra("editEventID");
        String habitName = EditEvent.getHabitName();
        Bitmap EditEventPhoto = decodeFromFirebase(EditEvent.getPhotoID());

        //setting UI to display data from selected habit event for editing
        habitTitle.setText("Edit " + EditEvent.getHabitName() + " Event");
        location.setText(EditEvent.getLocation());
        date.setText(new SimpleDateFormat("dd/MM/yyyy").format(EditEvent.getDate()));
        comment.setText(EditEvent.getComment());
        if(EditEvent.getPhotoID().equals("")){
            //do nothing!
        } else {
            photo.setImageBitmap(EditEventPhoto);
        }





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


        //create a map intent
        location.setOnClickListener(view -> {
            Intent googleMaps = new Intent(view.getContext(), GoogleMaps.class);
            startActivityForResult(googleMaps, REQUEST_LOCATION);
        });

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
                //HabitEvent newEvent = new HabitEvent(newDate, location.getText().toString(), comment.getText().toString(), habitName,takenPhotoID);

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
                                Date dummyDate = new Date(1000, 1, 1);
                                HabitEvent tempEvent = new HabitEvent(dummyDate,"","","","");
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventExists = true;
                                        tempEvent = document.toObject(HabitEvent.class);
                                    }

                                    if(eventExists == false || tempEvent.getDate().compareTo(EditEvent.getDate()) ==0) {
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
                                                        System.out.println(finalNewDate);
                                                        System.out.println(currentHabit.getStartDate());
                                                        System.out.println(finalNewDate.compareTo(currentHabit.getStartDate()));
                                                        if(finalNewDate.compareTo(currentHabit.getStartDate()) >= 0){
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
                                                                    HabitEvent newEvent = new HabitEvent(finalNewDate, location.getText().toString(), comment.getText().toString(), habitName, takenPhotoID);
                                                                    //storing event in firebase and returning to previous menu
                                                                    FirebaseStore fb = new FirebaseStore();
                                                                    fb.storeHabitEvent(mAuth.getUid(), habitName, newEvent);
                                                                    fb.deleteHabitEvent(mAuth.getUid(), habitName, EditEventID);
                                                                    Intent toEventDetail = new Intent(EditHabitEventView.this, ViewHabitEvent.class);
                                                                    //necessary details for previous menu
                                                                    toEventDetail.putExtra("user",mAuth.getUid());
                                                                    toEventDetail.putExtra("habit_event",newEvent);
                                                                    toEventDetail.putExtra("habit_eventID", EditEventID);
                                                                    startActivity(toEventDetail);
                                                                } else {
                                                                    //date not a valid date habit occurs on!
                                                                    new errorFragment("Cannot add event on day habit does not occur on! See habit details for valid days!").show(getSupportFragmentManager(), "Display_Error");
                                                                }


                                                            } else {
                                                                //date after today!
                                                                new errorFragment("Cannot add event in the future! Today's date is: " + today).show(getSupportFragmentManager(), "Display_Error");

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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        startActivityForResult(i,REQUEST_IMAGE_CAPTURE);
        //if(i.resolveActivity(getPackageManager()) != null){
         //   startActivityForResult(i,REQUEST_IMAGE_CAPTURE);
        //}
    }

    /**
     * function to encode the image into a string and store in takenPhotoID
     * @param bitmap
     */
    public void encodeBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        takenPhotoID = Base64.encodeToString(baos.toByteArray(), DEFAULT);
    }

    /**
     * function to decode the image code
     * @param image
     * @return Bitmap of image
     */
    public static Bitmap decodeFromFirebase(String image){
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
    }
}