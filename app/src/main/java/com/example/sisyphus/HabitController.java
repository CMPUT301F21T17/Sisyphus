package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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


public class HabitController extends AppCompatActivity {
    private EditText startDate, frequency,reason;
    private TextView habitName;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button addHabit,viewHabit,deleteButton;
    private FirebaseStore testbase = new FirebaseStore();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_controller);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("junrui@gmail.com","123456");
        ImageView cancelButton = findViewById(R.id.cancelButton);
        addHabit = findViewById(R.id.addHabit);
        viewHabit = findViewById(R.id.viewHabit);
        deleteButton = findViewById(R.id.deleteButton);
        habitName = findViewById(R.id.habitName);
        startDate = findViewById(R.id.startDate);
        frequency = findViewById(R.id.frequency);
        reason = findViewById(R.id.reason);
        ArrayList<String> days = new ArrayList<>();
        String dummyUser = "garbage";
        String dummyhabitname1 = "s";


        //getting habit info from database
        DocumentReference docRef = db.collection("Users").document(dummyUser).collection("Habits").document(dummyhabitname1);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Habit habit1 = documentSnapshot.toObject(Habit.class);
                habitName.setText(dummyhabitname1);
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                startDate.setText(simpleDateFormat.format(habit1.getStartDate()));
                frequency.setText(habit1.getFrequency().toString().replace("[","").replace("]",""));
                reason.setText(habit1.getReason());
            }
        });

        //Onclick for the the addHabit event button and storing data
        addHabit.setOnClickListener(view -> {
            Date dateInput = null;
            try {
                dateInput = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String reasonInput = reason.getText().toString().trim();

//            //Intent to send data Addhabit
//            //(dummyUser,dummyhabitname,dateInput,days,reasonInput);
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
                                    String dummyhabitname = "s";
                                    testbase.deleteHabit(dummyUser,dummyhabitname);
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