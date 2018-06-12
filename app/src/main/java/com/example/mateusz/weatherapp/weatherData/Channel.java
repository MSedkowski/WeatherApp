package com.example.mateusz.weatherapp.weatherData;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

public class Channel implements JSONPopulator {

    private Item item;
    private Units units;
    private Location location;
    private Atmosphere atmosphere;
    private Wind wind;

    public Wind getWind() {
        return wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Location getLocation() {
        return location;
    }

    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        location = new Location();
        location.populate(data.optJSONObject("location"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("item", item.toJSON());
            data.put("units", units.toJSON());
            data.put("location", location.toJSON());
            data.put("atmosphere", atmosphere.toJSON());
            data.put("wind", wind.toJSON());
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
