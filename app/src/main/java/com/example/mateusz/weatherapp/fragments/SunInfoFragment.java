package com.example.mateusz.weatherapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.WeatherActivity;

public class SunInfoFragment extends InfoFragment {

    private TextView sunrise;
    private TextView sunset;
    private TextView twilightMorning;
    private TextView twilightEvening;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.setLayout(R.layout.fragment_sun);
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        sunrise = rootView.findViewById(R.id.sunriseValue);
        sunset = rootView.findViewById(R.id.dawnValue);
        twilightMorning = rootView.findViewById(R.id.twilightMorningValue);
        twilightEvening = rootView.findViewById(R.id.twilightEveningValue);

        sunrise.setText(WeatherActivity.sunrise);
        sunset.setText(WeatherActivity.sunset);
        twilightMorning.setText(WeatherActivity.twilightMorning);
        twilightEvening.setText(WeatherActivity.twilightEvening);

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
        super.update();
        sunrise.setText(WeatherActivity.sunrise);
        sunset.setText(WeatherActivity.sunset);
        twilightMorning.setText(WeatherActivity.twilightMorning);
        twilightEvening.setText(WeatherActivity.twilightEvening);
        if(getContext() != null) Toast.makeText(getContext(), "Zaktualizowano", Toast.LENGTH_SHORT).show();
    }

}