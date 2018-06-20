package com.example.mateusz.weatherapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.WeatherActivity;
import com.example.mateusz.weatherapp.activities.WeatherData;
import com.example.mateusz.weatherapp.weatherData.Condition;

public class NextDayWeather extends Fragment {

    private TextView localization;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_next_days, container, false);
        localization = rootView.findViewById(R.id.cityNameValue);
        localization.setText(WeatherActivity.localization);
        update();
        return rootView;
    }


    public void update() {
        localization.setText(WeatherActivity.localization);
        int day = 0;
        for(Condition condition : WeatherActivity.weekWeather) {
            if(day > 4){
                break;
            }
            int viewId = getResources().getIdentifier("forecast_" + day, "id", getActivity().getPackageName());
            DayWeather fragment = (DayWeather) getChildFragmentManager().findFragmentById(viewId);

            if (fragment != null) {
                fragment.loadForecast(condition, WeatherActivity.units);
            }
            day++;
        }
    }
}
