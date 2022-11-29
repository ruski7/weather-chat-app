package com.example.team_7_tcss_450.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.team_7_tcss_450.R;

public class Utils
{
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_BLUE = 1;
    public final static int THEME_GREEN = 2;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.Theme_RelaxedBlue);//Theme_Team7TCSS450
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.Theme_RelaxedBlue);
                break;
            case THEME_GREEN:
                activity.setTheme(R.style.Theme_ForestGreen);
                break;
        }
    }
}