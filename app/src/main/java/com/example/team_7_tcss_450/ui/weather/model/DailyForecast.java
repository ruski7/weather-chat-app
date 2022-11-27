package com.example.team_7_tcss_450.ui.weather.model;

public class DailyForecast  {

    private final String mDate;
    private final double mAvgTemp;
    private final double mMinTemp;
    private final double mMaxTemp;
    private final double mPrecipitation;
    private final double mHumidity;
    private final double mWindSpeed;
    private final String mWeatherDesc;
    private final int mWeatherCode;

    public DailyForecast(final String theDate,
                         final double theAvgTemp,
                         final double theMinTemp,
                         final double theMaxTemp,
                         final double thePrecipitation,
                         final double theHumidity,
                         final double theWindSpeed,
                         final String theWeatherDesc,
                         final int theCode) {
        mDate = theDate;
        mAvgTemp = theAvgTemp;
        mMinTemp = theMinTemp;
        mMaxTemp = theMaxTemp;
        mPrecipitation = thePrecipitation;
        mHumidity = theHumidity;
        mWindSpeed = theWindSpeed;
        mWeatherDesc = theWeatherDesc;
        mWeatherCode = theCode;
    }

    public String getDate() {
        return mDate;
    }

    public double getAvgTemp() {
        return mAvgTemp;
    }

    public double getMinTemp() {
        return mMinTemp;
    }

    public double getMaxTemp() {
        return mMaxTemp;
    }

    public double getPrecipitation() {
        return mPrecipitation;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public String getWeatherDesc() {
        return mWeatherDesc;
    }

    public int getWeatherCode() {
        return mWeatherCode;
    }

}

