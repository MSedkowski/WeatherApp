package com.example.mateusz.weatherapp.weatherData;

import org.json.JSONException;
import org.json.JSONObject;

public class Atmosphere implements JSONPopulator {

    private int humidity;
    private double pressure;

    public int getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    @Override
    public void populate(JSONObject data) {
        humidity = data.optInt("humidity");
        pressure = data.optDouble("pressure") * 0.00029529983071445 * 100;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("humidity", humidity);
            data.put("pressure", pressure);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
