package com.example.sisyphus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewHabit extends AppCompatActivity {

    TextView habitTitleText, startDateText, frequencyText, habitReasonText;

    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        mAuth = FirebaseAuth.getInstance();

        habitTitleText = findViewById(R.id.habitTitleText);
        startDateText = findViewById(R.id.startDateText);
        frequencyText = findViewById(R.id.frequencyText);
        habitReasonText = findViewById(R.id.habitReasonText);

        Intent intent = getIntent();
        final String TAG = intent.getStringExtra("tag");
        String receivedUser = intent.getStringExtra("user");
        Habit receivedHabit = (Habit) intent.getSerializableExtra("habit");
        ArrayList<String> receivedFrequency = receivedHabit.getFrequency();
        String receivedTitle = receivedHabit.getHabitName();
        String receivedDate = new SimpleDateFormat("dd/MM/yyyy").format(receivedHabit.getStartDate());
        String receivedReason = receivedHabit.getReason();


        habitTitleText.setText(receivedTitle);
        startDateText.setText(receivedDate);
        habitReasonText.setText(receivedReason);
        frequencyText.setText(setFrequencyText(receivedFrequency));


        final Button viewHabitEventButton = findViewById(R.id.viewHabitEventButton);
        viewHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewHabitEventInt = new Intent(view.getContext(), ListHabitEvent.class);
                viewHabitEventInt.putExtra("1", receivedTitle);
                startActivity(viewHabitEventInt);
            }
        });

        final Button addHabitEventButton = findViewById(R.id.addHabitEventButton);
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addHabit = new Intent(view.getContext(), AddHabitEvent.class);
                addHabit.putExtra("1", receivedTitle);
                startActivity(addHabit);
            }
        });

        final Button editHabitButton = findViewById(R.id.editHabitButton);
        editHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewHabit.this,AllHabitListView.class);
                intent.putExtra("currentUserID",mAuth.getUid());
                intent.putExtra("currentTag","Sample");
                startActivity(intent);
            }
        });

        final Button deleteHabitButton = findViewById(R.id.deleteHabitButton);
        deleteHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHabit dialog = new deleteHabit();
                Bundle bundle = new Bundle();
                bundle.putString("selectedTitle",receivedTitle);
                bundle.putString("selectedUser",receivedUser);
                bundle.putString("selectedTag",TAG);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "DELETE");
            }
        });
    }

    public String setFrequencyText(ArrayList<String> frequencyArray) {
        //Initialize string builder
        StringBuilder stringBuilder = new StringBuilder();
        //Use for loop
        for (int j = 0; j < frequencyArray.size(); j++) {
            //Concat array value
            stringBuilder.append(frequencyArray.get(j).substring(0,3));
            //Check condition
            if (j != frequencyArray.size() - 1) {
                //When j value not equal to day list size -1
                //Add comma
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}