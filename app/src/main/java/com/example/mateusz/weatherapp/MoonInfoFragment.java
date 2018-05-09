package com.example.mateusz.weatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MoonInfoFragment extends Fragment {

    private TextView todayData;
    private TextView longitude;
    private TextView latitude;

    private TextView moonrise;
    private TextView moonset;
    private TextView nextFullMoon;
    private TextView nextNewMoon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moon, container, false);
        todayData = rootView.findViewById(R.id.dateValue);
        longitude = rootView.findViewById(R.id.longitudeValue);
        latitude = rootView.findViewById(R.id.latitudeValue);
        moonrise = rootView.findViewById(R.id.moonriseValue);
        moonset = rootView.findViewById(R.id.moonsetValue);
        nextFullMoon = rootView.findViewById(R.id.nextFullMoonValue);
        nextNewMoon = rootView.findViewById(R.id.nextNewMoonValue);

        todayData.setText(WeatherActivity.todayDate);
        longitude.setText("" + WeatherActivity.longitude);
        latitude.setText("" + WeatherActivity.latitude);
        moonrise.setText(WeatherActivity.moonrise);
        moonset.setText(WeatherActivity.moonset);
        nextFullMoon.setText(WeatherActivity.nextFullMoon);
        nextNewMoon.setText(WeatherActivity.nextNewMoon);
        return rootView;
    }

    public void updateLocation() {
        longitude.setText("" + WeatherActivity.longitude);
        latitude.setText("" + WeatherActivity.latitude);
    }

}