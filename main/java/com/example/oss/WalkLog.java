package com.example.oss;

public class WalkLog {
    private String date;
    private String duration;
    private int steps;
    private double distance;

    public WalkLog() {}
    public void setDate(String date) { this.date = date; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setSteps(int steps) { this.steps = steps; }
    public void setDistance(double distance) { this.distance = distance; }


    public String getDate() { return date; }
    public String getDuration() { return duration; }
    public int getSteps() { return steps; }
    public double getDistance() { return distance; }
}
