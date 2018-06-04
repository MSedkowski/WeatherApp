package com.example.mateusz.weatherapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.weatherData.Condition;
import com.example.mateusz.weatherapp.weatherData.Units;


public class DayWeather extends Fragment {

    private ImageView weatherIcon;
    private TextView dayName;
    private TextView dayWeatherValue;
    private TextView maxTempValue;
    private TextView minTempValue;

    public DayWeather() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_weather, container, false);

        weatherIcon = view.findViewById(R.id.weatherIcon);
        dayName = view.findViewById(R.id.dayName);
        dayWeatherValue = view.findViewById(R.id.dayWeatherValue);
        maxTempValue = view.findViewById(R.id.maxTempValue);
        minTempValue = view.findViewById(R.id.minTempValue);

        return view;
    }

    public void loadForecast(Condition forecast, Units units) {
        weatherIcon.setImageDrawable(setWeatherIcon(forecast));
        dayName.setText(setDayName(forecast));
        dayWeatherValue.setText(setDescription(forecast));
        maxTempValue.setText("" + forecast.getHighTemperature() + "\u00B0" + units.getTemperature());
        minTempValue.setText("" + forecast.getLowTemperature() + "\u00B0" + units.getTemperature());
    }

    private String setDescription(Condition item) {
        int itemCode = item.getCode();
        int resID = getResources().getIdentifier("PL_" + itemCode, "string", getActivity().getPackageName());
        return getString(resID);
    }

    private String setDayName(Condition item) {
        int resID = getResources().getIdentifier("PL_" + item.getDay(), "string", getActivity().getPackageName());
        return getString(resID);
    }

    private Drawable setWeatherIcon(Condition item) {
        int code = item.getCode();
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
