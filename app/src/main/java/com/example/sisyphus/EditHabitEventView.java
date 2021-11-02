package com.example.sisyphus;

import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditHabitEventView extends AppCompatActivity {
    FirebaseAuth mAuth;


    EditText location;
    EditText date;
    EditText comment;
    TextView habitTitle;
    Button add;
    Button cancel;

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit_event);

        mAuth = FirebaseAuth.getInstance();

        location = findViewById(R.id.editTextLocation);
        date = findViewById(R.id.editTextDate);
        comment = findViewById(R.id.editTextComment);
        habitTitle = findViewById(R.id.textViewHabitName);
        add = findViewById(R.id.buttonAdd);
        cancel = findViewById(R.id.buttonCancel);

        Intent intent = getIntent();
        HabitEvent EditEvent = (HabitEvent) intent.getSerializableExtra("editEvent");
        String EditEventID = intent.getStringExtra("editEventID");
        String habitName = EditEvent.getHabitName();
        habitTitle.setText(EditEvent.getHabitName());
        location.setText(EditEvent.getLocation());
        date.setText(new SimpleDateFormat("dd/MM/yyyy").format(EditEvent.getDate()));
        comment.setText(EditEvent.getComment());



        //RIPPED STRAIGHT FROM SIHAN'S CODE!!

        //creating the calendar for user to input startdate
        date.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(EditHabitEventView.this, mDateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //Transparent Background
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
                Date newDate = null;
                try {
                    newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                HabitEvent newEvent = new HabitEvent(newDate, location.getText().toString(), comment.getText().toString(), habitName);
                FirebaseStore fb = new FirebaseStore();
                fb.editHabitEvent(mAuth.getUid(), habitName,EditEventID,newEvent);
                Intent toEventDetail = new Intent(EditHabitEventView.this,ViewHabitEvent.class);
                toEventDetail.putExtra("user",mAuth.getUid());
                toEventDetail.putExtra("habit_event",newEvent);
                toEventDetail.putExtra("habit_eventID", EditEventID);
                startActivity(toEventDetail);
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