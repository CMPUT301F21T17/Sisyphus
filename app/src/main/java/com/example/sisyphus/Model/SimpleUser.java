/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

/**
 * Class that exists to store information from user search in format conducive
 * to display via custom adapter. ID is not typically stored as part of user, but
 * is necessary to differentiate users in the search function.
 * Does not require all the fields and methods of user
 */
public class SimpleUser{
    private String nameFirst;
    private String nameLast;
    private String id;

    /**
     * Default constructor
     */
    public SimpleUser(){

    }

    /**
     * Parametrized constructor
     * @param first
     * the first name of the user
     * @param last
     * the last name of the user
     * @param id
     * the ID of the user
     */
    public SimpleUser(String first, String last, String id){
        this.nameFirst = first;
        this.nameLast = last;
        this.id = id;
    }

    /**
     * Getter for first name
     * @return
     * the name of the user
     */
    public String getNameFirst() {
        return nameFirst;
    }

    /**
     * Setter for name
     * @param nameFirst
     * the new first name of the user (only affects this object, not the actual user info in firebase)
     */
    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    /**
     * Getter for last name
     * @return
     * the current last name of the user
     */
    public String getNameLast() {
        return nameLast;
    }

    /**
     * Setter for name
     * @param nameLast
     * the new last name for the user (only affects this object, not the actual user info in firebase)
     */
    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    /**
     * Getter for ID
     * @return
     * the ID of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for ID
     * @param id
     * The new ID for the user (only affects this object, not the actual user info in firebase)
     */
    public void setId(String id) {
        this.id = id;
    }
}
