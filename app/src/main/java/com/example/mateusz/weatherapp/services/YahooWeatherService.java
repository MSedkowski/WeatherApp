package com.example.mateusz.weatherapp.services;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mateusz.weatherapp.weatherData.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class YahooWeatherService {

    private WeatherServiceCallback callback;
    private String location;
    private Exception error;

    public YahooWeatherService(WeatherServiceCallback callback) {
        this.callback = callback;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshWeather(String loc) {
        this.location = loc;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                //String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", strings[0]);
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places where text=\"(%s)\") and u='c'", strings[0]);
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                Log.d("http: ", endpoint);
                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();

                } catch (MalformedURLException e) {
                    error = e;
                } catch (IOException e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if(s == null && error != null) {
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    JSONObject queryResult = data.optJSONObject("query");
                    int count = queryResult.optInt("count");

                    if(count == 0) {
                        callback.serviceFailure(new LocationNotExistsException("Lokalizacja: " + location + " nie istnieje."));
                        return;
                    }

                    Channel channel = new Channel();
                    channel.populate(queryResult.optJSONObject("results").optJSONObject("channel"));

                    callback.serviceSuccess(channel);

                } catch (JSONException e) {
                    callback.serviceFailure(e);
                }

            }

        }.execute(loc);
    }

    public class LocationNotExistsException extends Exception {
        public LocationNotExistsException(String message) {
            super(message);
        }
    }
}
