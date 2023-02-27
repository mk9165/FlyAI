package com.example.ttel;

import java.io.Serializable;

public class User implements Serializable {
    String name;
    String duration;
    String datetime;

    public User(String name, String duration, String datetime) {
        this.name = name;
        this.duration = duration;
        this.datetime = datetime;
    }
}