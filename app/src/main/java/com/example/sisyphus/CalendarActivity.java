package com.example.sisyphus;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Calendar selectedDay;

    DocumentReference calendarRef;
    CalendarView calendar;
    ListView habitsView;
    ArrayList<Habit> data;
    ArrayAdapter<Habit> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_page);

        calendar = findViewById(R.id.calendar);
        habitsView = findViewById(R.id.calendar_events);

        selectedDay = new GregorianCalendar();
        data = new ArrayList<Habit>();
        habitsView.setAdapter(adapter);


        // click listener for changes to calendar widget
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // get events from database for day year month day and populate habitsView
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                selectedDay.set(year, month - 1, day);
                // compare date_string to date field of calendar ref to get the correct group

            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = (FirebaseAuth) this.getIntent().getExtras().get("MAUTH");
        String userID = mAuth.getUid();

        // event listener to get up to date data for Habit list
        db.collection("Users")
                .document(userID)
                .collection("Habits")
                .whereGreaterThanOrEqualTo("dateStarted", selectedDay)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        data.clear();
                        for (QueryDocumentSnapshot d: value) {
                            // TODO adapt data to Habit list
                            d.getData();
                            data.add();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
