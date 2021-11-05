package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sisyphus.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Homepage of app
 */
public class EmptyMainMenu extends AppCompatActivity {
    final String TAG = "Sample";
    FirebaseAuth mAuth;

    /**
     * Creates the homepage of the app with settings and menu bar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_main_menu);

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        final Button button_Settings = findViewById(R.id.settingsButton);
        button_Settings.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(EmptyMainMenu.this, Settings.class);
                startActivity(intent);
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyMainMenu.this, CalendarActivity.class);
                startActivity(intent);
            }
        });


    }

}