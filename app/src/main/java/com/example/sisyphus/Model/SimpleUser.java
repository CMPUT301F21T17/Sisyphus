/*
 * Copyright (c) 2021.
 * Sisyphus, CMPUT 301
 * All Rights Reserved.
 */

package com.example.sisyphus.Model;

/**
 * Class that exists to store information from user search in format conducive
 * to display via custom adapter
 */
public class SimpleUser{
    private String nameFirst;
    private String nameLast;
    private String id;

    public SimpleUser(){

    }

    public SimpleUser(String first, String last, String id){
        this.nameFirst = first;
        this.nameLast = last;
        this.id = id;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
