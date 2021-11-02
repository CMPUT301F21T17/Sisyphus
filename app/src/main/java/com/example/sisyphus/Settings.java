package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//
// This activity is meant to represent our settings pages
// In this page, users can select an item and it will lead them to an activity where they can
// edit the information they want.
//
// User information will be auto filled from the database in text views to be seen
// Change password leads to a special activity to accommodate safety precautions
//
public class Settings extends AppCompatActivity {

    public static String item;


    TextView fName;
    TextView lName;
    TextView email;

    Button fNameEdit;
    Button lNameEdit;
    Button emailEdit;
    Button passwordEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersettings);

        // Insert here ties that retrieve users first name, last name, and email
        // Set it so that it replaces the text of fName, lName, and email

        // Initializing Textview for displaying current user information
        fName = findViewById(R.id.settingsFNameView);
        lName = findViewById(R.id.settingsLNameView);
        email = findViewById(R.id.settingsEmailView);
        // Initializing Buttons
        fNameEdit = findViewById(R.id.fNameEdit);
        lNameEdit = findViewById(R.id.lNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);


        // Passes the users first name to the info edit activity and switches activities
        fNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fNameInt = new Intent(getApplicationContext(), InfoEdit.class);
                // Put in users first name
                fNameInt.putExtra(item, "fName");
                startActivity(fNameInt);
            }
        });
        // Passes users last name to the info edit activity and switches activities
        lNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lNameInt = new Intent(getApplicationContext(), InfoEdit.class);
                // Put in users first name
                lNameInt.putExtra(item, "lName");
                startActivity(lNameInt);
            }
        });

        // Passes users email to info edit activity then switches activities
        emailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailInt = new Intent(getApplicationContext(), InfoEdit.class);
                // Put in users first name
                emailInt.putExtra(item, "email");
                startActivity(emailInt);
            }
        });
        // Moves to the change password activity, in a separate activity to maintain security
        passwordEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passInt = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(passInt);
            }
        });
    }
}