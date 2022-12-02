package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.team_7_tcss_450.model.PushyTokenViewModel;

import me.pushy.sdk.Pushy;

public class AuthenticationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        setContentView(R.layout.activity_authentication);
        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);
        initiatePushyTokenRequest();
    }

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
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