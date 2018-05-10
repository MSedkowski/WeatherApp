package com.example.mateusz.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("updated",1);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
}
