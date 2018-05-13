package com.example.mateusz.weatherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import java.util.regex.Pattern;

public class SettingsActivity extends PreferenceActivity {

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
        private final static Pattern datePattern = Pattern.compile("^(?:(?:31(\\/)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
        private SwitchPreference isGPSEnabled;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            EditTextPreference longitude = (EditTextPreference) getPreferenceScreen().findPreference("longitude_value");
            EditTextPreference latitude = (EditTextPreference) getPreferenceScreen().findPreference("latitude_value");
            isGPSEnabled = (SwitchPreference) getPreferenceScreen().findPreference("gps_enabled");
            EditTextPreference date = (EditTextPreference) getPreferenceScreen().findPreference("date_value");

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

            date.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Boolean rtnval = true;
                    if (isValidDate(newValue.toString())) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Błąd");
                        builder.setMessage("Wprowadzono błędną datę.\n Wprowadź datę w formacie dd/mm/yyyy.");
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

        private boolean isValidDate(String date) {
            return !datePattern.matcher(date).matches();
        }

    }
}
