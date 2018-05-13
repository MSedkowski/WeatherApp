package com.example.mateusz.weatherapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.WeatherActivity;

import java.text.DecimalFormat;

public class MoonInfoFragment extends Fragment {

    private TextView todayDate;
    private TextView longitude;
    private TextView latitude;

    private TextView moonrise;
    private TextView moonset;
    private TextView nextFullMoon;
    private TextView nextNewMoon;
    private TextView moonAge;
    private TextView moonPhase;

    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moon, container, false);
        todayDate = rootView.findViewById(R.id.dateValue);
        longitude = rootView.findViewById(R.id.longitudeValue);
        latitude = rootView.findViewById(R.id.latitudeValue);
        moonrise = rootView.findViewById(R.id.moonriseValue);
        moonset = rootView.findViewById(R.id.moonsetValue);
        nextFullMoon = rootView.findViewById(R.id.nextFullMoonValue);
        nextNewMoon = rootView.findViewById(R.id.nextNewMoonValue);
        moonAge = rootView.findViewById(R.id.moonAgeValue);
        moonPhase = rootView.findViewById(R.id.moonPhaseValue);

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                update();
                handler.postDelayed(this, WeatherActivity.refreshingTime * 1000);
            }
        };

        this.handler.postDelayed(runnable, 0);

        return rootView;
    }

    public void update() {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        longitude.setText(String.valueOf(WeatherActivity.longitude));
        latitude.setText(String.valueOf(WeatherActivity.latitude));
        todayDate.setText(WeatherActivity.todayDate);
        moonrise.setText(WeatherActivity.moonrise);
        moonset.setText(WeatherActivity.moonset);
        nextFullMoon.setText(WeatherActivity.nextFullMoon);
        nextNewMoon.setText(WeatherActivity.nextNewMoon);
        moonAge.setText(format.format(WeatherActivity.moonAge));
        StringBuilder moonPhaseText = new StringBuilder();
        moonPhaseText.append(format.format(WeatherActivity.moonPhase))
                     .append("%");
        moonPhase.setText(moonPhaseText);
        if(getContext() != null) Toast.makeText(getContext(), "Zaktualizowano", Toast.LENGTH_SHORT).show();
    }
}