package com.example.team_7_tcss_450.ui.weather.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.io.RequestQueueSingleton;
import com.example.team_7_tcss_450.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public class WeeklyForecastViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mErrorResponse;
    private MutableLiveData<Map<String, String>> mUserLocationData;
    private MutableLiveData<List<DailyForecast>> mDailyForecastList;
    private static final int totalForecasts = 7;

    public WeeklyForecastViewModel(@NonNull Application application) {
        super(application);

        mErrorResponse = new MutableLiveData<>();
        mErrorResponse.setValue(new JSONObject());

        mUserLocationData = new MutableLiveData<>();
        mDailyForecastList = new MutableLiveData<>();
        mDailyForecastList.setValue(new ArrayList<>(totalForecasts));
    }

    public void addDailyForecastListObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<DailyForecast>> observer) {
        mDailyForecastList.observe(owner, observer);
    }

    public void addErrorResponseObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super JSONObject> observer) {
        mErrorResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            final String weeklyForecastKey = getString.apply(R.string.keys_json_weekly_forecast_list);
            //Try to grab weekly forecast JSON object list
            if (result.has(weeklyForecastKey)) {
                JSONArray weeklyForecast = result.getJSONArray(weeklyForecastKey);
                for (int i = 0; i < weeklyForecast.length(); i++) {
                    JSONObject dailyForecast = weeklyForecast.getJSONObject(i);

                    //Get args for new DailyForecast object
                    final String date = dailyForecast.getString(getString.apply(R.string.keys_json_daily_forecast_date));
                    final double avgTemp = dailyForecast.getDouble(getString.apply(R.string.keys_json_daily_forecast_avg_temp));
                    final double minTemp = dailyForecast.getDouble(getString.apply(R.string.keys_json_daily_forecast_min_temp));
                    final double maxTemp = dailyForecast.getDouble(getString.apply(R.string.keys_json_daily_forecast_max_temp));
                    final double precipitation = dailyForecast.getDouble(getString.apply(R.string.keys_json_daily_forecast_precipitation));
                    final double humidity = dailyForecast.getDouble(getString.apply(R.string.keys_json_daily_forecast_humidity));
                    final double windSpd = dailyForecast.getDouble(getString.apply(R.string.keys_json_daily_forecast_wind_spd));
                    // Get specific info from nested daily weather description object
                    JSONObject weatherObj = dailyForecast.getJSONObject(getString.apply(R.string.keys_json_daily_forecast_weather_obj));
                    final String description = weatherObj.getString(getString.apply(R.string.keys_json_daily_forecast_description));
                    final int code = weatherObj.getInt(getString.apply(R.string.keys_json_daily_forecast_code));

                    //Add new DailyForecast object to daily forecast list
                    mDailyForecastList.getValue().add(
                            new DailyForecast(
                                    date,
                                    avgTemp,
                                    minTemp,
                                    maxTemp,
                                    precipitation,
                                    humidity,
                                    windSpd,
                                    description,
                                    code
                            )
                    );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", "No response");
        }
        mDailyForecastList.setValue(mDailyForecastList.getValue());
    }

    public void connectGetWeeklyForecast(final String jwt) {
        Log.d("f_connect", "GET CALLED");
        // Generate url for making web service request
        // URL USES HARDCODED latitude and longitude args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_weather_service) +
                "test?lat=42&lon=70";

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                error -> RequestMaker.defaultErrorHandler(error, mErrorResponse)
        ) {
            // Add user JWT token into request header
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

}


