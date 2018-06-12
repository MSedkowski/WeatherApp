package com.example.mateusz.weatherapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.WeatherActivity;
import com.example.mateusz.weatherapp.settings.WeatherSettings;
import com.example.mateusz.weatherapp.weatherData.Condition;
import com.example.mateusz.weatherapp.weatherData.Units;

import java.util.ArrayList;
import java.util.Locale;

public class TodayWeather extends Fragment {

    private TextView localizationValue;
    private TextView currentDateValue;
    private TextView weatherValue;
    private ImageView weatherIcon;
    private TextView tempValue;
    private TextView pressureValue;
    private TextView humidityValue;
    private TextView windValue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_today_weather, container, false);
        localizationValue = rootView.findViewById(R.id.localizationValue);
        currentDateValue = rootView.findViewById(R.id.currentDateValue);
        weatherValue = rootView.findViewById(R.id.weatherValue);
        weatherIcon = rootView.findViewById(R.id.weatherIcon);
        tempValue = rootView.findViewById(R.id.tempValue);
        pressureValue = rootView.findViewById(R.id.pressureValue);
        humidityValue = rootView.findViewById(R.id.humidityValue);
        windValue = rootView.findViewById(R.id.windValue);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    public void update() {
        localizationValue.setText(WeatherActivity.localization);
        currentDateValue.setText(WeatherActivity.currentDate);
        weatherValue.setText(WeatherActivity.currentWeather);
        weatherIcon.setImageDrawable(setWeatherIcon(WeatherActivity.code));
        tempValue.setText(WeatherActivity.temp);
        pressureValue.setText(WeatherActivity.pressure);
        humidityValue.setText(WeatherActivity.humidity);
        windValue.setText(WeatherActivity.wind + " " + WeatherActivity.windUnits);
        if (getContext() != null)
            Toast.makeText(getContext(), "Zaktualizowano", Toast.LENGTH_SHORT).show();
    }

    private Drawable setWeatherIcon(int code) {
        int resourceId = getResources().getIdentifier("drawable/icon_14", "drawable", getActivity().getPackageName());
        switch (code) {
            case 0: //Huragany
            case 2:
            case 19:
                resourceId = getResources().getIdentifier("drawable/icon_12", "drawable", getActivity().getPackageName());
                break;

            case 1: //Burze
            case 3:
            case 4:
            case 37:
            case 38:
            case 39:
            case 45:
            case 47:
                resourceId = getResources().getIdentifier("drawable/icon_3", "drawable", getActivity().getPackageName());
                break;

            case 5: //Śnieg
            case 7:
            case 13:
            case 14:
            case 17:
            case 18:
            case 41:
            case 42:
            case 43:
            case 44:
            case 46:
                resourceId = getResources().getIdentifier("drawable/icon_9", "drawable", getActivity().getPackageName());
                break;
            case 15:
            case 16:
                resourceId = getResources().getIdentifier("drawable/icon_10", "drawable", getActivity().getPackageName());
                break;

            case 6: //Deszcz
            case 8:
            case 9:
            case 10:
            case 40:
                resourceId = getResources().getIdentifier("drawable/icon_2", "drawable", getActivity().getPackageName());
                break;

            case 11:
            case 12:
                resourceId = getResources().getIdentifier("drawable/icon_4", "drawable", getActivity().getPackageName());
                break;

            case 26: //Chmury
            case 27:
            case 28:
                resourceId = getResources().getIdentifier("drawable/icon_1", "drawable", getActivity().getPackageName());
                break;
            case 29:
                resourceId = getResources().getIdentifier("drawable/icon_6", "drawable", getActivity().getPackageName());
                break;
            case 30:
                resourceId = getResources().getIdentifier("drawable/icon_11", "drawable", getActivity().getPackageName());
                break;

            case 33: //pogodnie
            case 34:
            case 36:
                resourceId = getResources().getIdentifier("drawable/icon_5", "drawable", getActivity().getPackageName());
                break;
            case 32:
                resourceId = getResources().getIdentifier("drawable/icon_8", "drawable", getActivity().getPackageName());
                break;
            case 31:
                resourceId = getResources().getIdentifier("drawable/icon_7", "drawable", getActivity().getPackageName());
                break;

            case 21: //mgła
            case 22:
            case 23:
            case 24:
                resourceId = getResources().getIdentifier("drawable/icon_13", "drawable", getActivity().getPackageName());
                break;

        }
        return getResources().getDrawable(resourceId);
    }
}
