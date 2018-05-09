package com.example.mateusz.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class WeatherActivity extends AppCompatActivity implements LocationListener{

    private static int REQUEST_LOCATION = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
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

    private ViewPager viewPager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setTime();
        setLocation();
        AstroCalculator today = getTodayInfo();
        setSunInfo(today);
        setMoonInfo(today);

        viewPager = (ViewPager) findViewById(R.id.sunMoonPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new CustomPagerAdapter(fragmentManager));

    }

    private void setMoonInfo(AstroCalculator today) {
        AstroDateTime todayMoonRise = today.getMoonInfo().getMoonrise();
        AstroDateTime todayMoonSet = today.getMoonInfo().getMoonset();
        AstroDateTime nextFullMoonDate = today.getMoonInfo().getNextFullMoon();
        AstroDateTime nextNewMoonDate = today.getMoonInfo().getNextNewMoon();
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

    private AstroCalculator getTodayInfo(){
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
        AstroCalculator.Location astroLocation = new AstroCalculator.Location(latitude, longitude);
        AstroCalculator todayInfo = new AstroCalculator(today, astroLocation);
        return todayInfo;
    }

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

        int differenceMorning = (todaySunrise.getHour()*60+todaySunrise.getMinute())-
                                (todayTwilingMorning.getHour()*60+todayTwilingMorning.getMinute());
        int differenceEvening = (todayTwilingEvening.getHour()*60+todayTwilingEvening.getMinute())-
                                (todayDawn.getHour()*60+todayDawn.getMinute());

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
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
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                String currentDate = dateFormatter.format(deviceDate);
                todayDate = currentDate;
                try {
                    while(!isInterrupted()) {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long deviceDate = System.currentTimeMillis();
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                String currentDate = dateFormatter.format(deviceDate);
                                todayDate = currentDate;
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
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        viewPager.getAdapter().notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
       finish();
    }
}
