package com.example.sisyphus.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sisyphus.R;

import java.util.ArrayList;

public class HabitEventListAdapter extends ArrayAdapter<HabitEvent> {

    private ArrayList<HabitEvent> habitEvents;
    private Context context;

    public HabitEventListAdapter( Context context, ArrayList<HabitEvent> habitEvents) {
        super(context,0, habitEvents);
        this.habitEvents = habitEvents;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_event_content, parent,false);
        }

        HabitEvent habitEvent = habitEvents.get(position);

        TextView habitEventTitle = view.findViewById(R.id.habit_title);
        TextView habitEventComment = view.findViewById(R.id.habit_event_comment);
        TextView habitEventDate = view.findViewById(R.id.habit_event_date);
        TextView habitEventLocation = view.findViewById(R.id.habit_event_location);

        habitEventTitle.setText(habitEvent.getHabitName());
        habitEventComment.setText(habitEvent.getComment());
        habitEventDate.setText(habitEvent.getDate().toString().substring(0,10));
        habitEventLocation.setText(habitEvent.getLocation());

        return view;

    }
}