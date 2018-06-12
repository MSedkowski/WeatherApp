package com.example.mateusz.weatherapp.activities;

import com.example.mateusz.weatherapp.services.WeatherServiceCallback;
import com.example.mateusz.weatherapp.weatherData.Channel;
import com.example.mateusz.weatherapp.weatherData.Condition;
import com.example.mateusz.weatherapp.weatherData.Units;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements WeatherServiceCallback {

    private String localization;
    private String currentDate;
    private String currentWeather;
    private String temp;
    private String pressure;
    private String humidity;
    private String wind;
    private String tempUnits;
    private String windUnits;

    public static Units units;
    public static int code;

    public static List<Condition> weekWeather = new ArrayList<>();

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTempUnits() {
        return tempUnits;
    }

    public void setTempUnits(String tempUnits) {
        this.tempUnits = tempUnits;
    }

    public String getWindUnits() {
        return windUnits;
    }

    public void setWindUnits(String windUnits) {
        this.windUnits = windUnits;
    }

    public static List<Condition> getWeekWeather() {
        return weekWeather;
    }

    public static void setWeekWeather(List<Condition> weekWeather) {
        WeatherData.weekWeather = weekWeather;
    }

    public void updateWeather(String cityName, char tempSign){
        //TODO
    }

    public void updateWeather(double longitiude, double latitiude, char tempSign) {
        //TODO
    }



    @Override
    public void serviceSuccess(Channel channel) {
        //TODO
    }

    @Override
    public void serviceFailure(Exception exception) {
        //TODO
    }
}
