package com.example.sisyphus.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.Model.habitFollowCalculator;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyHabitListView extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView dailyHabitsView;
    ArrayList<Habit> data;
    private ArrayList<String> percents;
    private AllHabitList_Adapter adapter;
    String TAG = "Adding percent completions";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_habit_list_view);
        dailyHabitsView = findViewById(R.id.dailyHabits);



        data = new ArrayList<>();
        percents = new ArrayList<>();

        // make habit clickable
        adapter = new AllHabitList_Adapter(this, data, percents, new AllHabitList_Adapter.ItemClickListener() {
            /**
             * A custom onItemClick Listener function to handle when habits are clicked.
             * Opens a ViewHabit intent
             *
             * @param clickedHabit The habit that was clicked to be passed to new intent
             */
            @Override
            public void onItemClick(Habit clickedHabit) {
                Intent intent = new Intent(DailyHabitListView.this, ViewHabit.class);
                intent.putExtra("habit", clickedHabit);
                startActivity(intent);
            }

        });



        dailyHabitsView.setLayoutManager(new LinearLayoutManager(this));
        dailyHabitsView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent userInfo = getIntent();
        String userName = userInfo.getStringExtra("1");

        //getting habit info from database
        CollectionReference getRef = db.collection("Users")
                .document(mAuth.getUid())
                .collection("Habits");
                getRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        data.clear();
                        for(QueryDocumentSnapshot doc:value) {
                            Habit result = doc.toObject(Habit.class);
                            boolean add = false;
                            //Comparing the frequency from database to current day
                            for (String resultsFreq : result.getFrequency()) {
                                if (resultsFreq.equals(getDayName().toUpperCase())) {
                                    add = true;
                                    break;
                                }
                            }
                            if (add) {
                                data.add(result);
                                percents.add("0");
                            }
                        }
                        setHabitCompletion();
                        adapter.notifyDataSetChanged();
                    }
                });





        //Add Button Intent
        final FloatingActionButton addHabitButton = findViewById(R.id.addHabitButton);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddHabit = new Intent(DailyHabitListView.this, AddHabit.class);
                startActivity(toAddHabit);
            }
        });



        final Button button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            /**
             * function called when home clicked
             *
             * @param v current view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyHabitListView.this, EmptyMainMenu.class);
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
                Intent intent = new Intent(DailyHabitListView.this, CalendarActivity.class);
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
                Intent intent = new Intent(DailyHabitListView.this, AllHabitListView.class);
                startActivity(intent);
            }
        });


    }

    //Getting Current Day name
    public String getDayName(){
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dayName = new SimpleDateFormat("EEEE").format(date);
        return dayName;
    }



    /**
     * Method that takes the list of habits that are to be displayed, and updates the array showing how
     * well they have been completed for each habit so that the progress bar can be filled accordingly.
     * Ideally would have been part of an object, but the asynchronous nature of firebase means returning values
     * to the main thread would have been too challenging
     */
    public void setHabitCompletion(){
        //initializing firebase connection object
        CollectionReference collectionReference = db.collection("Users");

        //for habit that is to be displayed, get all it's habit events and compare to how many it should have
        for(int i = 0; i < data.size(); i++){
            int finalI = i;

            //getting all the habitEvents of the current habit in the list
            collectionReference.document(mAuth.getUid()).collection("Habits").document(data.get(i).getHabitName()).collection("HabitEvent")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            //setting the value so it can be used asynchronously
                            final int currentIndex = finalI;

                            //for each habit event, iterate the counter.  Content unimportant, only
                            //number of events is relevant to completion %
                            if (task.isSuccessful()) {
                                int counter = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    counter += 1;

                                }

                                //creating object to determine the number of days events should have occurred on
                                habitFollowCalculator calc = new habitFollowCalculator();

                                //getting valid event days (guaranteed non-0)
                                int totalDays = calc.calculateCloseness(data.get(currentIndex));


                                //comparing # of events to # expected and formatting to %
                                int percentClose = (int) Math.floor((100*counter/totalDays));

                                //should never happen, but sets completion % to 100 just
                                //in case value exceeds days of occurrence
                                if(percentClose > 100){
                                    percentClose = 100;
                                }

                                //storing value in correct location in array
                                percents.set(currentIndex, String.valueOf(percentClose));

                                //updating UI
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }
}