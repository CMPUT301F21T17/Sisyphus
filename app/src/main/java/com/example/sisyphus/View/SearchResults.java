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
 * Class for displaying the data of a user that was searched, and giving the user the option to
 * send a follow request.
 */

//notes to self on repeat searches:
//-if user has already sent request to this user, make the button unclikable and change it's text
//-requires an extra layer of database information: that being who the user has sent requests to!
//-if a request is rejected, delete that user from the sender's sent-request list!

//RESULTS:  3 new collections for users!  (sentRequests, incomingRequests, followedUsers)
public class SearchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
    }
}