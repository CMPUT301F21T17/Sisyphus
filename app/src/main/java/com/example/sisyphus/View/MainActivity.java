/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.example.sisyphus.R;

/**
 * The starting activity for the program. Will not contain any meaningful
 * code other than what is necessary to launch the splash screen for logging in
 * and signing up
 */
public class MainActivity extends AppCompatActivity {


    /**
     * Called when app is first opened
     * @param savedInstanceState
     *  previous instances' states
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //launch app entry splashscreen
        Intent intent = new Intent(getApplicationContext(), Entry.class);
        startActivity(intent);
    }




}