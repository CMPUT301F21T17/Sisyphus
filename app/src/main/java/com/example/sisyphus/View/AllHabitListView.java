/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.habitFollowCalculator;
import com.example.sisyphus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A class to display all Habits in a views list
 */
public class AllHabitListView extends AppCompatActivity {
    //setting UI elements and initializing storage/formatting for listview
    private RecyclerView allhabitListView;
    private AllHabitList_Adapter habitAdapter;
    private ArrayList<Habit> habitDataList;
    private int fromPosition;
    private int toPosition;

    //initializing firebase authentication (session) object and establishing database connection
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    final CollectionReference collectionReference = db.collection("Users");

    /**
     * create view to display all habit events
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit_list);

        //setting authentication object to current session (signed in user)
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String currentUserID = mAuth.getUid();

        //attaching UI elements, and formatting listview boxes as well as storage array for habits
        allhabitListView= findViewById(R.id.allhabit_list);
        habitDataList = new ArrayList<>();

        setUserHabit(currentUserID);

        habitAdapter = new AllHabitList_Adapter(this, habitDataList);
        allhabitListView.setAdapter(habitAdapter);





        allhabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        // make habit clickable
        habitAdapter = new AllHabitList_Adapter(this, habitDataList, new AllHabitList_Adapter.ItemClickListener() {
            /**
             * A custom onItemClick Listener function to handle when habits are clicked.
             *  Opens a ViewHabit intent
             * @param clickedHabit
             *  The habit that was clicked to be passed to new intent
             */
            @Override
            /**
             * function to open ViewHabit when Habits clicked
             */
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                habitFollowCalculator c = new habitFollowCalculator();
                int a = c.calculateCloseness(habitDataList.get(i), mAuth.getUid());

            public void onItemClick(Habit clickedHabit) {
                Intent intent = new Intent (AllHabitListView.this, ViewHabit.class);
                intent.putExtra("habit",clickedHabit);
                startActivity(intent);
            }
        });
        allhabitListView.setLayoutManager(new LinearLayoutManager(this));
        allhabitListView.setAdapter(habitAdapter);

        // make habit movable
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            /**
             * Position updater for reordering habits
             * @param recyclerView
             *  the view in which reordering occurs
             * @param viewHolder
             *  the location to move from
             * @param target
             *  the location to move to
             * @return
             *  boolean false?
             */
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();

                // update position field in habit
                habitDataList.get(fromPosition).setPosition(toPosition);
                habitDataList.get(toPosition).setPosition(fromPosition);

                // update position inside data list
                Collections.swap(habitDataList, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            /**
             * Function called at end of Drag and Drop
             * @param recyclerView
             *  recycler view to drop in
             * @param viewHolder
             *  dragged view
             */
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                habitAdapter.editPosition(db, currentUserID, habitDataList.get(fromPosition), habitDataList.get(toPosition));
            }

            // used for swipe gestures
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(allhabitListView);

        final Button button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when home clicked
             * @param v
             *  current view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllHabitListView.this, EmptyMainMenu.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        button_calendar.setOnClickListener(new View.OnClickListener() {
            /**
             * function called to open calendar when clicked
             * @param v
             *  current view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllHabitListView.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        final Button button_allHabitList = findViewById(R.id.allhabitlist_button);
        button_allHabitList.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when all habits list button clicked
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllHabitListView.this,AllHabitListView.class);
                startActivity(intent);
            }
        });


        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            /**
             * function called to add new habit
             * @param view
             *  current view
             */
            @Override
            public void onClick(View view) {
                Intent toAddHabit = new Intent(AllHabitListView.this, AddHabit.class);
                startActivity(toAddHabit);
            }
        });
    }

    /**
     * Insert all habits from firebase into a list
     * @param ID
     * The userID of the user to store data under
     */
    public void setUserHabit(String ID){
        final CollectionReference habitRef = db.collection("Users").document(ID).collection("Habits");
        habitRef
                // get the habits in the correct order
                .orderBy("position")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //clears data so list can be updated, then gets all habit data from firebase to display
                        WriteBatch batch = db.batch();

                        habitDataList.clear();
                        int updated_position = 0;
                        for(QueryDocumentSnapshot doc:value){
                            Habit result = doc.toObject(Habit.class);
                            result.setPosition(updated_position);

                            DocumentReference docRef = db.collection("Users").document(ID)
                                    .collection("Habits").document(result.getHabitName());
                            batch.update(docRef, "position", updated_position);

                            habitDataList.add(result);
                            updated_position = updated_position + 1;
                        }
                        batch.commit();
                        habitAdapter.notifyDataSetChanged();
                    }
                });
    }
}