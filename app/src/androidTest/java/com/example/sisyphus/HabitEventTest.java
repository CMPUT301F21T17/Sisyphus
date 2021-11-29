package com.example.sisyphus;
import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sisyphus.View.AllHabitListView;
import com.example.sisyphus.View.Entry;
import com.example.sisyphus.View.ListHabitEvent;
import com.example.sisyphus.View.MainActivity;
import com.example.sisyphus.View.Register;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;


/**
 * Test class for AllHabitListView,AddHabit and HabitController. All the UI tests are written here. Robotium test framework is
 used
 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 !!!!!Please follow the order of testing !!!!!!Or it causes error!!!!!!
 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */

public class HabitEventTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Test1
     * Try to add habit from the habit list view
     */
    @Test
    public void addHabitEvent(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        //assertTrue(solo.waitForText("My habits",1,2000));
        solo.sleep(500);
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.allhabit_list);
        solo.sleep(500);
        View item = habitList.getChildAt(0);
        solo.clickOnView(item);
        //solo.waitForActivity("EditHabitEventView");
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.addHabitEventButton));
        solo.enterText((EditText) solo.getView(R.id.editTextDate),"28/11/2021");
        solo.clickOnButton("OK");
        solo.clickOnView(solo.getView(R.id.editTextLocation));
        solo.clickOnView(solo.getView(R.id.editTextLocation));
        solo.sleep(5000);
        solo.clickOnView(solo.getView(R.id.confirm_button));
        solo.enterText((EditText) solo.getView(R.id.editTextComment),"test");
        solo.clickOnView(solo.getView(R.id.buttonAdd));
        solo.sleep(500);
    }


    /**
     * Test2
     * Try to edit habit from the habit list view
     */
    @Test
    public void editHabitEvent(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        //assertTrue(solo.waitForText("My habits",1,2000));
        solo.sleep(500);
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.allhabit_list);
        solo.sleep(500);
        View item = habitList.getChildAt(0);
        solo.clickOnView(item);
        //solo.waitForActivity("EditHabitEventView");
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.viewHabitEventButton));
        solo.sleep(500);
        RecyclerView eventList = (RecyclerView) solo.getView(R.id.allhabit_list);
        solo.sleep(500);
        View event = eventList.getChildAt(0);
        solo.clickOnView(item);
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.search));
        solo.clickOnMenuItem("Edit Event");
        solo.clearEditText((EditText) solo.getView(R.id.editTextComment));
        solo.enterText((EditText) solo.getView(R.id.editTextComment), "edit");
        solo.clickOnView(solo.getView(R.id.buttonAdd));
        assertTrue(solo.waitForText("edit",1,2000));
    }

    /**
     * Test3
     * Try to delete a habit event from the habit list view
     */
    @Test
    public void deleteHabitEvent(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        //assertTrue(solo.waitForText("My habits",1,2000));
        solo.sleep(500);
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.allhabit_list);
        solo.sleep(500);
        View item = habitList.getChildAt(0);
        solo.clickOnView(item);
        //solo.waitForActivity("EditHabitEventView");
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.viewHabitEventButton));
        solo.sleep(500);
        RecyclerView eventList = (RecyclerView) solo.getView(R.id.allhabit_list);
        solo.sleep(500);
        View event = eventList.getChildAt(0);
        solo.clickOnView(event);
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.search));
        solo.clickOnMenuItem("Delete Event");
        solo.clickOnButton("Yes");
        solo.sleep(500);
        assertFalse(solo.searchText("SUN"));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
