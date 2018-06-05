package com.example.mateusz.weatherapp.weatherData;

import org.json.JSONObject;

public interface JSONPopulator {
    void populate(JSONObject data);
    JSONObject toJSON();
}
