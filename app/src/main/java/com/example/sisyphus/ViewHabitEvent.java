package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ViewHabitEvent extends AppCompatActivity {
    TextView habitEventTitleText, habitEventDateText, habitEventLocationText, habitEventCommentText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_event);

        mAuth = FirebaseAuth.getInstance();

        habitEventTitleText = findViewById(R.id.habit_title);
        habitEventDateText = findViewById(R.id.habit_event_date);
        habitEventLocationText = findViewById(R.id.habit_event_location);
        habitEventCommentText = findViewById(R.id.habit_event_comment);

        Intent intent = getIntent();
        //final String TAG = intent.getStringExtra("tag");
        String receivedUser = intent.getStringExtra("user");
        HabitEvent receivedHabitEvent = (HabitEvent) intent.getSerializableExtra("habit_event");
        habitEventTitleText.setText(receivedHabitEvent.getHabitName());
        habitEventDateText.setText(receivedHabitEvent.getDate().toString());
        habitEventLocationText.setText(receivedHabitEvent.getLocation());
        habitEventCommentText.setText(receivedHabitEvent.getComment());

        final Button editHabitEventButton = findViewById(R.id.edit_habitEvent_button);
        editHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
            }
        });

        /*final Button deleteHabitEventButton = findViewById(R.id.delete_habitEvent_button);
        deleteHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHabit dialog = new deleteHabit();
                Bundle bundle = new Bundle();
                bundle.putString("selectedEventDate",receivedTitle);
                bundle.putString("selectedUser",receivedUser);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "DELETE");
            }
        });*/
    }
}