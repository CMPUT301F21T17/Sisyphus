/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sisyphus.R;

import java.util.ArrayList;

/**
 * Custom adapter that permits the display of user name (first and last) as well as
 * ID in a listview in order to represent unique results obtained after seraching for users by name
 */
public class UserSearchAdapter extends ArrayAdapter<SimpleUser> {

    //setting information variables
    private ArrayList<SimpleUser> userList;
    private Context context;

    /**
     * Parametrized constructor
     * @param context
     * the context object in which to display
     * @param userList
     * the list of user information to display in each view
     */
    public UserSearchAdapter( Context context, ArrayList<SimpleUser> userList) {
        super(context,0, userList);
        this.userList = userList;
        this.context = context;
    }

    /**
     * function that sets and updates the view for each item in the listview
     * @param position
     * the position in the listview to update
     * @param convertView
     * the view to be updated and returned
     * @param parent
     * the parent object of the view
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;


        //intial view setup
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_search_content, parent,false);
        }
        //gets habit event from list and formats listview box with habit information
        SimpleUser user = userList.get(position);

        //sets UI elements
        TextView userName = view.findViewById(R.id.user_name);
        TextView userID = view.findViewById(R.id.user_id);
        Button follow = view.findViewById(R.id.follow_button);

        //Setting text for display
        userName.setText(user.getNameFirst() + " " + user.getNameLast());
        userID.setText(user.getId());

        //gets the correct user relative to the selected listview item
        SimpleUser requestUser = userList.get(position);

        //onClickListener to enable the user to click the follow button and send a follow request
        //to users via the listview
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creates object to initiate request send and sends request, then deleting the
                //listview item to prevent duplication
                followProtocol protocol = new followProtocol();
                protocol.sendRequest(requestUser.getId());
                userList.remove(position);
                UserSearchAdapter.this.notifyDataSetChanged();
            }
        });

        return view;

    }
}

