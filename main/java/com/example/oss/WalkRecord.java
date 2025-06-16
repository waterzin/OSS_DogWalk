package com.example.oss;

import java.io.Serializable;

public class WalkRecord implements Serializable {
    private String date;
    private String duration;
    private double distance;
    private int steps;

    private String memo;
    public WalkRecord(){

    }
    public WalkRecord(String date, String duration, double distance, int steps) {
        this.date = date;
        this.duration = duration;
        this.distance = distance;
        this.steps = steps;
    }

    public String getMemo() {return memo;}
    public String getDate() { return date; }
    public String getDuration() { return duration; }
    public double getDistance() { return distance; }
    public int getSteps() { return steps; }
}
