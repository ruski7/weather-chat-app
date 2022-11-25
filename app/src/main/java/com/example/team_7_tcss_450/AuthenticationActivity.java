package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.team_7_tcss_450.utils.Utils;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_authentication);
    }
}