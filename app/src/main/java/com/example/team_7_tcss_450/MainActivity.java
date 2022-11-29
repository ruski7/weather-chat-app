package com.example.team_7_tcss_450;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
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
import com.example.team_7_tcss_450.model.PushyTokenViewModel;
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

        /*if(sharedPreferences.getBoolean("changeorientation", true)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }*/

        if(sharedPreferences.getBoolean("changedarkmode", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

//        AccountViewModel mAccountModel = new ViewModelProvider(this).get(AccountViewModel.class);
//        mAccountModel.getStatus().observe(this, SwitchMaterial -> {
//            if(Boolean.TRUE.equals(mAccountModel.getStatus().getValue())) {
//                Log.d("Weather App", "Activity Event -> TRUE");
//                getApplication().setTheme(R.style.Theme_ForestGreen);
//            } else {
//                Log.d("Weather App", "Activity Event -> FALSE");
//                getApplication().setTheme(R.style.Theme_RelaxedBlue);
//            }
//        });

        String text = sharedPreferences.getString("status", "");
        Toast.makeText(this, "message from edit text " + text, Toast.LENGTH_SHORT).show();
        String message = String.valueOf(sharedPreferences.getBoolean("dark_mode", true));
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Weather App","Main Activity Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Weather App", "Main Activity Restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Weather App","Main Activity Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Weather App", "Main Activity Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Weather App","Main Activity Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Weather App","Main Activity Destroy");
    }

    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);

        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
        //End the app completely
        PushyTokenViewModel model = new ViewModelProvider(this)
                .get(PushyTokenViewModel.class);

        //when we hear back from the web service quit
        model.addResponseObserver(this, result -> finishAndRemoveTask());

        model.deleteTokenFromWebservice(
                new ViewModelProvider(this)
                        .get(UserInfoViewModel.class)
                        .getJWT()
        );
    }

}