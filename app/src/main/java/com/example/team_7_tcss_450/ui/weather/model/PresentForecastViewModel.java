package com.example.team_7_tcss_450.ui.weather.model;

import android.app.Application;
import android.location.Location;
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

public class PresentForecastViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mErrorResponse;
    private MutableLiveData<Map<String, String>> mUserLocationData;
    private MutableLiveData<HourlyForecast> mCurrentWeather;
    private MutableLiveData<List<HourlyForecast>> mHourlyWeather;
    private MutableLiveData<String> mSearchLocation;

    public PresentForecastViewModel(@NonNull Application application) {
        super(application);

        mErrorResponse = new MutableLiveData<>();
        mErrorResponse.setValue(new JSONObject());

        mUserLocationData = new MutableLiveData<>();
        mCurrentWeather = new MutableLiveData<>();
        mHourlyWeather = new MutableLiveData<>();
        mHourlyWeather.setValue(new ArrayList<>());
    }

    public String getSearchLocation() {
        return mSearchLocation.getValue();
    }

    public void addHourlyForecastListObserver(@NonNull LifecycleOwner owner,
                                             @NonNull Observer<? super List<HourlyForecast>> observer) {
        mHourlyWeather.observe(owner, observer);
    }

    public void addCurrentForecastObserver(@NonNull LifecycleOwner owner,
                                              @NonNull Observer<? super HourlyForecast> observer) {
        mCurrentWeather.observe(owner, observer);
    }

    public void connectGetHourlyForecast(final String jwt, final Location location) {
        Log.d("f_connect", "GET CALLED");


        // Generate url for making web service request
        // URL USES HARDCODED latitude and longitude args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_weather_service) +
                "hourly?" + "lat=" + location.getLatitude() + "&lon=" + location.getLongitude();

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleHourlyResult,
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

    public void handleHourlyResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            final String hourlyForecastKey = getString.apply(R.string.keys_json_hourly_forecast_list);

            if (result.has(hourlyForecastKey)) {
                JSONArray dataArray = result.getJSONArray(hourlyForecastKey);
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject hourlyForecast = dataArray.getJSONObject(i);

                    //Get args for new hourlyForecast object
                    final long date = hourlyForecast.getLong(getString.apply(R.string.keys_json_hourly_forecast_date));
                    final double temp = hourlyForecast.getDouble(getString.apply(R.string.keys_json_hourly_forecast_avg_temp));
                    final double precipitation = hourlyForecast.getDouble(getString.apply(R.string.keys_json_hourly_forecast_precipitation));
                    final double humidity = hourlyForecast.getDouble(getString.apply(R.string.keys_json_hourly_forecast_humidity));
                    final double windSpd = hourlyForecast.getDouble(getString.apply(R.string.keys_json_hourly_forecast_wind_spd));
                    final String description = hourlyForecast.getString(getString.apply(R.string.keys_json_hourly_forecast_description));
                    final int code = hourlyForecast.getInt(getString.apply(R.string.keys_json_hourly_forecast_code));

                    final HourlyForecast hourlyForecastResult = new HourlyForecast.HourlyForecastBuilder(date)
                            .setTemp(temp)
                            .setPrecipitation(precipitation)
                            .setDesc(description)
                            .setHumidity(humidity)
                            .setWeatherCode(code)
                            .setWindSpeed(windSpd)
                            .build();
                    mHourlyWeather.getValue().add(hourlyForecastResult);
                    mHourlyWeather.setValue(mHourlyWeather.getValue());
                }
                final String city = result.getString(getString.apply(R.string.keys_json_hourly_forecast_city));
                mSearchLocation.setValue(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", "No response");
        }
    }

    public void connectGetCurrentWeather(final String jwt, final Location location) {
        Log.d("f_connect", "GET CALLED");


        // Generate url for making web service request
        // URL USES HARDCODED latitude and longitude args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_weather_service) +
                "current?" + "lat=" + location.getLatitude() + "&lon=" + location.getLongitude();

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleCurrentResult,
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

    public void handleCurrentResult(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {

            //Get args for new hourlyForecast object
            final long date = result.getLong(getString.apply(R.string.keys_json_hourly_forecast_date));
            final double temp = result.getDouble(getString.apply(R.string.keys_json_hourly_forecast_avg_temp));
            final double humidity = result.getDouble(getString.apply(R.string.keys_json_hourly_forecast_humidity));
            final double windSpd = result.getDouble(getString.apply(R.string.keys_json_hourly_forecast_wind_spd));
            final String description = result.getString(getString.apply(R.string.keys_json_hourly_forecast_description));
            final int code = result.getInt(getString.apply(R.string.keys_json_hourly_forecast_code));

            final HourlyForecast currentForecast = new HourlyForecast.HourlyForecastBuilder(date)
                    .setTemp(temp)
                    .setDesc(description)
                    .setHumidity(humidity)
                    .setWeatherCode(code)
                    .setWindSpeed(windSpd)
                    .build();
            mCurrentWeather.setValue(currentForecast);
            final String city = result.getString(getString.apply(R.string.keys_json_hourly_forecast_city));
            mSearchLocation.setValue(city);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", "No response");
        }
    }

}
