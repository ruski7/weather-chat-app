package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.account.AccountViewModel;
import com.example.team_7_tcss_450.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private AccountViewModel accountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt())
        ).get(UserInfoViewModel.class);

        setContentView(R.layout.activity_main);

        // Make sure the new statements go BELOW setContentView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather,
                R.id.navigation_message, R.id.navigation_contacts,
                R.id.navigation_account).build();
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.
                setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // TODO: FIX DARK MODE TOGGLE - CURRENTLY BROKEN
        // Account Fragment Binding
        accountModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountModel.getStatus().observe(this, SwitchMaterial -> {

            // Perform an action with the latest item data
            if(Boolean.TRUE.equals(accountModel.getStatus().getValue())){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.d("Weather App", "Switch Gets Val \"ON\"");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Log.d("Weather App", "Switch Gets Val \"OFF\"");
            }
        });


        // Theme Setting - Allowing dynamic changing of app theme


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Weather App","Application Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Weather App", "Application Restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Weather App","Application Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Weather App", "Application Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Weather App","Application Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Weather App","Application Destroy");
    }

}