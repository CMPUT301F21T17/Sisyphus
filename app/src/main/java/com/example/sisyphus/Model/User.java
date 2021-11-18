/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

import java.util.Date;

/**
 * Individual User class
 */
public class User {
    private String nameFirst;
    private String nameLast;
    private Date dateJoined;
    private int habitCount;

    public User(){
        //Default constructor
    }

    /**
     * Constructor for User
     * @param nameFirst
     *  first name of user
     * @param nameLast
     *  last name of user
     * @param dateJoined
     *  date user joined
     */
    public User(String nameFirst, String nameLast, Date dateJoined) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.dateJoined = dateJoined;
    }

    /**
     * function to return first name
     * @return
     *  first name of user
     */
    public String getFirst(){
        return nameFirst;
    }

    /**
     * function to return last name
     * @return
     *  last name of user
     */
    public String getLast(){
        return nameLast;
    }

    /**
     * function to return date joined
     * @return
     *  date joined of user
     */
    public Date getDateJoined(){
        return dateJoined;
    }

    /**
     * function to set first name
     * @param nameFirst
     *  first name of user to set
     */
    public void setFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    /**
     * function to set last name
     * @param nameLast
     *  last name of user to set
     */
    public void setLast(String nameLast) {
        this.nameLast = nameLast;
    }

    /**
     * function to set date joined
     * @param dateJoined
     *  date joined of user to set
     */
    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

}