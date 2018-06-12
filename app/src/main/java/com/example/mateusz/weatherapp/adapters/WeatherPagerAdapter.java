package com.example.mateusz.weatherapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mateusz.weatherapp.activities.WeatherActivity;
import com.example.mateusz.weatherapp.fragments.LocationList;
import com.example.mateusz.weatherapp.fragments.NextDayWeather;
import com.example.mateusz.weatherapp.fragments.TodayWeather;

public class WeatherPagerAdapter extends FragmentPagerAdapter {
    private TodayWeather todayWeather;
    private NextDayWeather nextDayWeather;
    private LocationList locationList;

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
        todayWeather = new TodayWeather();
        nextDayWeather = new NextDayWeather();
        locationList = new LocationList();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return todayWeather;
        }
        if (position == 2) {
            return nextDayWeather;
        }
        if (position == 0) {
            return locationList;
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        todayWeather.update();
        nextDayWeather.update();
        if(locationList.isVisible()){
            locationList.setSavedLocations();
        }
    }
}
