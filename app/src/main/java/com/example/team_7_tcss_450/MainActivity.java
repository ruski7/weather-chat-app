package com.example.team_7_tcss_450;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team_7_tcss_450.databinding.ActivityMainBinding;
import com.example.team_7_tcss_450.databinding.FragmentAccountBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.account.AccountFragment;
import com.example.team_7_tcss_450.ui.account.AccountViewModel;
import com.example.team_7_tcss_450.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

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

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt())
        ).get(UserInfoViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Make sure the new statements go BELOW setContentView




        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather,
                R.id.navigation_message, R.id.navigation_contacts,
                R.id.navigation_account).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        AccountViewModel mAccountModel = new ViewModelProvider(this).get(AccountViewModel.class);
        mAccountModel.getStatus().observe(this, SwitchMaterial -> {
            if(Boolean.TRUE.equals(mAccountModel.getStatus().getValue())) {
                Log.d("Weather App", "Activity Event -> TRUE");
                getApplication().setTheme(R.style.Theme_ForestGreen);
            } else {
                Log.d("Weather App", "Activity Event -> FALSE");
                getApplication().setTheme(R.style.Theme_RelaxedBlue);
            }
        });

        String text = sharedPreferences.getString("status", "");
        Toast.makeText(this, "message from edit text " + text, Toast.LENGTH_SHORT).show();
        String message = String.valueOf(sharedPreferences.getBoolean("dark_mode", true));
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
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