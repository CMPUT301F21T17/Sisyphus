package com.example.sisyphus.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sisyphus.Model.AllHabitList_Adapter;
import com.example.sisyphus.Model.Habit;
import com.example.sisyphus.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
    ListView dailyHabitsView;
    ArrayList<Habit> data;
    ArrayAdapter<Habit> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_habit_list_view);
        dailyHabitsView = findViewById(R.id.dailyHabits);
        data = new ArrayList<>();
        adapter = new AllHabitList_Adapter(this, data);
        dailyHabitsView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent userInfo = getIntent();
        String userName = userInfo.getStringExtra("1");

        //getting habit info from database
        CollectionReference getref = db.collection("Users")
                .document(userName)
                .collection("Habits");
                getref.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            }
                        }

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
    }

    //Getting Current Day name
    public String getDayName(){
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dayName = new SimpleDateFormat("EEEE").format(date);
        return dayName;
    }
}