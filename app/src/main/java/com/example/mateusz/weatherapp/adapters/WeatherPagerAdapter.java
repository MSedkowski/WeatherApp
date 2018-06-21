package com.example.mateusz.weatherapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.mateusz.weatherapp.activities.WeatherActivity;
import com.example.mateusz.weatherapp.fragments.LocationList;
import com.example.mateusz.weatherapp.fragments.NextDayWeather;
import com.example.mateusz.weatherapp.fragments.TodayWeather;

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    private LocationList locationList;
    private TodayWeather todayWeather;
    private NextDayWeather nextDayWeather;

    public WeatherPagerAdapter(FragmentManager fm) {
        super(fm);
        locationList = new LocationList();
        todayWeather = new TodayWeather();
        nextDayWeather = new NextDayWeather();
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return locationList;
        } else if(position == 1) {
            return todayWeather;
        } else if(position == 2) {
            return nextDayWeather;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(position == 0) {
            locationList = (LocationList) super.instantiateItem(container, position);
            return locationList;
        }
        if(position == 1) {
            todayWeather = (TodayWeather) super.instantiateItem(container, position);
            return todayWeather;
        }
        if(position == 2) {
            nextDayWeather = (NextDayWeather) super.instantiateItem(container, position);
            return nextDayWeather;
        }
        return null;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(nextDayWeather.isVisible()) {
        nextDayWeather.update();}
        todayWeather.update();
        if(locationList.isVisible()){
            locationList.setSavedLocations();
        }
    }
}
