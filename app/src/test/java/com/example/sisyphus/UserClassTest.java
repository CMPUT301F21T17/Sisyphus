package com.example.sisyphus;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.sisyphus.Model.User;

import java.util.Date;

/**
 * A test class to test the "User" class.
 */
public class UserClassTest {

    /**
     * A method that returns a mock user object for use in testing.-
     * @return
     *      A user with first name "Bob", last name "Jones", and DateJoined set as the current system
     *      date.
     */
    private User mockUser(){
        User user = new User();
        user.setfirst("Bob");
        user.setlast("Jones");
        user.setDateJoined(new Date());
        return user;
    }

    /**
     * Tests whether getFirst correctly fetches the first name of a user.
     */
    @Test
    public void testGetFirst(){
        User user = mockUser();
        assertEquals(user.getFirst(), "Bob");
    }

    /**
     * Tests whether setFirst correctly changes the first name of a user.
     */
    @Test
    public void testSetFirst(){
        User user = mockUser();
        String newFirst = "Bill";
        user.setfirst(newFirst);
        assertEquals(user.getFirst(), newFirst);
    }

    /**
     * Tests whether getLast correctly fetches the last name of a user.
     */
    @Test
    public void testGetLast(){
        User user = mockUser();
        assertEquals(user.getLast(), "Jones");
    }

    /**
     * Tests whether setLast correctly changes the last name of a user.
     */
    @Test
    public void testSetLast(){
        User user = mockUser();
        String newLast = "James";
        user.setlast(newLast);
        assertEquals(user.getLast(), newLast);
    }

    /**
     * Tests whether getDateJoined correctly fetches the date when the user joined.
     */
    @Test
    public void testGetDateJoined(){
        User user = mockUser();
        Date currentDate = new Date();
        assertEquals(user.getDateJoined().toString(), currentDate.toString());
    }

    /**
     * Tests whether setDateJoined correctly changes the stored date when the user joined.
     */
    @Test
    public void testSetDateJoined(){
        User user = mockUser();
        Date newDate = new Date(21,3,21);
        user.setDateJoined(newDate);
        assertEquals(user.getDateJoined().toString(), newDate.toString());
    }
}
