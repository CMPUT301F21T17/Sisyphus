/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.sisyphus.View.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;


/**
 * Test class for FollowRequestListView and UserSearch. All the UI tests are written here. Robotium test framework is
 used
 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 !!!!!Please follow the order of testing !!!!!!Or it causes error!!!!!!
 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class UserFollowTest {
    private Solo solo;
    Random rand = new Random();
    Integer randomIn = rand.nextInt(100000);
    private String randomEmail;

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
     * Try to add a habit from the habit list view
     */
    @Test
    public void sendRequest(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign Up");
        randomEmail = randomIn.toString() + "@email.com";
        solo.enterText((EditText) solo.getView(R.id.registerFirstName),"User");
        solo.enterText((EditText) solo.getView(R.id.registerLastName),"FollowTest");
        solo.enterText((EditText) solo.getView(R.id.registerEmail),randomEmail);
        solo.enterText((EditText) solo.getView(R.id.registerPassword),"123456");
        solo.enterText((EditText) solo.getView(R.id.registerPasswordConfirm),"123456");
        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForText("Daily",1,2000));
        solo.clickOnView(solo.getView(R.id.social_button));
        solo.clickOnView(solo.getView(R.id.search));
        solo.enterText((EditText) solo.getView(R.id.editTextTextFirstName),"intent");
        solo.enterText((EditText) solo.getView(R.id.editTextTextLastName),"test");
        solo.clickOnView(solo.getView(R.id.search_user));
        assertTrue(solo.waitForText("intent test",1,2000));
        solo.clickOnButton("Follow");
    }

    /**
     * Test2
     * Try to add a habit from the habit list view
     */
    @Test
    public void receiveRequest(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        assertTrue(solo.waitForText("Daily",1,2000));
        solo.clickOnView(solo.getView(R.id.dropDown));
        solo.clickOnMenuItem("Follow Requests");
        assertTrue(solo.waitForText("User FollowTest",1,2000));
        solo.clickOnButton("Confirm");
    }

    /**
     * Test3
     * Try to add a habit from the habit list view
     */
    @Test
    public void displayFollower(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("Sign In");
        solo.enterText((EditText) solo.getView(R.id.signInEmail),"intenttest@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.signInPassword),"123456");
        solo.clickOnButton("Sign In");
        solo.clickOnView(solo.getView(R.id.social_button));
        assertTrue(solo.waitForText("followtest 2",1,2000));
    }



    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
