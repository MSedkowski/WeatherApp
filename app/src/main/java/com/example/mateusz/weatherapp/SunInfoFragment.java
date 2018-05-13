package com.example.mateusz.weatherapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SunInfoFragment extends Fragment {

    private TextView todayDate;
    private TextView longitude;
    private TextView latitude;
    private TextView sunrise;
    private TextView sunset;
    private TextView twilightMorning;
    private TextView twilightEvening;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sun, container, false);
        todayDate = rootView.findViewById(R.id.dateValue);
        longitude = rootView.findViewById(R.id.longitudeValue);
        latitude = rootView.findViewById(R.id.latitudeValue);
        sunrise = rootView.findViewById(R.id.sunriseValue);
        sunset = rootView.findViewById(R.id.dawnValue);
        twilightMorning = rootView.findViewById(R.id.twilightMorningValue);
        twilightEvening = rootView.findViewById(R.id.twilightEveningValue);

        todayDate.setText(WeatherActivity.todayDate);
        longitude.setText(String.valueOf(WeatherActivity.longitude));
        latitude.setText(String.valueOf(WeatherActivity.latitude));
        sunrise.setText(WeatherActivity.sunrise);
        sunset.setText(WeatherActivity.sunset);
        twilightMorning.setText(WeatherActivity.twilightMorning);
        twilightEvening.setText(WeatherActivity.twilightEvening);

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                update();
                handler.postDelayed(this, WeatherActivity.refreshingTime * 60000);
            }
        };

        this.handler.postDelayed(runnable, 0);

        return rootView;
    }

    public void update() {
        longitude.setText(String.valueOf(WeatherActivity.longitude));
        latitude.setText(String.valueOf(WeatherActivity.latitude));
        todayDate.setText(WeatherActivity.todayDate);
        sunrise.setText(WeatherActivity.sunrise);
        sunset.setText(WeatherActivity.sunset);
        twilightMorning.setText(WeatherActivity.twilightMorning);
        twilightEvening.setText(WeatherActivity.twilightEvening);
    }

}