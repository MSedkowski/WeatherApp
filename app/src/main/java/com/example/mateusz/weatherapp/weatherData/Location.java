package com.example.mateusz.weatherapp.weatherData;

import org.json.JSONException;
import org.json.JSONObject;

public class Location implements JSONPopulator{

    private String city;
    private String country;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public void populate(JSONObject data) {
        this.city = data.optString("city");
        this.country = data.optString("country");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("city", city);
            data.put("country", country);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
