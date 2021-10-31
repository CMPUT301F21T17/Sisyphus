package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignIn extends AppCompatActivity {

    EditText signInEmail;
    EditText signInPassword;
    Button signInConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        EditText signInEmail = findViewById(R.id.signInEmail);
        EditText signInPassword = findViewById(R.id.signInPassword);
        Button signInConfirm = findViewById(R.id.registerConfirm);

        signInConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here is where we can tie the sign in functions
                // Data Threads: signInEmail as the users email, signInPassword as the users password
            }
        });

    }
}