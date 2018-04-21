package com.example.mateusz.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WeatherActivity extends AppCompatActivity {

    private ImageView weatherIcon;
    private TextView date;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherIcon = findViewById(R.id.currentWeatherIcon);
        date = findViewById(R.id.dateValue);
        time = findViewById(R.id.timeValue);
        Date deviceDate = Calendar.getInstance().getTime();
        if(deviceDate.getHours() > 20 || deviceDate.getHours() < 6) {
            weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.moon));
        }
        else {
            weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:SS");
        String currentDate = dateFormatter.format(deviceDate);
        String currentTime = timeFormatter.format(deviceDate);
        date.setText(currentDate);
        time.setText(currentTime);

    }
}
