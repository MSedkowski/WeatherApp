package com.example.mateusz.weatherapp.db;

public class LocationModel {
    private long id;
    private double longitude;
    private double latitude;
    private String cityName;

    public LocationModel(long id, double longitude, double latitude, String cityName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.cityName = cityName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
