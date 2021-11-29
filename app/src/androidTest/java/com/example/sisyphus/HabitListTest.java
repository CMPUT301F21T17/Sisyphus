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
import java.util.Random;

/**
 * Test class for AllHabitListView,AddHabit and HabitController. All the UI tests are written here. Robotium test framework is
 used
 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 !!!!!Please follow the order of testing !!!!!!Or it causes error!!!!!!
 !!!!!Please finish all three tests before jump to other Test file!!!!!
 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class HabitListTest {
    private Solo solo;
    Random rand = new Random();
    Integer randomIn = rand.nextInt(100000);
    private String randomString = randomIn.toString();

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
     * Try to add a habit from the habit list view
     */
    @Test
    public void addHabit(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.allhabitlist_button));
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        assertTrue(solo.waitForText("Add Habit",1,2000));
        solo.enterText((EditText) solo.getView(R.id.habitNameInput), randomString);
        solo.sleep(500);
        solo.enterText((EditText) solo.getView(R.id.startDate), "01/11/2021");
        solo.clickOnButton("OK");
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.frequency));
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.frequency));
        //solo.enterText((EditText) solo.getView(R.id.frequency), "SUNDAY,MONDAY");
        //int[] butPosition = new int[2];
        solo.sleep(500);
        solo.clickOnButton("OK");
        solo.enterText((EditText) solo.getView(R.id.reasonInput), "addHabitTest");
        solo.clickOnView(solo.getView(R.id.checkButton));
        assertTrue(solo.waitForText(randomString,1,2000));
    }

    /**
     * Try to edit a habit from the habit list view
     */
    @Test
    public void editHabit(){
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
        solo.clickOnView(solo.getView(R.id.search));
        solo.clickOnMenuItem("Edit Habit");
        solo.clearEditText((EditText) solo.getView(R.id.reasonContainer));
        solo.enterText((EditText) solo.getView(R.id.reasonContainer), "edit");
        solo.clickOnView(solo.getView(R.id.confirm));
        assertTrue(solo.waitForText("edit",1,2000));
    }

    /**
     * Try to delete a habit from the habit list view
     */
    @Test
    public void deleteHabit(){
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
        solo.clickOnView(solo.getView(R.id.search));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnButton("Yes");
        assertFalse(solo.searchText(randomString));
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
