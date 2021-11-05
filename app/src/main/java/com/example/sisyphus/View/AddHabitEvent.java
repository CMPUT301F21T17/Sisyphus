package com.example.sisyphus.View;

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

    private FirebaseAuth mAuth;

    private EditText location,date,comment;
    private TextView habitTitle;
    private Button add,cancel;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    /**
     * function to create HabitEvent creation view
     * @param savedInstanceState
     */
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //Transparent Background
            dialog.show();
        });
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String newDate = day + "/" + month + "/" + year;
            date.setText(newDate);
        };



        //METHOD CURRENTLY ASSUMES FRIENDLY USER INPUT!!!
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date newDate = null;
                try {
                    newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                HabitEvent newEvent = new HabitEvent(newDate, location.getText().toString(), comment.getText().toString(), habitName);
                FirebaseStore fb = new FirebaseStore();
                fb.storeHabitEvent(mAuth.getUid(), habitName, newEvent);
                Intent toEventList = new Intent(AddHabitEvent.this, ListHabitEvent.class);
                toEventList.putExtra("1",habitName);
                startActivity(toEventList);
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