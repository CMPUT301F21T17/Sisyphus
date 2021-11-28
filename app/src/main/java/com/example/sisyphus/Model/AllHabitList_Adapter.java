
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sisyphus.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A class that adapts Habit objects to their content_all_habit_list xml
 * Used in both AllHabitList and CalendarActivity
 */
public class AllHabitList_Adapter extends RecyclerView.Adapter<AllHabitList_Adapter.HabitViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<Habit> habits;
    private final ItemClickListener itemListener;

    private Context context;
    private ArrayList<String> completionPercents;

    /**
     * Constructor for habit adapter
     * @param context
     *  current context
     * @param habits
     *  list of Habits
     * @param percents
     * the list of how well a user has been following a habit (in % out of 100 format) for each habit
     */
    public AllHabitList_Adapter( Context context, ArrayList<Habit> habits, ArrayList<String> percents, ItemClickListener itemListener) {
        inflater = LayoutInflater.from(context);
        this.habits = habits;
        this.itemListener = itemListener;
        this.completionPercents= percents;
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
     *  Function to populate habit ViewHolder
     * @param holder
     *  holder to populate with data
     * @param position
     *  position of view to be populated
     */
    @Override
    public void onBindViewHolder(@NonNull AllHabitList_Adapter.HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitTitle.setText(habit.getHabitName());
        holder.habitDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(habit.getStartDate()));
        System.out.println("Set indicator progress to be: " + completionPercents.get(position));
        holder.indicator.setProgress(Integer.valueOf(completionPercents.get(position)));
        holder.itemView.setOnClickListener(view -> itemListener.onItemClick(habits.get(position)));
    }

    /**
     * function to get the count of items
     * @return
     *  Count of items in habits
     */
    @Override
    public int getItemCount() {
        return habits.size();
    }

    /**
     * Updates positions of from and to habits in database
     * @param db
     *  database to update
     * @param currentUserID
     *  current user who owns habits
     * @param from
     *  from habit
     * @param to
     *  to habit
     */
    public void editPosition(FirebaseFirestore db, String currentUserID, Habit from, Habit to) {
        // update position field in habit in db, Must be done at same time otherwise SnapshotListener messes with it
        WriteBatch batch = db.batch();
        DocumentReference fromRef = db.collection("Users").document(currentUserID)
                .collection("Habits").document(from.getHabitName());
        DocumentReference toRef = db.collection("Users").document(currentUserID)
                .collection("Habits").document(to.getHabitName());

        batch.update(fromRef, "position", from.getPosition());
        batch.update(toRef, "position", to.getPosition());



        batch.commit();

        notifyItemMoved(from.getPosition(), to.getPosition());
    }


    /**
     *
     */
    public interface ItemClickListener {
        void onItemClick(Habit habit);
    }


    /**
     *
     */
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView habitTitle;
        private final TextView habitDate;
        private final ProgressBar indicator;

        /**
         * Construct a View holder for Habit
         * @param itemView
         *  current item being referenced
         */
        public HabitViewHolder(View itemView) {
            super(itemView);
            habitTitle = (TextView) itemView.findViewById(R.id.habit_title_text);
            habitDate = (TextView) itemView.findViewById(R.id.habit_date_text);
            indicator = itemView.findViewById(R.id.progressIndicator);
        }
    }
}
