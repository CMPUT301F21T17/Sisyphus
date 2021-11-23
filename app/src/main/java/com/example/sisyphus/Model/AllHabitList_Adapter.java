
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
public class AllHabitList_Adapter extends RecyclerView.Adapter<AllHabitList_Adapter.HabitViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Habit> habits;
    private ItemClickListener itemListener;


    /**
     * Constructor for habit adapter
     * @param context
     *  current context
     * @param habits
     *  list of Habits
     */
    public AllHabitList_Adapter( Context context, ArrayList<Habit> habits, ItemClickListener itemListener) {
        inflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;
        this.itemListener = itemListener;
    }

    /**
     * function for creating a Haibt ViewHolder
     * @param parent
     *  parent view of view holder
     * @param viewType
     *  type of ViewHolder
     * @return
     *  the habit ViewHolder created
     */
    @NonNull
    @Override
    public AllHabitList_Adapter.HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_all_habit_list, parent, false);
        return new HabitViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AllHabitList_Adapter.HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitTitle.setText(habit.getHabitName());
        holder.habitDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(habit.getStartDate()));

        holder.itemView.setOnClickListener(view -> {
                itemListener.onItemClick(habits.get(position));
            });
    }

    /**
     *
     */
    public interface ItemClickListener {
        void onItemClick(Habit habit);
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return habits.size();
    }

    /**
     *
     */
    public class HabitViewHolder extends RecyclerView.ViewHolder {
        private TextView habitTitle;
        private  TextView habitDate;

        /**
         *
         * @param itemView
         */
        public HabitViewHolder(View itemView) {
            super(itemView);
            habitTitle = (TextView) itemView.findViewById(R.id.habit_title_text);
            habitDate = (TextView) itemView.findViewById(R.id.habit_date_text);
        }
    }
}
