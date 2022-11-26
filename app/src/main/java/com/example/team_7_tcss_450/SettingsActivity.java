package com.example.team_7_tcss_450;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.Switch;

import java.util.prefs.Preferences;

public class SettingsActivity extends PreferenceActivity {

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


        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreference()).commit();
    }

    public static class MyPreference extends PreferenceFragment {

        SharedPreferences sharedPreferences;
        SwitchPreference colorSwitch;
        SwitchPreference orintationSwitch;
        EditTextPreference editTextPreference;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            colorSwitch = (SwitchPreference) findPreference("changecolor");

            orintationSwitch = (SwitchPreference) findPreference("changeorientation");

            editTextPreference = (EditTextPreference) findPreference("status");

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

            orintationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    boolean isChecked = (boolean) newValue;

                    if(isChecked) {
                        changetoLandscape();

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
                        changetoPortrait();
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
                        EnableColorChange();
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
                        DisableColorChange();
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

        private void changetoLandscape() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("changeorientation", true);
            editing.apply();
        }

        private void changetoPortrait() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("changeorientation", false);
            editing.apply();
        }

        private void EnableColorChange() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("colorchange", true);
            editing.apply();
        }

        private void DisableColorChange() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editing = sharedPreferences.edit();
            editing.putBoolean("colorchange", false);
            editing.apply();
        }


    }

}