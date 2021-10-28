package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ViewHabit extends AppCompatActivity {

    EditText habitTitleText, startDateText, frequencyText, habitReasonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        habitTitleText = findViewById(R.id.habitTitleText);
        startDateText = findViewById(R.id.startDateText);
        frequencyText = findViewById(R.id.frequencyText);
        habitReasonText = findViewById(R.id.habitReasonText);

        final Button viewHabitEventButton = findViewById(R.id.viewHabitEventButton);
        viewHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
            }
        });

        final Button addHabitEventButton = findViewById(R.id.addHabitEventButton);
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
            }
        });

        final Button editHabitButton = findViewById(R.id.editHabitButton);
        editHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
            }
        });

        final Button deleteHabitButton = findViewById(R.id.deleteHabitButton);
        deleteHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
            }
        });
    }
}