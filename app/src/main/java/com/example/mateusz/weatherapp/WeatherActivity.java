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
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WeatherActivity extends AppCompatActivity implements LocationListener {

    private static int REQUEST_LOCATION = 1;
    private ImageView weatherIcon;
    private TextView date;
    private TextView time;
    private TextView longitude;
    private TextView latitude;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherIcon = findViewById(R.id.currentWeatherIcon);
        date = findViewById(R.id.dateValue);
        time = findViewById(R.id.timeValue);
        longitude = findViewById(R.id.longitudeValue);
        latitude = findViewById(R.id.latitudeValue);
        setTime();
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
        Date deviceDate = Calendar.getInstance().getTime();
        if (deviceDate.getHours() > 20 || deviceDate.getHours() < 6) {
            weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.moon));
        } else {
            weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:SS");
        String currentDate = dateFormatter.format(deviceDate);
        String currentTime = timeFormatter.format(deviceDate);
        date.setText(currentDate);
        time.setText(currentTime);
    }

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
