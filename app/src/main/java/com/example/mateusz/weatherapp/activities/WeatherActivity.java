package com.example.mateusz.weatherapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.db.LocationDbAdapter;
import com.example.mateusz.weatherapp.db.LocationModel;
import com.example.mateusz.weatherapp.fragments.DayWeather;
import com.example.mateusz.weatherapp.services.WeatherServiceCallback;
import com.example.mateusz.weatherapp.services.YahooWeatherService;
import com.example.mateusz.weatherapp.settings.SettingsActivity;
import com.example.mateusz.weatherapp.settings.WeatherSettings;
import com.example.mateusz.weatherapp.weatherData.Channel;
import com.example.mateusz.weatherapp.weatherData.Condition;
import com.example.mateusz.weatherapp.weatherData.Item;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.mateusz.weatherapp.activities.SunMoonActivity.refreshingTime;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener {

    private ImageView weatherIcon;
    private TextView localizationValue;
    private TextView tempValue;
    private TextView weatherValue;
    private TextView pressureValue;
    private TextView humidityValue;
    private TextView currentDate;
    private Button changeDate;
    private ImageButton saveData;

    private ProgressDialog dialog;

    protected LocationManager locationManager;
    protected Context context;
    private SharedPreferences sharedPrefs;
    private LocationDbAdapter locationDbAdapter;
    private WeatherData weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_acitivity);

        weatherIcon = findViewById(R.id.weatherIcon);
        localizationValue = findViewById(R.id.localizationValue);
        tempValue = findViewById(R.id.tempValue);
        weatherValue = findViewById(R.id.weatherValue);
        pressureValue = findViewById(R.id.pressureValue);
        humidityValue = findViewById(R.id.humidityValue);
        currentDate = findViewById(R.id.currentDateValue);
        changeDate = findViewById(R.id.changeLocationButton);
        saveData = findViewById(R.id.saveButton);

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePreferences();
            }
        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentLocation();
            }
        });
        weatherData = new WeatherData();
        locationDbAdapter = new LocationDbAdapter(getApplicationContext());
        locationDbAdapter.open();
        weatherData.setService(new YahooWeatherService(this));

        dialog = new ProgressDialog(this);
        dialog.setMessage("Pobieram dane...");
        dialog.show();

        setLocation();
        weatherData.getService().refreshWeather(weatherData.getLocationString(), 0, 'c');
        updatePreferences();
    }

    private void checkIfLocationIsSaved() {
        int id = locationDbAdapter.getLocationID(weatherData.getCityName());
        if(id == 0){
            saveData.setBackground(getResources().getDrawable(R.drawable.star));
        }
        else {
            saveData.setBackground(getResources().getDrawable(R.drawable.star_clicked));
        }
    }

    private void saveCurrentLocation() {
        if(saveData.getBackground().getConstantState() == getResources().getDrawable(R.drawable.star).getConstantState()) {
            saveLocationIntoDatabase();
            saveData.setBackgroundResource(R.drawable.star_clicked);
        }
        else {
            removeLocationFromDatabase();
            saveData.setBackgroundResource(R.drawable.star);
        }

    }

    private void removeLocationFromDatabase() {
        int id = locationDbAdapter.getLocationID(weatherData.getCityName());
        locationDbAdapter.deleteLocation(id);
    }

    private void saveLocationIntoDatabase() {
        locationDbAdapter.insertLocation(weatherData.getLongitude(), weatherData.getLatitude(), weatherData.getCityName());
    }

    private void changePreferences() {
        Intent modifySettings = new Intent(WeatherActivity.this, WeatherSettings.class);
        int updated = 1;
        startActivityForResult(modifySettings, updated);
    }

    private void updatePreferences() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("gps_enabled", weatherData.isGPSLocationEnable());
        editor.putString("longitude_value", String.valueOf(weatherData.getLongitude()));
        editor.putString("latitude_value", String.valueOf(weatherData.getLatitude()));
        editor.putString("refreshing_time", String.valueOf(weatherData.getRefreshingTime()));
        editor.putBoolean("c_or_f", weatherData.isTempSignIsC());
        editor.putString("cityName_value", weatherData.getCityName());
        editor.apply();
    }

    @SuppressLint("ShowToast")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                updateChanges();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Brak zmian", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateChanges() {
        if(!sharedPrefs.getString("cityName_value", " ").equals(" ")){
            weatherData.setCityName(sharedPrefs.getString("cityName_value", weatherData.getCityName()));
        }
        else {
            weatherData.setGPSLocationEnable(sharedPrefs.getBoolean("gps_enabled", weatherData.isGPSLocationEnable()));
            if (!weatherData.isGPSLocationEnable()) {
                String longitudeText, latitudeText;
                longitudeText = sharedPrefs.getString("longitude_value", String.valueOf(weatherData.getLongitude()));
                weatherData.setLongitude(Double.parseDouble(longitudeText));
                latitudeText = sharedPrefs.getString("latitude_value", String.valueOf(weatherData.getLatitude()));
                weatherData.setLatitude(Double.parseDouble(latitudeText));
                StringBuilder builder = new StringBuilder();
                builder.append(String.format(Locale.US, "%.2f", weatherData.getLatitude()))
                        .append(",")
                        .append(String.format(Locale.US, "%.2f", weatherData.getLongitude()));
                weatherData.setLocationString(builder.toString());
            } else {
                setLocation();
            }
        }
        String refreshingTimeText;
        refreshingTimeText = sharedPrefs.getString("refreshing_time", String.valueOf(weatherData.getRefreshingTime()));
        weatherData.setRefreshingTime(Integer.parseInt(refreshingTimeText));
        weatherData.setTempSignIsC(sharedPrefs.getBoolean("c_or_f", weatherData.isTempSignIsC()));
        if(weatherData.getCityName() != null && weatherData.isTempSignIsC()) {
            weatherData.getService().refreshWeather(weatherData.getCityName(), 1, 'c');
        }
        else if(weatherData.getCityName() != null && !weatherData.isTempSignIsC()) {
            weatherData.getService().refreshWeather(weatherData.getCityName(), 1, 'f');
        }
        else if(weatherData.getCityName() == null && weatherData.isTempSignIsC()) {
            weatherData.getService().refreshWeather(weatherData.getLocationString(), 0, 'c');
        }
        else {
            weatherData.getService().refreshWeather(weatherData.getLocationString(), 0, 'f');
        }
        updatePreferences();
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
            if (weatherData.isGPSLocationEnable()) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    weatherData.setLongitude(location.getLongitude());
                    weatherData.setLatitude(location.getLatitude());
                    StringBuilder builder = new StringBuilder();
                    builder.append(String.format(Locale.US, "%.2f", weatherData.getLatitude()))
                            .append(",")
                            .append(String.format(Locale.US, "%.2f", weatherData.getLongitude()));
                    weatherData.setLocationString(builder.toString());
                } else {
                    Toast.makeText(this, "Brak możliwości śledzenia Twojej pozycji", Toast.LENGTH_SHORT).show();
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, weatherData.getRefreshingTime(), 500, this);
            }
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        Condition[] forecast = channel.getItem().getForecast();
        dialog.hide();
        checkIfLocationIsSaved();

        Item item = channel.getItem();
        weatherIcon.setImageDrawable(setWeatherIcon(item.getCondition()));
        localizationValue.setText(channel.getLocation().getCity() + ", " + channel.getLocation().getCountry());
        tempValue.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        weatherValue.setText(setDescription(item));
        pressureValue.setText(String.format("%.0f",channel.getAtmosphere().getPressure()) + " hPa");
        humidityValue.setText(channel.getAtmosphere().getHumidity() + " %");
        Date current = new Date(System.currentTimeMillis());
        currentDate.setText(DateFormat.getDateInstance(DateFormat.LONG).format(current) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(current));

        for (int day = 1; day < forecast.length; day++) {
            if (day >= 4) {
                break;
            }

            Condition currentCondition = forecast[day];

            int viewId = getResources().getIdentifier("forecast_" + day, "id", getPackageName());
            DayWeather fragment = (DayWeather) getSupportFragmentManager().findFragmentById(viewId);

            if (fragment != null) {
                fragment.loadForecast(currentCondition, channel.getUnits());
            }
        }

        weatherData.setCityName(channel.getLocation().getCity());
        weatherData.setTempSignIsC(channel.getUnits().getTemperature().equals("C"));
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
        if (weatherData.isGPSLocationEnable()) {
            weatherData.setLongitude(location.getLongitude());
            weatherData.setLatitude(location.getLatitude());
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(Locale.US, "%.2f", weatherData.getLatitude()))
                    .append(",")
                    .append(String.format(Locale.US, "%.2f", weatherData.getLongitude()));
            weatherData.setLocationString(builder.toString());
            weatherData.getService().refreshWeather(weatherData.getLocationString(), 0, 'c');
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

    public void refreshAction(View view) {
        if(weatherData.getCityName() != null && weatherData.isTempSignIsC()){
            weatherData.getService().refreshWeather(weatherData.getCityName(), 1,'c');
        }
        else if(weatherData.getCityName() != null && !weatherData.isTempSignIsC()){
            weatherData.getService().refreshWeather(weatherData.getCityName(), 1, 'f');
        }
        else if(weatherData.getCityName() == null && weatherData.isTempSignIsC()) {
            weatherData.getService().refreshWeather(weatherData.getLocationString(), 0, 'c');
        }
        else {
            weatherData.getService().refreshWeather(weatherData.getLocationString(), 0, 'f');
        }
        checkIfLocationIsSaved();
        Toast.makeText(this, "Odświeżono dane", Toast.LENGTH_SHORT).show();
    }

    private Drawable setWeatherIcon(Condition item) {
        int code = item.getCode();
        int resourceId = getResources().getIdentifier("drawable/icon_14", "drawable", getPackageName());
        switch (code) {
            case 0: //Huragany
            case 2:
            case 19:
                resourceId = getResources().getIdentifier("drawable/icon_12", "drawable", getPackageName());
                break;

            case 1: //Burze
            case 3:
            case 4:
            case 37:
            case 38:
            case 39:
            case 45:
            case 47:
                resourceId = getResources().getIdentifier("drawable/icon_3", "drawable", getPackageName());
                break;

            case 5: //Śnieg
            case 7:
            case 13:
            case 14:
            case 17:
            case 18:
            case 41:
            case 42:
            case 43:
            case 44:
            case 46:
                resourceId = getResources().getIdentifier("drawable/icon_9", "drawable", getPackageName());
                break;
            case 15:
            case 16:
                resourceId = getResources().getIdentifier("drawable/icon_10", "drawable", getPackageName());
                break;

            case 6: //Deszcz
            case 8:
            case 9:
            case 10:
            case 40:
                resourceId = getResources().getIdentifier("drawable/icon_2", "drawable", getPackageName());
                break;

            case 11:
            case 12:
                resourceId = getResources().getIdentifier("drawable/icon_4", "drawable", getPackageName());
                break;

            case 26: //Chmury
            case 27:
            case 28:
                resourceId = getResources().getIdentifier("drawable/icon_1", "drawable", getPackageName());
                break;
            case 29:
                resourceId = getResources().getIdentifier("drawable/icon_6", "drawable", getPackageName());
                break;
            case 30:
                resourceId = getResources().getIdentifier("drawable/icon_11", "drawable", getPackageName());
                break;

            case 33: //pogodnie
            case 34:
            case 36:
                resourceId = getResources().getIdentifier("drawable/icon_5", "drawable", getPackageName());
                break;
            case 32:
                resourceId = getResources().getIdentifier("drawable/icon_8", "drawable", getPackageName());
                break;
            case 31:
                resourceId = getResources().getIdentifier("drawable/icon_7", "drawable", getPackageName());
                break;

            case 21: //mgła
            case 22:
            case 23:
            case 24:
                resourceId = getResources().getIdentifier("drawable/icon_13", "drawable", getPackageName());
                break;

        }
        return getResources().getDrawable(resourceId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(locationDbAdapter != null)
            locationDbAdapter.close();
        super.onDestroy();
    }
}
