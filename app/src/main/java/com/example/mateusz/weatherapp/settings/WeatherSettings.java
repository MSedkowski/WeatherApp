package com.example.mateusz.weatherapp.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.example.mateusz.weatherapp.R;

import java.util.regex.Pattern;

public class WeatherSettings extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("updated", 1);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @SuppressWarnings({"RegExpSingleCharAlternation", "RegExpRedundantEscape", "Annotator"})
    public static class MyPreferenceFragment extends PreferenceFragment {
        private final static Pattern longitudePattern = Pattern.compile("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");
        private final static Pattern latitudePattern = Pattern.compile("^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$");
        private SwitchPreference isGPSEnabled;
        private SwitchPreference tempUnits;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.weather_preferences);

            EditTextPreference longitude = (EditTextPreference) getPreferenceScreen().findPreference("longitude_value");
            EditTextPreference latitude = (EditTextPreference) getPreferenceScreen().findPreference("latitude_value");
            isGPSEnabled = (SwitchPreference) getPreferenceScreen().findPreference("gps_enabled");
            tempUnits = (SwitchPreference) getPreferenceScreen().findPreference("c_or_f");
            EditTextPreference cityName = (EditTextPreference) getPreferenceScreen().findPreference("cityName_value");

            longitude.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Boolean rtnval = true;
                    if (isGPSEnabled.isChecked()) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Błąd");
                        builder.setMessage("Najpierw wyłącz odczyt GPS z urządzenia.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.show();
                        rtnval = false;
                    } else {

                        if (isValidLongitude(newValue.toString())) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Błąd");
                            builder.setMessage("Wprowadzono błędną długość geograficzną.\nPoprawne wartości są w zakresie -180 do 180 z kropką jako separator dzisiętny.\nMaksymalnie 6 miejsc po kropce");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.show();
                            rtnval = false;
                        }

                    }
                    return rtnval;
                }
            });

            latitude.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Boolean rtnval = true;
                    if (isGPSEnabled.isChecked()) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Błąd");
                        builder.setMessage("Najpierw wyłącz odczyt GPS z urządzenia.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.show();
                        rtnval = false;
                    } else {
                        if (isValidLatitude(newValue.toString())) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Błąd");
                            builder.setMessage("Wprowadzono błędną szerokość geograficzną.\nPoprawne wartości są w zakresie -90 do 90 z kropką jako separator dzisiętny.\nMaksymalnie 6 miejsc po kropce");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.show();
                            rtnval = false;
                        }
                    }
                    return rtnval;
                }
            });

            cityName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Boolean rtnval = true;
                    if (newValue.toString().equals("") || newValue.toString().equals(" ")) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Błąd");
                        builder.setMessage("Wprowadź nazwę poszukiwanego miasta");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.show();
                        rtnval = false;
                    }
                    return rtnval;
                }
            });
        }

        private boolean isValidLongitude(String longitude) {
            return !longitudePattern.matcher(longitude).matches();
        }

        private boolean isValidLatitude(String latitude) {
            return !latitudePattern.matcher(latitude).matches();
        }
    }
}
