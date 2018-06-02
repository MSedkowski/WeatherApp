package com.example.mateusz.weatherapp.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.SunMoonActivity;

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

        todayDate.setText(SunMoonActivity.todayDate);
        longitude.setText(String.valueOf(SunMoonActivity.longitude));
        latitude.setText(String.valueOf(SunMoonActivity.latitude));
        return rootView;
    }

    public void update() {
        longitude.setText(String.valueOf(SunMoonActivity.longitude));
        latitude.setText(String.valueOf(SunMoonActivity.latitude));
        todayDate.setText(SunMoonActivity.todayDate);
    }

    public void setLayout(int layout){
        this.layout = layout;
    }
}
