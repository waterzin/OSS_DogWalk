package com.example.oss;

public class Review {
    private String id;
    private String userId;
    private String content;
    private double rating;
    private String userName;
    public Review() {} // Firestore용 기본 생성자

    public String getUserId() { return userId; }

    public void setId(String id) {this.id = id;}

    public String getId() {return id;}
    public String getUserName() { return userName; }
    public String getContent() { return content; }
    public double getRating() { return rating; }
}

