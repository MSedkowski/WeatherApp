package com.example.mateusz.weatherapp.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.WeatherActivity;

public class InfoFragment extends Fragment {
    private TextView todayDate;
    private TextView longitude;
    private TextView latitude;
    private int layout;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(layout, container, false);
        todayDate = rootView.findViewById(R.id.dateValue);
        longitude = rootView.findViewById(R.id.longitudeValue);
        latitude = rootView.findViewById(R.id.latitudeValue);

        todayDate.setText(WeatherActivity.todayDate);
        longitude.setText(String.valueOf(WeatherActivity.longitude));
        latitude.setText(String.valueOf(WeatherActivity.latitude));
        return rootView;
    }

    public void update() {
        longitude.setText(String.valueOf(WeatherActivity.longitude));
        latitude.setText(String.valueOf(WeatherActivity.latitude));
        todayDate.setText(WeatherActivity.todayDate);
    }

    public void setLayout(int layout){
        this.layout = layout;
    }
}
