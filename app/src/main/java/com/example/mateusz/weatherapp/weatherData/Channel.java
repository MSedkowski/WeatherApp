package com.example.mateusz.weatherapp.weatherData;

import org.json.JSONObject;

public class Channel implements JSONPopulator {

    private Item item;
    private Units units;
    private Location location;

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

    }
}
