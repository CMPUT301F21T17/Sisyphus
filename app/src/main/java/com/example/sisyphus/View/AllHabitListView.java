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

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.User;
import com.example.sisyphus.Model.habitFollowCalculator;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private ArrayList<String> percents;

    Button dropDown;


    //initializing firebase authentication (session) object and establishing database connection
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    final CollectionReference collectionReference = db.collection("Users");
    String TAG = "firebase habitEvent load";

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
        allhabitListView = findViewById(R.id.allhabit_list);
        habitDataList = new ArrayList<>();

        percents = new ArrayList<>();

        setUserHabit(currentUserID);


        // make habit clickable
        habitAdapter = new AllHabitList_Adapter(this, habitDataList, percents, new AllHabitList_Adapter.ItemClickListener() {
            /**
             * A custom onItemClick Listener function to handle when habits are clicked.
             * Opens a ViewHabit intent
             *
             * @param clickedHabit The habit that was clicked to be passed to new intent
             */
            @Override
            public void onItemClick(Habit clickedHabit) {
                Intent intent = new Intent(AllHabitListView.this, ViewHabit.class);
                intent.putExtra("habit", clickedHabit);
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
             *
             * @param recyclerView the view in which reordering occurs
             * @param viewHolder   the location to move from
             * @param target       the location to move to
             * @return boolean false?
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
                Collections.swap(percents, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            /**
             * Function called at end of Drag and Drop
             *
             * @param recyclerView recycler view to drop in
             * @param viewHolder   dragged view
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
             *
             * @param v current view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllHabitListView.this, DailyHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_calendar = findViewById(R.id.calendar_button);
        button_calendar.setOnClickListener(new View.OnClickListener() {
            /**
             * function called to open calendar when clicked
             *
             * @param v current view
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
             *
             * @param view current view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllHabitListView.this, AllHabitListView.class);
                startActivity(intent);
            }
        });

        final Button button_social = findViewById(R.id.social_button);
        button_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllHabitListView.this, SocialView.class);
                startActivity(intent);
            }
        });


        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            /**
             * function called to add new habit
             *
             * @param view current view
             */
            @Override
            public void onClick(View view) {
                Intent toAddHabit = new Intent(AllHabitListView.this, AddHabit.class);
                startActivity(toAddHabit);
            }
        });

        // Dropdown Menu Button
        dropDown = (Button) findViewById(R.id.dropDown);
        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                showPopup(v);
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
                            percents.add("0");
                            updated_position = updated_position + 1;

                        }
                        batch.commit();
                        habitAdapter.notifyDataSetChanged();
                        setHabitCompletion();
                    }
                });
    }

    //method that polls each habit in the list and gets the completion result
    public void setHabitCompletion(){
        for(int i = 0; i < habitDataList.size(); i++){
            int finalI = i;
            collectionReference.document(mAuth.getUid()).collection("Habits").document(habitDataList.get(i).getHabitName()).collection("HabitEvent")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            final int currentIndex = finalI;
                            if (task.isSuccessful()) {
                                int counter = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    counter += 1;

                                }

                                System.out.println("Counted habit events: " + counter);
                                habitFollowCalculator calc = new habitFollowCalculator();
                                int totalDays = calc.calculateCloseness(habitDataList.get(currentIndex));

                                System.out.println(counter/totalDays);
                                System.out.println((100* counter/totalDays));


                                int percentClose = (int) Math.floor((100*counter/totalDays));

                                //should never happen, but sets completion % to 100 just
                                //in case value exceeds days of occurrence
                                if(percentClose > 100){
                                    percentClose = 100;
                                }

                                System.out.println("Calculated total = " + totalDays);
                                percents.set(currentIndex, String.valueOf(percentClose));

                                System.out.println("Actual val to set to: " + percentClose);
                                System.out.println("ArrayList Val at index: " + percents.get(currentIndex));

                                System.out.println("ran this");
                                habitAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }
    // Dropdown Menu Methods
    public void showPopup(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.Theme_App);
        PopupMenu popup = new PopupMenu(wrapper, v, Gravity.LEFT, R.style.Theme_App, 0);
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.dropdown, popup.getMenu());

        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dropdown, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the main activity layout object.
        // Get clicked menu item id.
        int itemId = item.getItemId();
        if(itemId == R.id.followRequests)
        {
            Intent intent = new Intent(this, FollowRequestListView.class);
            startActivity(intent);

        }else if(itemId == R.id.settings)
        {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);

        }else if(itemId == R.id.logout)
        {
            // implement logout
        }
        return true;
    }
}