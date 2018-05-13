package com.example.mateusz.weatherapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mateusz.weatherapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startWeatherActivity(View view) {
        Intent weatherActivity = new Intent(MainActivity.this, WeatherActivity.class);
        //simpleCalculator.putExtra("darkMode", darkMode); //Optional parameters
        MainActivity.this.startActivity(weatherActivity);
    }

    public void startAboutActivity(View view) {
        Intent aboutActivity = new Intent(MainActivity.this, AboutActivity.class);
        //simpleCalculator.putExtra("darkMode", darkMode); //Optional parameters
        MainActivity.this.startActivity(aboutActivity);
    }

}
