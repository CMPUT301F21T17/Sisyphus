/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.R;
import com.example.sisyphus.View.Dialog.deleteHabit;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A class to view the details of a habit
 */
public class ViewHabit extends AppCompatActivity {
    //setting UI elements
    TextView habitTitleText, startDateText, frequencyText, habitReasonText, topbarText;
    Button back, overflow;
    //initializing firebase authentication (session) object
    FirebaseAuth mAuth;
    private String receivedTitle, receivedDate, receivedReason, receivedUser, TAG;

    /**
     * Create a view to display all details of a habit
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        //setting authentication object to current session (signed in user) and setting log message
        mAuth = FirebaseAuth.getInstance();
        TAG = "Sample";

        //attaching UI elements to variables
        habitTitleText = findViewById(R.id.habitTitleText);
        startDateText = findViewById(R.id.startDateText);
        frequencyText = findViewById(R.id.frequencyText);
        habitReasonText = findViewById(R.id.habitReasonText);
        topbarText = findViewById(R.id.topbarText);

        //getting habit from intent
        Intent intent = getIntent();
        Habit receivedHabit = (Habit) intent.getSerializableExtra("habit");

        receivedUser = mAuth.getUid();

        //setting up UI elements to display habit information
        ArrayList<String> receivedFrequency = receivedHabit.getFrequency();
        receivedTitle = receivedHabit.getHabitName();
        receivedDate = new SimpleDateFormat("dd/MM/yyyy").format(receivedHabit.getStartDate());
        receivedReason = receivedHabit.getReason();
        habitTitleText.setText(receivedTitle);
        topbarText.setText(receivedTitle);
        startDateText.setText(receivedDate);
        habitReasonText.setText(receivedReason);
        frequencyText.setText(setFrequencyText(receivedFrequency));


        final Button viewHabitEventButton = findViewById(R.id.viewHabitEventButton);
        viewHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to view Habit Event
             */
            public void onClick(View view) {
                Intent viewHabitEventInt = new Intent(view.getContext(), ListHabitEvent.class);
                viewHabitEventInt.putExtra("1", receivedTitle);
                startActivity(viewHabitEventInt);
            }
        });

        final Button addHabitEventButton = findViewById(R.id.addHabitEventButton);
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * A function to switch to view Add habit event
             */
            public void onClick(View view) {
                Intent addHabit = new Intent(view.getContext(), AddHabitEvent.class);
                addHabit.putExtra("1", receivedTitle);
                startActivity(addHabit);
            }
        });


        overflow = (Button) findViewById(R.id.overflow);
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                showPopup(v);
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AllHabitListView.class);
                startActivity(intent);
            }
        });

    }

    /**
     * setFrequencyText
     *  A function to get display string for Frequency Field
     * @param frequencyArray
     * @return
     */
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
    public void showPopup(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.Theme_App_Overflow);
        PopupMenu popup = new PopupMenu(wrapper, v, Gravity.LEFT, R.style.Theme_App_Overflow, 0);
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.habit_dropdown, popup.getMenu());

        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.habit_dropdown, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the main activity layout object.
        // Get clicked menu item id.
        int itemId = item.getItemId();
        if(itemId == R.id.editHabit)
        {
            Intent editHabit = new Intent(this, HabitController.class);
            editHabit.putExtra("1", receivedTitle);
            startActivity(editHabit);

        }else if(itemId == R.id.deleteHabit)
        {
            deleteHabit dialog = new deleteHabit();
            Bundle bundle = new Bundle();
            bundle.putString("selectedTitle", receivedTitle);
            bundle.putString("selectedUser", receivedUser);
            bundle.putString("selectedTag", TAG);
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "DELETE");

        }
        return true;
    }

}