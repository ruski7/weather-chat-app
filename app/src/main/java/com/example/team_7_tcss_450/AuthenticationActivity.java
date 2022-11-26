package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

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

        setContentView(R.layout.activity_authentication);
    }
}