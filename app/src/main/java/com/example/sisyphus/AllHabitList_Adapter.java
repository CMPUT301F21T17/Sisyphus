package com.example.sisyphus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AllHabitList_Adapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public AllHabitList_Adapter( Context context, ArrayList<Habit> habits) {
        super(context,0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_all_habit_list, parent,false);
        }

        Habit habit = habits.get(position);

        TextView habitTitle = view.findViewById(R.id.habit_title_text);
        TextView habitDate = view.findViewById(R.id.habit_date_text);

        habitTitle.setText(habit.getHabitName());
        habitDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(habit.getStartDate()));

        return view;

    }
}
