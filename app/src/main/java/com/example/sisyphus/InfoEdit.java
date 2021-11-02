package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//
// This activity represents the area where users can edit their information
//
// Users are sent from the settings activity, with an extra from the intent that labels
// which bit of user information is being corrected.
// User will be able to see the current iteration of this information in a textview
// Then under the new section, they can edit the information.
// Upon pressing confirm, firebase is updated with the new information and user is returned to
// the settings activity.
//


public class InfoEdit extends AppCompatActivity {

    TextView infoCurrentItem;
    EditText infoEditItem;
    Button editCancel;
    Button editConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoedit);

        // Initializing Text and Edit Views
        infoCurrentItem = findViewById(R.id.infoCurrentItem);
        infoEditItem = findViewById(R.id.infoEditItem);
        // Initializing Buttons
        editCancel = findViewById(R.id.editCancel);
        editConfirm = findViewById(R.id.editConfirm);

        Intent intent = getIntent();
        String item = intent.getStringExtra(Settings.item);

        // If item == "fName"
        //    infoCurrentItem.setText(whatever users last name is)
        // Elif item == "lName"
        //    infoCurrentItem.setText(whatever users last name is)
        // Elif item == "Email"
        //    infoCurrentItem.setText(whatever users email is)

        // Abandons the changes and just returns back to settings
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelInt = new Intent(getApplicationContext(), Settings.class);
                startActivity(cancelInt);
            }
        });

        editConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Eventually add error checking here
                // Send data to database that changes the item specified
                // If item is first name, then replace databases first name, and so on

                // Then eventually we switch back to settings
                Intent confirmInt = new Intent(getApplicationContext(), Settings.class);
                startActivity(confirmInt);
            }
        });
    }
}