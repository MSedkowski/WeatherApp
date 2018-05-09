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
    private TextView sunrise;
    private TextView sunset;
    private TextView twilightMorning;
    private TextView twilightEvening;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sun, container, false);
        todayData = rootView.findViewById(R.id.dateValue);
        longitude = rootView.findViewById(R.id.longitudeValue);
        latitude = rootView.findViewById(R.id.latitudeValue);
        sunrise = rootView.findViewById(R.id.sunriseValue);
        sunset = rootView.findViewById(R.id.dawnValue);
        twilightMorning = rootView.findViewById(R.id.twilightMorningValue);
        twilightEvening = rootView.findViewById(R.id.twilightEveningValue);

        todayData.setText(WeatherActivity.todayDate);
        longitude.setText("" + WeatherActivity.longitude);
        latitude.setText("" + WeatherActivity.latitude);
        sunrise.setText(WeatherActivity.sunrise);
        sunset.setText(WeatherActivity.sunset);
        twilightMorning.setText(WeatherActivity.twilightMorning);
        twilightEvening.setText(WeatherActivity.twilightEvening);
        return rootView;
    }

    public void updateLocation() {
        longitude.setText("" + WeatherActivity.longitude);
        latitude.setText("" + WeatherActivity.latitude);
    }
}