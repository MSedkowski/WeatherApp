package com.example.mateusz.weatherapp.activities;

import com.example.mateusz.weatherapp.services.YahooWeatherService;

import java.io.Serializable;

public class WeatherDataParams implements Serializable{

    private double longitude;
    private double latitude;
    private boolean isGPSLocationEnable = true;
    private int refreshingTime = 15;
    private String locationString;
    private String cityName;
    private boolean tempSignIsC = true;

    private YahooWeatherService service;

    public YahooWeatherService getService() {
        return service;
    }

    public void setService(YahooWeatherService service) {
        this.service = service;
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

    public boolean isGPSLocationEnable() {
        return isGPSLocationEnable;
    }

    public void setGPSLocationEnable(boolean GPSLocationEnable) {
        isGPSLocationEnable = GPSLocationEnable;
    }

    public int getRefreshingTime() {
        return refreshingTime;
    }

    public void setRefreshingTime(int refreshingTime) {
        this.refreshingTime = refreshingTime;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isTempSignIsC() {
        return tempSignIsC;
    }

    public void setTempSignIsC(boolean tempSignIsC) {
        this.tempSignIsC = tempSignIsC;
    }


}
