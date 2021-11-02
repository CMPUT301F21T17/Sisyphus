package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mauth = FirebaseAuth.getInstance();
        mauth.signInWithEmailAndPassword("ktbrown@ualberta.ca", "123456");
        Intent i = new Intent(MainActivity.this, CalendarActivity.class);
        startActivity(i);


    }
}