package com.example.team_7_tcss_450.ui.weather;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentDailyForecastBinding;
import com.example.team_7_tcss_450.ui.weather.model.DailyForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DailyForecast}.
 */
public class WeeklyForecastRecyclerViewAdapter extends RecyclerView.Adapter<WeeklyForecastRecyclerViewAdapter.ForecastViewHolder> {

    private final List<DailyForecast> mValues;

    public WeeklyForecastRecyclerViewAdapter(List<DailyForecast> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_daily_forecast, parent, false));
    }

    @Override
    public void onBindViewHolder(final ForecastViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        DailyForecast item = holder.mItem;
        Resources res = holder.mView.getResources();
        final FragmentDailyForecastBinding binding = holder.binding;
        final String far = "°F";
        final String percent = "%";

        binding.valueMaxTemp.setText(res.getString(R.string.value_weather_with_units,
                item.getMaxTemp(), far));
        binding.valueAvgTemp.setText(res.getString(R.string.value_weather_with_units,
                item.getAvgTemp(), far));
        binding.valueMinTemp.setText(res.getString(R.string.value_weather_with_units,
                item.getMinTemp(), far));
        binding.valuePrecip.setText(res.getString(R.string.value_weather_with_units,
                item.getPrecipitation(), percent));
        binding.valueHumidity.setText(res.getString(R.string.value_weather_with_units,
                item.getHumidity(), percent));
        binding.valueWind.setText(res.getString(R.string.value_weather_with_units, item.getWindSpeed(), "mph"));
        binding.labelDescription.setText(item.getWeatherDesc());
        try {
            binding.labelDay.setText(getDayString(item.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final WeatherType icon = getWeatherType(item.getWeatherCode());
        setWeatherIconResource(binding.iconWeather, icon);


    }

    private static void setWeatherIconResource(final ImageView theImg, final WeatherType theIcon) {
        switch (theIcon) {
            case CLEAR:
                theImg.setImageResource(R.drawable.clear_sky);
                break;
            case FEW_CLOUDS:
                theImg.setImageResource(R.drawable.few_clouds);
                break;
            case CLOUDS:
                theImg.setImageResource(R.drawable.clouds);
                break;
            case FOG:
                theImg.setImageResource(R.drawable.fog);
                break;
            case LIGHT_RAIN:
                theImg.setImageResource(R.drawable.light_rain);
                break;
            case RAIN:
                theImg.setImageResource(R.drawable.moderate_rain);
                break;
            case SNOW:
                theImg.setImageResource(R.drawable.snow);
            case THUNDER:
                theImg.setImageResource(R.drawable.thunder);
                break;
            default:
                theImg.setImageResource(R.drawable.few_clouds);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        public DailyForecast mItem;
        public View mView;
        public FragmentDailyForecastBinding binding;

        public ForecastViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentDailyForecastBinding.bind(view);
        }

    }

    enum WeatherType {
        CLEAR,
        FEW_CLOUDS,
        CLOUDS,
        FOG,
        LIGHT_RAIN,
        RAIN,
        SNOW,
        THUNDER
    }

    private static WeatherType getWeatherType(final int theCode) {
        if (theCode == 800)
            return WeatherType.CLEAR;
        else if (theCode >= 801 && theCode <= 803)
            return WeatherType.FEW_CLOUDS;
        else if (theCode == 804)
            return WeatherType.CLOUDS;
        else if (theCode == 900 || (theCode >= 500 && theCode <= 522))
            return WeatherType.RAIN;
        else if (theCode >= 300 && theCode <= 302)
            return WeatherType.LIGHT_RAIN;
        else if (theCode >= 600 && theCode <= 623)
            return WeatherType.SNOW;
        else if (theCode >= 700 && theCode <= 751)
            return WeatherType.FOG;
        else if (theCode >= 200 && theCode <= 233)
            return WeatherType.THUNDER;
        else
            return WeatherType.FEW_CLOUDS;
    }

    private static String getDayString(final String theDate) throws ParseException {
        @SuppressLint("SimpleDateFormat") Date d = new SimpleDateFormat("yyyy-MM-dd").parse(theDate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(d);
    }

}