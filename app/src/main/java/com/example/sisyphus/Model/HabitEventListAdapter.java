/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sisyphus.R;

import java.util.ArrayList;

/**
 * A class that adapts Habit Events from an array to their habit habit_event_content.xml
 * Used in Habit event list
 */
public class HabitEventListAdapter extends ArrayAdapter<HabitEvent> {

    private ArrayList<HabitEvent> habitEvents;
    private Context context;

    /**
     * Constructor for HabitEventListAdapter
     * @param context
     *  current context
     * @param habitEvents
     *  list of habit events to add
     */
    public HabitEventListAdapter( Context context, ArrayList<HabitEvent> habitEvents) {
        super(context,0, habitEvents);
        this.habitEvents = habitEvents;
        this.context = context;
    }

    /**
     * A function called when populating ListHabitEvent
     * @param position
     *  position in data list of current HabitEvent
     * @param convertView
     * @param parent
     * @return
     *  populated habit event content view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_event_content, parent,false);
        }
        //gets habit event from list and formats listview box with habit information
        HabitEvent habitEvent = habitEvents.get(position);

        TextView habitEventTitle = view.findViewById(R.id.habit_title);
        TextView habitEventComment = view.findViewById(R.id.habit_event_comment);
        TextView habitEventDate = view.findViewById(R.id.habit_event_date);
        TextView habitEventLocation = view.findViewById(R.id.habit_event_location);
        ImageView habitEventPhoto = view.findViewById(R.id.habit_event_picture);

        habitEventTitle.setText(habitEvent.getHabitName());
        habitEventComment.setText("Comment: " + habitEvent.getComment());
        habitEventDate.setText(habitEvent.getDate().toString().substring(0,10));
        habitEventLocation.setText("Location: " + habitEvent.getLocation());
        habitEventPhoto.setImageBitmap(decodeFromFirebase(habitEvent.getPhotoID()));
        return view;
    }

    /**
     * function to decode the image code
     * @return Bitmap of image
     */
    public static Bitmap decodeFromFirebase(String image){
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
    }
}