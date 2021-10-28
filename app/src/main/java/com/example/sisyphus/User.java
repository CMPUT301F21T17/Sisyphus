package com.example.sisyphus;

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
}
