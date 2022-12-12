package com.example.team_7_tcss_450.ui.weather.model;

public class HourlyForecast {

    private long mDate;
    private double mTemp;
    private double mHumidity;
    private double mWindSpeed;
    private double mPrecipitation;
    private String mWeatherDesc;
    private int mWeatherCode;

    public HourlyForecast (HourlyForecastBuilder builder){
        this.mDate = builder.mDate;
        this.mTemp = builder.mTemp;
        this.mHumidity = builder.mHumidity;
        this.mWindSpeed = builder.mWindSpeed;
        this.mPrecipitation = builder.mPrecipitation;
        this.mWeatherDesc = builder.mWeatherDesc;
        this.mWeatherCode = builder.mWeatherCode;
    }

    public static class HourlyForecastBuilder {
        private long mDate;
        private double mTemp;
        private double mHumidity;
        private double mWindSpeed;
        private double mPrecipitation;
        private String mWeatherDesc;
        private int mWeatherCode;

        public HourlyForecastBuilder(long date){
            this.mDate = date;
        }

        public HourlyForecastBuilder setTemp(double temp){
            this.mTemp = temp;
            return this;
        }
        public HourlyForecastBuilder setHumidity(double humidity){
            this.mHumidity = humidity;
            return this;
        }
        public HourlyForecastBuilder setWindSpeed(double windSpeed){
            this.mWindSpeed = windSpeed;
            return this;
        }
        public HourlyForecastBuilder setPrecipitation(double precipitation){
            this.mPrecipitation = precipitation;
            return this;
        }
        public HourlyForecastBuilder setDesc(String desc){
            this.mWeatherDesc = desc;
            return this;
        }

        public HourlyForecastBuilder setWeatherCode(int weatherCode){
            this.mWeatherCode = weatherCode;
            return this;
        }

        public HourlyForecast build(){
             return new HourlyForecast(this);
        }
    }

}
