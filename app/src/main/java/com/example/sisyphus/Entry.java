package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Entry extends AppCompatActivity {

    Button signIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        signIn = findViewById(R.id.goToSignIn);
        signUp = findViewById(R.id.goToSignUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signUpInt = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signUpInt);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent registerInt = new Intent(getApplicationContext(), Register.class);
                startActivity(registerInt);
            }
        });
    }
}