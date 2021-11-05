/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.sisyphus.R;

/**
 * A class to search the Firebase for a user, along with any other necessary firebase documents
 *  Incomplete, and may not be fully completed depending on the layout of the project going forwards
 */
public class UserSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
    }
}