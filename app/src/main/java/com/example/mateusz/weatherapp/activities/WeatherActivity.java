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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.adapters.WeatherPagerAdapter;
import com.example.mateusz.weatherapp.db.LocationDbAdapter;
import com.example.mateusz.weatherapp.db.LocationModel;
import com.example.mateusz.weatherapp.services.WeatherServiceCallback;
import com.example.mateusz.weatherapp.services.YahooWeatherService;
import com.example.mateusz.weatherapp.settings.WeatherSettings;
import com.example.mateusz.weatherapp.weatherData.Channel;
import com.example.mateusz.weatherapp.weatherData.Condition;
import com.example.mateusz.weatherapp.weatherData.Item;
import com.example.mateusz.weatherapp.weatherData.Units;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener {

    public static List<LocationModel> listOfLocations;
    private Button changeDate;
    private ImageButton saveData;

    public static String localization;
    public static String currentDate;
    public static String currentWeather;
    public static String temp;
    public static String pressure;
    public static String humidity;
    public static String wind;
    public static String tempUnits;
    public static String windUnits;
    public static Units units;
    public static int code;

    private double longitude = 26.0;
    private double latitude = 19.0;

    public static List<Condition> weekWeather = new ArrayList<>();

    private ProgressDialog dialog;

    protected LocationManager locationManager;
    protected Context context;
    private SharedPreferences sharedPrefs;
    private LocationDbAdapter locationDbAdapter;
    public static WeatherDataParams weatherDataParams;
    private ViewPager weatherViewPager;
    public static String fileName = "weatherData.data";
    private WeatherData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_acitivity);

        data = new WeatherData();
        weatherViewPager = findViewById(R.id.weatherPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        weatherViewPager.setAdapter(new WeatherPagerAdapter(fragmentManager));
        weatherViewPager.setCurrentItem(1);
        weatherViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    if (weatherDataParams.getCityName() != null && weatherDataParams.isTempSignIsC()) {
                        weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'c');
                    } else if (weatherDataParams.getCityName() != null && !weatherDataParams.isTempSignIsC()) {
                        weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'f');
                    } else if (weatherDataParams.getCityName() == null && weatherDataParams.isTempSignIsC()) {
                        weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'c');
                    } else {
                        weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'f');
                    }
                    weatherViewPager.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
        weatherDataParams = new WeatherDataParams();
        locationDbAdapter = new LocationDbAdapter(getApplicationContext());
        locationDbAdapter.open();

        weatherDataParams.setService(new YahooWeatherService(this));
        dialog = new ProgressDialog(this);
        dialog.setMessage("Pobieram dane...");
        dialog.show();

        if(isOnline()) {
            weatherDataParams.setLongitude(this.longitude);
            weatherDataParams.setLatitude(this.latitude);
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(Locale.US, "%.2f", weatherDataParams.getLatitude()))
                    .append(",")
                    .append(String.format(Locale.US, "%.2f", weatherDataParams.getLongitude()));
            weatherDataParams.setLocationString(builder.toString());
            weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'c');
        } else {
            data = readFromFile(this);
            updateFromFile();
        }

        updatePreferences();
        setListOfLocations();
    }

    private void updateFromFile() {
        code = data.getCode();
        localization = data.getLocalization();
        temp = data.getTemp();
        currentWeather = setDescription(data.getCode());
        pressure = data.getPressure();
        humidity = data.getHumidity();
        currentDate = data.getCurrentDate();
        tempUnits = data.getTempUnits();
        windUnits = data.getWindUnits();
        wind = data.getWind();
        weekWeather.addAll(data.getWeekWeather());
        units = data.getUnits();
        dialog.hide();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setListOfLocations(){
        listOfLocations = new ArrayList<>();
        int i = 0, size = locationDbAdapter.getAllLocations().getCount(), id;
        Cursor cursor = locationDbAdapter.getAllLocations();
        cursor.moveToFirst();
        for(;i < size; i++){
            if(cursor != null) {
                id = cursor.getInt(0);
                listOfLocations.add(locationDbAdapter.getLocation(id));
            }
            cursor.moveToNext();
        }
    }

    private void checkIfLocationIsSaved() {
        int id = locationDbAdapter.getLocationID(weatherDataParams.getCityName());
        if (id == 0) {
            saveData.setBackground(getResources().getDrawable(R.drawable.star));
        } else {
            saveData.setBackground(getResources().getDrawable(R.drawable.star_clicked));
        }
    }

    private void saveCurrentLocation() {
        if (saveData.getBackground().getConstantState() == getResources().getDrawable(R.drawable.star).getConstantState()) {
            if (locationDbAdapter.getSize() < 6) {
                saveLocationIntoDatabase();
                saveData.setBackgroundResource(R.drawable.star_clicked);
            } else {
                Toast.makeText(this, "Osiągnięto maksymalną liczbę zapisanych miast", Toast.LENGTH_SHORT).show();
            }
        } else {
            removeLocationFromDatabase();
            saveData.setBackgroundResource(R.drawable.star);
        }
        weatherViewPager.getAdapter().notifyDataSetChanged();
        setListOfLocations();
    }

    private void removeLocationFromDatabase() {
        int id = locationDbAdapter.getLocationID(weatherDataParams.getCityName());
        locationDbAdapter.deleteLocation(id);
    }

    private void saveLocationIntoDatabase() {
        locationDbAdapter.insertLocation(weatherDataParams.getLongitude(), weatherDataParams.getLatitude(), weatherDataParams.getCityName());
    }

    private void changePreferences() {
        Intent modifySettings = new Intent(WeatherActivity.this, WeatherSettings.class);
        int updated = 1;
        startActivityForResult(modifySettings, updated);
    }

    private void updatePreferences() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("gps_enabled", weatherDataParams.isGPSLocationEnable());
        editor.putString("longitude_value", String.valueOf(weatherDataParams.getLongitude()));
        editor.putString("latitude_value", String.valueOf(weatherDataParams.getLatitude()));
        editor.putString("refreshing_time", String.valueOf(weatherDataParams.getRefreshingTime()));
        editor.putBoolean("c_or_f", weatherDataParams.isTempSignIsC());
        editor.putString("cityName_value", "");
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
        if (!sharedPrefs.getString("cityName_value", "").equals("")) {
            weatherDataParams.setCityName(sharedPrefs.getString("cityName_value", weatherDataParams.getCityName()));
        } else {
            weatherDataParams.setGPSLocationEnable(sharedPrefs.getBoolean("gps_enabled", weatherDataParams.isGPSLocationEnable()));
            if (!weatherDataParams.isGPSLocationEnable()) {
                String longitudeText, latitudeText;
                longitudeText = sharedPrefs.getString("longitude_value", String.valueOf(weatherDataParams.getLongitude()));
                weatherDataParams.setLongitude(Double.parseDouble(longitudeText));
                latitudeText = sharedPrefs.getString("latitude_value", String.valueOf(weatherDataParams.getLatitude()));
                weatherDataParams.setLatitude(Double.parseDouble(latitudeText));
                StringBuilder builder = new StringBuilder();
                builder.append(String.format(Locale.US, "%.2f", weatherDataParams.getLatitude()))
                        .append(",")
                        .append(String.format(Locale.US, "%.2f", weatherDataParams.getLongitude()));
                weatherDataParams.setLocationString(builder.toString());
            } else {
                setLocation();
            }
        }
        String refreshingTimeText;
        refreshingTimeText = sharedPrefs.getString("refreshing_time", String.valueOf(weatherDataParams.getRefreshingTime()));
        weatherDataParams.setRefreshingTime(Integer.parseInt(refreshingTimeText));
        weatherDataParams.setTempSignIsC(sharedPrefs.getBoolean("c_or_f", weatherDataParams.isTempSignIsC()));
        if (weatherDataParams.getCityName() != null && weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'c');
        } else if (weatherDataParams.getCityName() != null && !weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'f');
        } else if (weatherDataParams.getCityName() == null && weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'c');
        } else {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'f');
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
            if (weatherDataParams.isGPSLocationEnable()) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    weatherDataParams.setLongitude(location.getLongitude());
                    weatherDataParams.setLatitude(location.getLatitude());
                    StringBuilder builder = new StringBuilder();
                    builder.append(String.format(Locale.US, "%.2f", weatherDataParams.getLatitude()))
                            .append(",")
                            .append(String.format(Locale.US, "%.2f", weatherDataParams.getLongitude()));
                    weatherDataParams.setLocationString(builder.toString());
                } else {
                    Toast.makeText(this, "Brak możliwości śledzenia Twojej pozycji", Toast.LENGTH_SHORT).show();
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, weatherDataParams.getRefreshingTime(), 500, this);
            }
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        Condition[] forecast = channel.getItem().getForecast();
        data.update(channel);
        dialog.hide();

        Item item = channel.getItem();

        code = item.getCondition().getCode();
        localization = channel.getLocation().getCity() + ", " + channel.getLocation().getCountry();
        temp = item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature();
        currentWeather = setDescription(item);
        pressure = String.format("%.0f", channel.getAtmosphere().getPressure()) + " hPa";
        humidity = channel.getAtmosphere().getHumidity() + " %";
        Date current = new Date(System.currentTimeMillis());
        currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(current) + " " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(current);
        units = channel.getUnits();
        tempUnits = units.getTemperature();
        windUnits = units.getSpeed();
        wind = channel.getWind().getSpeed();

        weekWeather.addAll(Arrays.asList(forecast));
        weatherDataParams.setCityName(channel.getLocation().getCity());
        weatherDataParams.setTempSignIsC(channel.getUnits().getTemperature().equals("C"));
        checkIfLocationIsSaved();
        weatherViewPager.getAdapter().notifyDataSetChanged();
        saveToFile(this);
    }

    private String setDescription(Item item) {
        int itemCode = item.getCondition().getCode();
        int resID = getResources().getIdentifier("PL_" + itemCode, "string", getPackageName());
        return getString(resID);
    }

    private String setDescription(int code) {
        int resID = getResources().getIdentifier("PL_" + code, "string", getPackageName());
        return getString(resID);
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, "Nie udało się pobrać danych pogodowych.", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        if (weatherDataParams.isGPSLocationEnable()) {
            weatherDataParams.setLongitude(location.getLongitude());
            weatherDataParams.setLatitude(location.getLatitude());
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(Locale.US, "%.2f", weatherDataParams.getLatitude()))
                    .append(",")
                    .append(String.format(Locale.US, "%.2f", weatherDataParams.getLongitude()));
            weatherDataParams.setLocationString(builder.toString());
            weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'c');
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
        if (weatherDataParams.getCityName() != null && weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'c');
        } else if (weatherDataParams.getCityName() != null && !weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'f');
        } else if (weatherDataParams.getCityName() == null && weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'c');
        } else {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getLocationString(), 0, 'f');
        }
        checkIfLocationIsSaved();
        Toast.makeText(this, "Odświeżono dane", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(weatherViewPager.getCurrentItem() != 1) {
            weatherViewPager.setCurrentItem(1, true);
        } else {
            saveToFile(this);
            super.onBackPressed(); // This will pop the Activity from the stack.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (weatherDataParams.getCityName() != null && weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'c');
        } else if (weatherDataParams.getCityName() != null && !weatherDataParams.isTempSignIsC()) {
            weatherDataParams.getService().refreshWeather(weatherDataParams.getCityName(), 1, 'f');
        }
    }

    @Override
    protected void onDestroy() {
        if (locationDbAdapter != null)
            locationDbAdapter.close();
        super.onDestroy();
    }

    public void saveToFile(Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Creates an object by reading it from a file
    public static WeatherData readFromFile(Context context) {
        WeatherData weatherData = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            weatherData = (WeatherData) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return weatherData;
    }
}
