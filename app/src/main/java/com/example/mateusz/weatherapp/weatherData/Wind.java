package com.example.mateusz.weatherapp.weatherData;

import org.json.JSONException;
import org.json.JSONObject;

public class Wind implements JSONPopulator {

    private String speed;

    public String getSpeed() {
        return speed;
    }

    @Override
    public void populate(JSONObject data) {
        speed = data.optString("speed");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("speed", speed);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
