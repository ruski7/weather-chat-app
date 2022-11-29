package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.team_7_tcss_450.utils.Utils;

public class AuthenticationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(sharedPreferences.getBoolean("changecolor", true)) {
            setTheme(R.style.Theme_ForestGreen);
        } else {
            setTheme(R.style.Theme_RelaxedBlue);
        }

        if(sharedPreferences.getBoolean("changeorientation", true)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        if(sharedPreferences.getBoolean("changedarkmode", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_authentication);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Weather App","Authentication Activity Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Weather App", "Authentication Activity Restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Weather App","Authentication Activity Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Weather App", "Authentication Activity Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Weather App","Authentication Activity Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Weather App","Authentication Activity Destroy");
    }
}