package com.example.sisyphus.Model;

import java.util.Date;

public class User {

    private String nameFirst;
    private String nameLast;
    private Date dateJoined;

    public User(){
        //Default constructor
    }

    public User(String nameFirst, String nameLast, Date dateJoined) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.dateJoined = dateJoined;
    }

    public String getFirst(){
        return nameFirst;
    }

    public String getLast(){
        return nameLast;
    }

    public Date getDateJoined(){
        return dateJoined;
    }

    public void setfirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public void setlast(String nameLast) {
        this.nameLast = nameLast;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
}