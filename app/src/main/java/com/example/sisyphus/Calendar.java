package com.example.sisyphus;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Calendar extends AppCompatActivity {
    CollectionReference calendarRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CalendarView calendar;
    ListView calendar_events;
    ArrayList<HabitEvent> data;
    ArrayAdapter<HabitEvent> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_page);
        calendar = findViewById(R.id.calendar);
        calendar_events = findViewById(R.id.calendar_events);

        // TODO get collection name for habit events
        calendarRef = db.collection("");
        // TODO get HabitEvent adapter
        adapter  = new CalendarEventAdapter(this, data);
        data = new ArrayList<HabitEvent>();
        calendar_events.setAdapter(adapter);

        // click listener for changes to calendar widget
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // get events from database for day year month day and populate calendar_events
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                String date_string = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
                // compare date_string to date field of calendar ref to get the correct group
                calendarRef.whereArrayContains("date", date_string)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                data.clear();
                                for (QueryDocumentSnapshot d: queryDocumentSnapshots) {
                                    // TODO adapt data to HabitEvent list
                                    d.getData();
                                    HabitEvent habitEvent = HabitEvent();
                                    data.add(habitEvent);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        });

        // event listener to get up to date data for HabitEvent list
        calendarRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                data.clear();
                for (QueryDocumentSnapshot d: value) {
                    // TODO adapt data to HabitEvent list
                    data.add(d.getData());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
