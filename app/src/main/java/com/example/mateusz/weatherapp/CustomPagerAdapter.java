package com.example.mateusz.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    public SunInfoFragment sunInfoFragment;
    public MoonInfoFragment moonInfoFragment;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
        sunInfoFragment = new SunInfoFragment();
        moonInfoFragment = new MoonInfoFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return sunInfoFragment;
        }
        if (position == 1) {
            return moonInfoFragment;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void notifyDataSetChanged() {
        sunInfoFragment.updateLocation();
        sunInfoFragment.updateDate();
        sunInfoFragment.updateSunInfo();
        moonInfoFragment.updateLocation();
        moonInfoFragment.updateDate();
        moonInfoFragment.updateMoonInfo();
    }

}