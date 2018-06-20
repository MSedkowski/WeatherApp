package com.example.mateusz.weatherapp.activities;

import com.example.mateusz.weatherapp.weatherData.Channel;
import com.example.mateusz.weatherapp.weatherData.Condition;
import com.example.mateusz.weatherapp.weatherData.Units;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherData implements Serializable {

    private String cityName;
    private String localization;
    private String currentDate;
    private int currentWeather;
    private String temp;
    private String pressure;
    private String humidity;
    private String wind;
    private String tempUnits;
    private String windUnits;

    private int code;
    private List<Condition> weekWeather = new ArrayList<>();
    private Units units;

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

    public int getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(int currentWeather) {
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Condition> getWeekWeather() {
        return weekWeather;
    }

    public void setWeekWeather(List<Condition> weekWeather) {
        this.weekWeather = weekWeather;
    }

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public void update(Channel channel) {
        this.cityName = channel.getLocation().getCity();
        this.code = channel.getItem().getCondition().getCode();
        this.temp = channel.getItem().getCondition().getTemperature() + " \u00B0" + channel.getUnits().getTemperature();
        this.pressure = String.format(Locale.ENGLISH, "%.0f", channel.getAtmosphere().getPressure()) + " hPa";
        this.humidity = channel.getAtmosphere().getHumidity() + "%";
        this.wind = channel.getWind().getSpeed();
        this.tempUnits = channel.getUnits().getTemperature();
        this.windUnits = channel.getUnits().getSpeed();
        this.localization = channel.getLocation().getCity() + ", " + channel.getLocation().getCountry();
        Date current = new Date(System.currentTimeMillis());
        this.currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(current) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(current);
        Condition[] forecast = channel.getItem().getForecast();
        if(forecast != null) {
            for(int i = 0; i < forecast.length; i++) {
                this.weekWeather.add(forecast[i]);
            }
        }

        this.units = channel.getUnits();
    }
}
