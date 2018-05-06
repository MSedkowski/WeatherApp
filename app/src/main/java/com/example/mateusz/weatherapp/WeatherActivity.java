package com.example.mateusz.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WeatherActivity extends AppCompatActivity implements LocationListener {

    private static int REQUEST_LOCATION = 1;
    private ImageView weatherIcon;
    private TextView date;
    private TextClock time;
    private TextView longitude;
    private TextView latitude;
    private TextView sunrise;
    private TextView dawn;
    private TextView twilightMorning;
    private TextView twilightEvening;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherIcon = findViewById(R.id.currentWeatherIcon);
        date = findViewById(R.id.dateValue);
        time = findViewById(R.id.timeValue);
        longitude = findViewById(R.id.longitudeValue);
        latitude = findViewById(R.id.latitudeValue);
        sunrise = findViewById(R.id.sunriseValue);
        dawn = findViewById(R.id.dawnValue);
        twilightMorning = findViewById(R.id.twilightMorningValue);
        twilightEvening = findViewById(R.id.twilightEveningValue);
        setTime();
        setLocation();
        setData();
    }

    private void setData() {
        long deviceDate = System.currentTimeMillis();
        int year = Integer.parseInt(new SimpleDateFormat("YYYY", Locale.ENGLISH).format(deviceDate));
        int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(deviceDate));
        int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(deviceDate));
        int hour = Integer.parseInt(new SimpleDateFormat("hh", Locale.GERMANY).format(deviceDate));
        int minute = Integer.parseInt(new SimpleDateFormat("mm", Locale.GERMANY).format(deviceDate));
        int second = Integer.parseInt(new SimpleDateFormat("ss", Locale.GERMANY).format(deviceDate));
        int timeZoneOffset = 1;
        boolean dayLightSaving = true;
        AstroDateTime today = new AstroDateTime(year, month, day, hour, minute, second, timeZoneOffset, dayLightSaving);
        AstroCalculator.Location astroLocation = new AstroCalculator.Location(Double.parseDouble(latitude.getText().toString()),
                                                                              Double.parseDouble(longitude.getText().toString()));
        AstroCalculator astroCalculator = new AstroCalculator(today, astroLocation);
        AstroDateTime todaySunrise = astroCalculator.getSunInfo().getSunrise();
        AstroDateTime todayDawn = astroCalculator.getSunInfo().getSunset();
        AstroDateTime todayTwilingMorning = astroCalculator.getSunInfo().getTwilightMorning();
        AstroDateTime todayTwilingEvening = astroCalculator.getSunInfo().getTwilightEvening();
        sunrise.setText("" + todaySunrise.getHour() + ":" + todaySunrise.getMinute() + ":" + todaySunrise.getSecond());
        dawn.setText("" + todayDawn.getHour() + ":" + todayDawn.getMinute() + ":" + todayDawn.getSecond());
        int differenceMorning = (todaySunrise.getHour()*60+todaySunrise.getMinute())-
                                (todayTwilingMorning.getHour()*60+todayTwilingMorning.getMinute());
        int differenceEvening = (todayTwilingEvening.getHour()*60+todayTwilingEvening.getMinute())-
                                (todayDawn.getHour()*60+todayDawn.getMinute());
        twilightMorning.setText("" + differenceMorning + " minut");
        twilightEvening.setText("" + differenceEvening + " minut");

    }

    private void setLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null) {
                longitude.setText("" + location.getLongitude());
                latitude.setText("" + location.getLatitude());
            } else {
                Toast.makeText(this, "Brak możliwości śledzenia Twojej pozycji", Toast.LENGTH_SHORT).show();
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        }
    }
    private void setTime() {
        Thread t = new Thread() {
            @Override
            public void run() {
                long deviceDate = System.currentTimeMillis();
                SimpleDateFormat hourFormater = new SimpleDateFormat("HH");
                String hours = hourFormater.format(deviceDate);
                if (Integer.parseInt(hours) > 20 || Integer.parseInt(hours) < 6) {
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.moon));
                } else {
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sun));
                }
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                String currentDate = dateFormatter.format(deviceDate);
                date.setText(currentDate);
                try {
                    while(!isInterrupted()) {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long deviceDate = System.currentTimeMillis();
                                SimpleDateFormat hourFormater = new SimpleDateFormat("HH");
                                String hours = hourFormater.format(deviceDate);
                                if (Integer.parseInt(hours) > 20 || Integer.parseInt(hours) < 6) {
                                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.moon));
                                } else {
                                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sun));
                                }
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                String currentDate = dateFormatter.format(deviceDate);
                                date.setText(currentDate);
                            }
                        });
                    }
                } catch (InterruptedException e) {}
            }
        };
        t.start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        longitude.setText("" + location.getLongitude());
        latitude.setText("" + location.getLatitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
