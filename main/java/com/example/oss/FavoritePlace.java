package com.example.oss;

public class FavoritePlace {
    private String placeName;
    private String placeAddress;

    public FavoritePlace() {} // Firestore에 필요

    public FavoritePlace(String placeName, String address) {
        this.placeName = placeName;
        this.placeAddress = address;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddress() {
        return placeAddress;
    }

    public void setAddress(String address) {
        this.placeAddress = address;
    }
}
