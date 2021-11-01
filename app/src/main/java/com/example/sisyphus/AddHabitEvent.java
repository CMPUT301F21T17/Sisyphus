package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class AddHabitEvent extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText location;
    EditText date;
    EditText comment;
    TextView habitTitle;
    Button add;
    Button cancel;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

import android.os.Bundle;

public class AddHabitEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);

        mAuth = FirebaseAuth.getInstance();

        location = findViewById(R.id.editTextLocation);
        date = findViewById(R.id.editTextDate);
        comment = findViewById(R.id.editTextComment);
        habitTitle = findViewById(R.id.textViewHabitName);
        add = findViewById(R.id.buttonAdd);
        cancel = findViewById(R.id.buttonCancel);

        Intent intent = getIntent();
        String habitName = intent.getStringExtra("1");

        habitTitle.setText(habitName);




        //RIPPED STRAIGHT FROM SIHAN'S CODE!!

        //creating the calendar for user to input startdate
        date.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(AddHabitEvent.this, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //Transparent Background
            dialog.show();
        });
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String newDate = month + "/" + day + "/" + year;
            date.setText(newDate);
        };



        //METHOD CURRENTLY ASSUMES FRIENDLY USER INPUT!!!
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date newDate = new Date(date.getText().toString());
                System.out.println(newDate);
                System.out.println(habitName);
                HabitEvent newEvent = new HabitEvent(newDate, location.getText().toString(), comment.getText().toString(), habitName);
                FirebaseStore fb = new FirebaseStore();
                fb.storeHabitEvent(mAuth.getUid(), habitName, newEvent);
                finish();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }
}