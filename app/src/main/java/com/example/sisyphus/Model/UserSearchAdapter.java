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

public class UserSearchAdapter extends ArrayAdapter<SimpleUser> {


    private ArrayList<SimpleUser> userList;
    private Context context;

    public UserSearchAdapter( Context context, ArrayList<SimpleUser> userList) {
        super(context,0, userList);
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;



        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_search_content, parent,false);
        }
        //gets habit event from list and formats listview box with habit information
        SimpleUser user = userList.get(position);

        TextView userName = view.findViewById(R.id.user_name);
        TextView userID = view.findViewById(R.id.user_id);
        Button follow = view.findViewById(R.id.follow_button);


        userName.setText(user.getNameFirst() + " " + user.getNameLast());
        userID.setText(user.getId());

        SimpleUser requestUser = userList.get(position);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followProtocol protocol = new followProtocol();
                protocol.sendRequest(requestUser.getId());
                userList.remove(position);
                UserSearchAdapter.this.notifyDataSetChanged();
            }
        });

        return view;

    }
}

