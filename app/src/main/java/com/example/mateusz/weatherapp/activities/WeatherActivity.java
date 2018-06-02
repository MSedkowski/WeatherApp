package com.example.mateusz.weatherapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.services.WeatherServiceCallback;
import com.example.mateusz.weatherapp.services.YahooWeatherService;
import com.example.mateusz.weatherapp.weatherData.Channel;
import com.example.mateusz.weatherapp.weatherData.Item;

import java.text.DecimalFormat;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener {

    private ImageView weatherIcon;
    private TextView localizationValue;
    private TextView tempValue;
    private TextView weatherValue;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    protected LocationManager locationManager;
    protected Context context;

    public static double longitude;
    public static double latitude;
    private boolean isGPSLocationEnable = true;
    public static int refreshingTime = 15;
    private String locationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_acitivty);

        weatherIcon = findViewById(R.id.weatherIcon);
        localizationValue = findViewById(R.id.localizationValue);
        tempValue = findViewById(R.id.tempValue);
        weatherValue = findViewById(R.id.weatherValue);

        service = new YahooWeatherService(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Pobieram dane...");
        dialog.show();

        setLocation();
        Log.d("Loc:", locationString);
        service.refreshWeather(locationString);
//        service.refreshWeather("Lodz, pl");
    }

    private void setLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_LOCATION = 1;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            if (isGPSLocationEnable) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    StringBuilder builder = new StringBuilder();
                    builder.append(String.format(Locale.US, "%.2f", latitude)).append(",").append(String.format(Locale.US, "%.2f", longitude));
                    locationString = builder.toString();
                } else {
                    Toast.makeText(this, "Brak możliwości śledzenia Twojej pozycji", Toast.LENGTH_SHORT).show();
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, refreshingTime, 0, this);
            }
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + 2, null, getPackageName());
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        weatherIcon.setImageDrawable(weatherIconDrawable);
        localizationValue.setText(channel.getLocation().getCity() + ", " + channel.getLocation().getCountry());
        tempValue.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        weatherValue.setText(setDescription(item));
    }

    private String setDescription(Item item) {
        int itemCode = item.getCondition().getCode();
        int resID = getResources().getIdentifier("PL_" + itemCode, "string", getPackageName());
        return getString(resID);
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this,"Nie udało się pobrać danych pogodowych.", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        if (isGPSLocationEnable) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }
}
