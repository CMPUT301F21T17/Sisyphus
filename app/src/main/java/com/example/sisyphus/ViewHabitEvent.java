package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;

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
        String receivedHabitEventID = intent.getStringExtra("habit_eventID");
        HabitEvent receivedHabitEvent = (HabitEvent) intent.getSerializableExtra("habit_event");
        habitEventTitleText.setText(receivedHabitEvent.getHabitName());
        habitEventDateText.setText(new SimpleDateFormat("dd/MM/yyyy").format(receivedHabitEvent.getDate()));
        habitEventLocationText.setText(receivedHabitEvent.getLocation());
        habitEventCommentText.setText(receivedHabitEvent.getComment());

        final Button editHabitEventButton = findViewById(R.id.edit_habitEvent_button);
        editHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
                Intent editHabit = new Intent(view.getContext(), EditHabitEventView.class);
                editHabit.putExtra("editEvent",receivedHabitEvent);
                editHabit.putExtra("editEventID",receivedHabitEventID);
                startActivity(editHabit);
            }
        });

        final Button deleteHabitEventButton = findViewById(R.id.delete_habitEvent_button);
        deleteHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHabitEvent dialog = new deleteHabitEvent();
                Bundle bundle = new Bundle();
                bundle.putString("selectedHabit",receivedHabitEvent.getHabitName());
                bundle.putString("selectedEventID",receivedHabitEventID);
                bundle.putString("selectedUser",receivedUser);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "DELETE");
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabitEvent.this,AllHabitListView.class);
                intent.putExtra("currentUserID",mAuth.getUid());
                intent.putExtra("currentTag","Sample");
                startActivity(intent);
            }
        });

        final Button button_backToList = findViewById(R.id.BackButton);
        button_backToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEventList = new Intent(ViewHabitEvent.this,ListHabitEvent.class);
                toEventList.putExtra("1",receivedHabitEvent.getHabitName());
                startActivity(toEventList);
            }
        });

    }
}