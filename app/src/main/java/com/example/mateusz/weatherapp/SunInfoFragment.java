package com.example.mateusz.weatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SunInfoFragment extends Fragment {

    private TextView todayData;
    private TextView longitude;
    private TextView latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sun, container, false);
        todayData = rootView.findViewById(R.id.dateValue);
        longitude = rootView.findViewById(R.id.longitudeValue);
        latitude = rootView.findViewById(R.id.latitudeValue);
        todayData.setText(WeatherActivity.todayDate);
        longitude.setText("" + WeatherActivity.longitude);
        latitude.setText("" + WeatherActivity.latitude);
        return rootView;
    }

    public void updateLocation() {
        longitude.setText("" + WeatherActivity.longitude);
        latitude.setText("" + WeatherActivity.latitude);
    }
}