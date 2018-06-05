package com.example.mateusz.weatherapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mateusz.weatherapp.fragments.MoonInfoFragment;
import com.example.mateusz.weatherapp.fragments.SunInfoFragment;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private SunInfoFragment sunInfoFragment;
    private MoonInfoFragment moonInfoFragment;

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

}