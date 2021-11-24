package com.example.fitnessapp;

public class Workout {
    public String exercise;
    public String weight;
    public String date;

    public Workout() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Workout(String exercise, String weight, String date) {
        this.exercise = exercise;
        this.weight = weight;
        this.date = date;
    }
}
