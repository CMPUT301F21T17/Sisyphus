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
    private FirebaseAuth mauth;
    private Calendar selectedDay;

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
        adapter = new AllHabitList_Adapter(this, data);
        habitsView.setAdapter(adapter);

        // event listener to get up to date data for Habit list
        mauth = FirebaseAuth.getInstance();
        mauth.signInWithEmailAndPassword("ktbrown@ualberta.ca", "123456");
        // click listener for changes to calendar widget
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // get events from database for day year month day and populate habitsView
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                selectedDay.set(year, month - 1, day, 0, 0, 0);
                // compare date_string to date field of calendar ref to get the correct group
                update(mauth.getUid(), selectedDay);
            }
        });
        update(mauth.getUid(), selectedDay);
    }

    /**
     * Function to update the HabitList with the Habits starting after and repeating on selectedDay
     * @param userID
     *  The String userID returned buy mAuth
     * @param selectedDay
     *  Calendar Object to compare to start date and days repeated
     */
    private void update(String userID, Calendar selectedDay) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(userID)
                .collection("Habits")
                .whereGreaterThanOrEqualTo("date", selectedDay.getTime())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        data.clear();
                        if (value != null) {
                            for (QueryDocumentSnapshot d: value) {
                                // TODO adapt data to Habit list
                                Habit temp = d.toObject(Habit.class);
                                boolean add = false;
                                for (String e : temp.getDaysRepeated()){
                                    if (e.equals(CalToStr(selectedDay))){
                                        add = true;
                                        break;
                                    }
                                }
                                if (add) {
                                    data.add(temp);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Function that converts a Calendar Object to a string representation of the current day
     * @param selectedDay
     *  A calendar object
     * @return
     *  A string representation of the current day
     */
    private String CalToStr (Calendar selectedDay) {
            int i = selectedDay.get(Calendar.DAY_OF_WEEK);
            switch (i) {
                case Calendar.FRIDAY:
                    return "MONDAY";
                case Calendar.SATURDAY:
                    return "TUESDAY";
                case Calendar.SUNDAY:
                    return "WEDNESDAY";
                case Calendar.WEDNESDAY:
                    return "SATURDAY";
                case Calendar.MONDAY:
                    return "THURSDAY";
                case Calendar.TUESDAY:
                    return "FRIDAY";
                case Calendar.THURSDAY:
                    return "SUNDAY";
            }
        return "ERROR";
    }
}
