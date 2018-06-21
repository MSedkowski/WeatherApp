package com.example.mateusz.weatherapp.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.activities.WeatherActivity;
import com.example.mateusz.weatherapp.db.LocationModel;

public class LocationList extends Fragment {

    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_location_list, container, false);

        runnable = new Runnable() {

            @Override
            public void run() {
                setSavedLocations();
                handler.postDelayed(this, 1000);
            }
        };

        this.handler.postDelayed(runnable, 0);
        return rootView;
    }

    @Override
    public void onDetach() {
        handler.removeCallbacks(runnable);
        super.onDetach();
    }

    public void setSavedLocations() {
        int location = 0;
        for (LocationModel model : WeatherActivity.listOfLocations) {
            if(model != null){
                int viewId = getResources().getIdentifier("location_" + location, "id", getActivity().getPackageName());
                Location fragment = (Location) getChildFragmentManager().findFragmentById(viewId);

                if (fragment != null) {
                    fragment.setData(model, location+1);
                }
                location++;
            }
        }
        for(;location < 5; location++) {
            int viewId = getResources().getIdentifier("location_" + location, "id", getActivity().getPackageName());
            Location fragment = (Location) getChildFragmentManager().findFragmentById(viewId);

            fragment.clearData();
        }
    }

}
