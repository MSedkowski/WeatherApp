package com.example.mateusz.weatherapp.activities;

import android.widget.Toast;

import com.example.mateusz.weatherapp.services.WeatherServiceCallback;
import com.example.mateusz.weatherapp.services.YahooWeatherService;
import com.example.mateusz.weatherapp.weatherData.Channel;
import com.example.mateusz.weatherapp.weatherData.Condition;
import com.example.mateusz.weatherapp.weatherData.Item;
import com.example.mateusz.weatherapp.weatherData.Units;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherData implements WeatherServiceCallback, Serializable {

    private String localization;
    private String currentDate;
    private int currentWeather;
    private String temp;
    private String pressure;
    private String humidity;
    private String wind;
    private String tempUnits;
    private String windUnits;
    private boolean failure = false;

    private YahooWeatherService service;

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

    public static List<Condition> getWeekWeather() {
        return weekWeather;
    }

    public static void setWeekWeather(List<Condition> weekWeather) {
        WeatherData.weekWeather = weekWeather;
    }

    public static Units getUnits() {
        return units;
    }

    public static void setUnits(Units units) {
        WeatherData.units = units;
    }

    public static int getCode() {
        return code;
    }

    public static void setCode(int code) {
        WeatherData.code = code;
    }

    public boolean isFailure() {
        return failure;
    }

    public WeatherData(YahooWeatherService service) {
        this.service = service;
    }

    public void updateWeather(char tempSign) {
        service.refreshWeather(localization, 1, tempSign);
    }

    public void updateWeather(String cityName, char tempSign){
        service.refreshWeather(cityName, 1, tempSign);
    }

    public void updateWeather(double longitiude, double latitiude, char tempSign) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(Locale.US, "%.2f", latitiude))
                .append(",")
                .append(String.format(Locale.US, "%.2f", longitiude));
        service.refreshWeather(builder.toString(), 0, tempSign);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        Condition[] forecast = channel.getItem().getForecast();
        Item item = channel.getItem();

        code = item.getCondition().getCode();
        localization = channel.getLocation().getCity() + ", " + channel.getLocation().getCountry();
        temp = item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature();
        currentWeather = item.getCondition().getCode();
        pressure = String.format("%.0f", channel.getAtmosphere().getPressure()) + " hPa";
        humidity = channel.getAtmosphere().getHumidity() + " %";
        Date current = new Date(System.currentTimeMillis());
        currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(current) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(current);
        units = channel.getUnits();
        tempUnits = units.getTemperature();
        windUnits = units.getSpeed();
        wind = channel.getWind().getSpeed();

        weekWeather.addAll(Arrays.asList(forecast));
    }

    @Override
    public void serviceFailure(Exception exception) {
        failure = true;
    }


}
