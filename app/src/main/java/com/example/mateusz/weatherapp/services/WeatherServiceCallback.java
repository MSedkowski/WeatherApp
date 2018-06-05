package com.example.mateusz.weatherapp.services;

import com.example.mateusz.weatherapp.weatherData.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
