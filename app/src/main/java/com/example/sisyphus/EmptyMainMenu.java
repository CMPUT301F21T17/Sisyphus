package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class EmptyMainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_main_menu);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        System.out.println(mAuth.getCurrentUser().getUid());

    }
}