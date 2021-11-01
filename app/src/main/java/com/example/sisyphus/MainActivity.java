package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mauth = new FirebaseAuth();
        Intent i = new Intent(MainActivity.this, CalendarActivity.class);
        i.putExtra("MAUTH", mauth);
    }
}