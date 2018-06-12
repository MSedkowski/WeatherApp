package com.example.mateusz.weatherapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.WeatherActivity;
import com.example.mateusz.weatherapp.db.LocationModel;

public class Location extends Fragment {

    private Button location;
    private TextView number;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        location = rootView.findViewById(R.id.location);
        number = rootView.findViewById(R.id.number);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!location.getText().toString().equalsIgnoreCase("")){
                    WeatherActivity.weatherDataParams.setCityName(location.getText().toString());
                    WeatherActivity.localization = location.getText().toString();
                }
            }
        });

        return rootView;
    }

    public void setData(LocationModel model, int id) {
        number.setText("" + id);
        location.setText(model.getCityName());
    }

    public void clearData() {
        number.setText("");
        location.setText("");
    }
}
