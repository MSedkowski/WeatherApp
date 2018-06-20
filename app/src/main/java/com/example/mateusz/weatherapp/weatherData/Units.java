package com.example.mateusz.weatherapp.weatherData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Units implements JSONPopulator, Serializable {

    private String temperature;
    private String speed;

    public String getSpeed() {
        return speed;
    }

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
        speed = data.optString("speed");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("temperature", temperature);
            data.put("speed", speed);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
