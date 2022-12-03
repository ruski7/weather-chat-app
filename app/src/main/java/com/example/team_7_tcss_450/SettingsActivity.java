package com.example.team_7_tcss_450;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());



        // TODO: Fix below code to prevent constant landscape orientation
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


        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreference()).commit();
    }

    public static class MyPreference extends PreferenceFragment {

        SharedPreferences sharedPreferences;
        SwitchPreference colorSwitch;
        SwitchPreference darkModeSwitch;
        SwitchPreference orientationSwitch;
        EditTextPreference editTextPreference;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            editTextPreference = (EditTextPreference) findPreference("status");
            colorSwitch = (SwitchPreference) findPreference("changecolor");
            orientationSwitch = (SwitchPreference) findPreference("changeorientation");
            darkModeSwitch = (SwitchPreference) findPreference("changedarkmode");


            editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String text = (String) newValue;

                    if(text.equals(" ")) {
                        uploadText();
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    },1000);

                    return true;
                }
            });

            orientationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean isChecked = (boolean) newValue;

                    if(isChecked) {
                        changeToLandscape();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        },1000);

                    } else {
                        changeToPortrait();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        },1000);
                    }

                    return true;
                }
            });

            colorSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean isChecked = (boolean) newValue;

                    if(isChecked) {
                        enableColorChange();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        },1000);
                    } else {
                        disableColorChange();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        },1000);
                    }

                    return true;
                }
            });

            darkModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean isChecked = (boolean) newValue;

                    if(isChecked) {
                        enableDarkMode();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        },1000);
                    } else {
                        disableDarkMode();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        },1000);
                    }

                    return true;
                }
            });




        }

        private void uploadText() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putString("status", "text");
            editing.apply();
        }

        private void changeToLandscape() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("changeorientation", true);
            editing.apply();
        }

        private void changeToPortrait() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("changeorientation", false);
            editing.apply();
        }

        private void enableColorChange() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("colorchange", true);
            editing.apply();
        }

        private void disableColorChange() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("colorchange", false);
            editing.apply();
        }

        private void enableDarkMode() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("changedarkmode", true);
            editing.apply();
        }

        private void disableDarkMode() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("changedarkmode", false);
            editing.apply();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Weather App","Settings Activity Start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Weather App", "Settings Activity Restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Weather App","Settings Activity Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Weather App", "Settings Activity Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Weather App","Settings Activity Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Weather App","Settings Activity Destroy");
    }

}