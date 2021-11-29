/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus;

import static org.junit.Assert.assertEquals;

import com.example.sisyphus.Model.SimpleUser;
import com.example.sisyphus.Model.User;

import org.junit.Test;

import java.util.Date;

/**
 * A test class to test the "SimpleUser" class.
 */
public class SimpleUserTest {

    /**
     * A method that returns a mock user object for use in testing.-
     * @return
     *      A user with first name "Bob", last name "Jones", and DateJoined set as the current system
     *      date.
     */

    private SimpleUser mockSimpleUser(){
        SimpleUser simple = new SimpleUser("John", "Smith", "testID");
        return simple;
    }

    /**
     * Tests whether getFirst correctly fetches the first name of a user.
     */
    @Test
    public void testGetFirst(){
        SimpleUser user = mockSimpleUser();
        assertEquals(user.getNameFirst(), "John");
    }

    /**
     * Tests whether setFirst correctly changes the first name of a user.
     */
    @Test
    public void testSetFirst(){
        SimpleUser user = mockSimpleUser();
        String newFirst = "Bill";
        user.setNameFirst(newFirst);
        assertEquals(user.getNameFirst(), newFirst);
    }

    /**
     * Tests whether getLast correctly fetches the last name of a user.
     */
    @Test
    public void testGetLast(){
        SimpleUser user = mockSimpleUser();
        assertEquals(user.getNameLast(), "Smith");
    }

    /**
     * Tests whether setLast correctly changes the last name of a user.
     */
    @Test
    public void testSetLast(){
        SimpleUser user = mockSimpleUser();
        String newLast = "James";
        user.setNameLast(newLast);
        assertEquals(user.getNameLast(), newLast);
    }


    /**
     * Tests whether getID correctly fetches the ID of a user.
     */
    @Test
    public void testGetID(){
        SimpleUser user = mockSimpleUser();
        assertEquals(user.getId(), "testID");
    }

    /**
     * Tests whether setID correctly changes the ID of a user.
     */
    @Test
    public void testSetID(){
        SimpleUser user = mockSimpleUser();
        String newID = "newID";
        user.setId(newID);
        assertEquals(user.getId(), newID);
    }


}
