package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.team_7_tcss_450.model.PushyTokenViewModel;

import me.pushy.sdk.Pushy;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);
        initiatePushyTokenRequest();
    }

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }

}