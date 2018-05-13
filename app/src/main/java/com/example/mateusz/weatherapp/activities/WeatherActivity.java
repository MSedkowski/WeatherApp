package com.example.mateusz.weatherapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.mateusz.weatherapp.R;
import com.example.mateusz.weatherapp.adapters.CustomPagerAdapter;
import com.example.mateusz.weatherapp.settings.SettingsActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings({"StringBufferReplaceableByString", "ConstantConditions"})
public class WeatherActivity extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    protected Context context;

    public static String todayDate;

    public static double longitude;
    public static double latitude;

    public static String sunrise;
    public static String sunset;
    public static String twilightMorning;
    public static String twilightEvening;

    public static String moonrise;
    public static String moonset;
    public static String nextFullMoon;
    public static String nextNewMoon;
    public static double moonPhase;
    public static double moonAge;

    private boolean isGPSLocationEnable = true;
    public static int refreshingTime = 15;
    private SharedPreferences sharedPrefs;
    private ViewPager viewPager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Button changeDate = findViewById(R.id.changeDate);

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePreferences();
            }
        });

        setTime();
        setLocation();
        AstroCalculator today = getTodayInfo();
        setSunInfo(today);
        setMoonInfo(today);

        viewPager = findViewById(R.id.sunMoonPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new CustomPagerAdapter(fragmentManager));

        updatePreferences();
    }

    private void changePreferences() {
        Intent modifySettings = new Intent(WeatherActivity.this, SettingsActivity.class);
        int updated = 1;
        startActivityForResult(modifySettings, updated);
    }

    private void updatePreferences() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("gps_enabled", isGPSLocationEnable);
        editor.putString("longitude_value", String.valueOf(longitude));
        editor.putString("latitude_value", String.valueOf(latitude));
        editor.putString("refreshing_time", String.valueOf(refreshingTime));
        editor.putString("date_value", todayDate);
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
        isGPSLocationEnable = sharedPrefs.getBoolean("gps_enabled", isGPSLocationEnable);
        if (!isGPSLocationEnable) {
            String longitudeText, latitudeText;
            longitudeText = sharedPrefs.getString("longitude_value", String.valueOf(longitude));
            longitude = Double.parseDouble(longitudeText);
            latitudeText = sharedPrefs.getString("latitude_value", String.valueOf(latitude));
            latitude = Double.parseDouble(latitudeText);
        } else {
            setLocation();
        }
        String refreshingTimeText;
        refreshingTimeText = sharedPrefs.getString("refreshing_time", String.valueOf(refreshingTime));
        refreshingTime = Integer.parseInt(refreshingTimeText);
        todayDate = sharedPrefs.getString("date_value", todayDate);
        setCustomData();
        viewPager.getAdapter().notifyDataSetChanged();
        updatePreferences();
    }

    private void setCustomData() {
        AstroCalculator today = getDataInfo(String.valueOf(todayDate));
        if (today != null) {
            setSunInfo(today);
            setMoonInfo(today);
        }
    }

    private void setMoonInfo(AstroCalculator today) {
        AstroDateTime todayMoonRise = today.getMoonInfo().getMoonrise();
        AstroDateTime todayMoonSet = today.getMoonInfo().getMoonset();
        AstroDateTime nextFullMoonDate = today.getMoonInfo().getNextFullMoon();
        AstroDateTime nextNewMoonDate = today.getMoonInfo().getNextNewMoon();
        moonPhase = today.getMoonInfo().getIllumination() * 100;
        moonAge = today.getMoonInfo().getAge();

        DecimalFormat format = new DecimalFormat();
        format.setMinimumIntegerDigits(2);

        StringBuilder moonRiseText = new StringBuilder();
        moonRiseText.append(format.format(todayMoonRise.getHour()));
        moonRiseText.append(":");
        moonRiseText.append(format.format(todayMoonRise.getMinute()));
        moonRiseText.append(":");
        moonRiseText.append(format.format(todayMoonRise.getSecond()));

        StringBuilder moonSetText = new StringBuilder();
        moonSetText.append(format.format(todayMoonSet.getHour()));
        moonSetText.append(":");
        moonSetText.append(format.format(todayMoonSet.getMinute()));
        moonSetText.append(":");
        moonSetText.append(format.format(todayMoonSet.getSecond()));

        moonrise = moonRiseText.toString();
        moonset = moonSetText.toString();

        StringBuilder nextFullMoonDateText = new StringBuilder();
        nextFullMoonDateText.append(format.format(nextFullMoonDate.getDay()));
        nextFullMoonDateText.append("/");
        nextFullMoonDateText.append(format.format(nextFullMoonDate.getMonth()));
        nextFullMoonDateText.append("\t");

        StringBuilder nextNewMoonDateText = new StringBuilder();
        nextNewMoonDateText.append(format.format(nextNewMoonDate.getDay()));
        nextNewMoonDateText.append("/");
        nextNewMoonDateText.append(format.format(nextNewMoonDate.getMonth()));
        nextNewMoonDateText.append("\t");

        nextFullMoon = nextFullMoonDateText.toString();
        nextNewMoon = nextNewMoonDateText.toString();
    }

    private AstroCalculator getTodayInfo() {
        long deviceDate = Calendar.getInstance().getTime().getTime();
        int year = Integer.parseInt(new SimpleDateFormat("YYYY", Locale.ENGLISH).format(deviceDate));
        int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(deviceDate));
        int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(deviceDate));
        int hour = Integer.parseInt(new SimpleDateFormat("hh", Locale.GERMANY).format(deviceDate));
        int minute = Integer.parseInt(new SimpleDateFormat("mm", Locale.GERMANY).format(deviceDate));
        int second = Integer.parseInt(new SimpleDateFormat("ss", Locale.GERMANY).format(deviceDate));
        int timeZoneOffset = Calendar.getInstance().getTimeZone().getRawOffset() / 3600000;
        int daySaveTime = Calendar.getInstance().getTimeZone().getDSTSavings();
        AstroDateTime today = new AstroDateTime(year, month, day, hour, minute, second, timeZoneOffset, daySaveTime != 0);
        AstroCalculator.Location astroLocation = new AstroCalculator.Location(latitude, longitude);
        return new AstroCalculator(today, astroLocation);
    }

    @SuppressLint("SimpleDateFormat")
    private AstroCalculator getDataInfo(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date customDate = dateFormat.parse(date);
            int year = Integer.parseInt(new SimpleDateFormat("YYYY", Locale.ENGLISH).format(customDate));
            int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(customDate));
            int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(customDate));
            int hour = Integer.parseInt(new SimpleDateFormat("hh", Locale.GERMANY).format(System.currentTimeMillis()));
            int minute = Integer.parseInt(new SimpleDateFormat("mm", Locale.GERMANY).format(System.currentTimeMillis()));
            int second = Integer.parseInt(new SimpleDateFormat("ss", Locale.GERMANY).format(System.currentTimeMillis()));
            int timeZoneOffset = Calendar.getInstance().getTimeZone().getRawOffset() / 3600000;
            int daySaveTime = Calendar.getInstance().getTimeZone().getDSTSavings();
            AstroDateTime today = new AstroDateTime(year, month, day, hour, minute, second, timeZoneOffset, daySaveTime != 0);
            AstroCalculator.Location astroLocation = new AstroCalculator.Location(latitude, longitude);
            return new AstroCalculator(today, astroLocation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    private void setSunInfo(AstroCalculator today) {
        AstroDateTime todaySunrise = today.getSunInfo().getSunrise();
        AstroDateTime todayDawn = today.getSunInfo().getSunset();
        AstroDateTime todayTwilingMorning = today.getSunInfo().getTwilightMorning();
        AstroDateTime todayTwilingEvening = today.getSunInfo().getTwilightEvening();
        DecimalFormat format = new DecimalFormat();
        format.setMinimumIntegerDigits(2);

        StringBuilder sunriseText = new StringBuilder();
        sunriseText.append(format.format(todaySunrise.getHour()));
        sunriseText.append(":");
        sunriseText.append(format.format(todaySunrise.getMinute()));
        sunriseText.append(":");
        sunriseText.append(format.format(todaySunrise.getSecond()));
        sunriseText.append("\n Azymut: ");
        sunriseText.append(String.format("%.2f", today.getSunInfo().getAzimuthRise()));

        StringBuilder dawnText = new StringBuilder();
        dawnText.append(format.format(todayDawn.getHour()));
        dawnText.append(":");
        dawnText.append(format.format(todayDawn.getMinute()));
        dawnText.append(":");
        dawnText.append(format.format(todayDawn.getSecond()));
        dawnText.append("\n Azymut: ");
        dawnText.append(String.format("%.2f", today.getSunInfo().getAzimuthSet()));

        sunrise = sunriseText.toString();
        sunset = dawnText.toString();

        int differenceMorning = (todaySunrise.getHour() * 60 + todaySunrise.getMinute()) -
                (todayTwilingMorning.getHour() * 60 + todayTwilingMorning.getMinute());
        int differenceEvening = (todayTwilingEvening.getHour() * 60 + todayTwilingEvening.getMinute()) -
                (todayDawn.getHour() * 60 + todayDawn.getMinute());

        StringBuilder twilightMorningText = new StringBuilder();
        twilightMorningText.append(differenceMorning);
        twilightMorningText.append(" minut");

        StringBuilder twilightEveningText = new StringBuilder();
        twilightEveningText.append(differenceEvening);
        twilightEveningText.append(" minut");

        twilightMorning = twilightMorningText.toString();
        twilightEvening = twilightEveningText.toString();

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
                } else {
                    Toast.makeText(this, "Brak możliwości śledzenia Twojej pozycji", Toast.LENGTH_SHORT).show();
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, refreshingTime, 0, this);
            }
        }
    }

    private void setTime() {
        long deviceDate = System.currentTimeMillis();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        todayDate = dateFormatter.format(deviceDate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        if (isGPSLocationEnable) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            viewPager.getAdapter().notifyDataSetChanged();
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("todayDate", todayDate);
        savedInstanceState.putString("sunrise", sunrise);
        savedInstanceState.putString("sunset", sunset);
        savedInstanceState.putString("twilightMorning", twilightMorning);
        savedInstanceState.putString("twilightEvening", twilightEvening);
        savedInstanceState.putString("moonrise", moonrise);
        savedInstanceState.putString("moonset", moonset);
        savedInstanceState.putString("nexFullMoon", nextFullMoon);
        savedInstanceState.putString("nextNewMoon", nextNewMoon);
        savedInstanceState.putDouble("longitude", longitude);
        savedInstanceState.putDouble("latitude", latitude);
        savedInstanceState.putDouble("moonPhase", moonPhase);
        savedInstanceState.putDouble("moonAge", moonAge);
        savedInstanceState.putInt("refreshingTime", refreshingTime);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        todayDate = savedInstanceState.getString("todayDate");
        sunrise = savedInstanceState.getString("sunrise");
        sunset = savedInstanceState.getString("sunset");
        twilightMorning = savedInstanceState.getString("twilightMorning");
        twilightEvening = savedInstanceState.getString("twilightEvening");
        moonrise = savedInstanceState.getString("moonrise");
        moonset = savedInstanceState.getString("moonset");
        nextFullMoon = savedInstanceState.getString("nextFullMoon");
        nextNewMoon = savedInstanceState.getString("nextNewMoon");
        longitude = savedInstanceState.getDouble("longitude");
        latitude = savedInstanceState.getDouble("latitude");
        moonPhase = savedInstanceState.getDouble("moonPhase");
        moonAge = savedInstanceState.getDouble("moonAge");
        refreshingTime = savedInstanceState.getInt("refreshingTime");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
