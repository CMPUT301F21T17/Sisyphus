
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A class that adapts Habit objects to their content_all_habit_list xml
 * Used in both AllHabitList and CalendarActivity
 */
public class AllHabitList_Adapter extends RecyclerView.Adapter<AllHabitList_Adapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Habit> habits;


    /**
     * Constructor for habit adapter
     * @param context
     *  current context
     * @param habits
     *  list of Habits
     */
    public AllHabitList_Adapter( Context context, ArrayList<Habit> habits) {
        inflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public AllHabitList_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_all_habit_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllHabitList_Adapter.MyViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitTitle.setText(habit.getHabitName());
        holder.habitDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(habit.getStartDate()));

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView habitTitle;
        private TextView habitDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitTitle = (TextView) itemView.findViewById(R.id.habit_title_text);
            habitDate = (TextView) itemView.findViewById(R.id.habit_date_text);
        }
    }
}
